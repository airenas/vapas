package com.aireno.utils;

import com.aireno.Constants;
import org.apache.commons.lang.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.12.31
 * Time: 15.47
 * To change this template use File | Settings | File Templates.
 */
public class ADateUtils {
    public static boolean equalDate(Date date1, Date date2) {
        if (date1 == null && date2 == null)
            return true;
        if (date1 == null || date2 == null)
            return false;
        return DateUtils.isSameDay(date1, date2);
    }

    /**
     * Date1 > date2
     * @param date1
     * @param date2
     * @return Date1 > date2
     */
    public static boolean greaterDate(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return false;
        return trimTime(date1).after(trimTime(date2));
    }

    /**
     * Date1 < date2
     * @param date1
     * @param date2
     * @return Date1 < date2
     */
    public static boolean lessDate(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return false;
        return trimTime(date1).before(trimTime(date2));
    }

    public static Date trimTime(Date date1) {
        return DateUtils.truncate(date1, Calendar.DAY_OF_MONTH);
    }

    public static String dateToString(Date item1) {
        if (item1 == null)
            return null;
        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
        return df.format(item1);
    }
}
