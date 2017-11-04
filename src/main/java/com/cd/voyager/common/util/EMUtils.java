package com.cd.voyager.common.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMUtils {

    private static EntityManagerFactory entityManagerFactory;
/*
    @BeforeClass
    public static void setUpEntityManagerFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory( "voyager" );
    }

    @AfterClass
    public static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }
*/}