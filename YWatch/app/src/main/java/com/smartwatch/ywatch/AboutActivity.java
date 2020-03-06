package com.smartwatch.ywatch;
import android.app.*;
import android.view.*;
import android.view.ViewGroup.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.net.*;
import android.content.pm.*;
import android.content.pm.PackageManager.*;

public class AboutActivity extends BaseActivity
{

	private static boolean blFlag = true;
	private RelativeLayout qqUrl;
	private RelativeLayout emailUrl;
	private RelativeLayout githubUrl;
	private TextView about_version;
	

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
		//truetheme();
		setContentView(R.layout.about_page);
		
		
		initview();
		}
		
	private void initview(){
		//relat=(RelativeLayout)findViewById(R.id.relativeLayout);
		about_version=(TextView)findViewById(R.id.about_Version);
		qqUrl=(RelativeLayout)findViewById(R.id.qqRelativeLayout);
		emailUrl=(RelativeLayout)findViewById(R.id.emailRelativeLayout);
		githubUrl=(RelativeLayout)findViewById(R.id.githubRelativeLayout);

		
		try
		{
			PackageInfo packageInfo = getPackageManager()
				.getPackageInfo(getPackageName(), 0);
			String versionName = packageInfo.versionName;
			about_version.setText("v "+versionName);
		}
		catch (PackageManager.NameNotFoundException e)
		{}
		}
	

	public void emailClick(View v){
		switch (v.getId()){
			case R.id.qqRelativeLayout:
				try {
					String url = "mqqwpa://im/chat?chat_type=wpa&uin=2404000720";
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this,"请检查QQ是否安装",Toast.LENGTH_SHORT).show();
				}
				break;
				case R.id.emailRelativeLayout:
				try{
				Intent emailintent = new Intent(Intent.ACTION_SENDTO);
				emailintent.setData(Uri.parse("mailto:"));
				emailintent.putExtra(Intent.EXTRA_EMAIL, new String[]{"2404000720@qq.com"});
				AboutActivity.this.startActivity(emailintent);
				}catch(Exception e){
					e.printStackTrace();
					Toast.makeText(this,"请检查是否有邮箱服务",Toast.LENGTH_LONG).show();
				}

				break;
				case R.id.githubRelativeLayout:
				Intent intent = new Intent();
                Uri uri = Uri.parse(String.format("https://github.com/WenSomeone"));
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                AboutActivity.this.startActivity(intent);
				break;
					
		}
	}
	
	@Override
	public void onBackPressed()
	{
		// TODO: Implement this method
		finish();
		super.onBackPressed();
	}
	@Override
	public void finish(){
		overridePendingTransition(0,R.anim.slide_out_right);
		
		super.finish();
	}
	
}
