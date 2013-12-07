package app.makito.kjs;

import android.app.*;
import android.os.*;
import android.widget.*;
import java.io.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.params.*;
import org.json.*;
import android.view.View;

public class RtOneRep extends Activity
{
	private LinearLayout LtProgress;
	private LinearLayout LtContent;
	Handler RtHandler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rt_one_rep);

		RtHandler.postDelayed(new Runnable() {
				public void run()
				{
					LtProgress = (LinearLayout) findViewById(R.id.LtProgress);
					LtContent = (LinearLayout) findViewById(R.id.LtContent);

					StringBuffer sb = new StringBuffer();
					String url = "http://makito.duapp.com/api/ceic_eew.php";
					try
					{
						String content = getContent(url);
						JSONArray array = new JSONArray(content);
						for (int i = 0; i < array.length(); i++)
						{
							JSONObject object = array.getJSONObject(i);
							sb.append("时间:").append(object.getString("O_TIME")).append("\n");
							sb.append("震中位置:").append(object.getString("LOCATION_C")).append("\n");
							sb.append("震级:").append(object.getString("M")).append("\n");
							sb.append("\n");
						}

					}
					catch (Exception e)
					{
						e.printStackTrace();
						System.out.println(e.getMessage());
						//Toast.makeText(RtOneRep.this, e.getMessage(), Toast.LENGTH_LONG).show();
						Toast.makeText(RtOneRep.this, "( ﾟдﾟ) 获取数据失败\n・请检查网络连接\n・同步服务器可能发生故障", Toast.LENGTH_LONG).show();
						RtOneRep.this.finish();
					}
					TextView tView = (TextView) findViewById(R.id.rt_one_rep_out);

					tView.setText(sb.toString());

					LtProgress.setVisibility(View.GONE);
					LtContent.setVisibility(View.VISIBLE);
				}
			}, 2000);
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
}

