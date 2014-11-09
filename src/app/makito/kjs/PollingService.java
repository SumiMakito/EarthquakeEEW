package app.makito.kjs;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.params.*;
import org.json.*;

public class PollingService extends Service
{
	Handler PollHandler = new Handler();



    @Override  
    public IBinder onBind(Intent intent)
	{  
        return null;  
    }  

    @Override  
    public void onCreate()
	{  
        super.onCreate();  
    }  

    @Override  
    public void onStart(Intent intent, int startId)
	{  
		//设置休眠锁
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock pswl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PollingServiceWL");
		pswl.acquire();
		
		IntentFilter iFNFSOn = new IntentFilter();
		iFNFSOn.addAction("app.makito.kjs.NF_ON");
		IntentFilter iFNFSOff = new IntentFilter();
		iFNFSOff.addAction("app.makito.kjs.NF_OFF");
		registerReceiver(NFSwitcherOn, iFNFSOn);
		registerReceiver(NFSwitcherOff, iFNFSOff);

		//Toast.makeText(this, "Polling Service Started", Toast.LENGTH_LONG).show();

		PollHandler.postDelayed(new Runnable() {

				NotificationManager NFManager = (NotificationManager) PollingService.this.getSystemService(NOTIFICATION_SERVICE);
				Notification NFNotification = new Notification(R.drawable.ic_launcher, "同步服务已启动", System.currentTimeMillis());
				int lastId = 0;
				public void run()
				{
					String syncUrl = "http://makito.duapp.com/api/ceic_sync.php";
					try
					{
						NFNotification.setLatestEventInfo(getApplicationContext(), "EEW 地震速报", "已连接到同步服务器", PendingIntent.getActivity(PollingService.this, 0, new Intent("app.makito.kjs.MainActivity"), 0));
						NFNotification.flags |= NFNotification.FLAG_ONGOING_EVENT;
						NFManager.notify(1, NFNotification);

						String syncId = getContent(syncUrl);
						int Id = Integer.parseInt(syncId);
						if (lastId == 0)
						{
							//说明第一次获取编号
							//Toast.makeText(PollingService.this, "Debug: The first time to get id\nId >" + Id, Toast.LENGTH_SHORT).show();
							lastId = Id;
							PollHandler.postDelayed(this, 1000);
						}
						else
						{
							if (lastId == Id)
							{
								//Toast.makeText(PollingService.this, "Debug: Nothing is new\nCurrentId >" + Id, Toast.LENGTH_SHORT).show();
								//编号未改变，无新事件发生，不做处理
							}
							else
							{
								lastId = Id;
								String url = "http://makito.duapp.com/api/ceic_eew.php";
								String content = getContent(url);
								JSONArray array = new JSONArray(content);
								JSONObject object = array.getJSONObject(0);
								//此处发出速报信号
								Intent mSignal = new Intent("app.makito.kjs.ALERT");
								String LOC = object.getString("LOCATION_C");
								String M = object.getString("M");
								String DEPTH = object.getString("EPI_DEPTH");
								String OTIME = object.getString("O_TIME");
								String X = object.getString("EPI_LAT");
								String Y = object.getString("EPI_LON");

								mSignal.putExtra("LOC", LOC);
								mSignal.putExtra("M", M);
								mSignal.putExtra("DEPTH", DEPTH);
								mSignal.putExtra("OTIME", OTIME);
								mSignal.putExtra("X", X);
								mSignal.putExtra("Y", Y);
								//发送速报信号
								sendBroadcast(mSignal);
							}
						}
					}
					catch (Exception e)
					{
						NFNotification.setLatestEventInfo(getApplicationContext(), "EEW 地震速报", "暂时不能与同步服务器建立连接", PendingIntent.getActivity(PollingService.this, 0, new Intent("app.makito.kjs.MainActivity"), 0));
						NFNotification.flags |= NFNotification.FLAG_ONGOING_EVENT;
						NFManager.notify(1, NFNotification);

						e.printStackTrace();
						//System.out.println(e.getMessage());
						//Toast.makeText(PollingService.this, e.getMessage(), Toast.LENGTH_LONG).show();
					}
					PollHandler.postDelayed(this, 30000);
				}
			}, 1000);

        super.onStart(intent, startId);  
    }  

    @Override  
    public int onStartCommand(Intent intent, int flags, int startId)
	{ 
        return super.onStartCommand(intent, flags, startId);  
    } 
	public void onDestroy()
	{
		NotificationManager NFManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		NFManager.cancel(1);
		//Toast.makeText(this, "Polling Service Stopped", Toast.LENGTH_LONG).show();
		super.onDestroy();
	}

	private String getContent(String url) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		HttpPost   httpRequest=new HttpPost(url);
		HttpClient httpClient = new DefaultHttpClient();

		HttpParams params = httpClient.getParams();

		HttpConnectionParams.setConnectionTimeout(params, 1000);
		HttpConnectionParams.setSoTimeout(params, 5000);

		HttpResponse response = httpClient.execute(httpRequest);
		HttpEntity entity = response.getEntity();

		if (response.getStatusLine().getStatusCode() == 200)
		{
			if (entity != null)
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(
															   entity.getContent()));
				String line = null;
				while ((line = reader.readLine()) != null)
				{
					buffer.append(line);
				}
			}
		}

		return buffer.toString();
	}
	
	private BroadcastReceiver NFSwitcherOn = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent)
		{
			
		}};
	private BroadcastReceiver NFSwitcherOff = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent)
		{
			NotificationManager NFManager = (NotificationManager) PollingService.this.getSystemService(NOTIFICATION_SERVICE);
			NFManager.cancel(1);
		}};
}
