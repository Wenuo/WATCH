package com.smartwatch.ywatch;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import com.smartwatch.ywatch.notification.NLService;
import android.content.*;
import android.provider.*;
import android.text.*;
import android.widget.CompoundButton.*;
import android.support.v4.app.*;
import java.util.*;
import android.content.pm.*;


public class NotificationActivity extends BaseActivity 
{
	private Switch qqon,smson,mmon,callon,notifiSwitch;
    private ImageButton notificationbutton;
	private TextView QQ_State,WeChat_State,Call_State,Sms_State;
	//private SharedPreferences notifisp;
	//private SharedPreferences notisp;

	private String ENABLED_NOTIFICATION_LISTENERS="enabled_notification_listeners";

	private static  boolean blFlag = true;
	private static boolean qqFlag = true;
	private static boolean mmFlag = true;
	private static boolean callFlag = true;
	private static boolean smsFlag = true;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//truetheme();
        setContentView(R.layout.n_activity);
		
      /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
	//registBroadCast();
	QQ_State=(TextView)findViewById(R.id.QQ_State);
	WeChat_State=(TextView)findViewById(R.id.WeChat_State);
	Call_State=(TextView)findViewById(R.id.Call_State);
	Sms_State=(TextView)findViewById(R.id.Sms_State);
	 notifiSwitch=(Switch)findViewById(R.id.notiswitch);
     qqon=(Switch)findViewById(R.id.qqon);
	 smson=(Switch)findViewById(R.id.smson);
	 mmon=(Switch)findViewById(R.id.mmon);
	 callon=(Switch)findViewById(R.id.callon);
	 notificationbutton=(ImageButton)findViewById(R.id.back_n_title);
	 
     one();
	 two();
     zhuang();
     notification_setting();//QQ监听开关
	 
	 
	 
       	notifiSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override  
				public void onCheckedChanged(CompoundButton switchView,boolean isChecked) {  
					SharedPreferences sps=getSharedPreferences("notisetting",MODE_PRIVATE);
					SharedPreferences.Editor edi=sps.edit();
			        if (isChecked){
						if(!isEnabled()){
						  Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
						  startActivity(intent); 
							edi.putBoolean("aiChecked",true);
						
						}
					}
					else{
						if(isEnabled()){
							Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
							startActivity(intent);
							edi.putBoolean("aiChecked",false);
						}
					}
					 
				edi.commit();
				}
			});

	   /*QQ监听服务开启*/
		qqon.setOnCheckedChangeListener(new OnCheckedChangeListener() {  

				@Override  
				public void onCheckedChanged(CompoundButton switchView,boolean isChecked) {  
			 SharedPreferences sp=getSharedPreferences("notisetting",MODE_PRIVATE);
					
					SharedPreferences.Editor ed=sp.edit();
					if (isChecked) {  
						Toast.makeText(NotificationActivity.this, "QQ通知已开启", Toast.LENGTH_SHORT).show();
						QQ_State.setText("打开");
						qqFlag=true;
						ed.putBoolean("qqChecked",true);
		                      }
				   else{
					   Toast.makeText(NotificationActivity.this, "QQ通知已关闭", Toast.LENGTH_SHORT).show();
					   QQ_State.setText("关闭");
					qqFlag=false;
					ed.putBoolean("qqChecked",false);
					   }
					ed.commit();
					   } 
			  });
		
	/*短信监听服务开启*/			   
	smson.setOnCheckedChangeListener(new OnCheckedChangeListener() {  

	@Override  
	public void onCheckedChanged(CompoundButton switchView,boolean isChecked) {  
		SharedPreferences sp=getSharedPreferences("notisetting",MODE_PRIVATE);
		SharedPreferences.Editor ed=sp.edit();
		if (isChecked) {  
			Toast.makeText(NotificationActivity.this, "短信通知已开启", Toast.LENGTH_SHORT).show();
			 ed.putBoolean("smsChecked",true);
			Sms_State.setText("打开");
		}
		else{
			Toast.makeText(NotificationActivity.this, "短信通知已关闭", Toast.LENGTH_SHORT).show();
			ed.putBoolean("smsChecked",false);
			Sms_State.setText("关闭");
			
		}
		ed.commit();
	}
	});
	/*来电监听服务开启*/
		callon.setOnCheckedChangeListener(new OnCheckedChangeListener() {  

				@Override  
				public void onCheckedChanged(CompoundButton switchView,boolean isChecked) {  
					SharedPreferences sp=getSharedPreferences("notisetting",MODE_PRIVATE);
					 SharedPreferences.Editor ed=sp.edit();
					if (isChecked) {  
						Toast.makeText(NotificationActivity.this, "来电通知已开启", Toast.LENGTH_SHORT).show();
						 ed.putBoolean("callChecked",true);
						Call_State.setText("打开");
					}
					else{
						Toast.makeText(NotificationActivity.this, "来电通知已关闭", Toast.LENGTH_SHORT).show();
						ed.putBoolean("callChecked",false);
						Call_State.setText("关闭");
					}
					ed.commit();
				}
			});
			/*WeChat监听服务开启*/
		mmon.setOnCheckedChangeListener(new OnCheckedChangeListener() {  

				@Override  
				public void onCheckedChanged(CompoundButton switchView,boolean isChecked) {  
					SharedPreferences sp=getSharedPreferences("notisetting",MODE_PRIVATE);
					SharedPreferences.Editor ed=sp.edit();
					if (isChecked) {  
						Toast.makeText(NotificationActivity.this, "微信通知已开启", Toast.LENGTH_SHORT).show();
						 ed.putBoolean("mmChecked",true);
						WeChat_State.setText("打开");
		
					}
					else{
						Toast.makeText(NotificationActivity.this, "微信通知已关闭", Toast.LENGTH_SHORT).show();
						ed.putBoolean("mmChecked",false);
						WeChat_State.setText("关闭");
					
					}
					
					ed.commit();
				}
			});
	
	

 /*返回*/
	notificationbutton.setOnClickListener(new OnClickListener(){
	@Override
	public void onClick(View v) {
		finish();
		
	}

	
	});
	}
	private boolean isEnabled() {
        String pkgName = NotificationActivity.this.getPackageName();
        final String flat = Settings.Secure.getString(NotificationActivity.this.getContentResolver(),ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
	
	//保存开关，读取状态方法
	private void zhuang(){
		SharedPreferences spares=getSharedPreferences("notisetting",MODE_PRIVATE);
		SharedPreferences.Editor edito= spares.edit();
		if(isEnabled()){
			notifiSwitch.setChecked(true);
			edito.putBoolean("Checked",true);
			
		}
		else{
			notifiSwitch.setChecked(false);
			edito.putBoolean("Checked",false);
		}
		edito.commit();
	}
	
	
	private void one(){
	SharedPreferences notifisp=getSharedPreferences("notisetting",MODE_PRIVATE);
	boolean aisChecked=notifisp.getBoolean("aiChecked",false);
     if(aisChecked){
		 notifiSwitch.setChecked(false);
		 
	 }
	 else{
		 notifiSwitch.setChecked(true);
		 
	 }
	}
	
	
	private void two(){
	SharedPreferences	notisp=getSharedPreferences("notisetting",MODE_PRIVATE);
		boolean CHECKED=notisp.getBoolean("Checked",false);
		if(CHECKED){
			notifiSwitch.setChecked(false);
			
		}
		else{
			notifiSwitch.setChecked(true);
			
		}
	}
	
	private void notification_setting(){
		SharedPreferences sp=getSharedPreferences("notisetting",MODE_PRIVATE);
		boolean QQON_OFF=sp.getBoolean("qqChecked",true);
		boolean MMON_OFF=sp.getBoolean("mmChecked",true);
		boolean CALLON_OFF=sp.getBoolean("callChecked",true);
		boolean SMSON_OFF=sp.getBoolean("smsChecked",true);
		if(QQON_OFF){
			qqon.setChecked(true);
			QQ_State.setText("打开");
			}
		else{
				qqon.setChecked(false);
			QQ_State.setText("关闭");
			}
		if(MMON_OFF){
			mmon.setChecked(true);
			WeChat_State.setText("打开");
		}
		else
		{
			mmon.setChecked(false);
			WeChat_State.setText("关闭");
		}
		if(CALLON_OFF){
			callon.setChecked(true);
			Call_State.setText("打开");
		}
		else{
			callon.setChecked(false);
			Call_State.setText("关闭");
		}
		if(SMSON_OFF)
		{
			smson.setChecked(true);
			Sms_State.setText("打开");
		}
		else
		{
			smson.setChecked(false);
			Sms_State.setText("关闭");
		}
	}
	private void unclickable(){
		qqon.setClickable(false);
		mmon.setClickable(false);
		callon.setClickable(false);
		smson.setClickable(false);
	}
   private void clickable(){
	   qqon.setClickable(true);
	   mmon.setClickable(true);
	   callon.setClickable(true);
	   smson.setClickable(true);
   }
	

	@Override
	protected void onResume()
	{
		// TODO: Implement this method

		super.onResume();
	}


	@Override
	protected void onStop()
	{
		// TODO: Implement this method
		recreate();
		super.onStop();
	}
}



