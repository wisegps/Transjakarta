package com.wise.transjakarta.net;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.wise.transjakarta.config.UrlConfig;
import com.wise.transjakarta.service.WebService;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class NetThread {
	
	/**
	 * 获取所有的公交路线
	 */
	public static class GetRodListThread extends Thread{
		Handler tHandler;
		int tWhere;
		String url;
		String nameSpace;
		String method;
		int timeout;

		public GetRodListThread(Handler handler,String url,String nameSpace,String method,int timeout,int Where){
			tHandler = handler;
			tWhere = Where;
			this.url = url;
			this.nameSpace = nameSpace;
			this.method = method;
			this.timeout = timeout;
		}
		@Override
		public void run() {
			super.run();
			String str = null;
			try {
				str = WebService.SoapGetRoadList(url, nameSpace, method, timeout);
			} catch (Exception e) {
				Log.e("error------>","异常");
				e.printStackTrace();
			}finally{
				Message msg = new Message();
				msg.what = tWhere;
				msg.obj = str;
				tHandler.sendMessage(msg);
			}
		}
	}
	
	
	/**
	 * 获取指定公交路线站点系信息
	 */
	public static class GetRoadStationInfoThread extends Thread{
		Handler tHandler;
		int tWhere;
		String url;
		String nameSpace;
		String method;
		int timeout;
		String roadName;

		public GetRoadStationInfoThread(Handler handler,String url,String nameSpace,String method, String roadName,int timeout,int Where){
			tHandler = handler;
			tWhere = Where;
			this.url = url;
			this.nameSpace = nameSpace;
			this.method = method;
			this.timeout = timeout;
			this.roadName = roadName;
		}
		@Override
		public void run() {
			super.run();
			SoapObject request = null;
			try {
				request = WebService.SoapGetRoadStationInfo(url, nameSpace, method, roadName, timeout);
			} catch (Exception e) {
				Log.e("error------>","异常");
				e.printStackTrace();
			}finally{
				Message msg = new Message();
				msg.what = tWhere;
				msg.obj = request;
				tHandler.sendMessage(msg);
			}
		}
	}
	
	public static class GetDataThread extends Thread{
		Handler handler;
		String url;
		int what;
		/**
		 * Get获取数据
		 * @param handler
		 * @param url
		 * @param what
		 */
		public GetDataThread(Handler handler,String url,int what){
			this.handler = handler;
			this.url = url;
			this.what =what;
		}
		@Override
		public void run() {
			super.run();
			try {
				URL myURL = new URL(url);
				URLConnection httpsConn = (URLConnection) myURL.openConnection();
				if (httpsConn != null) {
					httpsConn.setConnectTimeout(20*1000);
					httpsConn.setReadTimeout(20*1000);
					InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
					BufferedReader br = new BufferedReader(insr, 1024);
					String data = "";
					String line = "";
					while ((line = br.readLine()) != null) {
						data += line;
					}
					insr.close();
					Message message = new Message();
					message.what = what;
					message.obj = data;
					handler.sendMessage(message);
				}else{
					Message message = new Message();
					message.what = what;
					message.obj = "";
					handler.sendMessage(message);
				}
			}catch (SocketTimeoutException e) {
				e.printStackTrace();
				Message message = new Message();
				message.what = what;
				message.obj = "SocketTimeoutException";
				handler.sendMessage(message);
			}catch (SocketException e) {
				e.printStackTrace();
				Message message = new Message();
				message.what = what;
				message.obj = "SocketTimeoutException";
				handler.sendMessage(message);
			}
			catch (Exception e) {
				e.printStackTrace();
				Message message = new Message();
				message.what = what;
				message.obj = "";
				handler.sendMessage(message);
			}
		}
	}
	
	/**
	 * post请求服务器
	 * @author hp
	 *
	 */
	public static class postDataThread extends Thread{
		Handler handler;
		String url;
		int what;
		List<NameValuePair> params;
		public postDataThread(Handler handler,String url,List<NameValuePair> params,int what){
			this.handler = handler;
			this.url = url;
			this.what = what;
			this.params = params;
		}
		@Override
		public void run(){
			super.run();
			Log.d("tag", url);
			HttpPost httpPost = new HttpPost(url);
			Message message = null;
			HttpClient client = null;
			HttpResponse httpResponse = null;
			try {

				 httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

				 client = new DefaultHttpClient();

				 client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);

				 client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);

				 httpResponse = client.execute(httpPost);

				 if(httpResponse.getStatusLine().getStatusCode() == 200){

					 String strResult = EntityUtils.toString(httpResponse.getEntity());
					 Log.e("net----->",strResult);
					 message = new Message();

					 message.what = what;

					 message.obj = strResult;

					 handler.sendMessage(message);
				 }else{
					 message = new Message();
						message.what = what;
						message.obj = "Exception";
						handler.sendMessage(message);
					 Log.d("tag", "状态" +httpResponse.getStatusLine().getStatusCode());
				 }
			}catch (SocketTimeoutException e) {
				e.printStackTrace();
				message = new Message();
				message.what = what;
				message.obj = "SocketTimeoutException";
				handler.sendMessage(message);
			}catch (SocketException e) {
				e.printStackTrace();
				message = new Message();
				message.what = what;
				message.obj = "SocketTimeoutException";
				handler.sendMessage(message);
			}catch (Exception e) {
				message = new Message();
				message.what = what;
				message.obj = "Exception";
				handler.sendMessage(message);
				e.printStackTrace();	
			}
			//释放资源
			finally{
				if(httpPost != null || params != null || message != null || client != null || httpResponse!= null){
					httpPost = null;
					params = null;
					message = null;
					client = null;
					httpResponse = null;
					System.gc();
				}
			}
		}
	}
	
	public static class GetLocation implements Runnable{
		String tLat;
		String tLon;
		Handler tHandler;
		int tWhere;

		Context mContext;
		/**
		 * 获取地址
		 * @param Lat
		 * @param Lon
		 * @param handler
		 */
		public GetLocation(String Lat, String Lon,Handler handler,int where,Context context) {
			tLat = Lat;
			tLon = Lon;
			tHandler = handler;
			tWhere = where;
			mContext = context;
		}

		public void run() {
			String Address = geocodeAddr(mContext,tLat, tLon);
			Message msg = new Message();
			if(Address == null || Address.equals("")){
				msg.what = tWhere;
				msg.obj = "没找到地址";
				tHandler.sendMessage(msg);
			}else{
				msg.what = tWhere;
				msg.obj = Address;
				tHandler.sendMessage(msg);
			}
		}
	}
	
	public static String geocodeAddr(Context context, String latitude, String longitude) { 
		String addr = ""; 
		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+ latitude +"," + longitude + "&sensor=true";
		URL myURL = null; 
		URLConnection httpsConn = null; 
		try { 
			myURL = new URL(url); 
		} catch (MalformedURLException e) { 
			e.printStackTrace(); 
			return null; 
		} 
		try { 
			httpsConn = (URLConnection) myURL.openConnection(); 
			if (httpsConn != null) { 
				System.out.println("开始读取数据");
				InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8"); 
				BufferedReader br = new BufferedReader(insr,1024); 
				String data = ""; 
				String line = "";
				while((line = br.readLine())!= null){
					data += line ;
				}
				insr.close(); 
				return returnAddress(data);
			} 
		} catch (Exception e) { 
			e.printStackTrace(); 
			return null; 
		}
		return addr; 
	} 
	
	
	/**
	 * 返回地址
	 * @param str
	 * @return
	 */
	public static String returnAddress(String str){
		try {
			JSONObject jsonObject = new JSONObject(str);
			JSONArray jsonArray = jsonObject.getJSONArray("results");
			JSONObject temp = (JSONObject)jsonArray.get(0);
			String address = temp.getString("formatted_address");
			String returnAddress;
			if(address.indexOf("邮政编码") > -1){
				returnAddress = address.substring(0, address.indexOf("邮政编码"));
			}else{
				returnAddress = address;
			}
			return returnAddress;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
}
