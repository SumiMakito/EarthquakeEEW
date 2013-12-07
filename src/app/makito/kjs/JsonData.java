package app.makito.kjs;

import java.io.*;
import java.net.*;
import org.json.*;
import android.view.*;

public class JsonData
{
	public static JSONObject getJson(String mUrl) throws JSONException, IOException, Exception {
		URL url = new URL(mUrl);
		HttpURLConnection conn = null; 
		conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(5 * 1000);
		conn.setRequestMethod("GET"); 

		InputStream inStream = conn.getInputStream();
		byte[] data = StreamTool.readStream(inStream);
		String json = new String(data);
		JSONObject rs = new JSONObject(json);
		return rs;
		//int id = rs.getInt("id"); 
		//String name = rs.getString("title"); 
	}
	
	public static String getRaw(String mUrl) throws IOException, Exception {
		URL url = new URL(mUrl);
		HttpURLConnection conn = null; 
		conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(5 * 1000);
		conn.setRequestMethod("GET"); 

		InputStream inStream = conn.getInputStream();
		byte[] data = StreamTool.readStream(inStream);
		String json = new String(data.toString());
		return json;
	}
	
	public static int getItemInt(String mUrl, String item) throws JSONException, IOException, Exception {
		URL url = new URL(mUrl);
		HttpURLConnection conn = null; 
		conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(5 * 1000);
		conn.setRequestMethod("GET"); 

		InputStream inStream = conn.getInputStream();
		byte[] data = StreamTool.readStream(inStream);
		String json = new String(data);
		JSONObject rs = new JSONObject(json);
		int i = rs.getInt(item); 
		return i;
	}
	
	public static String getItemStr(String mUrl, String item) throws JSONException, IOException, Exception {
		URL url = new URL(mUrl);
		HttpURLConnection conn = null; 
		conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(5 * 1000);
		conn.setRequestMethod("GET"); 

		InputStream inStream = conn.getInputStream();
		byte[] data = StreamTool.readStream(inStream);
		String json = new String(data);
		JSONObject rs = new JSONObject(json);
		String str = rs.getString(item); 
		return str;
	}
}
