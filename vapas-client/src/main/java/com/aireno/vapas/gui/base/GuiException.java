package com.aireno.vapas.gui.base;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.12.1
 * Time: 12.00
 * To change this template use File | Settings | File Templates.
 */
public class GuiException extends Exception {
    private String errorOn;

    public GuiException(String errorOn, Throwable cause) {
        super(cause);
        this.errorOn = errorOn;
    }

    public String getErrorOn() {
        return errorOn;
    }

    public void setErrorOn(String errorOn) {
        this.errorOn = errorOn;
    }
}
