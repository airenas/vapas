package com.aireno.vapas.service.base;

import com.aireno.vapas.service.persistance.GydomuGyvunuZurnalas;
import com.aireno.vapas.service.persistance.Nurasymas;
import org.hibernate.classic.Session;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.Assert;

import java.util.List;

public class Repository
{
    private Assert assertor;

    public Repository(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    Session session;

    public <T> T get(long id, Class<T> tClass) throws Exception {
        String queryString = "from " + tClass.getSimpleName() + " c where c.id = ?1";
        List<T> list = getSession().createQuery(queryString)
                .setParameter("1", id).list();
        getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
        return list.get(0);
    }

    public Assertor getAssertor() {
        return new Assertor();
    }
}
