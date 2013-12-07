package app.makito.kjs;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.app.AlertDialog;

public class AlertPopupDialog extends Activity
{

	@Override
    protected void onCreate(Bundle savedInstanceState)
	{  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
		new AlertDialog.Builder(this)  
		    .setTitle("地震速报")
			.setMessage("這是一條緊急地震速報、請小心接下來的強烈搖晃、請注意安全、遠離容易傾倒的家具等物品！")
			.setPositiveButton("確認", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int i)
				{
					sendBroadcast(new Intent("BGSoundPlayback_Finish"));
					sendBroadcast(new Intent("TvPopup_DISMISS"));
					AlertPopupDialog.this.finish();
				}
			})
			.show();

		Handler mTimeoutHandler = new Handler();
		mTimeoutHandler.postDelayed(new Runnable(){
				public void run()
				{
					sendBroadcast(new Intent("BGSoundPlayback_Finish"));
					sendBroadcast(new Intent("TvPopup_DISMISS"));
					AlertPopupDialog.this.finish();
				}
			}, 30000);
	}

}
