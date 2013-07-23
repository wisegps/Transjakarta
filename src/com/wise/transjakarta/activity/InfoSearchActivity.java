package com.wise.transjakarta.activity;

import java.util.ArrayList;
import java.util.List;

import com.wise.transjakarta.bean.RoadInfo;
import com.wise.transjakarta.config.UrlConfig;
import com.wise.transjakarta.net.NetThread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class InfoSearchActivity extends Activity {
	
	private AutoCompleteTextView actv = null;
	private MyHandler myHandler = null;
	private List<String> roadLine = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_search);
		actv = (AutoCompleteTextView) findViewById(R.id.et_ListSearch);
		roadLine = new ArrayList<String>();
		myHandler = new MyHandler();
		new Thread(new NetThread.GetRodListThread(myHandler, UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetRoadName, UrlConfig.timeout, 14)).start();
	}
	
	
	//获取自动补全框中的内容
	class MyHandler extends Handler{
		String result;
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 14:
				if(roadLine.size() > 0){
					roadLine.clear();
				}
				result = msg.obj.toString();
				String[] array1 = result.split("Road=anyType");
				String[] array2 = null;
				ArrayList<String[]> lists = new ArrayList<String[]>();
				for(int i = 1 ; i < array1.length ; i++){
					array2 = array1[i].split("; ");
					lists.add(array2);
				}
				for(int i = 0; i < lists.size() ; i ++){
					String[] str = lists.get(i);
					if(str[i].indexOf("=") > 0){
						roadLine.add(str[1].substring(str[1].indexOf("=") + 1));
					}
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(InfoSearchActivity.this,android.R.layout.simple_dropdown_item_1line, roadLine);
				actv.setAdapter(adapter);
				actv.setThreshold(0);
				
				
				
				break;
			}
		}
	}
}
