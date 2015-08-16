/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hieund.gui.Object;

/**
 *
 * @author Do Thanh An
 */
public class ResultObject {
    private String origin;
    private String destination;
    private Lines line;

    public ResultObject(String origin, String destination, Lines line) {
        this.origin = origin;
        this.destination = destination;
        this.line = line;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Lines getLine() {
        return line;
    }

    public void setLine(Lines line) {
        this.line = line;
    }
    
}
