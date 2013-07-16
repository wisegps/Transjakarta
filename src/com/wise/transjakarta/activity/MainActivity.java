package com.wise.transjakarta.activity;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import com.wise.transjakarta.bean.RoadInfo;
import com.wise.transjakarta.config.UrlConfig;
import com.wise.transjakarta.net.NetThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
	
	private ProgressBar waitBar = null;
	private MyHandler myHandler = null;
	private RoadInfo roadInfo = null;
	private ArrayList<RoadInfo> roadInfos = new ArrayList<RoadInfo>();
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waitBar = (ProgressBar) findViewById(R.id.progressBar1);
        myHandler = new MyHandler();
        new Thread(new NetThread.GetRodListThread(myHandler, UrlConfig.url, UrlConfig.nameSpace, UrlConfig.MethodGetRoadName, UrlConfig.timeout, 0)).start();
    }
    
    class MyHandler extends Handler{
    	String result;
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			waitBar.setVisibility(View.GONE);
			result = msg.obj.toString();
			String[] array1 = result.split("Road=anyType");
			String[] array2 = null;
			for(int i = 1 ; i < array1.length ; i++){
				array2 = array1[i].split("; ");
			}
			for(int j = 0 ; j < array2.length - 3 ; j ++){
					if(array2[j].indexOf("=") > 0){
						roadInfo = new RoadInfo();
						roadInfo.setRoadID(Integer.valueOf(array2[0].substring(array2[0].indexOf("=") + 1)));
						roadInfo.setRoadName(array2[1].substring(array2[1].indexOf("=") + 1));
						roadInfo.setOffset(Integer.valueOf(array2[2].substring(array2[2].indexOf("=") + 1)));
						roadInfo.setSpacing(Integer.valueOf(array2[3].substring(array2[3].indexOf("=") + 1)));
						roadInfo.setPlatform(Integer.valueOf(array2[4].substring(array2[4].indexOf("=") + 1)));
						roadInfo.setSpeed(Integer.valueOf(array2[5].substring(array2[5].indexOf("=") + 1)));
						roadInfos.add(roadInfo);
					}
			}
			
			//Ìø×ª
			roadInfo.setRoadInfos(roadInfos);
			Intent intent = new Intent();
			intent.putExtra("roadInfos", roadInfo);
			intent.setClass(MainActivity.this,RoadActivity.class);
			startActivity(intent);
			MainActivity.this.finish();
			
		}
    }
}
