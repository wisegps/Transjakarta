package com.wise.transjakarta.activity;
import java.util.ArrayList;
import org.ksoap2.serialization.SoapObject;
import com.wise.transjakarta.bean.RoadInfo;
import com.wise.transjakarta.bean.RoadStationInf;
import com.wise.transjakarta.config.UrlConfig;
import com.wise.transjakarta.net.NetThread;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class RoadActivity extends Activity {
	private ListView listView = null;
	private ArrayList<String> objects = new ArrayList<String>();
	private Intent intent = null;
	private ArrayList<RoadInfo> roadInfos = new ArrayList<RoadInfo>();
	private RoadInfo roadInfo = null;
	
	private MyHandler myHandler = null;
	
	private static final int GET_STATION_INFO = 38;
	private static final int GET_NEAR_CAR = 39;
	
	private ArrayList<RoadStationInf> roadStationInfs = new ArrayList<RoadStationInf>();
	
	
	private ProgressDialog stationDialog = null;
	private ProgressDialog nearCarDialog = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.road_info);
		intent = getIntent();
		
		//初始化控件
        initView();
	}
	private void initView() {
		//读取数据
		roadInfo = (RoadInfo) intent.getSerializableExtra("roadInfos");
		roadInfos = roadInfo.getRoadInfos();
        
		listView = (ListView) findViewById(R.id.route);
		
        //动态添加ListView控件内容

		
		myHandler = new MyHandler();
		
        final ArrayAdapter<RoadInfo> adapter = new ArrayAdapter<RoadInfo>(getApplicationContext(), android.R.layout.simple_list_item_1, roadInfos);
        adapter.setDropDownViewResource(android.R.drawable.list_selector_background);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//获取这条路线上面的站点信息
				new Thread(new NetThread.GetRoadStationInfoThread(myHandler, UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetRoadStationInfo,roadInfos.get(arg2).getRoadName(), UrlConfig.timeout, GET_STATION_INFO)).start();
				stationDialog = ProgressDialog.show(RoadActivity.this, getString(R.string.find_station), getString(R.string.find_in),true);
			}
		});
	}  
	
	class MyHandler extends Handler{
		SoapObject result = null;
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case GET_STATION_INFO:
				if(msg.obj != null){
					stationDialog.dismiss();
					result = (SoapObject) msg.obj;
					//显示所有公交站点信息
					
					System.out.println(result);
					ShowstationList(ParseSocpObject(String.valueOf(result)));
				}
				break;
			case GET_NEAR_CAR:
				if(msg.obj != null){
					nearCarDialog.dismiss();
					System.out.println("result----------->" + msg.obj.toString());
				}
				break;
			}
		}
	}
	
	
	//解析访问webService返回的SocpObject对象
	public ArrayList<RoadStationInf> ParseSocpObject(String data){
		RoadStationInf roadStationInf = null;
		ArrayList<String[]> strs = new ArrayList<String[]>();
		String[] str1 = data.split("Station=anyType");
		for(int i = 1 ; i < str1.length ; i ++){
			String[] str2 = str1[i].split(";");
			strs.add(str2);
		}
		for(int i = 0; i < strs.size() ; i ++){
			String[] str = strs.get(i);
			for(int j = 0 ; j < str.length ; j ++ ){
				if(str[j].indexOf("=") > 0){
					if(str[j].indexOf("{}") > 0){
						str[j] = str[j].replace("{}", "");
						roadStationInf = new RoadStationInf();
						roadStationInf.setStationID(Integer.valueOf(str[0].substring(str[0].indexOf("=") + 1)));
						roadStationInf.setStationName(str[1].substring(str[1].indexOf("=") + 1));
						roadStationInf.setCenterX(Integer.valueOf(str[2].substring(str[2].indexOf("=") + 1)));
						roadStationInf.setCenterY(Integer.valueOf(str[3].substring(str[3].indexOf("=") + 1)));
						roadStationInf.setMile(Integer.valueOf(str[4].substring(str[4].indexOf("=") + 1)));
						roadStationInf.setPlatform(Integer.valueOf(str[5].substring(str[5].indexOf("=") + 1)));
						roadStationInf.setSegAvgSpeed(Integer.valueOf(str[6].substring(str[6].indexOf("=") + 1)));
						roadStationInf.setStationID2(Integer.valueOf(str[7].substring(str[7].indexOf("=") + 1)));
						roadStationInf.setRoadName1(str[8].substring(str[8].indexOf("=") + 1));
						roadStationInf.setRoadName2(str[9].substring(str[9].indexOf("=") + 1));
						roadStationInfs.add(roadStationInf);
					}
				}
			}
		}
		return roadStationInfs;
	}

	
	//显示站点信息
	public void ShowstationList(ArrayList<RoadStationInf> list){
		
		for(RoadStationInf roadStationInf : list){
			
			
			System.out.println("getStationID----->" + roadStationInf.getStationID());
			System.out.println("getStationName----->" + roadStationInf.getStationName());
			
		}
		
		if(list.size() == 0 ){
			Toast.makeText(getApplicationContext(), "没有数据可显示", 0).show();
		}else{
		//初始化对话框组件
				LayoutInflater layoutInfalter = LayoutInflater.from(RoadActivity.this);
				View myView = layoutInfalter.inflate(R.layout.station_info_dialog,null);
				final ListView stationList = (ListView) myView.findViewById(R.id.station_info);
				//显示对话框
				AlertDialog.Builder myDialog = new AlertDialog.Builder(RoadActivity.this);
				ArrayAdapter<RoadStationInf> adapter = new ArrayAdapter<RoadStationInf>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
				stationList.setAdapter(adapter);
				stationList.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
						Toast.makeText(getApplicationContext(), "点击了" + arg2, 0).show();
						RoadStationInf rsi = (RoadStationInf) stationList.getItemAtPosition(arg2);
						//访问WebService接口
						new Thread(new NetThread.GetNearCarOnTimeThread(myHandler, UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetNear2Vehicle,rsi.getStationID(), UrlConfig.timeout, GET_NEAR_CAR)).start();
						nearCarDialog = ProgressDialog.show(RoadActivity.this, "搜索最近公交车","正在搜索，请稍等.....",true);
					}
				});
				
				myDialog.setTitle("All Station");
				myDialog.setIcon(android.R.drawable.ic_dialog_info);
				myDialog.setView(myView);
				myDialog.show();
		}
	}
}
