package app.makito.kjs;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import app.makito.kjs.*;
import java.util.*;
import android.app.ActivityManager.RunningServiceInfo;
import android.widget.AdapterView.*;
import android.view.*;

public class Settings extends Activity
 {
    public int RTIsRunning = 0;
	protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.settings);  

		//载入列表信息
		//String[] str = {"数据同步服务","机巧少女不会受伤"};
		final ArrayList<String> list = new ArrayList<String>();
		list.add("数据同步服务");
		list.add("通知栏提醒");
		ListView listView = (ListView)findViewById(R.id.SettingsList);
		ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, list);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setAdapter(arrAdapter);
		
		switch(isRtRunning()) {
			case 0:
			listView.setItemChecked(0, false);
			break;
			case 1:
			listView.setItemChecked(0, true);
			break;
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					String mItem = list.get(p3);
					if(mItem.equals("数据同步服务")){
						switch(isRtRunning()){
							case 0:
							RTServiceStart();
							break;
							case 1:
							RTServiceStop();
							break;
						}
						//Toast.makeText(Settings.this, "Data Sync Service Clicked", Toast.LENGTH_LONG).show();
					}
				}
		});

	}

	private void RTServiceStart() {
		startService(new Intent("app.makito.kjs.RT_SERVICE"));
	}
	private void RTServiceStop() {
		stopService(new Intent("app.makito.kjs.RT_SERVICE"));
	}
	public void onBackPressed() {
		this.finish();
	}

	public int isRtRunning() {
		int isR = 0;
		Context c = getApplicationContext();
		ActivityManager ServiceAM=(ActivityManager)c.getSystemService(Context.ACTIVITY_SERVICE); 
		ArrayList<RunningServiceInfo> runningServices = (ArrayList<RunningServiceInfo>) ServiceAM.getRunningServices(40);
		//获取最多40个当前正在运行的服务，放进ArrList里,以现在手机的处理能力，要是超过40个服务，估计已经卡死，所以不用考虑超过40个该怎么办
		for(int i = 0 ; i<runningServices.size();i++)//循环枚举对比
		{
			if(runningServices.get(i).service.getClassName().toString().equals("app.makito.kjs.RealtimeService"))
			{
				isR = 1;
			}
		}
		return isR;
	}
}
