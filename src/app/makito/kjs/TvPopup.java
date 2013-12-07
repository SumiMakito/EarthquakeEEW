package app.makito.kjs;

import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import app.makito.kjs.*;
import java.util.*;

public class TvPopup extends Activity implements SoundPool.OnLoadCompleteListener
{
	private SoundPool sTvPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	private HashMap<Integer, Integer> sMap = new HashMap<Integer, Integer>();
	private AudioManager sManager=null;
	private final Handler mHandler = new MyHandler();
	private static final int SOUND_LOAD_OK = 1;

	private String LOC;
	private String M;
	private String DEPTH;
	private String OTIME;
	private String X;
	private String Y;

	Handler handler = new Handler();
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);  
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TVPOPUP_WL");  
        wl.acquire(300000);
		KeyguardManager mKeyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE); 
		if(mKeyguardManager.inKeyguardRestrictedInputMode()){  
		  //处于锁定界面,界面则通过KeyguardLock类方法来解锁  
		  KeyguardManager.KeyguardLock keyguard = mKeyguardManager.newKeyguardLock(getLocalClassName());  
		  keyguard.disableKeyguard();  
		}  
		
        super.onCreate(savedInstanceState);  
		setTheme(android.R.style.Theme_Holo_Wallpaper_NoTitleBar);
        setContentView(R.layout.tv_popup);
		
		Intent rIn = this.getIntent();
		TextView mLOC = (TextView) findViewById(R.id.tvp_loc);
		mLOC.setText(rIn.getStringExtra("LOC"));
		LOC = rIn.getStringExtra("LOC");
		M = rIn.getStringExtra("M");
		DEPTH = rIn.getStringExtra("DEPTH");
		OTIME = rIn.getStringExtra("OTIME");
		X = rIn.getStringExtra("X");
		Y = rIn.getStringExtra("Y");
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("TvPopup_DISMISS");
		registerReceiver(TvPopupReceiver, intentFilter);

		sManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

		sMap.put(1, sTvPool.load(this, R.raw.alert, 1));  
		sMap.put(2, sTvPool.load(this, R.raw.alarm, 1)); 

		//例子 sPool.play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
		sTvPool.setOnLoadCompleteListener(this);

		showToast(LOC, DEPTH, M);
		
		long LENGTH = 6000;
		handler.postDelayed(new Runnable() {  
				@Override  
				public void run()
				{  
					Intent intent = new Intent(TvPopup.this, AlertActivity.class);  
					intent.putExtra("LOC", LOC);
					intent.putExtra("M", M);
					intent.putExtra("DEPTH", DEPTH);
					intent.putExtra("OTIME", OTIME);
					intent.putExtra("X", X);
					intent.putExtra("Y", Y);

					startActivity(intent);  
				}  
			}, LENGTH);
	}

	//销毁sPool
	public void onDestroy()
    {   
		sTvPool.release() ;
		sManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
		super.onDestroy();
    }
	private BroadcastReceiver TvPopupReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent)
		{
			TvPopup.this.finish();
		}};
/*
	protected void onResume()
	{
		super.onResume();
		Intent rIn = this.getIntent();
		TextView mLOC = (TextView) findViewById(R.id.tvp_loc);
		mLOC.setText(rIn.getStringExtra("LOC"));
		LOC = rIn.getStringExtra("LOC");
		M = rIn.getStringExtra("M");
		DEPTH = rIn.getStringExtra("DEPTH");
		OTIME = rIn.getStringExtra("OTIME");
		X = rIn.getStringExtra("X");
		Y = rIn.getStringExtra("Y");

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("TvPopup_DISMISS");
		registerReceiver(TvPopupReceiver, intentFilter);
	}
*/
	
	private void showToast(String mLoc, String mDepth, String mM)
	{
		LayoutInflater inflater =getLayoutInflater();
		View layout =inflater.inflate(R.layout.popup_toast,(ViewGroup)findViewById(R.id.popup_toast));
		TextView LOC = (TextView) layout.findViewById(R.id.PT_Loc);
		TextView DEPTH = (TextView) layout.findViewById(R.id.PT_Depth);
		TextView M = (TextView) layout.findViewById(R.id.PT_M);
		LOC.setText(mLoc);
		DEPTH.setText("震源深度 "+mDepth+" Km");
		M.setText("     震度 M"+mM);
		Toast toast = new Toast(getApplicationContext());
		toast.setDuration(100000);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(layout);
		toast.show();
	}
	
	//加载监听 类
    private class MyHandler extends Handler
	{
		//private int defaultVolume = sManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case SOUND_LOAD_OK:
					sManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);
					sTvPool.play(1, 6, 6, 0, -1, 0.9f);
					break;
			}
		}
    }
	//监听加载完成
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
	{
		Message msg = mHandler.obtainMessage(SOUND_LOAD_OK);
		msg.arg1 = sampleId ;     
		mHandler.sendMessage(msg);
    }

}
