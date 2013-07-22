package com.wise.transjakarta.activity;
import java.util.ArrayList;

import com.wise.transjakarta.bean.RoadInfo;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {
	private TabHost tabHost = null;
	private Intent ArrivalIntent = null;
	private ArrayList<RoadInfo> roadInfos = new ArrayList<RoadInfo>();
	private RoadInfo roadInfo = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
      setContentView(R.layout.main_activity);      
      tabHost = getTabHost();
      ArrivalIntent = getIntent();
      roadInfo = (RoadInfo) ArrivalIntent.getSerializableExtra("roadInfos");
      roadInfos = roadInfo.getRoadInfos();
      ArrivalIntent.setClass(MainActivity.this,RoadActivity.class);
      
      //ÃÌº”±Í«©
      addArrival();
      addAnnouncement();
      addInfoSearch();
      tabHost.setCurrentTab(0);
      
    //±Í«©«–ªªº‡Ã˝
      tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {   
          public void onTabChanged(String tabId) {   
          	Toast.makeText(getApplicationContext(), "«–ªª", 0).show();
          }   
      });
	}
	
	
	//ÃÌº”Arrival±Í«©“≥
    public void addArrival(){
		TabHost.TabSpec ArrivalSpec = getTabHost().newTabSpec("Est Arrival");
		ArrivalSpec.setIndicator("Est Arrival", getResources().getDrawable(R.drawable.ic_launcher));
		
		ArrivalSpec.setContent(ArrivalIntent);
		getTabHost().addTab(ArrivalSpec);

	}
	//ÃÌº”Announcement±Í«©“≥
    public void addAnnouncement(){
		Intent AnnouncementIntent = new Intent();
		AnnouncementIntent.setClass(MainActivity.this,Announcement.class);
		TabHost.TabSpec AnnouncementSpec = getTabHost().newTabSpec("Announcement");
		AnnouncementSpec.setIndicator("Announcement", getResources().getDrawable(R.drawable.ic_launcher));
		
		AnnouncementSpec.setContent(AnnouncementIntent);
		getTabHost().addTab(AnnouncementSpec);
    }
  //ÃÌº”InfoSearch±Í«©“≥
    public void addInfoSearch(){
    	Intent InfoSearchIntent = new Intent();
    	InfoSearchIntent.setClass(MainActivity.this,Announcement.class);
		TabHost.TabSpec InfoSearchSpec = getTabHost().newTabSpec("Info Search");
		InfoSearchSpec.setIndicator("Info Search", getResources().getDrawable(R.drawable.ic_launcher));
		
		InfoSearchSpec.setContent(InfoSearchIntent);
		getTabHost().addTab(InfoSearchSpec);
    }
}
