package com.easybus.gui.Object;

import java.util.ArrayList;

public class ResultSearchObject {

	private String startEndDeclare;
	private ArrayList<String> busIds;
	private String detailInfo;
	private ArrayList<ResultObject> arrNode;
	private String nodeInfo;
	private String walkStart;
	private String walkEnd;
	public ResultSearchObject() {
		super();
		nodeInfo = "";
	}

	public ResultSearchObject(String startEndDeclare, String detailInfo) {
		super();
		this.startEndDeclare = startEndDeclare;
		this.detailInfo = detailInfo;
		walkStart = "";
	}
	

	
	public ArrayList<ResultObject> getArrNode() {
		return arrNode;
	}

	public void setArrNode(ArrayList<ResultObject> arrNode) {
		this.arrNode = arrNode;
	}

	public String getStartEndDeclare() {
		return startEndDeclare;
	}

	public void setStartEndDeclare(String startEndDeclare) {
		this.startEndDeclare = startEndDeclare;
	}

	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public ArrayList<String> getBusIds() {
		return busIds;
	}

	public void setBusIds(ArrayList<String> busIds) {
		this.busIds = busIds;
	}

	public String getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(String nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	public String getWalkStart() {
		return walkStart;
	}

	public void setWalkStart(String walkStart) {
		this.walkStart = walkStart;
	}

}
