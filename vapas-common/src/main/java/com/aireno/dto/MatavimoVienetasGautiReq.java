package com.aireno.dto;

import com.aireno.base.RequestBase;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.49
 * To change this template use File | Settings | File Templates.
 */
public class MatavimoVienetasGautiReq extends RequestBase {
    public long id;

    public MatavimoVienetasGautiReq(long id) {
        this.id = id;
    }
}
