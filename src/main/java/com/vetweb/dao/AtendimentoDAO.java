package com.vetweb.dao;
// @author Maria Jéssica

import com.vetweb.model.Atendimento;

import com.vetweb.model.TipoDeAtendimento;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class AtendimentoDAO implements IDAO<Atendimento> {
	
	@PersistenceContext(unitName = "vetwebpu")
    EntityManager entityManager;
    
    @Override
    public void salvar(Atendimento atendimento) {
    	if(atendimento.getAtendimentoId() == null)
    		entityManager.persist(atendimento);
    	else 
    		entityManager.merge(atendimento);
    }

    @Override
    public List<Atendimento> listar() {
        return entityManager
        		.createQuery("SELECT a FROM Atendimento a", Atendimento.class)
        		.getResultList();
    }

    @Override
    public Atendimento consultarPorId(long id) {
    	return entityManager.find(Atendimento.class, id);
    }

    @Override
    public void remover(Atendimento e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Atendimento consultarPorNome(String nome) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long quantidadeRegistros() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void salvarTipoDeAtendimento(TipoDeAtendimento tipoDeAtendimento){
        if(tipoDeAtendimento.getTipoDeAtendimentoId() == null) {
            entityManager.persist(tipoDeAtendimento);
        } else {
            entityManager.merge(tipoDeAtendimento);
        }
    }
    public List<TipoDeAtendimento> tiposDeAtendimento(){
        return entityManager.createQuery("SELECT ta FROM TipoDeAtendimento ta", TipoDeAtendimento.class).getResultList();
    }
    public TipoDeAtendimento tipoDeAtendimentoPorId(Long tipoDeAtendimentoId) {
        return entityManager.find(TipoDeAtendimento.class, tipoDeAtendimentoId);
    }
    public void removerTipoDeAtendimento(TipoDeAtendimento tipoDeAtendimento) {
        entityManager.remove(tipoDeAtendimento);
    }
}
