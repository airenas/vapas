package com.aireno.utils;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

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
        return item1.compareTo(item2) == 0;
    }

    public static boolean greater(double a1, double a2) {
        return a1 > a2 + 0.0000001;
    }

    public static boolean lessOrEqual(double a1, double a2) {
        return  a1 <= a2 + 0.0000001;
    }

    public static String decimalToString(BigDecimal item1) {
        if (item1 == null)
            return null;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(item1);
    }

    public static String decimalToString(double item1) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(item1);
    }

    public static BigDecimal stringToDecimal(String item1) throws ParseException {
        if (StringUtils.isEmpty(item1)) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        Number number = df.parse(item1);
        return new BigDecimal(number.doubleValue());
    }

    public static double defaultValue(BigDecimal kiekis) {
        if (kiekis == null) {
            return 0;
        }
        return kiekis.doubleValue();
    }

    public static Long defaultValue(Long kiekis) {
        if (kiekis == null) {
            return new Long(0);
        }
        return kiekis;
    }
}
