package com.aireno.vapas.gui.controls;

import com.aireno.base.LookupDto;
import com.aireno.vapas.gui.Constants;
import javafx.scene.control.ComboBox;
import jfxtras.labs.scene.control.CalendarTextField;

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

public class DateControl extends CalendarTextField {

    public void setDate(Date value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value);
        setValue(calendar);
    }

    public Date getDate() {
        Calendar calendar = getValue();
        return calendar.getTime();
    }

    @Override protected String getUserAgentStylesheet()
    {
        return getClass().getResource("/jfxtras/labs/internal/scene/control/CalendarTextField.css").toString();
    }
}

