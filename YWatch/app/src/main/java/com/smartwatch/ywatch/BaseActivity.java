package com.smartwatch.ywatch;
import me.imid.swipebacklayout.lib.app.*;
import me.imid.swipebacklayout.lib.*;
import android.os.*;
import android.view.*;
import android.content.*;
import android.content.pm.*;

public class BaseActivity extends SwipeBackActivity {

    private SwipeBackLayout mSwipeBackLayout;

	private static  boolean blFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		truetheme();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mSwipeBackLayout = getSwipeBackLayout();
        //设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        mSwipeBackLayout.setEdgeSize(200);//滑动删除的效果只能从边界滑动才有效果，如果要扩大touch的范围，可以调用这个方法
		
    }
	
	private void truetheme(){
		SharedPreferences preferences = this.getSharedPreferences("Theme", MODE_WORLD_READABLE);
        blFlag = preferences.getBoolean("ThemeFlag",true);
		int b=preferences.getInt("ThemeColor",0);
		int themeId = R.style.Color_H;
		if(blFlag){
			switch(b){
				case R.style.Color_A:
					themeId=R.style.Color_A;
					break;
				case R.style.Color_B:
					themeId=R.style.Color_B;
					break;
				case R.style.Color_C:
					themeId=R.style.Color_C;
					break;
				case R.style.Color_D:
					themeId=R.style.Color_D;
					break;
				case R.style.Color_E:
					themeId=R.style.Color_E;
					break;
				case R.style.Color_F:
					themeId=R.style.Color_F;
					break;
				case R.style.Color_G:
					themeId=R.style.Color_G;
					break;
				case R.style.Color_H:
					themeId=R.style.Color_H;
					break;
				case R.style.Color_I:
					themeId=R.style.Color_I;
					break;
				case R.style.Color_J:
					themeId=R.style.Color_J;
					break;
				case R.style.Color_K:
					themeId=R.style.Color_K;
					break;
				case R.style.Color_L:
					themeId=R.style.Color_L;
					break;
			}	
		this.setTheme(themeId);
	}
	else{
		this.setTheme(R.style.NightTheme);
	}
	}
}
