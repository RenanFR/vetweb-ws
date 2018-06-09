package com.vetweb.dao;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// @author 11151504898
import com.vetweb.model.Animal;
import com.vetweb.model.Especie;
import com.vetweb.model.Patologia;
import com.vetweb.model.Pelagem;
import com.vetweb.model.Raca;

public class AnimalDAO implements IDAO<Animal>{
	
    @PersistenceContext(unitName = "vetwebpu")
    private EntityManager entityManager;
    
    @Override
    public void salvar(Animal animal) { 
        if(animal.getAnimalId() == null) {
            entityManager.persist(animal);
        }
        else {
            entityManager.merge(animal);
        }
    }
    
    @Override
    public List<Animal> listar() {
        return entityManager.createQuery("select a from Animal a", Animal.class).getResultList();
    }

    @Override
    public Animal consultarPorId(long id) {
        return entityManager.find(Animal.class, id);
    }

    @Override
    public void remover(Animal e) {
        entityManager.remove(e);
    }
    
    public Animal atualizar(Animal animal) {
        return entityManager.merge(animal);
    }

    @Override
    public Animal consultarPorNome(String nome) {
        Optional<Animal> optionalAnimal = Optional.of(entityManager.createNamedQuery("animalPorNome", Animal.class).setParameter("nomeAnimal", nome).getSingleResult());                
        Animal a = optionalAnimal.orElseThrow(RuntimeException::new);
        return a;
    }
    
    public Animal consultar(Animal animal) {
        return entityManager.createNamedQuery("consultaGetId", Animal.class)
                .setParameter("nomeAnimal", animal.getNome())
                .setParameter("idPessoa", animal.getProprietario().getPessoaId())
                .setParameter("nascimentoAnimal", animal.getDtNascimento())
                .getSingleResult();
    }
    
    @Override
    public long quantidadeRegistros() {
        return entityManager.createNamedQuery("quantidadeAnimais", Long.class).getSingleResult();
    }
    
    public void salvarEspecie(Especie especie){
        if(especie.getEspecieId() == null){
            entityManager.persist(especie);
        }else{
            entityManager.merge(especie);
        }
    }
    
    public void salvarRaca(Raca raca){
        if(raca.getRacaId() == null)
            entityManager.persist(raca);
        else
            entityManager.merge(raca);
    }
    
    public void salvarPelagem(Pelagem pelagem){
        if(pelagem.getPelagemId()== null)
            entityManager.persist(pelagem);
        else
            entityManager.merge(pelagem);
    }
    
    public void salvarPatologia(Patologia patologia){
        if(patologia.getNome() == null)
            entityManager.persist(patologia);
        else
            entityManager.merge(patologia);
    }
    
    public List<Pelagem> pelagens(){
        return entityManager.createQuery("SELECT p FROM Pelagem p", Pelagem.class).getResultList();
    }
    
    public List<Especie> especies(){
        return entityManager.createQuery("SELECT E FROM Especie E", Especie.class).getResultList();
    }
    
    public List<Raca> racas(){
        return entityManager.createQuery("SELECT R FROM Raca R", Raca.class).getResultList();
    }
    
    public List<Patologia> patologias(){
        return entityManager.createQuery("SELECT pat FROM Patologia pat", Patologia.class).getResultList();
    }
    
    public Especie especiePorDescricao(String especie){
        return entityManager.createQuery("SELECT e FROM Especie e WHERE e.descricao = :desc", Especie.class)
                .setParameter("desc", especie).getSingleResult();
    }
    
    public List<Raca> racasPorEspecie(String especie){
        return entityManager.createQuery("SELECT r FROM Raca r WHERE r.especie.descricao = :especie", Raca.class)
                .setParameter("especie", especie).getResultList();
    }
    
    public Raca racaPorDescricao(String raca){
        return entityManager.createQuery("SELECT r FROM Raca r WHERE r.descricao = :descricao", Raca.class)
                .setParameter("descricao", raca).getSingleResult();
    }
    
    public Pelagem pelagemPorDescricao(String pelagem){
        return entityManager.createQuery("SELECT p FROM Pelagem p WHERE p.descricao = :descricao", Pelagem.class)
                .setParameter("descricao", pelagem).getSingleResult();
    }
    
    public Patologia patologiaPorDescricao(String patologia){
        return entityManager.createQuery("SELECT p FROM Patologia p WHERE p.nome = :nome", Patologia.class)
                .setParameter("nome", patologia).getSingleResult();
    }
    
    public Especie especiePorId(Long especieId) {
        return entityManager.find(Especie.class, especieId);
    }
    
    public Raca racaPorId(Long racaId) {
        return entityManager.find(Raca.class, racaId);
    }
    
    public Pelagem pelagemPorId(Long pelagemId) {
        return entityManager.find(Pelagem.class, pelagemId);
    }
    
    public void removerEspecie(Especie especie) {
        entityManager.remove(especie);
    }
    
    public void removerPelagem(Pelagem pelagem) {
        entityManager.remove(pelagem);
    }
    
    public void removerRaca(Raca raca) {
        entityManager.remove(raca);
    }
    
    public void removerPatologia(Patologia patologia) {
        entityManager.remove(patologia);
    }
    
}
