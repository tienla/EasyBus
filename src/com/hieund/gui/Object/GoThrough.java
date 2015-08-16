package com.hieund.gui.Object;

public class GoThrough {
	private String busLineCode;
	private String busStopCode;
	private boolean direction;
	private int gtOrder;
	private int nextBusStop;
	private double distance;

	public GoThrough(String busLineCode, String busStopCode, int direction, int gtOrder, int nextBusStop)
	{
		this.setBusLineCode(busLineCode);
		this.setBusStopCode(busStopCode);
		if (direction == 0)
			this.setDirection(false);
		else
			this.setDirection(true);
		this.setGtOrder(gtOrder);
		this.setNextBusStop(nextBusStop);
		this.setDistance(-1);
	}
	
	public GoThrough() { }

	public String getBusLineCode() {
		return busLineCode;
	}

	public void setBusLineCode(String busLineCode) {
		this.busLineCode = busLineCode;
	}

	public String getBusStopCode() {
		return busStopCode;
	}

	public void setBusStopCode(String busStopCode) {
		this.busStopCode = busStopCode;
	}

	public int getGtOrder() {
		return gtOrder;
	}

	public void setGtOrder(int gtOrder) {
		this.gtOrder = gtOrder;
	}

	public boolean isDirection() {
		return direction;
	}

	public void setDirection(boolean direction) {
		this.direction = direction;
	}

	public int getNextBusStop() {
		return nextBusStop;
	}

	public void setNextBusStop(int nextBusStop) {
		this.nextBusStop = nextBusStop;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
