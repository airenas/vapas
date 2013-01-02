package com.aireno.utils;

import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.12.31
 * Time: 15.47
 * To change this template use File | Settings | File Templates.
 */
public class ADateUtils {
    public static boolean equalDate(Date date1, Date date2)

    {
        if (date1 == null && date2 == null)
            return true;
        if (date1 == null || date2 == null)
        return false;
        return DateUtils.isSameDay(date1, date2);
    }
}
