package com.aireno.vapas.service.base;

import com.aireno.base.ApplicationContextProvider;
public abstract class ServiceBase
{
    protected <T> T getBean(Class<T> cls) {
        return ApplicationContextProvider.getProvider().getBean(cls);
    }

    protected Assertor getAssertor() {
        return new Assertor();  //To change body of created methods use File | Settings | File Templates.
    }
}
