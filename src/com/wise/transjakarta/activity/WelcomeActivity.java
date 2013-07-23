package com.wise.transjakarta.activity;

import java.util.ArrayList;

import com.wise.transjakarta.bean.RoadInfo;
import com.wise.transjakarta.config.UrlConfig;
import com.wise.transjakarta.net.NetThread;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

public class WelcomeActivity extends Activity{
	
	private ProgressBar waitBar = null;
	private MyHandler myHandler = null;
	private RoadInfo roadInfo = null;
	private ArrayList<RoadInfo> roadInfos = new ArrayList<RoadInfo>();
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        waitBar = (ProgressBar) findViewById(R.id.progressBar1);
        myHandler = new MyHandler();
        new Thread(new NetThread.GetRodListThread(myHandler, UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetRoadName, UrlConfig.timeout, 0)).start();
    }

	//用于异步接收并且处理相应的结果
	class MyHandler extends Handler{
  	String result;
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
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
					roadInfo = new RoadInfo();
					roadInfo.setRoadID(Integer.valueOf(str[0].substring(str[0].indexOf("=") + 1)));
					roadInfo.setRoadName(str[1].substring(str[1].indexOf("=") + 1));
					roadInfo.setOffset(Integer.valueOf(str[2].substring(str[2].indexOf("=") + 1)));
					roadInfo.setSpacing(Integer.valueOf(str[3].substring(str[3].indexOf("=") + 1)));
					roadInfo.setPlatform(Integer.valueOf(str[4].substring(str[4].indexOf("=") + 1)));
					roadInfo.setSpeed(Integer.valueOf(str[5].substring(str[5].indexOf("=") + 1)));
					roadInfos.add(roadInfo);
				}
			}
			//跳转显示所有路线
			roadInfo.setRoadInfos(roadInfos);
			Intent intent = new Intent();
			intent.putExtra("roadInfos", roadInfo);
			intent.setClass(WelcomeActivity.this,MainActivity.class);
			startActivity(intent);
			WelcomeActivity.this.finish();
		}
  }
}
