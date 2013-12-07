package app.makito.kjs;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.IOException;  
import java.util.HashMap;
import java.net.URL;
import android.graphics.BitmapFactory;

import android.media.AudioManager;  
import android.media.MediaPlayer;  
import android.media.SoundPool;
import android.os.Bundle;  
import android.app.Activity;  
import android.view.Menu;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.SeekBar;  
import android.content.*;

public class AlertBGPlay extends Activity implements SoundPool.OnLoadCompleteListener
{
	private SoundPool sPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	private HashMap<Integer, Integer> sMap = new HashMap<Integer, Integer>();
	private AudioManager sManager=null;
	private final Handler mHandler = new MyHandler();
	private static final int SOUND_LOAD_OK = 1;


	@Override
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.alert);  
		//btn_close = (Button) findViewById(R.id.btn_close);
		sManager = (AudioManager) AlertBGPlay.this.getSystemService(Context.AUDIO_SERVICE);

		sMap.put(1, sPool.load(this, R.raw.alert, 1));  
		sMap.put(2, sPool.load(this, R.raw.alarm, 1)); 

		showDialog();
		//例子 sPool.play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
		sPool.setOnLoadCompleteListener(this);
		//设置监听
		//sPool.play(1, 1, 1, 0, 5, 1);

	}
	//销毁sPool
	public void onDestroy()
    {   
		sPool.release() ;
		sManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
		super.onDestroy();
    }
	private void showDialog() {
		Intent ShowDialogInt = new Intent(AlertBGPlay.this, AlertPopupDialog.class);  
		startActivity(ShowDialogInt); 
	}
	
	private BroadcastReceiver BGPReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
			}};

	protected void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("BGSoundPlayback_Finish");
		registerReceiver(BGPReceiver, intentFilter);
	}
	
	//加载监听 类
    private class MyHandler extends Handler {
		//private int defaultVolume = sManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		public void handleMessage(Message msg){
			switch( msg.what){
				case SOUND_LOAD_OK:
					sManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);
					//sPool.play(1, 6, 6, 0, -1, 1);
					break;
			}
		}
    }
	//监听加载完成
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status)  {
		Message msg = mHandler.obtainMessage(SOUND_LOAD_OK);
		msg.arg1 = sampleId ;     
		mHandler.sendMessage(msg);
    }
}
