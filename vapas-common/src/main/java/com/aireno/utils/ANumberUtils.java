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

    public static String DecimalToString(BigDecimal item1) {
        if (item1 == null)
            return null;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(item1);
    }

    public static String DecimalToString(double item1) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(item1);
    }

    public static BigDecimal StringToDecimal(String item1) throws ParseException {
        if (StringUtils.isEmpty(item1)) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        Number number = df.parse(item1);
        return new BigDecimal(number.doubleValue());
    }

    public static double DefaultValue(BigDecimal kiekis) {
        if (kiekis == null) {
            return 0;
        }
        return kiekis.doubleValue();
    }
}
