package com.smartwatch.ywatch;
import android.app.*;
import android.content.*;
import android.support.annotation.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.graphics.*;

public class DialogCreat extends Dialog{
	
	private TextView dialog_title;
	private ImageButton dialog_cancel,dialog_confirm; 
	private String titleTv;
	private onNoOnclickListener noOnclickListener;
    private onYesOnclickListener yesOnclickListener;
	public DialogCreat(@NonNull Context context){
		super(context);
	}
	public DialogCreat(@NonNull Context context,int themeId){
		super(context,themeId);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog);
		
		WindowManager m=getWindow().getWindowManager();
		Display d=m.getDefaultDisplay();
		WindowManager.LayoutParams p=getWindow().getAttributes();
		Point size=new Point();
		d.getSize(size);
		p.width=(int)(size.x *0.8);
		getWindow().setAttributes(p);
		
		initview();
		initEvent();
		initData();
	}
	private void initview(){
		dialog_title=(TextView)findViewById(R.id.dialog_title);
		dialog_cancel=(ImageButton)findViewById(R.id.dialog_cencel);
		dialog_confirm=(ImageButton)findViewById(R.id.dialog_confirm);
	}
	private void initEvent(){
		dialog_cancel.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (noOnclickListener != null) {  
						noOnclickListener.onNoClick();}
				}	
		});
		dialog_confirm.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (yesOnclickListener != null) {  
						yesOnclickListener.onYesClick();  
						}
				}
	
		});
	}
	private void initData(){
		if(titleTv!=null){
			dialog_title.setText(titleTv);
		}
	}
	public void setTitle(String title) {  
        titleTv = title;  
    }  
	public void setNoOnclickListener(onNoOnclickListener onNoOnclickListener) {  
        this.noOnclickListener = onNoOnclickListener;  
    }  
	public void setYesOnclickListener(onYesOnclickListener onYesOnclickListener) {  
    
        this.yesOnclickListener = onYesOnclickListener;  
    }  
	public interface onYesOnclickListener {  
        public void onYesClick();  
    }  

    public interface onNoOnclickListener {  
        public void onNoClick();  
		}
}

	
