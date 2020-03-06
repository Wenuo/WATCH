package com.smartwatch.ywatch;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View;
import android.view.View.*;
import android.view.WindowManager;
import android.text.*;
import java.util.*;
import android.database.sqlite.*;
import java.text.*;
import android.database.*;
import android.support.v7.widget.*;
import com.rengwuxian.materialedittext.*;
import android.content.*;
import com.zaaach.toprightmenu.*;
import android.content.pm.*;
public class SearchActivity extends Activity
{
	private Context context;
	private ListView lv;
	private List<Trace> traceList = new ArrayList<>(10);
	private TraceListAdapter aapter;
	private ImageButton searchFind;
	private ImageButton backtotheH;
	private EditText startEdittext;
	private EditText endEdittext;
	private CardView card;
	private static String ss="select*from heart where heartdata>? and heartdata<?";
	private TopRightMenu topright;
	private static boolean blFlag = false;
	private int theme=AlertDialog.THEME_HOLO_LIGHT;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		truetheme();
        setContentView(R.layout.search_activity);
		//setSwipeBackEnable(false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
	    begin();
		HeartSqliteOpenHelper.getInstance(SearchActivity.this);
	}
	private void begin()
{
	    lv=(ListView)findViewById(R.id.searchListView);
		searchFind=(ImageButton)findViewById(R.id.search_find);
		backtotheH=(ImageButton)findViewById(R.id.serch_backto);
		endEdittext=(EditText)findViewById(R.id.endEdit);
		startEdittext=(EditText)findViewById(R.id.startEdit);
		startEdittext.setInputType(InputType.TYPE_NULL);
		endEdittext.setInputType(InputType.TYPE_NULL);
		card=(CardView)findViewById(R.id.lv_background);
	    card.setVisibility(View.INVISIBLE);
	backtotheH.setOnClickListener(new OnClickListener(){

			

				@Override
				public void onClick(View p1)
				{
					card.setVisibility(View.INVISIBLE);
					finishAfterTransition();
				}	
			});
	searchFind.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View p1)
			{
				// TODO: Implement this method
				showSecondPopupMenu();
				return false;
			}		
		});

	searchFind.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					
				if(endEdittext.getText().toString().length()==0||startEdittext.getText().toString().length()==0){
					Toast.makeText(SearchActivity.this,"请输入正确日期!",Toast.LENGTH_LONG).show();
				}
				else{
					card.setVisibility(View.VISIBLE);
				searchtime(context);
					}	
				
				}		
			});
		startEdittext.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					startEdittext.setFocusableInTouchMode(true);
					showstartDatePickerDialog();
				}




			});


		endEdittext.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View k)
				{
					// TODO: Implement this method
				endEdittext.setFocusableInTouchMode(true);
					showendDatePickerDialog();
				}


			});

	}
	
	
	private void showstartDatePickerDialog()
	{
		// TODO: Implement this method
		Calendar c = Calendar.getInstance(); 
        new DatePickerDialog(SearchActivity.this, theme,new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					// TODO Auto-generated method stub
					startEdittext.setText(year+"-"+((monthOfYear+1)<10?"0"+(monthOfYear+1):(monthOfYear+1))+"-"+((dayOfMonth<10)?"0"+dayOfMonth:dayOfMonth));  
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
		
	}
	private void showendDatePickerDialog(){
		Calendar b = Calendar.getInstance(); 
        new DatePickerDialog(SearchActivity.this,theme,new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					// TODO Auto-generated method stub
					endEdittext.setText(year+"-"+((monthOfYear+1)<10?"0"+(monthOfYear+1):(monthOfYear+1))+"-"+((dayOfMonth<10)?"0"+dayOfMonth:dayOfMonth));  
				}
			}, b.get(Calendar.YEAR), b.get(Calendar.MONTH), b.get(Calendar.DAY_OF_MONTH)).show();
		
		
	}
	public void searchtime(Context context){
		traceList.clear();
		SQLiteDatabase sqliteDatabase=HeartSqliteOpenHelper.getInstance(context).getReadableDatabase();
		Date s=getDate(startEdittext.getText().toString());
		Date e=getDate(endEdittext.getText().toString());
		Calendar cal = Calendar.getInstance();
		cal.setTime(s);
		Long startTime=cal.getTimeInMillis();
		Calendar cals = Calendar.getInstance();
		cals.setTime(e);
		Long endTime=cals.getTimeInMillis();
	
		long four=(24*60*60*1000);
	    Long thirdTime=endTime+four;
		//Long thirdTime=endTime+(firsTtime-secondTime);
		Cursor cursor = sqliteDatabase.rawQuery(ss,new String[] { String.valueOf(startTime), String.valueOf(thirdTime) });
		if(endTime<startTime){
			Toast.makeText(SearchActivity.this,"终止时间不能小于起始时间",Toast.LENGTH_LONG).show();
			traceList.clear();
			card.setVisibility(View.INVISIBLE);
			}
		else{
		if(cursor.getCount()==0){
			Toast.makeText(SearchActivity.this,"没有该日期的数据",Toast.LENGTH_LONG).show();
			traceList.clear();
			card.setVisibility(View.INVISIBLE);
		}
		else{
		if(cursor.moveToLast()){
			do{

				Long hd=cursor.getLong(cursor.getColumnIndex("heartdata"));
				String heart_num=cursor.getString(cursor.getColumnIndex("heartnum"));
				SimpleDateFormat dq=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss ");
				String hdd=dq.format(hd);
				traceList.add(new Trace(hdd,heart_num));
			}while(cursor.moveToPrevious());
		    aapter = new TraceListAdapter(this, traceList);
			lv.setAdapter(aapter);
			aapter.notifyDataSetChanged();
			cursor.close();
			sqliteDatabase.close();
		}
	}
	}
	}
	
	private void d_delect(Context context){
		SQLiteDatabase sqliteDatebase=HeartSqliteOpenHelper.getInstance(context).getWritableDatabase();
	   String sqlab="delete from heart where heartdata>? and heartdata<?";
	

		Date s=getDate(startEdittext.getText().toString());
		Date e=getDate(endEdittext.getText().toString());
		Calendar cal = Calendar.getInstance();
		cal.setTime(s);
		Long startTime=cal.getTimeInMillis();
		Calendar cals = Calendar.getInstance();
		cals.setTime(e);
		Long endTime=cals.getTimeInMillis();

        long secondTime=(24*60*60*1000);
		Long thirdTime=endTime+secondTime;
		sqliteDatebase.execSQL(sqlab,new Object[]{String.valueOf(startTime), String.valueOf(thirdTime) });
		if(endTime<startTime){
			Toast.makeText(SearchActivity.this,"终止时间不能小于起始时间",Toast.LENGTH_LONG).show();
			traceList.clear();
			card.setVisibility(View.INVISIBLE);
		}
        sqliteDatebase.close();
	}
	
	
	
	
	
	public Date getDate(String str){
		Date date=null;
		try
		{
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
			date=formatter.parse(str);
			return date;
			
		}catch(Exception e){
			
		}
		return null;
	}
	
	
	private void showSecondPopupMenu(){
		topright=new TopRightMenu(SearchActivity.this);
		List<MenuItem> menuItem = new ArrayList<>();
		menuItem.add(new MenuItem(R.drawable.searchinlist, "查找"));
		menuItem.add(new MenuItem(R.drawable.deletethedata, "删除"));
		topright
			.setHeight(190)     //默认高度480
			.setWidth(280) //默认宽度wrap_content
			.showIcon(true)     //显示菜单图标，默认为true
			.dimBackground(false)           //背景变暗，默认为true
			.needAnimationStyle(true)   //显示动画，默认为true
			.setAnimationStyle(R.style.TRM_ANIM_STYLE) 
			.addMenuList(menuItem)
			.setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
				@Override
				public void onMenuItemClick(int p)
				{
					switch(p){
						case 0:
							if(endEdittext.getText().toString().length()==0||startEdittext.getText().toString().length()==0){
								Toast.makeText(SearchActivity.this,"请输入正确日期!",Toast.LENGTH_LONG).show();
							}
							else{
								card.setVisibility(View.VISIBLE);
								searchtime(context);
							}	
							
							break;
						case 1:
							if(endEdittext.getText().toString().length()==0||startEdittext.getText().toString().length()==0){
								Toast.makeText(SearchActivity.this,"请输入正确日期!",Toast.LENGTH_LONG).show();
							}
							else{
							d_delect(context);
							card.setVisibility(View.INVISIBLE);
							Toast.makeText(SearchActivity.this,"已删除相关日期数据",Toast.LENGTH_LONG).show();
							}
							
							break;
					}
				}}).showAsDropDown(searchFind,-220,0);
	}
	
	private void truetheme(){
		SharedPreferences preferences = this.getSharedPreferences("Theme", MODE_WORLD_READABLE);
        blFlag = preferences.getBoolean("ThemeFlag",true);
		int b=preferences.getInt("ThemeColor",0);
		int themeId = R.style.Color_H ;
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
            theme=AlertDialog.THEME_HOLO_LIGHT;
			this.setTheme(themeId);
		}
		else{
			theme=AlertDialog.THEME_HOLO_DARK;
			this.setTheme(R.style.NightTheme);
		}
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		truetheme();
		super.onResume();
	}


	@Override
	protected void onStop()
	{
		// TODO: Implement this method
		recreate();
		super.onStop();
	}

	@Override
	public void onBackPressed()
	{
		// TODO: Implement this method
		super.onBackPressed();
	}
	@Override
	public void finish(){
		overridePendingTransition(0,R.anim.slide_out_right);
		super.finish();
	}
	
	
}
