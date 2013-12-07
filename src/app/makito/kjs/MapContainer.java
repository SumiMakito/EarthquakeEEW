package app.makito.kjs;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.widget.*;
import java.io.*;
import android.content.*;

public class MapContainer extends Activity
{
	private String LNG;
	private String LAT;
	private ImageView MapView;
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.map_container);  
		setTitle("地图正在加载中…");
		MapView = (ImageView) findViewById(R.id.MapView);
		
		Intent rIn = this.getIntent();
		LNG = rIn.getStringExtra("LNG");
		LAT = rIn.getStringExtra("LAT");
		
		Handler MapLoader = new Handler();
		MapLoader.postDelayed(new Runnable() {
				public void run()
				{
					MapView.setImageBitmap(getMap(LNG, LAT));
					setTitle("查看地图");
				}
			}, 2000);
	}

    private Bitmap getMap(String lng, String lat)
	{  
        // TODO Auto-generated method stub  
        String url = "http://api.map.baidu.com/staticimage?width=1000&height=1000&copyright=1&zoom=10&centre=" + lng + "," + lat + "&size=l&markers=" + lng + "," + lat;
        Bitmap tmpBitmap = null;  
        try
		{  
            InputStream is = new java.net.URL(url).openStream();  
            tmpBitmap = BitmapFactory.decodeStream(is);  
        }
		catch (Exception e)
		{  
            e.printStackTrace();  
			setTitle("地图加载失败");
        }  
        return tmpBitmap;  
    }  
}
