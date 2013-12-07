package app.makito.kjs;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.widget.SearchView.*;
import android.view.*;

public class About extends Activity
{
    protected void onCreate(Bundle savedInstanceState)
	{  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.about);  
		new DialogView(this).show();
		/*
		Button btnClose = (Button) findViewById(R.id.aboutBtnClose);
		btnClose.setOnClickListener(new OnClickListener(){
				public void onClick(View p1)
				{
					About.this.finish();
				}	
		});
		*/
	}
}
