package com.easybus.gui.Object;

import com.google.android.gms.maps.model.LatLng;

public class Nodes {

    private Lines line;
    private Nodes nextNode;
    private String stopName;
    private LatLng geo;
    private Stop busStop;
    private String fleetOver;
    
    public LatLng getGeo() {
		return geo;
	}

	public void setGeo(LatLng geo) {
		this.geo = geo;
	}

	public Lines getLine() {
        return line;
    }

    public Nodes(Lines line, String stopName) {
        this.line = line;
        this.stopName = stopName;
    }

    public void setLine(Lines line) {
        this.line = line;
    }

    public Nodes getNextNode() {
        return nextNode;
    }

    public void setNextNode(Nodes nextNode) {
        this.nextNode = nextNode;
    }

    public String getStopName() {
        return stopName;
    }
    
    public void setFleetOver(String fleetOver) {
        this.fleetOver = fleetOver;
    }

    public String getsetFleetOver() {
        return fleetOver;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public static boolean nodeEqual(Nodes a, Nodes b) {
        return Lines.lineEqual(a.getLine(), b.getLine()) && (a.getStopName().equals(b.getStopName()));
    }

	public Stop getBusStop() {
		return busStop;
	}

	public void setBusStop(Stop busStop) {
		this.busStop = busStop;
	}
}

