package com.vetweb.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vetweb.model.Animal;
import com.vetweb.model.Atendimento;
import com.vetweb.model.Prontuario;
import com.vetweb.model.ProntuarioVacina;
import com.vetweb.model.Proprietario;

 //@author renanrodrigues
@Repository//Indica que a classe é gerenciada pelo container do Spring e é uma entidade de persistência
public class ProprietarioDAO implements IDAO<Proprietario> {
	
	@PersistenceContext(unitName = "vetwebpu")//Especificação da JPA para receber injeção de objeto de contexto com o banco de dados (EntityManager)
    private EntityManager entityManager;
    
    @Autowired
    private AnimalDAO animalDAO;
    
    @Autowired
    private ProntuarioDAO prontuarioDAO;
    
    @Override
    public void salvar(Proprietario proprietario) {
        if(proprietario.getPessoaId() == null)
            entityManager.persist(proprietario);//Spring instancia, injeta e finaliza o EntityManager
        else
            entityManager.merge(proprietario);
    }

    @Override
    public List<Proprietario> listar() {
        return entityManager.createQuery("select p from Proprietario p", Proprietario.class).getResultList();
    }

    @Override
    public Proprietario consultarPorId(long id) {
        return entityManager.find(Proprietario.class, id);
    }

    @Override
    public void remover(Proprietario proprietario) {
        entityManager.remove(proprietario);
    }
    
    public void removerAnimal(long animalId) {
        Animal animal = animalDAO.consultarPorId(animalId);
        Proprietario proprietario = consultarPorId(animal.getProprietario().getPessoaId());
        Animal a = proprietario.getAnimais().stream().filter(animal1 -> animal1.getAnimalId().equals(animalId)).findFirst().get();
        proprietario.getAnimais().remove(a);
        animalDAO.remover(a);
        System.out.println(a.getNome() + a.getAnimalId());
    }

    @Override
    public Proprietario consultarPorNome(String nome) {
        Optional<Proprietario> o = Optional.of(entityManager.createNamedQuery("proprietarioPorNome", Proprietario.class)
                .setParameter("nomeProprietario", nome).getSingleResult());
        Proprietario p = o.orElseThrow(RuntimeException::new);
        return p;
    }

    public void detachProprietario(Proprietario proprietario) {
    	entityManager.detach(proprietario);
    }
    
    @Override
    public long quantidadeRegistros() {
        return entityManager.createNamedQuery("quantidadeClientes", Long.class).getSingleResult();
    }
    
    public List<Prontuario> getProntuariosDosAnimaisDoCliente(Long proprietarioId) {
    	return entityManager.createNamedQuery("prontuariosAnimaisDoCliente", Prontuario.class)
    			.setParameter("Id", proprietarioId)
    			.getResultList();
    }
    
    public List<ProntuarioVacina> getVacinasAplicadasPorCliente(Long proprietario) {
    	return getProntuariosDosAnimaisDoCliente(proprietario)
    			.stream()
    			.flatMap(p -> p.getVacinas().stream()).collect(Collectors.toList());
    }
    
    public List<Atendimento> getAtendimentosRealizadosPorCliente(Long proprietario) {
    	return getProntuariosDosAnimaisDoCliente(proprietario)
    			.stream()
    			.flatMap(p -> p.getAtendimentos().stream()).collect(Collectors.toList());
    }
    
    public BigDecimal getValoresPendentesDoCliente(Proprietario proprietario) {
    	List<Atendimento> atendimentosAnimaisCliente = getAtendimentosRealizadosPorCliente(proprietario.getPessoaId());
    	List<ProntuarioVacina> vacinasAnimaisCliente = getVacinasAplicadasPorCliente(proprietario.getPessoaId());
    	BigDecimal totalPendente;
    	double totalPendenteAtendimentos = atendimentosAnimaisCliente.stream()
    			.filter(at -> !at.isPago())
    			.mapToDouble(a -> a.getTipoDeAtendimento().getCusto().doubleValue())
    			.sum();
    	double totalPendenteVacinas = vacinasAnimaisCliente.stream()
    			.filter(vac -> !vac.isPago())
    			.mapToDouble(v -> v.getVacina().getPreco().doubleValue())
    			.sum();
    	totalPendente = new BigDecimal(totalPendenteAtendimentos + totalPendenteVacinas);    
    	return totalPendente;
    }
    
    public Set<Proprietario> getClientesEmDebito() {
    	List<BigInteger> idsProprietariosComDebitoVacina = idsClientesComDebitoVacina();
    	List<BigInteger> idsProprietariosComDebitoAtendimento = idsClientesComDebitoAtendimento();
    	Set<Proprietario> proprietariosComDebito = new HashSet<>();
    	idsProprietariosComDebitoVacina.stream().forEach(prop -> proprietariosComDebito.add(consultarPorId(Long.parseLong(prop.toString()))));
    	idsProprietariosComDebitoAtendimento.stream().forEach(prop -> proprietariosComDebito.add(consultarPorId(Long.parseLong(prop.toString()))));
    	return proprietariosComDebito;
    }

	public List<BigInteger> idsClientesComDebitoAtendimento() {
		List<Object> resultList = (List<Object>) entityManager.createNativeQuery("select cli.pessoaid from proprietarios cli\n" + 
    			"join animais a on cli.pessoaid = a.proprietario_pessoaid\n" + 
    			"join prontuarios p on a.prontuario_prontuarioid = p.prontuarioid\n" + 
    			"join prontuarios_atendimento ate on ate.prontuario_prontuarioid = p.prontuarioid\n" + 
    			"join atendimento atend on atend.atendimentoid = ate.atendimentos_atendimentoid\n" + 
    			"where atend.pago = false;").getResultList();
		List<BigInteger> idsProprietariosComDebitoAtendimento = new ArrayList<>();
		for(Object result : resultList) idsProprietariosComDebitoAtendimento.add(new BigInteger(result.toString()));
		return idsProprietariosComDebitoAtendimento;
	}

	public List<BigInteger> idsClientesComDebitoVacina() {
		List<Object> resultList = (List<Object>) entityManager.createNativeQuery("select cli.pessoaid from proprietarios cli\n" + 
    			"join animais a on cli.pessoaid = a.proprietario_pessoaid\n" + 
    			"join prontuarios p on a.prontuario_prontuarioid = p.prontuarioid\n" + 
    			"join prontuarios_prontuariovacina vac on vac.prontuario_prontuarioid = p.prontuarioid\n" + 
    			"join prontuariovacina prvac on prvac.prontuariovacinaid = vac.vacinas_prontuariovacinaid\n" + 
    			"where prvac.pago = false;").getResultList();
		List<BigInteger> idsProprietariosComDebitoVacina = new ArrayList<>();
		for(Object result : resultList) idsProprietariosComDebitoVacina.add(new BigInteger(result.toString()));		
		return idsProprietariosComDebitoVacina;
	}
	
	public Set<Proprietario> getClientesInativosAdimplentes() {
		List<Prontuario> prontuariosDeClientesInativos = entityManager
				.createQuery("SELECT p FROM Prontuario p JOIN p.animal a JOIN a.proprietario cli WHERE cli.ativo = FALSE", Prontuario.class)
				.getResultList();
		Set<Proprietario> clientesInativosAdimplentes = new HashSet<>();
		for (Proprietario proprietario : prontuariosDeClientesInativos.stream().map(p -> p.getAnimal().getProprietario()).collect(Collectors.toList())) {
			boolean todasAsVacinasPagas = prontuariosDeClientesInativos
					.stream()
					.flatMap(p -> p.getVacinas().stream())
					.filter(vac -> vac.getProntuario().getAnimal().getProprietario().equals(proprietario))
					.allMatch(vac -> vac.isPago());
			boolean todosOsAtendimentosPagos =	prontuariosDeClientesInativos
					.stream()
					.flatMap(p -> p.getAtendimentos().stream())
					.filter(ate -> prontuarioDAO.buscarProntuarioDoAtendimento(ate).getAnimal().getProprietario().equals(proprietario))
					.allMatch(ate -> ate.isPago());
			if (todasAsVacinasPagas && todosOsAtendimentosPagos) {
				clientesInativosAdimplentes.add(proprietario);				
			}
			return clientesInativosAdimplentes;
		}
		return clientesInativosAdimplentes;
	}
	
}
