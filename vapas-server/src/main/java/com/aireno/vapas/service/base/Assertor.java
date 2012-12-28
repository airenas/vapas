package com.aireno.vapas.service.base;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.18
 * Time: 00.41
 * To change this template use File | Settings | File Templates.
 */
public class Assertor {

    public void isNotNull(Object item, String s) throws Exception {
        isTrue(item != null, s);

    }

    public void isTrue(boolean item, String s) throws Exception {
        if (item)
            return;
        throw new Exception(s);
    }

    public void isTrue(boolean item, String s, Object param) throws Exception {
        if (item)
            return;
        throw new Exception(String.format(s, param));
    }

    public void isNotNullStr(String kodas, String s) throws Exception {
        isTrue(kodas != null && kodas.length() > 0, s);
    }

    public void hasId(long id, String s) throws Exception {
        isTrue(id != 0, s);
    }

    public void isFalse(boolean b, String s) throws Exception {
        isTrue(!b, s);
    }
}
