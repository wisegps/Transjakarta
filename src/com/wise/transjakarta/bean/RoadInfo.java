package com.wise.transjakarta.bean;
import java.io.Serializable;
import java.util.ArrayList;
public class RoadInfo implements Serializable{
	
	private int RoadID = 0 ;
	
	private String RoadName = "";
	
	private int Offset = 0;
	
	private int Spacing = 0;
	
	private int Platform = 0;
	
	private int Speed = 0;
	
	private ArrayList<RoadInfo>  roadInfos = null;

	public int getRoadID() {
		return RoadID;
	}

	public void setRoadID(int roadID) {
		RoadID = roadID;
	}

	public String getRoadName() {
		return RoadName;
	}

	public void setRoadName(String roadName) {
		RoadName = roadName;
	}

	public int getOffset() {
		return Offset;
	}

	public void setOffset(int offset) {
		Offset = offset;
	}

	public int getSpacing() {
		return Spacing;
	}

	public void setSpacing(int spacing) {
		Spacing = spacing;
	}

	public int getPlatform() {
		return Platform;
	}

	public void setPlatform(int platform) {
		Platform = platform;
	}

	public int getSpeed() {
		return Speed;
	}

	public void setSpeed(int speed) {
		Speed = speed;
	}

	public ArrayList<RoadInfo> getRoadInfos() {
		return roadInfos;
	}

	public void setRoadInfos(ArrayList<RoadInfo> roadInfos) {
		this.roadInfos = roadInfos;
	}
	public String toString() {
		return RoadName;
	}
}
