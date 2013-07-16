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
}
