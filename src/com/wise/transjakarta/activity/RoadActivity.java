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
		
		//��ʼ���ؼ�
        initView();
	}
	private void initView() {
		//��ȡ����
		roadInfo = (RoadInfo) intent.getSerializableExtra("roadInfos");
		roadInfos = roadInfo.getRoadInfos();
        
		listView = (ListView) findViewById(R.id.route);
		
        //��̬���ListView�ؼ�����

		
		myHandler = new MyHandler();
		
        final ArrayAdapter<RoadInfo> adapter = new ArrayAdapter<RoadInfo>(getApplicationContext(), android.R.layout.simple_list_item_1, roadInfos);
        adapter.setDropDownViewResource(android.R.drawable.list_selector_background);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//��ȡ����·�������վ����Ϣ
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
			//��ʾ����վ����Ϣ
			case GET_STATION_INFO:
				if(msg.obj != null){
					stationDialog.dismiss();
					System.out.println("result------>" + msg.obj);
					if(msg.obj.toString().indexOf("Exception") > 0){
						Toast.makeText(RoadActivity.this, msg.obj.toString(), 0).show();
						return;
					}else{
						result = (SoapObject) msg.obj;
						//��ʾ���й���վ����Ϣ
						
						System.out.println(result);
						ShowstationList(ParseSocpObject(String.valueOf(result)));
					}
				}
				break;
				
				//��ʾ��һ����������ʱ��
			case GET_NEAR_CAR:
				if(msg.obj != null){
					nearCarDialog.dismiss();
					if(msg.obj.toString().indexOf("Exception") > 0){
						Toast.makeText(RoadActivity.this, msg.obj.toString(), 0).show();
						return;
					}else{
						
						
					System.out.println("result----------->" + msg.obj.toString());
					}
				}
				break;
			}
		}
	}
	
	
	//��������webService���ص�SocpObject����
	public ArrayList<RoadStationInf> ParseSocpObject(String data){
		if(roadStationInfs.size() > 0){
			roadStationInfs.clear();
		}
		RoadStationInf roadStationInf = null;
		ArrayList<String[]> strs = new ArrayList<String[]>();
		String[] str1 = data.split("Station=anyType");
		for(int i = 1 ; i < str1.length ; i ++){
			String[] str2 = str1[i].split("; ");
			roadStationInf = new RoadStationInf();
			roadStationInf.setStationID(Integer.valueOf(str2[0].substring(11)));
			roadStationInf.setStationName(str2[1].substring(12));
			roadStationInfs.add(roadStationInf);
			System.out.print(str2[0].substring(11));
			System.out.print(str2[1].substring(12));
		}
		return roadStationInfs;
	}

	
	//��ʾվ����Ϣ
	public void ShowstationList(ArrayList<RoadStationInf> list){
		
		for(RoadStationInf roadStationInf : list){
			
			
			System.out.println("getStationID----->" + roadStationInf.getStationID());
			System.out.println("getStationName----->" + roadStationInf.getStationName());
			
		}
		
		if(list.size() == 0 ){
			Toast.makeText(getApplicationContext(), "û�����ݿ���ʾ", 0).show();
		}else{
		//��ʼ���Ի������
				LayoutInflater layoutInfalter = LayoutInflater.from(RoadActivity.this);
				View myView = layoutInfalter.inflate(R.layout.station_info_dialog,null);
				final ListView stationList = (ListView) myView.findViewById(R.id.station_info);
				//��ʾ�Ի���
				AlertDialog.Builder myDialog = new AlertDialog.Builder(RoadActivity.this);
				ArrayAdapter<RoadStationInf> adapter = new ArrayAdapter<RoadStationInf>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
				stationList.setAdapter(adapter);
				stationList.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
						Toast.makeText(getApplicationContext(), "�����" + arg2, 0).show();
						RoadStationInf rsi = (RoadStationInf) stationList.getItemAtPosition(arg2);
						//����WebService�ӿ�
						new Thread(new NetThread.GetNearCarOnTimeThread(myHandler, UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetNear2Vehicle,rsi.getStationID(), UrlConfig.timeout, GET_NEAR_CAR)).start();
						nearCarDialog = ProgressDialog.show(RoadActivity.this, "�������������","�������������Ե�.....",true);
					}
				});
				
				myDialog.setTitle("All Station");
				myDialog.setIcon(android.R.drawable.ic_dialog_info);
				myDialog.setView(myView);
				myDialog.show();
		}
	}
}
