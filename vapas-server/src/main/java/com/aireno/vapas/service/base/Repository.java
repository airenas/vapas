package com.aireno.vapas.service.base;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.util.Assert;

import java.util.List;

public class Repository {
    private Assert assertor;

    public Repository(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    Session session;

    public <T> T get(Class<T> tClass, long id) throws Exception {
        String queryString = "from " + tClass.getSimpleName() + " c where c.id = ?1";
        List<T> list = getSession().createQuery(queryString)
                .setParameter("1", id).list();
        getAssertor().isTrue(list.size() == 1, "Nerastas įrašas '%s' pagal id '%s'", tClass.getSimpleName(), id);
        return list.get(0);
    }

    public <T> List<T> getList(Class<T> tClass, String filterField, long filterId) throws Exception {
        String queryString = "from " + tClass.getSimpleName() + " c where c."
                + filterField + " = ?1";
        List<T> list = getSession().createQuery(queryString)
                .setParameter("1", filterId).list();
        return list;
    }

    public <T> Query prepareQuery(Class<T> tClass, String filterFields) throws Exception {
        String queryString = "from " + tClass.getSimpleName() + " where " + filterFields;
        return getSession().createQuery(queryString);
    }

    public <T> int deleteList(Class<T> tClass, String filterField, long filterId) throws Exception {
        String queryString = "delete from " + tClass.getSimpleName() + " c where c."
                + filterField + " = ?1";
        Query query = session.createQuery(queryString).setParameter("1", filterId);
        int rowCount = query.executeUpdate();
        return rowCount;
    }

    public <T> int delete(Class<T> tClass, long id) throws Exception {
        return deleteList(tClass, "id", id);
    }

    public Assertor getAssertor() {
        return new Assertor();
    }
}
