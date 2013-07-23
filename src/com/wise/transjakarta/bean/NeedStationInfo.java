package com.wise.transjakarta.bean;

public class NeedStationInfo {
	int Mile;
	int SegAvgSpeed;
	public int getMile() {
		return Mile;
	}
	public void setMile(int mile) {
		Mile = mile;
	}
	public int getSegAvgSpeed() {
		return SegAvgSpeed;
	}
	public void setSegAvgSpeed(int segAvgSpeed) {
		SegAvgSpeed = segAvgSpeed;
	}
	@Override
	public String toString() {
		return "NeedStationInfo [Mile=" + Mile + ", SegAvgSpeed=" + SegAvgSpeed
				+ "]";
	}		
}