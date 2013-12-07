package app.makito.kjs;

import android.app.*;
import android.os.*;
import android.widget.*;
import app.makito.kjs.JsonData;
import org.json.*;

public class DataContainer extends Activity
{
	private TextView TextViewOutput;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.data_container);
		TextViewOutput = (TextView) findViewById(R.id.dcontainer_output);
		String url = "http://app.szumi.info/api/ceic_eew.php";
		try
		{
			JSONObject data = JsonData.getJson(url);
			TextViewOutput.setText(data.toString());
		}
		catch (Exception e)
		{}
	}
}
