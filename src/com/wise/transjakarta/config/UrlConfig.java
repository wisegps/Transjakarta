package com.wise.transjakarta.config;

public class UrlConfig {
	//链接地址
	public static final String url = "http://202.78.201.146:8765/wspubsc/Service.asmx";
	//命名空间
	public static final String nameSpace = "http://tempuri.org/";
	//获取线路
	public static final String MethodGetRoadName = "GetRoadName";
	//获取站点
	public static final String MethodGetRoadStationInfo = "GetRoadStationInfo";
	//当前站名称
	public static final String MethodGetCurrentStation = "GetCurrentStation";
	//根据所需站点间车辆信息
	public static final String MethodGetBetweenStation = "GetBetweenStation";
	//找到离当前点最近的两辆车
	public static final String MethodGetNear2Vehicle = "GetNear2Vehicle";
	//所需站点信息
	public static final String MethodGetStation_json = "GetStation_json";
	//找到车辆所出站点到当前站点间所有站点的信息
	public static final String MethodGetNeedStation = "GetNeedStation";
	//取出线路的起点终点
	public static final String MethodGetDestination2 = "GetDestination2";
	//取广告信息
	public static final String MethodGetAD = "GetAD";
	
	//设置连接超时
	public static int timeout = 20000;
	
}
