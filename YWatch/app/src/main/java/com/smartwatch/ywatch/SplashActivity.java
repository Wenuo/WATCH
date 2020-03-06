package com.smartwatch.ywatch;
import android.app.*;
import android.os.*;
import android.content.*;

public class SplashActivity extends Activity
{


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.splash_activity);
		new Handler().postDelayed(new Runnable(){

				@Override
				public void run()
				{
					Intent i=new Intent(SplashActivity.this,YActivity.class);
					startActivity(i);

					finish();
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}		
		},1000);
		
	}
	
	
	
}
