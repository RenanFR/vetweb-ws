package com.vetweb.cdi.factory;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ProduceEntityManager {
	
	    @SuppressWarnings("unused")
		private static final long serialVersionUID = 1L;
	    
	    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit");
	
	    @Produces
	    public EntityManager createEntityManager() {
	        return entityManagerFactory.createEntityManager();
	    }
	
	    public void close(@Disposes EntityManager entityManager) {
	        if (entityManager.isOpen()) {
	        	entityManager.close();
	        }
	    }
	
	    public EntityManagerFactory getEntityManager() {
	        return entityManagerFactory;
	    }
	
	    public void setEntityManager(EntityManagerFactory entityManagerFactory) {
	        this.entityManagerFactory = entityManagerFactory;
	    }
}
