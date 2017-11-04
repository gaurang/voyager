package com.cd.voyager.web.service.dao;
 
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
 

public abstract class AbstractDao {
	
	protected static EntityManagerFactory entityManagerFactory;
	

    public static void setUpEntityManagerFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory( "voyager" );
    }

    public static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }


    @Autowired
    private SessionFactory sessionFactory;
 
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void persist(Object entity) {
        getSession().persist(entity);
    }
 
    public void delete(Object entity) {
        getSession().delete(entity);
    }
    
    
}