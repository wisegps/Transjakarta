package com.wise.transjakarta.activity;

import java.util.ArrayList;
import java.util.List;
import com.wise.transjakarta.bean.BusArrivalInfo;
import com.wise.transjakarta.bean.NeedStationInfo;
import com.wise.transjakarta.config.UrlConfig;
import com.wise.transjakarta.service.WebService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class BusArrivalActivity extends Activity{
	private static final int GetGetDestinationA = 1;
	private static final int GetGetDestinationB = 2;
	private static final int GetBusArrivalA = 3;
	private static final int GetBusArrivalB = 4;
	TextView tv_bus_arraival_A,tv_bus_arraival_B,tv_DestinationA,tv_DestinationB;
	
	boolean isRun = true;
	int PointIDA;
	int PointIDB;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_arrival);
		tv_DestinationA = (TextView)findViewById(R.id.tv_DestinationA);
		tv_DestinationB = (TextView)findViewById(R.id.tv_DestinationB);
		tv_bus_arraival_A = (TextView)findViewById(R.id.tv_bus_arraival_A);
		tv_bus_arraival_B = (TextView)findViewById(R.id.tv_bus_arraival_B);
		Intent intent = getIntent();
		PointIDA = intent.getIntExtra("PointIDA", 0);
		PointIDB = intent.getIntExtra("PointIDB", 0);
		
		new Thread(new GetDestination2()).start();
		new Thread(new GetBusArrivalThread()).start();
	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GetGetDestinationA:
				tv_DestinationA.setText("去往："+ msg.obj.toString());
				break;
			case GetGetDestinationB:
				tv_DestinationB.setText("去往："+ msg.obj.toString());
				break;
			case GetBusArrivalA:
				tv_bus_arraival_A.setText(msg.obj.toString());
				break;
			case GetBusArrivalB:
				tv_bus_arraival_B.setText(msg.obj.toString());
				break;
			}
		}		
	};
	
	class GetDestination2 extends Thread{
		@Override
		public void run() {
			super.run();
			try {
				String request1 = WebService.GetDestination2(UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetDestination2, PointIDA, 20000);
				//request1.substring(request1.indexOf("="), request1.indexOf(";"));
				String request2 = WebService.GetDestination2(UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetDestination2, PointIDB, 20000);
				//request2.substring(request2.indexOf("="), request2.indexOf(";"));
				
				Message message1 = new Message();
				message1.what = GetGetDestinationA;
				message1.obj = request1.substring(request1.indexOf("=")+1, request1.indexOf(";"));
				handler.sendMessage(message1);
				
				Message message2 = new Message();
				message2.what = GetGetDestinationB;
				message2.obj = request2.substring(request2.indexOf("=")+1, request2.indexOf(";"));
				handler.sendMessage(message2);
			} catch (Exception e) {
				e.printStackTrace();
				new Thread(new GetDestination2()).start();
			}
		}
	}
	
	class GetBusArrivalThread extends Thread{
		@Override
		public void run() {
			super.run();
			while(isRun){
				//获取公交车辆信息
				String request = "";
				try {
					request = WebService.SoapGetNearCar(UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetNear2Vehicle, PointIDA, 20000);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				System.out.println("request="+request);
				if(request == ""){
					System.out.println("获取公交信息异常");
				}else{
					float Distance = 0;
                    float AvgSpeed = 0;
					//解析车辆数据
					List<BusArrivalInfo> busArrivalInfos = GetBusArrivalEx(request);
					if(busArrivalInfos.size() > 0){
						String result1 = "";
						//如果当前车的起始站点为当前查询的站点，且Percent为0，显示到达
						if(busArrivalInfos.get(0).getStationID() == PointIDA && busArrivalInfos.get(0).getPercent() == 0){
							result1 = busArrivalInfos.get(0).getVName()+"车辆到站";
						}else{
							//System.out.println("车辆所在站点="+busArrivalInfos.get(0).getStationID());
							String resultGetNeedStationNext = "";
							try {
								resultGetNeedStationNext = WebService.GetNeedStation(UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetNeedStation,busArrivalInfos.get(0).getStationID(), PointIDA,20000);
							} catch (Exception e) {
								e.printStackTrace();
							}
							List<NeedStationInfo> needStationInfos = GetNeedStation(resultGetNeedStationNext);
							if(needStationInfos.size() ==2){
								Distance = (needStationInfos.get(1).getMile() - needStationInfos.get(0).getMile()) * (100 - busArrivalInfos.get(0).getPercent())/100;
								System.out.println((needStationInfos.get(1).getMile() - needStationInfos.get(0).getMile()) + "*" + (100 - busArrivalInfos.get(0).getPercent()) + "%=" + Distance);
								AvgSpeed = needStationInfos.get(0).getSegAvgSpeed() * 1000/60;
							}else if(needStationInfos.size() > 2){
								Distance = (needStationInfos.get(needStationInfos.size()-1).getMile() - needStationInfos.get(0).getMile()) - (needStationInfos.get(1).getMile() - needStationInfos.get(0).getMile()) * busArrivalInfos.get(0).getPercent()/100;
								float TotalSpeed = 0;
								int TotalNum = 0;
								for(int i = 0 ; i < needStationInfos.size(); i++){
									TotalSpeed += needStationInfos.get(i).getSegAvgSpeed();
									TotalNum +=1;
								}
								AvgSpeed = TotalSpeed/TotalNum*1000/60;
							}
							if(Distance <= 200){
								result1 = busArrivalInfos.get(0).getVName()+"车辆即将到达";
							}else{
								if((int)(Distance/AvgSpeed) == 0){
									result1 = busArrivalInfos.get(0).getVName()+"车辆即将到达";
								}else{
									result1 = busArrivalInfos.get(0).getVName()+"车辆到达大概时间："+(int)(Distance/AvgSpeed) + "min";
								}
							}
						}

						Message message1 = new Message();
						message1.what = GetBusArrivalA;
						message1.obj = result1;
						handler.sendMessage(message1);
					};
					
				/*if(busArrivalInfos.size() > 3){
						System.out.println("第二辆车");
						if(busArrivalInfos.get(1).getStationID() == PointIDA && busArrivalInfos.get(1).getPercent() == 0){
							System.out.println(busArrivalInfos.get(1).getVName()+"车辆到站");
						}else{
							//System.out.println("车辆所在站点="+busArrivalInfos.get(1).getStationID());
							String resultGetNeedStationNext = "";
							try {
								resultGetNeedStationNext = WebService.GetNeedStation(UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetNeedStation,busArrivalInfos.get(1).getStationID(), PointIDA,20000);
							} catch (Exception e) {
								e.printStackTrace();
							}
							List<NeedStationInfo> needStationInfos = GetNeedStation(resultGetNeedStationNext);
							if(needStationInfos.size() ==2){
								Distance = (needStationInfos.get(1).getMile() - needStationInfos.get(0).getMile()) * (100 - busArrivalInfos.get(1).getPercent())/100;
								System.out.println((needStationInfos.get(1).getMile() - needStationInfos.get(0).getMile()) + "*" + (100 - busArrivalInfos.get(1).getPercent()) + "%=" + Distance);
								AvgSpeed = needStationInfos.get(1).getSegAvgSpeed() * 1000/60;
							}else if(needStationInfos.size() > 2){
								Distance = (needStationInfos.get(needStationInfos.size()-1).getMile() - needStationInfos.get(0).getMile()) - (needStationInfos.get(1).getMile() - needStationInfos.get(0).getMile()) * busArrivalInfos.get(1).getPercent()/100;
								float TotalSpeed = 0;
								int TotalNum = 0;
								for(int i = 0 ; i < needStationInfos.size(); i++){
									TotalSpeed += needStationInfos.get(i).getSegAvgSpeed();
									TotalNum +=1;
								}
								AvgSpeed = TotalSpeed/TotalNum*1000/60;
							}
							if(Distance <= 200){
								System.out.println(busArrivalInfos.get(1).getVName()+"车辆即将到达");
							}else{
								System.out.println(busArrivalInfos.get(1).getVName()+"车辆到达大概时间："+(int)(Distance/AvgSpeed));
							}
						}
					};*/
				}
				
				
				//获取公交车辆信息
				String request1 = "";
				try {
					request1 = WebService.SoapGetNearCar(UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetNear2Vehicle, PointIDB, 20000);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if(request1 == ""){
					System.out.println("获取公交信息异常");
				}else{
					float Distance = 0;
                    float AvgSpeed = 0;
					//解析车辆数据
					List<BusArrivalInfo> busArrivalInfos = GetBusArrivalEx(request1);
					if(busArrivalInfos.size() > 0){
						String result1 = "";
						//如果当前车的起始站点为当前查询的站点，且Percent为0，显示到达
						if(busArrivalInfos.get(0).getStationID() == PointIDB && busArrivalInfos.get(0).getPercent() == 0){
							result1 = busArrivalInfos.get(0).getVName()+"车辆到站";
						}else{
							//System.out.println("车辆所在站点="+busArrivalInfos.get(0).getStationID());
							String resultGetNeedStationNext = "";
							try {
								resultGetNeedStationNext = WebService.GetNeedStation(UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetNeedStation,busArrivalInfos.get(0).getStationID(), PointIDB,20000);
							} catch (Exception e) {
								e.printStackTrace();
							}
							List<NeedStationInfo> needStationInfos = GetNeedStation(resultGetNeedStationNext);
							if(needStationInfos.size() ==2){
								Distance = (needStationInfos.get(1).getMile() - needStationInfos.get(0).getMile()) * (100 - busArrivalInfos.get(0).getPercent())/100;
								System.out.println((needStationInfos.get(1).getMile() - needStationInfos.get(0).getMile()) + "*" + (100 - busArrivalInfos.get(0).getPercent()) + "%=" + Distance);
								AvgSpeed = needStationInfos.get(0).getSegAvgSpeed() * 1000/60;
							}else if(needStationInfos.size() > 2){
								Distance = (needStationInfos.get(needStationInfos.size()-1).getMile() - needStationInfos.get(0).getMile()) - (needStationInfos.get(1).getMile() - needStationInfos.get(0).getMile()) * busArrivalInfos.get(0).getPercent()/100;
								float TotalSpeed = 0;
								int TotalNum = 0;
								for(int i = 0 ; i < needStationInfos.size(); i++){
									TotalSpeed += needStationInfos.get(i).getSegAvgSpeed();
									TotalNum +=1;
								}
								AvgSpeed = TotalSpeed/TotalNum*1000/60;
							}
							if(Distance <= 200){
								result1 = busArrivalInfos.get(0).getVName()+"车辆即将到达";
							}else{
								if((int)(Distance/AvgSpeed) == 0){
									result1 = busArrivalInfos.get(0).getVName()+"车辆即将到达";
								}else{
									result1 = busArrivalInfos.get(0).getVName()+"车辆到达大概时间："+(int)(Distance/AvgSpeed) + "min";
								}
							}
						}

						Message message1 = new Message();
						message1.what = GetBusArrivalB;
						message1.obj = result1;
						handler.sendMessage(message1);
					};
				}
				try {
					System.out.println("线程休眠20s");
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("线程关闭,isRun = " + isRun);
		}
	}
	/**
	 * 解析公交数据
	 * @param result
	 * @return
	 */
	private List<BusArrivalInfo> GetBusArrivalEx(String result){
		List<BusArrivalInfo> busArrivalInfos = new ArrayList<BusArrivalInfo>();
		String[] str1 = result.split("Bus=anyType");
		for(int i = 1 ; i < str1.length ; i++){
			String[] str2 = str1[i].split("; ");
			BusArrivalInfo busArrivalInfo = new BusArrivalInfo();
			busArrivalInfo.setStationID(Integer.valueOf(str2[2].substring(10)));
			busArrivalInfo.setPercent(Integer.valueOf(str2[3].substring(8)));
			busArrivalInfo.setVName(str2[1].substring(7));
			busArrivalInfos.add(busArrivalInfo);
		}
		return busArrivalInfos;
	}
	/**
	 * 解析站点之间的数据
	 * @param result
	 * @return
	 */
	private List<NeedStationInfo> GetNeedStation(String result){
		List<NeedStationInfo> needStationInfos = new ArrayList<NeedStationInfo>();
		String[] str1 = result.split("Station=anyType");
		for (int i =1; i < str1.length; i++) {
			String[] str2 = str1[i].split("; ");
			NeedStationInfo needStationInfo = new NeedStationInfo();
			needStationInfo.setMile(Integer.valueOf(str2[4].substring(5)));
			needStationInfo.setSegAvgSpeed(Integer.valueOf(str2[6].substring(12)));			
			needStationInfos.add(needStationInfo);
		}
		return needStationInfos;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isRun = false;
	}
}