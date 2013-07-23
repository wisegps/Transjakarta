package com.wise.transjakarta.service;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class WebService {
	/**
	 * 获取所有的公交路线
	 * @param url  访问地址
	 * @param nameSpace  命名空间
	 * @param methodName  webService接口
	 * @param timeout   链接超时
	 * @return xml
	 * @throws Exception
	 */
	public static String SoapGetRoadList(String url , String nameSpace ,String methodName , int timeout) throws Exception{
		
		String soapAction = nameSpace + methodName;  
		 
		SoapObject rpc = new SoapObject(nameSpace, methodName);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
	    envelope.bodyOut = rpc;  
	    
	    envelope.dotNet = false;   
        // 设置连接超时时间为20秒   
	    HttpTransportSE transportSE = new HttpTransportSE(url, timeout);
	    
	    transportSE.call(soapAction, envelope);
	    
    	String result = envelope.bodyIn.toString();
    	
		return result;
	}
	
	
	/**
	 * 通过公交路线获取站点信息
	 * @param url  访问地址
	 * @param nameSpace  命名空间
	 * @param methodName  webService接口
	 * @param roadName   公交路线
	 * @param timeout   链接超时
	 * @return xml
	 * @throws Exception
	 */
	public static SoapObject SoapGetRoadStationInfo(String url , String nameSpace ,String methodName, String roadName, int timeout) throws Exception{
		
		//初始化SOAP对象
    	SoapObject request = new SoapObject(nameSpace, methodName);
    	//调用参数
    	request.addProperty("p_strRoadName", roadName);
    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	envelope.setOutputSoapObject(request);
    	envelope.dotNet = true;
    	HttpTransportSE ht = new HttpTransportSE(url,timeout);
    	ht.call(nameSpace + methodName, envelope);
		SoapObject result = (SoapObject)envelope.getResponse();
		
		return result;
	}
	/**
	 * 获取下一辆到达时间
	 * @param url
	 * @param nameSpace
	 * @param methodName
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static String SoapGetNearCar(String url , String nameSpace ,String methodName ,int stationId, int timeout) throws Exception{
		
		//初始化SOAP对象
    	SoapObject request = new SoapObject(nameSpace, methodName);
    	//调用参数
    	request.addProperty("p_intPointID", stationId);
    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	envelope.setOutputSoapObject(request);
    	envelope.dotNet = true;
    	HttpTransportSE ht = new HttpTransportSE(url,timeout);
    	ht.call(nameSpace + methodName, envelope);
		SoapObject result = (SoapObject)envelope.getResponse();
		
		return result.toString();
	}
}
