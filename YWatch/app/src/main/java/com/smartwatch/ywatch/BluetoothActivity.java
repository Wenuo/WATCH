package com.smartwatch.ywatch;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;
import android.widget.*;
import android.widget.CompoundButton.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.bluetooth.*;
import java.util.*;
import java.io.*;
import com.getbase.floatingactionbutton.*;
import java.lang.reflect.*;
import com.baoyz.swipemenulistview.*;
import android.widget.AdapterView.*;
import android.graphics.drawable.*;
import android.graphics.*;
import android.util.*;
import android.app.*;
import android.telephony.*;
import android.provider.*;
import com.android.internal.telephony.ITelephony;
import fr.castorflex.android.circularprogressbar.*;
import android.support.v7.widget.*;
import android.bluetooth.BluetoothAdapter.*;
import android.app.admin.*;
import android.animation.*;
import android.database.sqlite.*;
public class BluetoothActivity extends BaseActivity implements OnItemClickListener
{
	//UI
    private Switch blue_switch;
	private TextView Equipment_Text;
	private TextView Connect_Text;
	private TextView State_Text;
	private CircularProgressBar State_Bar;
	private ImageButton back;
	private FloatingActionButton search_device;
	private CardView discoveredCard;
	private ListView find_device_list;
	private List <BluetoothDevice> devicelist=new ArrayList<BluetoothDevice>();
	private MyListAdapter discoveredAdapter;
	
	private SwipeMenuListView connectList;
	private MyListAdapter connectAdapter;
	private List<BluetoothDevice> connect_device_list=new ArrayList<BluetoothDevice>();
	private ProgressDialog mdiglog;
	//保存
	private SharedPreferences sp;
	private SharedPreferences spare;
	
	private String Equipment_Name=null;

	//监听action
    private static final String MESSAGE_BROADCAST="SEND_WX_BROADCAST";
	private static final String QQ="com.tencent.mobileqq";
	private static final String WX="com.tencent.mm";
	private int QQ_MESSAGE=0;
	private int WX_MESSAGE=0;
	private int CALL_MESSAGE=0;
	private int SMS_MESSAGE=0;
	private String INCOMING_NUMBER = null;
	private String INCOMING_MESSAGE_NUMBER=null;
	private String INCOMING_MESSAGE_BODY=null;
	
	private Context context;
	
	//蓝牙
	private BluetoothManager manager;
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothDevice bluetoothDevice;
	private BluetoothGatt bluetoothGatt;
	private BluetoothGattCharacteristic WriteCharacteristic,NotifiCharacteristic;
	
	private SendMessageThread sendMessageThread;
	
	private int connect_click_position;
	private String stateInformation="未连接";
	private boolean isScaning = false;
	private boolean connect_flag=false;
	private String state;
	private Handler myHandler =new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
				// 判断发送的消息
		
					case 101:{
					 state = msg.getData().getString("STATE");
					 State_Text.setText(Equipment_Name+":"+state);
					 dismissDialog();
					 bluetoothGatt.discoverServices();
					 //在已连接里处理设备的位置，在已连接的列表里进行删除操作
					 connect_device_list.add(devicelist.get(connect_click_position));
					 devicelist.remove(connect_click_position);
					 discoveredAdapter.notifyDataSetChanged();
					 connectAdapter.notifyDataSetChanged();
					 find_device_list.setAdapter(discoveredAdapter);
					 connectList.setAdapter(connectAdapter);
					 
					 sendMessageThread=new SendMessageThread();
					 sendMessageThread.start();
					}
						break;
						case 102:{
							state = msg.getData().getString("STATE");
							State_Text.setText(Equipment_Name+":"+state);
							showDialog();}
					
							break;
							case 103:{
								state= msg.getData().getString("STATE");
									State_Text.setText(Equipment_Name+":"+state);
									for(int i=0;i<connect_device_list.size();i++){
										if(connect_device_list.get(i).equals(bluetoothDevice)){
											//devicelist.add(connect_device_list.get(i));
											connect_device_list.remove(i);

										}
									}
									SETADAPTER(devicelist,connect_device_list,find_device_list,connectList);
									sendMessageThread=new SendMessageThread();
									sendMessageThread.stopthread=false;
		
									}
								break;
								case 104:{
										state = msg.getData().getString("STATE");
									    State_Text.setText(state);}
									break;
									case 111:{
										String data=msg.getData().getString("DATA_NOTIFI");
										if(data.equals("01")){
											Toast.makeText(BluetoothActivity.this,"哈哈哈哈哈",Toast.LENGTH_SHORT).show();
										}
										else if(data.equals("02")){
											Toast.makeText(BluetoothActivity.this,"嘿嘿嘿嘿嘿",Toast.LENGTH_SHORT).show();
										}
										else {
											writeHeartNumber(context,data);
										}
									}break;
				}
		super.handleMessage(msg);
		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.b_activity);
		manager=(BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
		bluetoothAdapter=manager.getAdapter();
		initUI();
		HeartSqliteOpenHelper.getInstance(BluetoothActivity.this);
		
		/*if(WX_MESSAGE==1){
			EndCall();
		}*/
	}
	
	
	
	private void initUI(){

		discoveredCard=(CardView)findViewById(R.id.adapter_background);
		State_Bar=(CircularProgressBar)findViewById(R.id.CircularProgressBar);
		Connect_Text=(TextView)findViewById(R.id.paired);
		Equipment_Text=(TextView)findViewById(R.id.aequipment);
        State_Text=(TextView)findViewById(R.id.connect_view);
		search_device=(FloatingActionButton)findViewById(R.id.refresh);
		back=(ImageButton)findViewById(R.id.back_title);
		find_device_list=(ListView)findViewById(R.id.findbluelist);
		discoveredAdapter=new MyListAdapter(BluetoothActivity.this,devicelist);
		find_device_list.setOnItemClickListener(this);
		connectList=(SwipeMenuListView)findViewById(R.id.bluelist);
		connectAdapter=new MyListAdapter(BluetoothActivity.this,connect_device_list);
		//侧滑取消链接
		
		SwipeMenuCreator creator = new SwipeMenuCreator() {


			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));//Color.rgb(0xff, 0xff, 0xff)));
				openItem.setWidth(dp2px(90));
				openItem.setIcon(R.drawable.shanchu);
				menu.addMenuItem(openItem);
			}
		};//动态添加滑动事件
		connectList.setMenuCreator(creator);
		connectList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
    				
					bluetoothGatt.disconnect();
					for(int i=0;i<connect_device_list.size();i++){
						if(connect_device_list.get(i).equals(bluetoothDevice)){
							devicelist.add(connect_device_list.get(i));
							connect_device_list.remove(i);
	
						}
					}
					SETADAPTER(devicelist,connect_device_list,find_device_list,connectList);
					
					return false;
				}
			});
		
		back.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					startActivity(new Intent(BluetoothActivity.this,YActivity.class));
				}
			});
		blue_switch=(Switch)findViewById(R.id.bluetoothon);

		search_device.setVisibility(View.INVISIBLE);
	    sp=getSharedPreferences("config",MODE_PRIVATE);
		boolean isChecked=sp.getBoolean("isChecked",false);
	    spare=getSharedPreferences("cao",MODE_PRIVATE);
		boolean Checked=spare.getBoolean("Checked",true);
		blue_switch.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(android.widget.CompoundButton p1, boolean p2)
				{
					if(p2&&bluetoothAdapter!=null){
						Connect_Text.setVisibility(View.VISIBLE);
						search_device.setVisibility(View.VISIBLE);	
						if(!bluetoothAdapter.isEnabled()){
							Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
							startActivity(intent);
							SharedPreferences sp=getSharedPreferences("config",MODE_PRIVATE);
							SharedPreferences.Editor ed=sp.edit();
							ed.putBoolean("isChecked",true);
							ed.commit();
							}
							}
						else{
							if (bluetoothAdapter!=null) {
								if(bluetoothAdapter.isEnabled()){
									bluetoothAdapter.disable();
									UIinvisibility();
									SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
									SharedPreferences.Editor ed = sp.edit();
									ed.putBoolean("isChecked",false);
									ed.commit();
		           	}
			    }
			}
			
		}
		});

		if(bluetoothAdapter.isEnabled()){

			blue_switch.setChecked(true);
			UIvisibility();

			SharedPreferences spare=getSharedPreferences("cao",MODE_PRIVATE);
			SharedPreferences.Editor editor= spare.edit();
			editor.putBoolean("Checked",true);
			editor.commit();
		}
		else if(bluetoothAdapter.disable()){
			blue_switch.setChecked(false);
			SharedPreferences spare=getSharedPreferences("cao",MODE_PRIVATE);
			SharedPreferences.Editor editor=spare.edit();
			editor.putBoolean("Checked",false);
			editor.commit();
		}
		//广播
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		mFilter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
		mFilter.addAction(MESSAGE_BROADCAST);
		registerReceiver(discoveryFinishReceiver, mFilter);
		

	}
	//搜索
	public void backonClick(View v) {
        switch (v.getId()){
			case R.id.refresh:
				Connect_Text.setVisibility(View.VISIBLE);
				Equipment_Text.setVisibility(View.VISIBLE);
				Toast.makeText(BluetoothActivity.this,"开始搜索",Toast.LENGTH_SHORT).show();
				
				devicelist.clear();
				isScaning=true;
				bluetoothAdapter.startLeScan(mScanCallback);
				State_Bar.setVisibility(View.VISIBLE);
				new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							//结束扫描
							bluetoothAdapter.stopLeScan(mScanCallback);
							runOnUiThread(new Runnable() {
									@Override
									public void run() {
										isScaning=false;
										Toast.makeText(BluetoothActivity.this,"搜索完毕",Toast.LENGTH_SHORT).show();
										State_Bar.setVisibility(View.INVISIBLE);
									}
								});
						}
					},10000);
				break;
				//EndCall();
		}

	}
	
	private void UIinvisibility(){
		discoveredCard.setVisibility(View.INVISIBLE);
		Connect_Text.setVisibility(View.INVISIBLE);
		Equipment_Text.setVisibility(View.INVISIBLE);
		search_device.setVisibility(View.INVISIBLE);
		State_Text.setVisibility(View.INVISIBLE);
		if(isScaning==true){
		stopScan();
		}
	}
	private void UIvisibility(){
		search_device.setVisibility(View.VISIBLE);
		Connect_Text.setVisibility(View.VISIBLE);
		Equipment_Text.setVisibility(View.VISIBLE);
	
	
	}
	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int position, long p4)
	{
		switch (p1.getId()) {
				// 未连接的蓝牙点击事件
			case R.id.findbluelist:
				
				
				
				if(isScaning){
					stopScan();
				}
				bluetoothDevice=devicelist.get(position);
				Equipment_Name=bluetoothDevice.getName();
				connect_click_position=position;
				bluetoothGatt=
					bluetoothDevice.connectGatt(BluetoothActivity.this,false,GattCallback);
		
				break;
			default:
				break;
		}
	}
	
	private void stopScan(){
		State_Bar.setVisibility(View.INVISIBLE);
		isScaning=false;
		bluetoothAdapter.stopLeScan(mScanCallback);
	}
	
	
	
	private BluetoothGattCallback GattCallback = new BluetoothGattCallback(){

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
		{
			super.onCharacteristicChanged(gatt, characteristic);
			if(characteristic.getUuid().toString().equals(NotifiCharacteristic.getUuid().toString())){
				byte[] values=characteristic.getValue();
				//	String str = new String(values, "ISO-8859-1");
				Log.e("读取数据",""+bytesToHexString(values));
				String a=bytesToHexString(values);
				Message msg=new Message();
				msg.what = 111;
				Bundle bundle = new Bundle();
				bundle.putString("DATA_NOTIFI", a);
				msg.setData(bundle);
				myHandler.sendMessage(msg);
		}
}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
		{
			// TODO: Implement this method
			super.onCharacteristicRead(gatt, characteristic, status);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
		{
			// TODO: Implement this method
			super.onCharacteristicWrite(gatt, characteristic, status);
		}

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
		{
			super.onConnectionStateChange(gatt, status, newState);
			switch(newState){
			case BluetoothGatt.STATE_CONNECTED:
				stateInformation="已连接";
				messageUploadUI(101,stateInformation);
				connect_flag=true;
				break;
				case BluetoothGatt.STATE_CONNECTING:
					stateInformation="正在连接";
					Log.e("","正在连接，为什么没有进度条");
					messageUploadUI(102,stateInformation);
					break;
					case BluetoothGatt.STATE_DISCONNECTED:
						stateInformation="已断开";
						messageUploadUI(103,stateInformation);
						connect_flag=false;
						break;
						case BluetoothGatt.STATE_DISCONNECTING:
							stateInformation="正在断开";
							messageUploadUI(104,stateInformation);
						break;
						default:
						break;
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status)
		{
			super.onServicesDiscovered(gatt, status);
			if(status==bluetoothGatt.GATT_SUCCESS){
				List<BluetoothGattService> services=bluetoothGatt.getServices();
				for(BluetoothGattService bluetoothGattSerivice:services){
					//Log.e("","services:"+bluetoothGattSerivice.getUuid().toString());
					List<BluetoothGattCharacteristic> character=bluetoothGattSerivice.getCharacteristics();

					for(BluetoothGattCharacteristic bluetoothGattCharacteristic:character){
						Log.e(""," Charaic:"+bluetoothGattCharacteristic.getUuid().toString());
						if(bluetoothGattCharacteristic.getUuid().toString().equals("00003323-0000-1000-8000-00805f9b34fb")){
							WriteCharacteristic=bluetoothGattCharacteristic;
							NotifiCharacteristic=bluetoothGattCharacteristic;
							setNotification(NotifiCharacteristic,true);
						}
					}

				}
			}
		}

		@Override
		public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
		{
			// TODO: Implement this method
			super.onDescriptorRead(gatt, descriptor, status);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
		{
			// TODO: Implement this method
			super.onDescriptorWrite(gatt, descriptor, status);
		}
		
	};
	
	
	private LeScanCallback mScanCallback=new LeScanCallback(){

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] values)
		{
			runOnUiThread(new Runnable(){

					@Override
					public void run()
					{
						if(!devicelist.contains(device)){
							devicelist.add(device);
							find_device_list.setAdapter(discoveredAdapter);
							discoveredAdapter.notifyDataSetChanged();
							//SETADAPTER(devicelist,connect_device_list,find_device_list,connectList);
						}
					}	
				});

		}
		
		
	};
	
	
	private final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
			SharedPreferences sp=getSharedPreferences("notisetting",MODE_PRIVATE);
			boolean QQON_OFF=sp.getBoolean("qqChecked",true);	
			boolean MMON_OFF=sp.getBoolean("mmChecked",true); 
			boolean SMSON_OFF=sp.getBoolean("smsChecked",true);
           if(action.equals(MESSAGE_BROADCAST)){
				Bundle bundle=intent.getExtras();
				String packageName=bundle.getString("packageName");
				String message = bundle.getString("message");
				switch(packageName){
					case WX:
						if(MMON_OFF){
							WX_MESSAGE=1;
                            Toast.makeText(BluetoothActivity.this,"wechat:"+message,Toast.LENGTH_SHORT).show();
							EndCall();
						}
						else{
							WX_MESSAGE=0;
						}

						break;
					case QQ:
						if(QQON_OFF){

							QQ_MESSAGE=1;
							Toast.makeText(BluetoothActivity.this,"QQ:"+message,Toast.LENGTH_SHORT).show();
						}
						else{
							QQ_MESSAGE=0;
						}
						break;
				}
			}
			else if(action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
				doReceivePhone(context,intent);  
			}
			else if(action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)){
				for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    String messageBody = smsMessage.getMessageBody();
                    INCOMING_MESSAGE_BODY=messageBody;
                    INCOMING_MESSAGE_NUMBER = smsMessage.getOriginatingAddress();
					if(SMSON_OFF){
						SMS_MESSAGE=1;
						Toast.makeText(BluetoothActivity.this,"Message state "+SMS_MESSAGE+INCOMING_MESSAGE_NUMBER, Toast.LENGTH_LONG).show();
					}
					else{
						SMS_MESSAGE=0;
						INCOMING_MESSAGE_NUMBER=null;
					}
                }

			}


		};

	};
	
	
	public void doReceivePhone(Context context, Intent intent) {  
		SharedPreferences sp=getSharedPreferences("notisetting",MODE_PRIVATE);
		boolean CALLON_OFF=sp.getBoolean("callChecked",true);
        String phoneNumber = intent.getStringExtra(  
			TelephonyManager.EXTRA_INCOMING_NUMBER);  
        TelephonyManager telephony =   
			(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
        int state = telephony.getCallState();  
        switch(state){  
			case TelephonyManager.CALL_STATE_RINGING:  
				Log.i("dianhua", "[Broadcast]等待接电话="+phoneNumber);  
				if(CALLON_OFF){
					CALL_MESSAGE=1;
					INCOMING_NUMBER=phoneNumber;
					Toast.makeText(this,phoneNumber,Toast.LENGTH_LONG).show();
				}
				else{
					CALL_MESSAGE=0;
					INCOMING_NUMBER=null;
				}
				break;  
			case TelephonyManager.CALL_STATE_IDLE:  
				Log.i("dianhua", "[Broadcast]电话挂断="+phoneNumber);  
				CALL_MESSAGE=0;
				Toast.makeText(this,phoneNumber,Toast.LENGTH_LONG).show();
				break;  
        }  
    }  
	public void showDialog (){
      // if (mdiglog == null)
          mdiglog = new ProgressDialog(this);
        mdiglog.setCancelable(false);
        mdiglog.setIndeterminate(false);
		mdiglog.setMessage("正在连接");
        mdiglog.show();
    }
	public void dismissDialog() {
        if (mdiglog != null && mdiglog.isShowing()) {
            mdiglog.dismiss();
        }
    }
	
	private void messageUploadUI(int a,String s){
		Message msg = new Message();
		msg.what = a;
		Bundle bundle = new Bundle();
		bundle.putString("STATE", s);
		msg.setData(bundle);
		myHandler.sendMessage(msg);
	}
	//挂断电话
	public void EndCall() {
        try {
            Method method = Class.forName("android.os.ServiceManager")
				.getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, new Object[]{Context.TELEPHONY_SERVICE});
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
            telephony.endCall();
        } catch (NoSuchMethodException e) {
           // Log.d(TAG, "", e);
        } catch (ClassNotFoundException e) {
          //  Log.d(TAG, "", e);
        } catch (Exception e) {
        }
    }

	
	private void setNotification(
		BluetoothGattCharacteristic characteristic, boolean enabled)
	{
		if (bluetoothAdapter == null || bluetoothGatt == null)
		{
			//Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
		BluetoothGattDescriptor descriptor = characteristic
			.getDescriptor(UUID
						   .fromString("00002902-0000-1000-8000-00805f9b34fb"));

		if (enabled)
		{
			descriptor
				.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		} else
		{
			descriptor
				.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
		}
		bluetoothGatt.writeDescriptor(descriptor);
	}

	/** 
	 * @Title: getCharacteristicDescriptor 
	 * @Description: TODO(得到特征值下的描述值) 
	 * @param @param 无
	 * @return void   
	 * @throws 
	 */ 
	public void getCharacteristicDescriptor(BluetoothGattDescriptor descriptor)
	{
		if (bluetoothAdapter == null || bluetoothGatt == null)
		{
			//Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}

		bluetoothGatt.readDescriptor(descriptor);
	}
	
	
	private static String bytesToHexString(byte[] src){
		StringBuilder builder=new StringBuilder();
		if(src==null||src.length<=0){
			return null;
		}
		for(int i=0;i<src.length;i++){
			int v=src[i] & 0xFF;
			String hv=Integer.toHexString(v);
			if(hv.length()<2){
				builder.append(0);
			}
			builder.append(hv);
		}
		return builder.toString();
	}
	
	private void SETADAPTER(List<BluetoothDevice> a,List<BluetoothDevice> b,ListView c,SwipeMenuListView d){
		discoveredAdapter=new MyListAdapter(BluetoothActivity.this,a);
		discoveredAdapter.notifyDataSetChanged();
		c.setAdapter(discoveredAdapter);
		connectAdapter=new MyListAdapter(BluetoothActivity.this,b);
		connectAdapter.notifyDataSetChanged();
		d.setAdapter(connectAdapter);
		
	}
//侧滑删除  dp2px方法
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}
	
	@Override
	protected void onDestroy()
	{
		unregisterReceiver(discoveryFinishReceiver);
		super.onDestroy();
	}
   	private void writeHeartNumber(Context context,String string){
		java.util.Date writeTime = new java.util.Date();
		SQLiteDatabase db=HeartSqliteOpenHelper.getInstance(context).getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("heartdata",writeTime.getTime());
		values.put("heartnum",string);
		db.insert("heart",null,values);
		db.close();
	}
	
	

	private class SendMessageThread extends Thread{
		private volatile boolean stopthread=false;
		public  void run(){
			while(!stopthread){
				try{
					Thread.sleep(1000);
					if(QQ_MESSAGE==1){
						Log.d("AAA","qq有消息");
					}
					java.util.Calendar c = java.util.Calendar.getInstance();
					int hh = c.get(java.util.Calendar.HOUR_OF_DAY);
					int mm = c.get(java.util.Calendar.MINUTE);
					int ss = c.get(java.util.Calendar.SECOND);
					String dataUrl = hh+":"+mm+":"+ss+":"+QQ_MESSAGE+"\0"+WX_MESSAGE+"\0"+CALL_MESSAGE+"\0"+INCOMING_NUMBER+"\0"+SMS_MESSAGE+"\0"+INCOMING_MESSAGE_BODY+"\0"+INCOMING_MESSAGE_NUMBER+"\0";
					Log.d("AAA","QQ : "+QQ_MESSAGE);
					//Log.d("mybt","Message received :"+MESSAGE_STATE+" "+INCOMING_MESSAGE_NUMBER+" "+INCOMING_MESSAGE_BODY);
					byte[] byte_data = dataUrl.getBytes();
					WriteCharacteristic.setValue(byte_data);
					bluetoothGatt.writeCharacteristic(WriteCharacteristic);
				}catch(Exception e){			
					Log.d("AAA","send failed");
				}
				if(stopthread){
					break;
				}
				QQ_MESSAGE=0;
				WX_MESSAGE=0;
				CALL_MESSAGE=0;
				INCOMING_NUMBER=null;
				SMS_MESSAGE=0;
				INCOMING_MESSAGE_NUMBER=null;
				INCOMING_MESSAGE_BODY=null;
			}
		}
	
	
	
}
}
