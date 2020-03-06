package com.smartwatch.ywatch;
import android.os.*;
import lecho.lib.hellocharts.view.*;
import android.widget.*;
import lecho.lib.hellocharts.listener.*;
import lecho.lib.hellocharts.model.*;
import java.util.*;
import lecho.lib.hellocharts.util.*;
import lecho.lib.hellocharts.gesture.*;
import android.view.*;
import android.app.*;
import android.database.sqlite.*;
import android.content.*;
import android.database.*;
import java.text.*;
import android.graphics.*;
import android.view.View.*;
import android.support.annotation.*;
import android.widget.CompoundButton.*;
import android.util.*;
public class ChartActivity extends BaseActivity
{

    private TextView day_text;
	//private int totalDays = 20;//总共有多少天的数据显示
    private int minY = 30;//Y轴坐标最小值
    private int maxY = 120;//Y轴坐标最大值
    private LineChartData data;
    private Switch ChartSwitch;
   // String[] labelsX = new String[totalDays];//X轴的标注
   //int[] valuesY = new int[totalDays];//图表的数据点

    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private List<AxisValue> mAxisYValues = new ArrayList<AxisValue>();
	
	//每天数据
	private List<String> Data_X=new ArrayList<String>();
	private List<Integer> Data_Point=new ArrayList<Integer>();
	//每周数据
	private List<String> Week_Data_X=new ArrayList<String>();
	private List<Integer> Week_Data_Point=new ArrayList<Integer>();

	
	private static boolean blFlag = false;
	
	
	private static boolean Y_Flag=true;
    private LineChartView lineChart;
	//天/周切换
	private static  boolean TimeFlag = true;
    private static int LineColor=Color.parseColor("#000000");

	private Context context;//设置字体颜色
	
	
	
	

	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);

        lineChart = (LineChartView) findViewById(R.id.chart);
	    ChartSwitch=(Switch)findViewById(R.id.chart_Switch);
		day_text=(TextView)findViewById(R.id.chart_day);
		UI();
	 //   chooseTime();
		readChartSetting();
		truetheme();
		fill_the_white_white();
		oneday(context);
	    getAxisXYLables();//获取x轴的标注
		getAxisPoints();//获取坐标点
	   initDay_LineChart();//初始
	   
    }
    private void UI(){
		ChartSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2)
				{
					// TODO: Implement this method
					SharedPreferences sp=getSharedPreferences("ChartSetting",MODE_PRIVATE);
					SharedPreferences.Editor ed=sp.edit();
					if (p2) {  
						TimeFlag=true;
						Y_Flag=false;
						ed.putBoolean("ChartTime",true);
				
					}
					else{
						Toast.makeText(ChartActivity.this, "已关闭", Toast.LENGTH_SHORT).show();
						TimeFlag=false;
						Y_Flag=false;
						ed.putBoolean("ChartTime",false);
					}
					ed.commit();
				}
		});
	}
    
    
	

    private void initDay_LineChart() {
        Line line = new Line(mPointValues).setColor(LineColor).setStrokeWidth(1);  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点
		line.setPointRadius(3);
		line.setHasGradientToTransparent(false);
        lines.add(line);
         data = new LineChartData();
        data.setLines(lines);

        //坐标轴X
		
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setValues(mAxisXValues);
		axisX.setMaxLabelChars(3);
		axisX.setTextSize(11);
		data.setBaseValue(minY);
        data.setAxisXBottom(axisX);//x 轴在底部

	   
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
		lineChart.setValueSelectionEnabled(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 4);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
	
        lineChart.setVisibility(View.VISIBLE);
	

        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.bottom = minY;
        v.top = maxY;
        //固定Y轴的范围,如果没有这个,Y轴的范围会根据数据的最大值和最小值决定,这不是我想要的
        lineChart.setMaximumViewport(v);
		if(Data_Point!=null&&Data_Point.size()>0){
			v.left = Data_X.size()-7;
	        v.right = Data_X.size()-1;
			lineChart.setCurrentViewport(v);
			lineChart.moveTo(0,Data_Point.get(0));
		}
		else{
			v.left=0;
			v.right=6;
			lineChart.setCurrentViewport(v);
			lineChart.moveTo(0,Week_Data_Point.get(0));
		}
    }

	
	
	private void oneday(Context context){
		SQLiteDatabase db=HeartSqliteOpenHelper.getInstance(context).getReadableDatabase();
		String ss="select*from heart where heartdata>=? and heartdata<=?";
		Date now=new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		Long endTime=cal.getTimeInMillis();
		Calendar calendar = Calendar.getInstance();  
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long startTime = calendar.getTimeInMillis();
	
		SimpleDateFormat day=new SimpleDateFormat("MM-dd");
		String today=day.format(startTime);
		day_text.setText(today);
	    Cursor  cursor = db.rawQuery(ss,new String[] { String.valueOf(startTime), String.valueOf(endTime) });
		Integer b=0;
		if(cursor.getCount()==0){
			Toast.makeText(ChartActivity.this,"meiy",Toast.LENGTH_LONG).show();
		}
		
		else{
			if(cursor.moveToFirst()){
				do{
					Long time=cursor.getLong(cursor.getColumnIndex("heartdata"));
					String heart_num=cursor.getString(cursor.getColumnIndex("heartnum"));
					SimpleDateFormat zh=new SimpleDateFormat("HH:mm");
					String Time=zh.format(time);
					b=Integer.valueOf(heart_num).intValue();
					try{
						Data_Point.add(b);
						Data_X.add(Time);
					}catch(Exception e){
						Toast.makeText(ChartActivity.this,"太刺激",Toast.LENGTH_SHORT).show();
					}
				}while(cursor.moveToNext());			
			}
			
			cursor.close();
			db.close();
		}

	}
	
	

	
	private void getAxisXYLables() {
	   try{
		   
		  if(Data_X!=null&&Data_X.size()>0){
        for (int i = 0; i < Data_X.size(); i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(Data_X.get(i)));
        }
		//Toast.makeText(ChartActivity.this,"第一次",Toast.LENGTH_LONG).show();
		Log.e("X轴","数据库有数据，哈哈哈哈哈");
		}else{
			for(int i=0;i<Week_Data_X.size();i++){
				mAxisXValues.add(new AxisValue(i).setLabel(Week_Data_X.get(i)));
			}
			//Toast.makeText(ChartActivity.this,"第二次",Toast.LENGTH_SHORT).show();
			Log.e("X轴","数据库没有数据，使用自定义0-6");
			}
		
		}catch(Exception e){
			Toast.makeText(ChartActivity.this,"shbdbd",Toast.LENGTH_SHORT).show();
			System.out.println(Week_Data_X);
			System.out.println(mAxisXValues);
		}
        for (int i = minY; i <= maxY; i+=10) {
            mAxisYValues.add(new AxisValue(i).setLabel(i+""));
        }
		Log.e("BBB","Yzhou");
		System.out.println(mAxisYValues);
    }

    private void getAxisPoints() {
		if(Data_Point!=null&&Data_Point.size()>0){
          for (int i = 0; i <Data_Point.size(); i++) {
            mPointValues.add(new PointValue(i, Data_Point.get(i)));
        }
		Log.e("点集","来自数据库");
		}
		else{
		for(int i=0;i<Week_Data_Point.size();i++){
			mPointValues.add(new PointValue(i,Week_Data_Point.get(i)));
		}
	
			System.out.println(mPointValues);
			Log.e("点集","自定义");
	}
    }


	private void readChartSetting(){
		SharedPreferences sp=getSharedPreferences("ChartSetting",MODE_PRIVATE);
		boolean TimeON_OFF=sp.getBoolean("ChartTime",true);
		if(TimeON_OFF){
			ChartSwitch.setChecked(true);
			
		}
		else{
			ChartSwitch.setChecked(false);
		}
	}
	
	
	private void fill_the_white_white(){
		for(int i=0;i<7;i++){
			Week_Data_X.add("hh:ss");
			Week_Data_Point.add(0);
		}
	}
	
	private void truetheme(){
		SharedPreferences preferences = this.getSharedPreferences("Theme", MODE_WORLD_READABLE);
        blFlag = preferences.getBoolean("ThemeFlag",true);
		if(blFlag){
			LineColor=Color.parseColor("#000000");
			}
		else{
		    LineColor=Color.parseColor("#5C3AE3");
		}
	}

}

