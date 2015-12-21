package com.easybus.gui.Object;

import java.util.ArrayList;
import java.util.List;


// Bus line class
public class Lines {

	private int _id;
	private String code;
	private String bus_name;
	private String goRoutePath;
	private String goRoutePathGeo;
	private String goRouteThroughStops;
	private String returnRoutePath;
	private String returnRoutePathGeo;
	private String returnRouteThroughStops;
	private int direction;
	private String cost;
	private String frequency;
	private String operationTime;
	public List<Nodes> goNode, returnNode;

	public Lines(String FleetID, String Code, String Name,
			String OperationsTime, String Frequency, String Cost,
			String RouteGo, String RouteGoGeo, String RouteGoStops,
			String RouteReturn, String RouteReturnGeo, String RouteReturnStops) {
		this._id = Integer.parseInt(FleetID);
		this.setCode(Code);
		this.bus_name = Name;
		this.operationTime = OperationsTime;
		this.frequency = Frequency;
		this.cost = Cost;
		this.setGoRoutePath(RouteGo);
		this.setGoRoutePathGeo(RouteGoGeo);
		this.goRouteThroughStops = RouteGoStops;
		this.returnRoutePath = RouteReturn;
		this.returnRoutePathGeo = RouteReturnGeo;
		this.returnRouteThroughStops = RouteReturnStops;

		goNode = new ArrayList<Nodes>();
		returnNode = new ArrayList<Nodes>();
	}

	public Lines(String bus_id, String bus_name, int direction, String cost,
			String frequency, String operationTime) {
		this.code = bus_id;
		this.bus_name = bus_name;
		this.direction = direction;
		this.cost = cost;
		this.frequency = frequency;
		this.setOperationTime(operationTime);

		goNode = new ArrayList<Nodes>();
		returnNode = new ArrayList<Nodes>();
	}

	public Lines() {
		this._id = 0;
		goNode = new ArrayList<Nodes>();
		returnNode = new ArrayList<Nodes>();
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Lines(String code, String bus_name, int direction) {
		this.code = code;
		this.bus_name = bus_name;
		this.direction = direction;
	}

	public int getID() {
		return _id;
	}

	public void setBus_id(String bus_id) {
		this._id = Integer.parseInt(bus_id);
	}

	public String getBus_name() {
		return bus_name;
	}

	public void setBus_name(String bus_name) {
		this.bus_name = bus_name;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public static boolean lineEqual(Lines a, Lines b) {
		return (a.getCode().equals(b.getCode()) && (a.getDirection() == b
				.getDirection()));
	}

	public String getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGoRoutePath() {
		return goRoutePath;
	}

	public void setGoRoutePath(String goRoutePath) {
		this.goRoutePath = goRoutePath;
	}

	public String getGoRoutePathGeo() {
		return goRoutePathGeo;
	}

	public void setGoRoutePathGeo(String goRoutePathGeo) {
		this.goRoutePathGeo = goRoutePathGeo;
	}
}
