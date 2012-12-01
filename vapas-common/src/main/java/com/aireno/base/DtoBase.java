package com.aireno.base;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 16.25
 * To change this template use File | Settings | File Templates.
 */
public abstract class DtoBase {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    protected long id;
}
