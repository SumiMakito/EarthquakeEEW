package app.makito.kjs;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.util.*;
import android.content.Intent;

public class RealtimeService extends Service
{
	//private static final String TAG = "ServiceDemo" ;  

    @Override  
    public IBinder onBind(Intent intent)
	{  
        //Log.v(TAG, "ServiceDemo onBind");  
        return null;  
    }  

    @Override  
    public void onCreate()
	{  
        //Log.v(TAG, "ServiceDemo onCreate");
        super.onCreate();  
    }  

    @Override  
    public void onStart(Intent intent, int startId)
	{  
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock ntwl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NotificationServiceWL");
		ntwl.acquire();
        //Log.v(TAG, "ServiceDemo onStart"); 
		//startService(new Intent("app.makito.kjs.NF_SERVICE"));
		startService(new Intent("app.makito.kjs.POLL_SERVICE"));
		//Toast.makeText(this, "Realtime Service Started", Toast.LENGTH_LONG).show();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("app.makito.kjs.ALERT");
		registerReceiver(RTServiceReceiver, intentFilter);
		/*
		 IntentFilter DataIF = new IntentFilter();
		 DataIF.addAction("app.makito.kjs.DATA");
		 registerReceiver(RTDataReceiver, DataIF);
		 */
        super.onStart(intent, startId);  
    }  

    @Override  
    public int onStartCommand(Intent intent, int flags, int startId)
	{  
        //Log.v(TAG, "ServiceDemo onStartCommand");  
        return super.onStartCommand(intent, flags, startId);  
    } 
	public void onDestroy()
	{
		//Log.v(TAG, "Service onDestroy");
		//stopService(new Intent("app.makito.kjs.NF_SERVICE"));
		stopService(new Intent("app.makito.kjs.POLL_SERVICE"));
		//Toast.makeText(this, "Realtime Service Stopped", Toast.LENGTH_LONG).show();
		super.onDestroy();
	}

	private BroadcastReceiver RTServiceReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent)
		{
			Intent in = new Intent();
			in.putExtra("LOC", intent.getStringExtra("LOC"));
			in.putExtra("M", intent.getStringExtra("M"));
			in.putExtra("DEPTH", intent.getStringExtra("DEPTH"));
			in.putExtra("OTIME", intent.getStringExtra("OTIME"));
			in.putExtra("X", intent.getStringExtra("X"));
			in.putExtra("Y", intent.getStringExtra("Y"));

			in.setClass(context, TvPopup.class);
			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(in); 
		}};

	private BroadcastReceiver RTDataReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent)
		{
			Intent in = new Intent();
			in.setClass(context, TvPopup.class);
			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(in); 
		}};


}
