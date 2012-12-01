package com.aireno.base;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.45
 * To change this template use File | Settings | File Templates.
 */
public interface Processor<RequestBase, ResponseBase> {
    ResponseBase process(RequestBase request) throws Exception;
}
