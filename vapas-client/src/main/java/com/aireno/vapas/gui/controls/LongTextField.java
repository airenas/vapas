package com.aireno.vapas.gui.controls;

import com.aireno.base.LookupDto;
import com.aireno.vapas.gui.Constants;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import jfxtras.labs.scene.control.CalendarTextField;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.lang.StringUtils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.24
 * Time: 11.41
 * To change this template use File | Settings | File Templates.
 */

public class LongTextField extends TextField {

    public void setValue(Long value) {
        if (value == null){
        setText(null);}
        else
        {setText(Long.toString(value));}
    }

    public Long getValue() {
        String text = getText();
        if (StringUtils.isEmpty(text)){
            return null;
        }
        return Long.parseLong(text);
    }
}

