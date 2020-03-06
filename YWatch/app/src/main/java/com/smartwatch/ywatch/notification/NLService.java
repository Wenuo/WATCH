package com.smartwatch.ywatch.notification;
import android.service.notification.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.app.*;
import android.content.pm.*;

public class NLService extends NotificationListenerService {
    public static final String SEND_WX_BROADCAST="SEND_WX_BROADCAST";
	private Context mContext;
   /* private static final class ApplicationPackageNames {
        public static final String QQ = "com.tencent.mobileqq";
        public static final String WX = "com.tencent.mm";
        public static final String MMS = "com.android.mms";
        public static final String CALL= "com.android.incallui";
    }*/
    

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        Log.e("AAA", "onNotificationPosted   ID :"
			  + sbn.getId() + "\t"
			  + sbn.getNotification().tickerText + "\t"
			  + sbn.getPackageName());
        Intent sintent=new Intent();
        sintent.setAction(SEND_WX_BROADCAST);
        Bundle bundle=new Bundle();
        if (!sbn.getPackageName().isEmpty()){
            bundle.putString("packageName",sbn.getPackageName());
        }
        if (sbn.getNotification().tickerText != null){
            bundle.putString("message",sbn.getNotification().tickerText.toString());
        }
        sintent.putExtras(bundle);
        this.sendBroadcast(sintent);

    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
    }
	public void toggleNotificationListenerService() {
        PackageManager pm = mContext.getPackageManager();
        pm.setComponentEnabledSetting(
			new ComponentName(mContext,NLService.class),
			PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(
			new ComponentName(mContext,NLService.class),
			PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
	
}

