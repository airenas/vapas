package com.aireno.vapas.service.base;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.ClassBase;
import com.aireno.base.Processor;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.42
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProcessorBase<RequestBase, ResponseBase> extends ClassBase
        implements Processor<RequestBase, ResponseBase> {

    public Session getSession() {
        return session;
    }

    Session session;


    Repository repo;

    public Repository getRepo() {
        return repo;
    }

    @Override
    public ResponseBase process(RequestBase request) throws Exception {
        getLog().debug("Starting process");
        ResponseBase result = null;
        SessionFactory sessionFactory = VapasSessionFactory.getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        repo = new Repository(session);
        try {
            result = processInt(request);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            session.close();
            throw e;
        }
        if (session != null)
            session.close();
        return result;
    }

    protected abstract ResponseBase processInt(RequestBase request) throws Exception;

    protected <T> T getBean(Class<T> cls) {
        return ApplicationContextProvider.getProvider().getBean(cls);
    }

    protected SttsAssertor getAssertor() {
        return new SttsAssertorImpl();  //To change body of created methods use File | Settings | File Templates.
    }
}
