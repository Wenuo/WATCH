package com.smartwatch.ywatch;
import android.database.sqlite.*;
import android.content.*;

public class HeartSqliteOpenHelper extends SQLiteOpenHelper
{
	private static final String DB_NAME = "HEART.db";//数据库名称
    private static final int DB_VERSION = 1;  //数据库的版本
	private static final String HEART="create table heart(id integer primary key autoincrement, heartdata text,heartnum integer)";    //数据库表格
	private static HeartSqliteOpenHelper instance=null;
    public  static HeartSqliteOpenHelper getInstance(Context context){

        if(instance==null){

            instance=new HeartSqliteOpenHelper(context);
        }

        return instance;
    }
	
	
	
	private HeartSqliteOpenHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }


	@Override
	public void onCreate(SQLiteDatabase sqlitedatabase)
	{
		sqlitedatabase.execSQL(HEART);
		// TODO: Implement this method

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqlitedatabase, int oldversion, int newversion)
	{
		// TODO: Implement this method
	}

}
