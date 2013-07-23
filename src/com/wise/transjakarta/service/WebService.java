package com.wise.transjakarta.service;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class WebService {
	/**
	 * ��ȡ���еĹ���·��
	 * @param url  ���ʵ�ַ
	 * @param nameSpace  �����ռ�
	 * @param methodName  webService�ӿ�
	 * @param timeout   ���ӳ�ʱ
	 * @return xml
	 * @throws Exception
	 */
	public static String SoapGetRoadList(String url , String nameSpace ,String methodName , int timeout) throws Exception{
		
		String soapAction = nameSpace + methodName;  
		 
		SoapObject rpc = new SoapObject(nameSpace, methodName);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
	    envelope.bodyOut = rpc;  
	    
	    envelope.dotNet = false;   
        // �������ӳ�ʱʱ��Ϊ20��   
	    HttpTransportSE transportSE = new HttpTransportSE(url, timeout);
	    
	    transportSE.call(soapAction, envelope);
	    
    	String result = envelope.bodyIn.toString();
    	
		return result;
	}
	
	
	/**
	 * ͨ������·�߻�ȡվ����Ϣ
	 * @param url  ���ʵ�ַ
	 * @param nameSpace  �����ռ�
	 * @param methodName  webService�ӿ�
	 * @param roadName   ����·��
	 * @param timeout   ���ӳ�ʱ
	 * @return xml
	 * @throws Exception
	 */
	public static SoapObject SoapGetRoadStationInfo(String url , String nameSpace ,String methodName, String roadName, int timeout) throws Exception{
		
		//��ʼ��SOAP����
    	SoapObject request = new SoapObject(nameSpace, methodName);
    	//���ò���
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
	 * ��ȡ��һ������ʱ��
	 * @param url
	 * @param nameSpace
	 * @param methodName
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static String SoapGetNearCar(String url , String nameSpace ,String methodName ,int stationId, int timeout) throws Exception{
		
		//��ʼ��SOAP����
    	SoapObject request = new SoapObject(nameSpace, methodName);
    	//���ò���
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
