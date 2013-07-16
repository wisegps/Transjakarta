package com.wise.transjakarta.activity;

import java.util.ArrayList;

import com.wise.transjakarta.bean.RoadInfo;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RoadActivity extends TabActivity {
	private TabHost tabHost = null;
	private ListView listView = null;
	private ArrayList<String> objects = new ArrayList<String>();
	private Intent intent = null;
	private ArrayList<RoadInfo> roadInfos = new ArrayList<RoadInfo>();
	private RoadInfo roadInfo = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//初始化控件
        initView();
	}
	private void initView() {
		intent = getIntent();
		roadInfo = (RoadInfo) intent.getSerializableExtra("roadInfos");
		roadInfos = roadInfo.getRoadInfos();
		
		for(RoadInfo ri : roadInfos){
			System.out.println("name -->" + ri.getRoadName());
		}
		
		
        tabHost = this.getTabHost();
        LayoutInflater li = LayoutInflater.from(this);   
        li.inflate(R.layout.road_info, tabHost.getTabContentView(), true);
        listView = (ListView) findViewById(R.id.route);
        //添加标签
        tabHost.addTab(tabHost.newTabSpec("Tab_1").setContent(R.id.tab1).setIndicator("Est Arrival",   
                        this.getResources().getDrawable(R.drawable.ic_launcher)));   
        tabHost.addTab(tabHost.newTabSpec("Tab_2").setContent(R.id.tab2).setIndicator("Announcement",   
                        this.getResources().getDrawable(R.drawable.ic_launcher)));   
        tabHost.addTab(tabHost.newTabSpec("Tab_3").setContent(R.id.tab3).setIndicator("Info Search",   
                        this.getResources().getDrawable(R.drawable.ic_launcher)));
        tabHost.setCurrentTab(1);
        //标签切换监听
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {   
        	
            public void onTabChanged(String tabId) {   
            	Toast.makeText(getApplicationContext(), "切换", 0).show();
            }   
        });
        //动态添加ListView控件内容
        String lineNumber = "bus";
        for(int i = 0 ; i <= 9 ; i ++){
        	objects.add(lineNumber + (i + 1));
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, objects);
        adapter.setDropDownViewResource(android.R.drawable.list_selector_background);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(getApplicationContext(), adapter.getItem(arg2), 0).show();
				
			}
		});
	}  
}
