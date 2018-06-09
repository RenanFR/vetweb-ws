package com.vetweb.dao;
// @author Maria Jéssica

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.vetweb.model.Clinica;
@Repository
public class ConfigDAO {
	
	@PersistenceContext(unitName = "vetwebpu")
    private EntityManager entityManager;
    
    public void salvarClinica(Clinica c){
        if(c.getCnpj() == null) entityManager.persist(c);
        else entityManager.merge(c);
    }
    
    public Clinica clinicaPorCnpj(String razaoSocial){
        return entityManager.createQuery("SELECT c FROM Clinica c WHERE c.razaoSocial = :razaoSocial", Clinica.class)
                .setParameter("razaoSocial", razaoSocial).getSingleResult();
    }
    
    public Optional<Clinica> clinica(){
        return entityManager.createQuery("SELECT c FROM Clinica c ORDER BY c.cnpj ASC", Clinica.class)
                .getResultList().stream().findFirst();
    }
}
