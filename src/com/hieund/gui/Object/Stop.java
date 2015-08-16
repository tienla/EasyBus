/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hieund.gui.Object;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

/**
 *
 * @author Do Thanh An
 */
public class Stop {
	private String code;
    private String name;
    private ArrayList<Nodes> arrayNode;
    private LatLng geo;
    private String fleetOver;
    public String getFleetOver() {
        return fleetOver;
    }

    public void setFleetOver(String fleetOver) {
        this.fleetOver = fleetOver;
    }
    public Stop(String name) {
        arrayNode = new ArrayList<Nodes>();
        this.name = name;
    }
    
    public Stop() 
	{
		arrayNode = new ArrayList<Nodes>();
	}

    public Stop(String name, LatLng geo, String fleetOver) {
        this.name = name;
        this.geo = geo;
        this.fleetOver = fleetOver;
        arrayNode=new ArrayList<Nodes>();
    }

    public Stop(String name, LatLng geo) {
        this.name = name;
        this.geo = geo;
        arrayNode=new ArrayList<Nodes>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Nodes> getArrayNode() {
        return arrayNode;
    }

    public void setArrayNode(ArrayList<Nodes> arrayNode) {
        this.arrayNode = arrayNode;
    }

    public LatLng getGeo() {
        return geo;
    }

    public void setGeo(LatLng geo) {
        this.geo = geo;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
    
}
