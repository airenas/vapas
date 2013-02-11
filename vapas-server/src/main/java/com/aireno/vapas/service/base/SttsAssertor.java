package com.aireno.vapas.service.base;

import com.aireno.base.Assertor;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.18
 * Time: 00.41
 * To change this template use File | Settings | File Templates.
 */
public interface SttsAssertor extends Assertor {
    public void hasId(long id, String s, Object ... params) throws Exception;
}
