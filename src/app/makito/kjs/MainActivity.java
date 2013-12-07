package app.makito.kjs;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.view.View;
import java.io.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.params.*;
import org.json.*;

public class MainActivity extends Activity implements View.OnClickListener
{
    private Button mainBtnTest;
	private Button mainBtnStartService;
	private Button mainBtnStopService;
	private Button mainBtnSettings;
	private Button mainBtnRtOneRep;
	private EditText mainEtTLOC;
	private Button mainBtnAbout;
	private TextView mainSub;

	@Override
    protected void onCreate(Bundle savedInstanceState)
	{  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
		mainBtnTest = (Button) findViewById(R.id.mainBtnTest);
		mainBtnStartService = (Button) findViewById(R.id.mainBtnStartService);
		mainBtnStopService = (Button) findViewById(R.id.mainBtnStopService);
		mainBtnSettings = (Button) findViewById(R.id.mainBtnSettings);
		mainBtnRtOneRep = (Button) findViewById(R.id.mainBtnRtReports);
		Button mainBtnTBB = (Button) findViewById(R.id.mainBtnTestByBroadcast);
		mainBtnAbout = (Button) findViewById(R.id.mainBtnAbout);
		mainSub = (TextView) findViewById(R.id.mainSub);
		//mainEtTLOC = (EditText) findViewById(R.id.mainEtTestLOC);
		
		final LinearLayout mLoading = (LinearLayout) findViewById(R.id.mainLoading);
		Handler mSub = new Handler();
		mSub.postDelayed(new Runnable(){
				public void run()
				{
					StringBuffer sb = new StringBuffer();
					String url = "http://makito.duapp.com/api/ceic_eew.php";
					try
					{
						String content = getContent(url);
						JSONArray array = new JSONArray(content);
	
							JSONObject object = array.getJSONObject(0);
							mLoading.setVisibility(View.GONE);
							mainSub.setText("最新速报: "+object.getString("O_TIME")+" "+object.getString("LOCATION_C")+" 发生 M"+object.getString("M")+"级地震");
					}
					catch (Exception e)
					{
						e.printStackTrace();
						mLoading.setVisibility(View.GONE);
					}
				}
		},2000);
		
		//String mTLOC = mainEtTLOC.getText().toString();
		mainBtnTest.setOnClickListener(this);
		mainBtnStartService.setOnClickListener(this);
		//mainBtnStopervice.setOnClickListener(this);
		mainBtnSettings.setOnClickListener(this);
		mainBtnRtOneRep.setOnClickListener(this);
		mainBtnTBB.setOnClickListener(this);
		mainBtnAbout.setOnClickListener(this);
	}
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.mainBtnTest:
				TestAlert();
				break;
			case R.id.mainBtnStartService:
				RTServiceStart();
				break;
			case R.id.mainBtnStopService:
				RTServiceStop();
				break;
			case R.id.mainBtnSettings:
				Settings();
				break;
			case R.id.mainBtnRtReports:
			    Intent intent = new Intent(this, RtOneRep.class);
				startActivity(intent);
				break;
			case R.id.mainBtnTestByBroadcast:
			    TBB();
				break;
			case R.id.mainBtnAbout:
				new DialogView(this).show();
				break;
			default:
				break;
		}

	}

	private void Popup()
	{
		Intent mPopupIntent = new Intent(this, TvPopup.class);
		startActivity(mPopupIntent);
	}
	private void TestAlert()
	{
		Intent TestAlertint = new Intent(MainActivity.this, TvPopup.class);  
		startActivity(TestAlertint); 
	}
	private void PushBroadcast()
	{
		Intent intent = new Intent();
		intent.setAction("KJS_PushMSG");
		sendBroadcast(intent);
	}
	private void Settings()
	{
		Intent intent = new Intent(MainActivity.this, Settings.class);
		startActivity(intent);
	}
	private void TBB()
	{
		Intent in = new Intent(this, TestOne.class);
		//in.putExtra("LOC", mainEtTLOC.getText().toString());
		startActivity(in);
	}
	private void RTServiceStart()
	{
		startService(new Intent("app.makito.kjs.RT_SERVICE"));
	}
	private void RTServiceStop()
	{
		Intent intent = new Intent();
		intent.setAction("app.makito.kjs.RT_STOP");
		sendBroadcast(intent);
		stopService(new Intent("app.makito.kjs.RT_SERVICE"));
		/*  旧式
		 Intent service = new Intent(this, RealtimeService.class);
		 stopService(service);
		 */
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
	
	public void onBackPressed()
	{
		this.finish();
	}

}
