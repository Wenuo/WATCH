package com.smartwatch.ywatch;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.support.v7.widget.CardView;
import android.support.v4.widget.*;
import android.support.v4.app.*;
import java.util.*;
import android.widget.AdapterView.*;
import android.support.annotation.*;
import android.net.*;
import android.provider.*;
import android.database.*;
import java.io.*;
import android.graphics.drawable.*;
import android.util.*;
import android.widget.SlidingDrawer.*;
import android.support.v7.widget.*;
import android.content.pm.*;

public class YActivity extends Activity implements View.OnClickListener,OnItemClickListener,OnItemLongClickListener
{
	//private ListView colorlist;
	private ColorAdapter colorAdapter;
	private List<Colors> color;
	private RecyclerView colorlist;
	
	
	private DialogCreat dialogCreat;
    private ImageView head_image;
	private SharedPreferences sp;
	private SharedPreferences.Editor spe;
	private static boolean blFlag = true;
	private Button finger;
    private CardView blue;
	private CardView noti;
	private DrawerLayout drawerlayouy;
	private SideListAdapter myAdapter;
	private ListView sidelistview;
	private List<Tool> Tools;
	private long firstTime= 0;
    private ColorPickerView colorpick;
	private LinearLayout lineary;
	private LinearLayout relayout;
	private static final int IMAGE_REQUEST_CODE=1;
	private static final int RESIZE_REQUEST_CODE=2;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		truetheme();
		setContentView(R.layout.side_main);
		
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
		head_image=(ImageView)findViewById(R.id.head_iamge);
		lineary=(LinearLayout)findViewById(R.id.main_activityLinearLayout);
		 relayout=(LinearLayout)findViewById(R.id.side_RelativeLayout);
		colorlist=(RecyclerView)findViewById(R.id.color_recycler);
		LinearLayoutManager linear =new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
		colorlist.setLayoutManager(linear);
		addcolor();
		colorpick=(ColorPickerView)findViewById(R.id.colorPickerView);
		sidelistview=(ListView)findViewById(R.id.side_ListView);
		sidelistview.setOnItemClickListener(this);
		sidelistview.setOnItemLongClickListener(this);
		drawerlayouy=(DrawerLayout)findViewById(R.id.activity_na);
		blue=(CardView)findViewById(R.id.bluecard);
		noti=(CardView)findViewById(R.id.noticard);
		
		finger=(Button)findViewById(R.id.finger);
		finger.setOnClickListener(this);
		findViewById(R.id.mybluetooth).setOnClickListener(this);
		findViewById(R.id.mynotification).setOnClickListener(this);
		head_image.setOnLongClickListener((new OnLongClickListener(){
				  @Override
				  public boolean onLongClick(View p1)
					  {
										  // TODO: Implement this method
						  showDialog();
						 return false;
					  }
		}));
		
		
		
		colorAdapter.setOnItemClickListener(new ColorAdapter.OnItemClickListener(){

				@Override
				public void onItemClick(View view, int position)
				{
					// TODO: Implement this metho
					switch(position){
						case 0:
						changeTheme(R.style.Color_A);
							break;
							case 1:
								changeTheme(R.style.Color_B);
								break;
								case 2:
									changeTheme(R.style.Color_C);
									break;
									case 3:
							           changeTheme(R.style.Color_D);
										break;
										case 4:
											changeTheme(R.style.Color_E);
											break;
											case 5:
												changeTheme(R.style.Color_F);
												break;
												case 6:
													changeTheme(R.style.Color_G);
													break;
													case 7:
														changeTheme(R.style.Color_H);
														break;
														case 8:
															changeTheme(R.style.Color_I);
															break;
															case 9:
																changeTheme(R.style.Color_J);
																break;
																case 10:
																	changeTheme(R.style.Color_K);
																	break;
																	case 11:
																		changeTheme(R.style.Color_L);
																		break;
		
					}
					Toast.makeText(YActivity.this,"已更换主题",Toast.LENGTH_SHORT).show();
			
				}

				@Override
				public void onItemLongClick(View view, int position)
				{
					Toast.makeText(YActivity.this,"未确认功能",Toast.LENGTH_SHORT).show();
				}
				
			
		});
		
		drawerlayouy.addDrawerListener(new DrawerLayout.DrawerListener(){

				@Override
				public void onDrawerSlide(View p1, float p2)
				{
					// TODO: Implement this method
			
				}

				@Override
				public void onDrawerOpened(View p1)
				{
					// TODO: Implement this method
					readdat();
				}

				@Override
				public void onDrawerClosed(View p1)
				{
					// TODO: Implement this method
					colorpick.setVisibility(View.INVISIBLE);
					colorlist.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onDrawerStateChanged(int p1)
				{
					// TODO: Implement this method
					
				}
			});

		initTool();
	
      
		ColorPickerView.OnColorPickerChangeListener l=new ColorPickerView.OnColorPickerChangeListener(){

			@Override
			public void onColorChanged(ColorPickerView picker, int color)
			{
				// TODO: Implement this method
				
			relayout.setBackgroundColor(color);
	        
			}

			@Override
			public void onStartTrackingTouch(ColorPickerView picker)
			{
				// TODO: Implement this method
			}

			@Override
			public void onStopTrackingTouch(ColorPickerView picker)
			{
				// TODO: Implement this method
				
			}

		};
		colorpick.setOnColorPickerChangeListener (l);
		
		
	}
	
	 
	 
	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
	{
		

		switch (p3)
{
			case 0:
				startActivity(new Intent(YActivity.this,SearchActivity.class));
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
           
				break;
			case 1:
				uptheme();
				
				break;
			case 2:
				//colorpick.setVisibility(View.VISIBLE);
				colorlist.setVisibility(View.VISIBLE);
				break;
				
			case 3:
				startActivity(new Intent(YActivity.this,AboutActivity.class));
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
	
				break;
			
			case 4:
				long seondTime=System.currentTimeMillis();
				if(seondTime-firstTime>2000){
					Toast.makeText(YActivity.this,"再按一次退出应用",Toast.LENGTH_SHORT).show();
					firstTime=seondTime;
				}
				else{
					moveTaskToBack(false);
				}
			
				break;
		}
		
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
	{
		// TODO: Implement this method
		switch(p3){
			case 2:
				writeColor();
			  colorpick.setVisibility(View.INVISIBLE);
				Toast.makeText(YActivity.this,"已保存设置",Toast.LENGTH_SHORT).show();
				break;
		}
		return true;
	}

  

	
	
	
	@Override
    public void onClick(View v) {
        switch (v.getId()){

          case R.id.mybluetooth:

               startActivity(new Intent(YActivity.this,BluetoothActivity.class));
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
				
				//ActivityOptions.makeSceneTransitionAnimation(YActivity.this,blue,"bluecard").toBundle());
	
                break;

            case R.id.mynotification:

                startActivity(new Intent(YActivity.this,NotificationActivity.class));
			// startActivity(new Intent(YActivity.this,SettingsActivity.class));
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_right);
				//ActivityOptions.makeSceneTransitionAnimation(this,noti,"noticard").toBundle());
				
                break;
				case R.id.finger:
					startActivity(new Intent(YActivity.this,HeartActivity.class));
					overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_right);
					//ActivityOptions.makeSceneTransitionAnimation(this,finger,"s").toBundle());
					break;
			  default:
			  break;
				

        }
    }
	
	private void showDialog(){
		dialogCreat=new DialogCreat(YActivity.this);
		dialogCreat.setTitle("是否更换背景图片?");
		dialogCreat.setYesOnclickListener(new DialogCreat.onYesOnclickListener(){

				@Override
				public void onYesClick()
				{
					Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent,IMAGE_REQUEST_CODE);
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
	
	private void changeTheme(int theme){
		SharedPreferences themePreference=getSharedPreferences("Theme",MODE_PRIVATE);
		SharedPreferences.Editor Themeeditor = themePreference.edit();
		Themeeditor.putInt("ThemeColor",theme);
		Themeeditor.commit();
		reload();
		}
	private void initTool(){
		
		Tools=new ArrayList<Tool>();
		    Tool tool1=new Tool();
			tool1.settoolName("数据");
			tool1.settoolIcon(R.drawable.data);
			Tools.add(tool1);
			Tool tool2 = new Tool();
			tool2.settoolName("夜间");
			tool2.settoolIcon(R.drawable.nighttheme);
			Tools.add(tool2);
			Tool tool3=new Tool();
			tool3.settoolName("换肤");
			tool3.settoolIcon(R.drawable.skin);
			Tools.add(tool3);
			Tool tool4=new Tool();
			tool4.settoolName("关于");
			tool4.settoolIcon(R.drawable.about);
			Tools.add(tool4);
			Tool tool5=new Tool();
			tool5.settoolName("退出");
			tool5.settoolIcon(R.drawable.exit);
			Tools.add(tool5);
			
	    myAdapter  = new SideListAdapter(Tools,YActivity.this);
		sidelistview.setAdapter(myAdapter);	
	}
	private void addcolor(){
		color=new ArrayList<Colors>();
		Colors color_a=new Colors();
		color_a.setColorIcon(R.drawable.color_a);
		color.add(color_a);
		Colors color_b=new Colors();
		color_b.setColorIcon(R.drawable.color_b);
		color.add(color_b);
Colors color_c=new Colors();
		color_c.setColorIcon(R.drawable.color_c);
		color.add(color_c);
Colors color_d=new Colors();
		color_d.setColorIcon(R.drawable.color_d);
		color.add(color_d);
Colors color_e=new Colors();
		color_e.setColorIcon(R.drawable.color_e);
		color.add(color_e);
Colors color_f=new Colors();
		color_f.setColorIcon(R.drawable.color_f);
		color.add(color_f);
Colors color_g=new Colors();
		color_g.setColorIcon(R.drawable.color_g);
		color.add(color_g);
Colors color_h=new Colors();
		color_h.setColorIcon(R.drawable.color_h);
		color.add(color_h);
Colors color_i=new Colors();
		color_i.setColorIcon(R.drawable.color_i);
		color.add(color_i);
Colors color_j=new Colors();
		color_j.setColorIcon(R.drawable.color_j);
		color.add(color_j);
Colors color_k=new Colors();
		color_k.setColorIcon(R.drawable.color_k);
		color.add(color_k);
Colors color_l=new Colors();
		color_l.setColorIcon(R.drawable.color_l);
		color.add(color_l);

		colorAdapter=new ColorAdapter(color);
		colorlist.setAdapter(colorAdapter);
	}
  
	public void uptheme(){
		SharedPreferences preferences = getSharedPreferences("Theme",MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		int a=preferences.getInt("ThemeColor",0);
        blFlag = preferences.getBoolean("ThemeFlag",true);
		if (blFlag) {
			this.setTheme(R.style.NightTheme); 
			startActivity(new Intent(YActivity.this, WhiteActivity.class));
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			blFlag =false;
			editor.putBoolean("ThemeFlag",false);
		
		} else {
			
		  this.setTheme(a);
			startActivity(new Intent(YActivity.this, NightActivity.class));
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			blFlag = true;
			editor.putBoolean("ThemeFlag",true);
		}
		editor.commit();
	}
	
	private void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

	
	private void readdat(){
	
	SharedPreferences sp = getSharedPreferences("color",MODE_PRIVATE);
	int a=sp.getInt("key",0);
	if(a==0)
	{
		relayout.setBackgroundColor(Color.parseColor("#ffffff"));
	}
	else{
	relayout.setBackgroundColor(a);
	}
	}
	
	private void readHead(){
		SharedPreferences sharedhead = getSharedPreferences("Bitmap",Context.MODE_PRIVATE);
		String bt = sharedhead.getString("bitmap", "");
		/*if(bt!=""){
			byte[] decode=Base64.decode(bt.getBytes(),1);
			Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
			head_image.setImageBitmap(bitmap);
		}
		else{
			head_image.setImageResource(R.drawable.head);
		}*/
		byte[] decode=Base64.decode(bt.getBytes(),1);
		Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
		head_image.setImageBitmap(bitmap);
	}
    private void writeColor(){
		sp=getSharedPreferences("color",MODE_PRIVATE);
		spe=sp.edit();
		spe.putInt("key",colorpick.getColor());
		spe.apply();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
	    if(resultCode!=RESULT_OK){
			return;
			
		}
		else{
			switch(requestCode){
				case IMAGE_REQUEST_CODE:
					resizeImage(data.getData());
					break;
					case RESIZE_REQUEST_CODE:
						if(data!=null){
							showImage(data);
						}
						break;
			}
		}
				
		super.onActivityResult(requestCode, resultCode, data);
		
	}

	private void showImage(Intent data)
	{
		Bundle extras=data.getExtras();
		if(extras!=null){
			Bitmap photo=extras.getParcelable("data");
			head_image.setImageBitmap(photo);
			SharedPreferences sharedPreferences = getSharedPreferences("Bitmap",MODE_PRIVATE);
			SharedPreferences.Editor edit = sharedPreferences.edit();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG,50,bos);
			String Base64 = new String(android.util.Base64.encodeToString(bos.toByteArray(), android.util.Base64.DEFAULT));
			edit.putString("bitmap",Base64);
			edit.commit();
			
			
			
		}
	}

	private void resizeImage(Uri uri)
	{
		// TODO: Implement this method
		Intent intent=new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri,"image/*");
		intent.putExtra("crop","true");
		intent.putExtra("aspectX",37);
		intent.putExtra("aspectY",25);
		intent.putExtra("outputX",592);
		intent.putExtra("outputY",400);
		intent.putExtra("return-data",true);
		startActivityForResult(intent,RESIZE_REQUEST_CODE);
		
	}
	
	
	

	
	
	
	@Override
	protected void onResume()
{
		truetheme();
		readdat();
		readHead();
		super.onResume();
	}
	
	
	@Override
	protected void onStop()
	{
		recreate();
		super.onStop();
	}
}
