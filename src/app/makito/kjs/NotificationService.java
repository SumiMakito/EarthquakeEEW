package app.makito.kjs;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.util.*;

public class NotificationService extends Service {
	//private static final String TAG = "ServiceDemo" ;  

    @Override  
    public IBinder onBind(Intent intent) {  
        //Log.v(TAG, "ServiceDemo onBind");  
        return null;  
    }  

    @Override  
    public void onCreate() {  
        //Log.v(TAG, "ServiceDemo onCreate");
        super.onCreate();  
    }  

    @Override  
    public void onStart(Intent intent, int startId) {  
        //Log.v(TAG, "ServiceDemo onStart"); 
		//Toast.makeText(this, "Notification Service Started", Toast.LENGTH_LONG).show();
		
		NotificationManager NFManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		Notification NFNotification = new Notification(R.drawable.ic_launcher, "同步服务已启动", System.currentTimeMillis());
		NFNotification.setLatestEventInfo(getApplicationContext(), "EEW 地震速报", "正在与同步服务器建立连接", PendingIntent.getActivity(this, 0, new Intent("app.makito.kjs.MainActivity"),0));
		NFNotification.flags |= NFNotification.FLAG_ONGOING_EVENT;
		NFManager.notify(1, NFNotification);
        super.onStart(intent, startId);  
    }  

    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        //Log.v(TAG, "ServiceDemo onStartCommand");  
        return super.onStartCommand(intent, flags, startId);  
    } 
	public void onDestroy() {
		//Log.v(TAG, "Service onDestroy");
		NotificationManager NFManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		NFManager.cancel(1);
		//Toast.makeText(this, "Notification Service Stopped", Toast.LENGTH_LONG).show();
		super.onDestroy();
	}
		
		private BroadcastReceiver NetworkStateReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Intent in = new Intent();
				in.setClass(context, AlertActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(in); 
			}};
}

