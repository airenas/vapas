package com.aireno.vapas.service.base;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 21.24
 * To change this template use File | Settings | File Templates.
 */
public class VapasSessionFactory {
    private static  SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory()
    {
        if (sessionFactory == null){
            sessionFactory = new Configuration()
                 .configure()
                 // configures settings from hibernate.cfg.xml

                    .buildSessionFactory();
        }
        return sessionFactory;
    }
}
