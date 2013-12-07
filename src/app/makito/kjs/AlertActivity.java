package app.makito.kjs;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import android.view.View.*;

public class AlertActivity extends Activity 
{
	private TextView AlertM;
	private TextView AlertLOC;
	private TextView AlertDEPTH;
	private TextView AlertOTIME;
	private TextView AlertPOS;
	private TextView AlertTMap;
	private ImageButton AlertMAP;
	private String X;
	private String Y;
	private float lng;
	private float lat;
	
	Handler MapHandler = new Handler();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.alert);  
		AlertM = (TextView) findViewById(R.id.AlertM);
		AlertLOC = (TextView) findViewById(R.id.AlertLOC);
		AlertDEPTH = (TextView) findViewById(R.id.AlertDEPTH);
		AlertOTIME = (TextView) findViewById(R.id.AlertOTIME);
		AlertPOS = (TextView) findViewById(R.id.AlertPOS);
		AlertMAP = (ImageButton) findViewById(R.id.AlertMAP);
		AlertTMap = (TextView) findViewById(R.id.AlertTMap);

		Intent intent = this.getIntent();
		String LOC = intent.getStringExtra("LOC");
		String M = intent.getStringExtra("M");
		String DEPTH = intent.getStringExtra("DEPTH");
		String OTIME = intent.getStringExtra("OTIME");
		final String X = intent.getStringExtra("X");
		final String Y = intent.getStringExtra("Y");
		
		AlertM.setText("M "+M);
		AlertLOC.setText(LOC);
		AlertDEPTH.setText(DEPTH+" Km");
		AlertOTIME.setText(OTIME);
		AlertPOS.setText("纬度"+X+" 经度"+Y);
		
		MapHandler.postDelayed(new Runnable(){
				public void run()
				{
					AlertMAP.setImageBitmap(getMap(Y, X));
				}
		},500);
		
		AlertMAP.setOnClickListener(new OnClickListener(){
				public void onClick(View p1)
				{
					Intent mapIntent = new Intent(AlertActivity.this, MapContainer.class);
					mapIntent.putExtra("LNG", Y);
					mapIntent.putExtra("LAT", X);
					startActivity(mapIntent);
				}
		});
		
		switchBGP();

	}
	
	private void getMapMethod(){
		AlertMAP.setImageBitmap(getMap(Y, X));
	}
	
	private void switchBGP() {
		Intent ShowDialogInt = new Intent(AlertActivity.this, AlertBGPlay.class);  
		startActivity(ShowDialogInt); 
	}
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
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.setView(layout);
		toast.show();
	}
	public void onBackPressed() {
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addCategory(Intent.CATEGORY_HOME);
		startActivity(i);
		this.finish();
	}
	

    private Bitmap getMap(String lng, String lat) {  
        // TODO Auto-generated method stub  
        String url = "http://api.map.baidu.com/staticimage?width=580&height=400&copyright=1&zoom=11&centre="+lng+","+lat+"&size=s&markers="+lng+","+lat;
        Bitmap tmpBitmap = null;  
        try {  
            InputStream is = new java.net.URL(url).openStream();  
            tmpBitmap = BitmapFactory.decodeStream(is);  
			String MapT = AlertTMap.getText().toString();
			MapT += " (点击地图可查看大地图)";
			AlertTMap.setText(MapT);
        } catch (Exception e) {  
            e.printStackTrace();  
			String MapT = AlertTMap.getText().toString();
			MapT += " (加载失败)";
			AlertTMap.setText(MapT);
        }  
        return tmpBitmap;  
    }  
}
