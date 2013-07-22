package com.wise.transjakarta.bean;

public class RoadStationInf {
	
	private int StationID = 0 ;
	
	private String StationName = null;
	
	private int CenterX = 0;
	
	private int  CenterY = 0;
	
	private int Mile = 0;
	
	private int Platform = 0;
	
	private int SegAvgSpeed = 0;
	
	private int StationID2 = 0;
	
	private String RoadName1 = null;
	
	private String RoadName2 = null;

	public int getStationID() {
		return StationID;
	}

	public void setStationID(int stationID) {
		StationID = stationID;
	}

	public String getStationName() {
		return StationName;
	}

	public void setStationName(String stationName) {
		StationName = stationName;
	}

	public int getCenterX() {
		return CenterX;
	}

	public void setCenterX(int centerX) {
		CenterX = centerX;
	}

	public int getCenterY() {
		return CenterY;
	}

	public void setCenterY(int centerY) {
		CenterY = centerY;
	}

	public int getMile() {
		return Mile;
	}

	public void setMile(int mile) {
		Mile = mile;
	}

	public int getPlatform() {
		return Platform;
	}

	public void setPlatform(int platform) {
		Platform = platform;
	}

	public int getSegAvgSpeed() {
		return SegAvgSpeed;
	}

	public void setSegAvgSpeed(int segAvgSpeed) {
		SegAvgSpeed = segAvgSpeed;
	}

	public int getStationID2() {
		return StationID2;
	}

	public void setStationID2(int stationID2) {
		StationID2 = stationID2;
	}

	public String getRoadName1() {
		return RoadName1;
	}

	public void setRoadName1(String roadName1) {
		RoadName1 = roadName1;
	}

	public String getRoadName2() {
		return RoadName2;
	}

	public void setRoadName2(String roadName2) {
		RoadName2 = roadName2;
	}

	public String toString() {
		return StationID + "　　　　　　　　" + StationName;
	}
}
