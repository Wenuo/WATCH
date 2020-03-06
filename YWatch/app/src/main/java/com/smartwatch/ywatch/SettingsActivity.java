package com.smartwatch.ywatch;
import android.preference.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class SettingsActivity extends PreferenceActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		MenuInflater inflater = getMenuInflater();  
        inflater.inflate(R.menu.new_menu,menu);
		return true;
	}

/*	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		// TODO: Implement this method
		switch(item.getItemId()){
			case R.id.action_refresh:
				Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT).show();  
				break;  
				// action with ID action_settings was selected  
			case R.id.action_settings:  
				Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();  
				break;
		}
		return true;
		}*/
	



}

