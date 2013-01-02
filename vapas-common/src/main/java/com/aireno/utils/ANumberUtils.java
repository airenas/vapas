package com.aireno.utils;

import org.apache.commons.lang.time.DateUtils;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.12.31
 * Time: 15.47
 * To change this template use File | Settings | File Templates.
 */
public class ANumberUtils {
    public static boolean equal(BigDecimal item1, BigDecimal item2) {
        if (item1 == null && item2 == null)
            return true;
        if (item1 == null || item2 == null)
            return false;
       return item1.compareTo(item2) ==0 ;
    }
}
