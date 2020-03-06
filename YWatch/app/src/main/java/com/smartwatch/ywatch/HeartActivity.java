package com.smartwatch.ywatch;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.os.*;
import android.widget.*;
import java.text.*;
import java.util.*;
import java.lang.reflect.*;
import android.support.v7.widget.CardView;
import android.app.*;
import android.graphics.*;
import com.zaaach.toprightmenu.*;
import com.zaaach.toprightmenu.MenuItem;
import android.view.*;
import android.view.View.*;
import android.util.*;
public class HeartActivity extends BaseActivity implements View.OnClickListener
{

    private ImageView heartImage;
    private ImageView view;
    private static boolean blFlag = false;
	private ImageButton back;
	private ImageButton search;
	private RelativeLayout RL;
	private ListView lvTrace;
    private List<Trace> traceList = new ArrayList<>(10);
    private TraceListAdapter adapter;
	private TextView h_num;
	private Handler myHandler;
	private CardView sevenCardView;
	private TopRightMenu secondtopright;

	private Context context;

	//private DialogCreat dialogCreat;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//truetheme();
        setContentView(R.layout.heart_activity);
		
		myHandler=new Handler(){
			public void handleMessage(Message msg)
			{
                super.handleMessage(msg);
                switch (msg.what) {// 通过msg.what区分消息来源
                    case 7:
						// Log.v(TAG, "otherThread call MainThread handler running:" + Thread.currentThread().getName());
						String xinlv=msg.getData().getString("h_state");
                        h_num.setText(xinlv);
                        break;
						case 3:
							oneday(context);
							Toast.makeText(HeartActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
							break;
							case 4:
								String xin_l=msg.getData().getString("heartnum");
								h_num.setText(xin_l);
								break;
          

                }
            }
		};
		//oneday();
		/*IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(SEND_HN);
		registerReceiver(heartReceiver, mFilter);*/
		
	
        view=(ImageView)findViewById(R.id.h_View);
		heartImage=(ImageView)findViewById(R.id.heart_Image);
		view.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					writeheartnum(context);
					//showDialog();
					Message msg=new Message();
					msg.what=3;
					myHandler.sendMessage(msg);
				}
				
			
		});
		RL=(RelativeLayout)findViewById(R.id.h_RelativeLayout);
		sevenCardView=(CardView)findViewById(R.id.seven_cardview);
		sevenCardView.setVisibility(View.INVISIBLE);
		lvTrace = (ListView)findViewById(R.id.lvTrace);
		back=(ImageButton)findViewById(R.id.heart_back);
		search=(ImageButton)findViewById(R.id.heart_find);
	    h_num=(TextView)findViewById(R.id.heartnum);
		back.setOnClickListener(this);
		//heartSqliteOpenHelper=new HeartSqliteOpenHelper(heartSqliteOpenHelper);
		//heartSqliteOpenHelper.getWritableDatabase();
		HeartSqliteOpenHelper.getInstance(HeartActivity.this);
		oneday(context);
		search.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v)
				{
					showSecondPopupMenu();
				}
		 });
		}
	@Override
    public void onClick(View id) {
        switch (id.getId()){
			case R.id.heart_back:
               finish();
                break;
        }
    }
	
/*BroadcastReceiver heartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
			if(action.equals(SEND_HN)){
				Bundle bundle=intent.getExtras();
				String number= bundle.getString("number");
				Log.e("","有广播");
				//SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss ");
				//Date curDate = new Date(System.currentTimeMillis());
				//String str = formatter.format(curDate);
				/*java.util.Date writeTime = new java.util.Date();
				 SQLiteDatabase db=HeartSqliteOpenHelper.getInstance(context).getWritableDatabase();
				 ContentValues values=new ContentValues();
				 values.put("heartdata",writeTime.getTime());
				 values.put("heartnum",number);
				 db.insert("heart",null,values);
				 db.close();
				 Message msg = new Message();
				 msg.what = 7;
				 Bundle b = new Bundle();
				 b.putString("h_state", number);
				 msg.setData(b);
				 // 将连接状态更新的UI的textview上
				 myHandler.sendMessage(msg);
				Toast.makeText(HeartActivity.this,number,Toast.LENGTH_SHORT).show();
			}
		}

	};*/
	private void truetheme(){
		SharedPreferences preferences = getSharedPreferences("default_night", MODE_PRIVATE);
        blFlag = preferences.getBoolean("default_night",true);
        if (blFlag) {
            this.setTheme(R.style.Color_H);
        }
        else {
            this.setTheme(R.style.NightTheme);
        }
	}
	

	private void showSecondPopupMenu(){
		secondtopright=new TopRightMenu(HeartActivity.this);
		List<MenuItem> menuItem = new ArrayList<>();
		menuItem.add(new MenuItem(R.drawable.weekinlist, " 7天"));
		menuItem.add(new MenuItem(R.drawable.searchinlist, "查找"));
		menuItem.add(new MenuItem(R.drawable.line_chart,"图表"));
		secondtopright
			.setHeight(285)     //默认高度480
			.setWidth(280) //默认宽度wrap_content
			.showIcon(true)     //显示菜单图标，默认为true
			.dimBackground(false)           //背景变暗，默认为true
			.needAnimationStyle(true)   //显示动画
			.setAnimationStyle(R.style.TRM_ANIM_STYLE) 
			.addMenuList(menuItem)
			.setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
				@Override
				public void onMenuItemClick(int p)
				{
					switch(p){
						case 0:
							sevenCardView.setVisibility(View.VISIBLE);
							seven(context);
							Toast.makeText(HeartActivity.this,"week",Toast.LENGTH_LONG).show();
							break;
					
							
							case 1:
							startActivity(new Intent(HeartActivity.this,SearchActivity.class),
										  ActivityOptions.makeSceneTransitionAnimation(HeartActivity.this,heartImage,"s").toBundle());
								break;
								case 2:
									startActivity(new Intent(HeartActivity.this,ChartActivity.class));
		
					}
				}}).showAsDropDown(search,-220,0);
	}
	

	/*private void initData() {
		SQLiteDatabase sqliteDatabase=heartSqliteOpenHelper.getReadableDatabase();
		Cursor cursor=sqliteDatabase.query("heart",null,null,null,null,null,null);
		if(cursor.moveToLast()){
			do{
				String heart_data=cursor.getString(cursor.getColumnIndex("heartdata"));
				String heart_num=cursor.getString(cursor.getColumnIndex("heartnum"));
				traceList.add(new Trace(heart_data,heart_num));
			}while(cursor.moveToPrevious());

		}
        adapter = new TraceListAdapter(this, traceList);
        lvTrace.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		cursor.close();
    }*/
	
			
	private void writeheartnum(Context context){
		java.util.Date writeTime = new java.util.Date();
		SQLiteDatabase db=HeartSqliteOpenHelper.getInstance(context).getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("heartdata",writeTime.getTime());
		values.put("heartnum","78");
		db.insert("heart",null,values);
		db.close();
		Message msg=new Message();
		msg.what=4;
		Bundle bund=new Bundle();
		bund.putString("heartnum","78");
		msg.setData(bund);
		myHandler.sendMessage(msg);
	
	}
	private void seven(Context context){
		traceList.clear();
		SQLiteDatabase db=HeartSqliteOpenHelper.getInstance(context).getReadableDatabase();
		String ss="select*from heart where heartdata>=? and heartdata<?";
		Date now=new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		Long endTime=cal.getTimeInMillis();
		Calendar cals=Calendar.getInstance();
		cals.set(Calendar.DAY_OF_YEAR,cals.get(Calendar.DAY_OF_YEAR)-7);
		long startTime = cals.getTimeInMillis();
		Cursor cursor = db.rawQuery(ss,new String[] { String.valueOf(startTime), String.valueOf(endTime) });
	    if(cursor.getCount()==0){
			Toast.makeText(HeartActivity.this,"没有数据",Toast.LENGTH_LONG).show();
			sevenCardView.setVisibility(View.INVISIBLE);
		}
		else if(cursor.moveToLast()){
			do{

				Long dddd=cursor.getLong(cursor.getColumnIndex("heartdata"));
				String heart_num=cursor.getString(cursor.getColumnIndex("heartnum"));
				SimpleDateFormat dq=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss ");
				String ddddd=dq.format(dddd);
				traceList.add(new Trace(ddddd,heart_num));
			}while(cursor.moveToPrevious());
		}
		adapter = new TraceListAdapter(this, traceList);
		lvTrace.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		cursor.close();
		db.close();
	}
	
	/*
	private void fifth(Context context){
		traceList.clear();
		SQLiteDatabase db=HeartSqliteOpenHelper.getInstance(context).getReadableDatabase();
		String ss="select*from heart where heartdata>=? and heartdata<=?";
		Date now=new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		Long endTime=cal.getTimeInMillis();
		Calendar cals=Calendar.getInstance();
		cals.set(Calendar.DAY_OF_YEAR,cals.get(Calendar.DAY_OF_YEAR)-30);
		long startTime = cals.getTimeInMillis();
		Cursor cursor = db.rawQuery(ss,new String[] { String.valueOf(startTime), String.valueOf(endTime) });
		if(cursor.getCount()==0){
			sevenCardView.setVisibility(View.INVISIBLE);
		}
		if(cursor.moveToLast()){
			do{

				Long dddd=cursor.getLong(cursor.getColumnIndex("heartdata"));
				String heart_num=cursor.getString(cursor.getColumnIndex("heartnum"));
				SimpleDateFormat dq=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss ");
				String ddddd=dq.format(dddd);
				traceList.add(new Trace(ddddd,heart_num));
			}while(cursor.moveToPrevious());
		}
		adapter = new TraceListAdapter(this, traceList);
		lvTrace.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		cursor.close();
		db.close();
	}	
	*/
	private void oneday(Context context){
		traceList.clear();
		sevenCardView.setVisibility(View.VISIBLE);
		SQLiteDatabase db=HeartSqliteOpenHelper.getInstance(context).getReadableDatabase();
		String ss="select*from heart where heartdata>=? and heartdata<=?";
		Date now=new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		Long endTime=cal.getTimeInMillis();
		//cals.set(Calendar.DAY_OF_YEAR,cals.get(Calendar.DAY_OF_YEAR)-1);
		Calendar calendar = Calendar.getInstance();  
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long startTime = calendar.getTimeInMillis();
		Cursor cursor = db.rawQuery(ss,new String[] { String.valueOf(startTime), String.valueOf(endTime) });
		if(cursor.getCount()==0){
		traceList.add(new Trace("今天还没测量心率喔!","0"));
			adapter = new TraceListAdapter(this, traceList);
			lvTrace.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		else{
		if(cursor.moveToLast()){
			do{
				Long dddd=cursor.getLong(cursor.getColumnIndex("heartdata"));
				String heart_num=cursor.getString(cursor.getColumnIndex("heartnum"));
				SimpleDateFormat dq=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss ");
				String ddddd=dq.format(dddd);
				traceList.add(new Trace(ddddd,heart_num));
			}while(cursor.moveToPrevious());
		}
		adapter = new TraceListAdapter(this, traceList);
		lvTrace.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		cursor.close();
		db.close();
	}
	}

	/*private void showDialog(){
		dialogCreat=new DialogCreat(HeartActivity.this);
		dialogCreat.setTitle("请确认手表是否佩戴好");
		dialogCreat.setYesOnclickListener(new DialogCreat.onYesOnclickListener(){

				@Override
				public void onYesClick()
				{
					writeheartnum(context);
					dialogCreat.dismiss();
				}	  
			});
		dialogCreat.setNoOnclickListener(new DialogCreat.onNoOnclickListener(){

				@Override
				public void onNoClick()
				{
					dialogCreat.dismiss();
				}


			});
		dialogCreat.show();
		}*/
	
	
	@Override
	protected void onResume()
	{
	   
		super.onResume();
		oneday(context);
		//   uptheme();
		truetheme();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onStop()
	{
		// TODO: Implement this method
		recreate();
		super.onStop();
	}

	
	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		//recreate();

		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		//unregisterReceiver(heartReceiver);
		super.onDestroy();
		
	}


}
