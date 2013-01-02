package com.aireno.vapas.service.base;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.vapas.service.persistance.Preke;
import org.hibernate.classic.Session;

import java.util.List;

public abstract class ServiceBase
{
    protected <T> T getBean(Class<T> cls) {
        return ApplicationContextProvider.getProvider().getBean(cls);
    }

    protected Assertor getAssertor() {
        return new Assertor();  //To change body of created methods use File | Settings | File Templates.
    }

    protected boolean validateUnique(String s, String pavadinimas, long id, Session session) {
        List<Preke> list =  session.createQuery(s)
                .setParameter("1", id).setParameter("2", pavadinimas).setFetchSize(1).list();
        return list.size() == 0;

    }
}
