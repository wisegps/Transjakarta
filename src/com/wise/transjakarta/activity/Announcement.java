package com.wise.transjakarta.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wise.transjakarta.net.GetSystem;
import com.wise.transjakarta.net.NetThread;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author 唐飞 
 */
public class Announcement extends Activity {
	private static final int GetData = 1;
	ListView lv_Announcement;
	List<AnnouncementData> announcementDatas = new ArrayList<AnnouncementData>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcement);
		init();
	}
	Handler handler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
			case GetData:
				jsonData(msg.obj.toString());
				//绑定到list
				NewAdapter newAdapter = new NewAdapter(Announcement.this, announcementDatas);
				lv_Announcement.setAdapter(newAdapter);
				break;
			}
		}		
	};
	private void init(){
		lv_Announcement = (ListView)findViewById(R.id.lv_Announcement);
		String url = "http://www.wiselbs.cn/wspub/service.asmx/GetAnnouncementInfo_json";
		new Thread(new NetThread.GetDataThread(handler, url, GetData)).start();
	}
	/**
	 * 解析数据
	 * @param result
	 */
	private void jsonData(String result){
		try {
			JSONArray jsonArray = new JSONArray(result.substring(1, result.length()-2));
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				System.out.println(jsonObject.getString("Time").substring(6, 19));
				AnnouncementData announcementData = new AnnouncementData();
				announcementData.setTime(GetSystem.getStrTime(jsonObject.getString("Time").substring(6, 19)));
				announcementData.setMessage(jsonObject.getString("Message"));
				announcementData.setKoridor(jsonObject.getString("Koridor"));
				announcementDatas.add(announcementData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class AnnouncementData{
		String Time;
		String Koridor;
		String Message;
		public String getTime() {
			return Time;
		}
		public void setTime(String time) {
			Time = time;
		}
		public String getKoridor() {
			return Koridor;
		}
		public void setKoridor(String koridor) {
			Koridor = koridor;
		}
		public String getMessage() {
			return Message;
		}
		public void setMessage(String message) {
			Message = message;
		}		
	}
	
	public class NewAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		Context mContext;
		List<AnnouncementData> Datas;
		public NewAdapter(Context context,List<AnnouncementData> smsDatas){
			mInflater = LayoutInflater.from(context);
			mContext = context;
			this.Datas = smsDatas;
		}
		public int getCount() {
			return Datas.size();
		}
		public Object getItem(int position) {
			return Datas.get(position);
		}
		public long getItemId(int position) {
			return position;
		}
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.announcement_row, null);
				holder = new ViewHolder();
				holder.tv_announcement_time = (TextView) convertView.findViewById(R.id.tv_announcement_time);
				holder.tv_announcement_Koridor = (TextView)convertView.findViewById(R.id.tv_announcement_Koridor);
				holder.tv_announcement_Message = (TextView)convertView.findViewById(R.id.tv_announcement_Message);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_announcement_time.setText(Datas.get(position).getTime());
			holder.tv_announcement_Koridor.setText(Datas.get(position).getKoridor());
			holder.tv_announcement_Message.setText(Datas.get(position).getMessage());
			return convertView;
		}
		private class ViewHolder {
			TextView tv_announcement_time,tv_announcement_Koridor,tv_announcement_Message;
		}
	}
}