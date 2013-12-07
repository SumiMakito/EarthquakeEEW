package app.makito.kjs;

import android.app.*;
import android.view.*;
import android.view.View.*;
import android.view.ViewGroup.*;
import android.widget.*;
import app.makito.kjs.*;

import android.view.View.OnClickListener;

public class DialogView
{
    private Dialog dialog; 

    public DialogView(Activity mActivity) { 
        dialog = new Dialog(mActivity, R.style.mask_dialog); 
        LinearLayout popView = (LinearLayout) LayoutInflater. 
			from(mActivity).inflate(R.layout.dialog_view, null); 

        // 关闭按钮 
        ImageView viewClose = (ImageView) popView.findViewById(R.id.win_close); 
        viewClose.setOnClickListener(new OnClickListener() { 
				@Override 
				public void onClick(View v) { 
					hide(); 
				} 
			}); 

        dialog.setContentView(popView,  
							  new LinearLayout.LayoutParams( 
								  LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
        dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0); 
    } 

    public void show() { 
        dialog.show(); 
    } 

    public void hide() { 
        dialog.dismiss(); 
    } 
}
