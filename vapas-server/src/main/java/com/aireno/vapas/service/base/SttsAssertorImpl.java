package com.aireno.vapas.service.base;

import com.aireno.base.AssertorImpl;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.18
 * Time: 00.41
 * To change this template use File | Settings | File Templates.
 */
public class SttsAssertorImpl extends AssertorImpl implements SttsAssertor {
    public Exception newException(String s, Object... params) {
        return new Exception(String.format(s, params));
    }

    @Override
    public Exception newException(Throwable e, String s, Object... params) {
        return new Exception(String.format(s, params), e);
    }

    @Override
    public void hasId(long id, String s, Object... params) throws Exception {
        isTrue(id != 0, s, params);
    }
}
