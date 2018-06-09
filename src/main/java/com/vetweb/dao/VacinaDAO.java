package com.vetweb.dao;
// @author Maria Jéssica

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.vetweb.model.Vacina;

@Repository
public class VacinaDAO implements IDAO<Vacina>{
	
	@PersistenceContext(unitName = "vetwebpu")
    private EntityManager entityManager;
    
    @Override
    public void salvar(Vacina vacina) {
        if(vacina.getVacinaId() == null)
            entityManager.persist(vacina);
        else
            entityManager.merge(vacina);
    }

    @Override
    public List<Vacina> listar() {
        return entityManager.createQuery("SELECT v FROM Vacina v", Vacina.class)
                .getResultList();
    }

    @Override
    public Vacina consultarPorId(long vacinaId) {
        return entityManager.find(Vacina.class, vacinaId);
    }

    @Override
    public void remover(Vacina vacina) {
        entityManager.remove(vacina);
    }

    @Override
    public Vacina consultarPorNome(String nomeVacina) {
        return entityManager.createQuery("SELECT v FROM Vacina v WHERE v.nome = :nomeVacina", Vacina.class)
                .setParameter("nomeVacina", nomeVacina).getSingleResult();
    }

    @Override
    public long quantidadeRegistros() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
