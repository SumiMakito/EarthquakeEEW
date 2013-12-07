package app.makito.kjs;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.params.*;
import org.json.*;
import android.app.ActivityManager.RunningServiceInfo;

public class TestOne extends Activity 
{
	String LOC;
	String M;
	String DEPTH;
	String OTIME;
	String X;
	String Y;

	@Override
    protected void onCreate(Bundle savedInstanceState)
	{  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.testone); 
		switch(isRtRunning()){
			case 0:
			Toast.makeText(this, "请进入设置页面将同步服务开启后再进行测试", Toast.LENGTH_LONG).show();
			this.finish();
			break;
		}
		String url = "http://makito.duapp.com/api/ceic_eew.php";
		try
		{
			String content = getContent(url);
			JSONArray array = new JSONArray(content);
			JSONObject object = array.getJSONObject(0);
			String LOC = object.getString("LOCATION_C");
			String M = object.getString("M");
			String DEPTH = object.getString("EPI_DEPTH");
			String OTIME = object.getString("O_TIME");
			String X = object.getString("EPI_LAT");
			String Y = object.getString("EPI_LON");
			Intent in = new Intent("app.makito.kjs.ALERT");  
			in.putExtra("LOC", LOC);
			in.putExtra("M", M);
			in.putExtra("DEPTH", DEPTH);
			in.putExtra("OTIME", OTIME);
			in.putExtra("X", X);
			in.putExtra("Y", Y);
			sendBroadcast(in);
			
			this.finish();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			//Toast.makeText(TestOne.this, e.getMessage(), Toast.LENGTH_LONG).show();
			Toast.makeText(this, "网络连接异常 请确认已连接至网络", Toast.LENGTH_LONG).show();
			this.finish();
		}

	}

	/*
	 public void Test()
	 {
	 Intent in = new Intent(TestOne.this, TvPopup.class);  
	 in.putExtra("LOC", LOC);
	 in.putExtra("M", M);
	 in.putExtra("DEPTH", DEPTH);
	 in.putExtra("OTIME", OTIME);
	 in.putExtra("X", X);
	 in.putExtra("Y", Y);
	 startActivity(in);
	 }
	 */

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
