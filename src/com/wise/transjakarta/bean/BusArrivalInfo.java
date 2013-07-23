package com.wise.transjakarta.bean;

public class BusArrivalInfo {
	int StationID;
	String VName;
	int Percent;
	public int getStationID() {
		return StationID;
	}
	public void setStationID(int stationID) {
		StationID = stationID;
	}
	public String getVName() {
		return VName;
	}
	public void setVName(String vName) {
		VName = vName;
	}
	public int getPercent() {
		return Percent;
	}
	public void setPercent(int percent) {
		Percent = percent;
	}
	@Override
	public String toString() {
		return "BusArrivalInfo [StationID=" + StationID + ", VName=" + VName
				+ ", Percent=" + Percent + "]";
	}	
}