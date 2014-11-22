package com.ibm.uprefs;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import bolts.Continuation;
import bolts.Task;

import com.ibm.db.Preferences;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMQuery;

public class PreferencesActivity extends Activity {

	private static final String CLASS_NAME = "PreferencesActivity";
	String new_tz = null;
	
	UPrefsApplication uprefApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		uprefApplication = (UPrefsApplication) getApplication();

		try {
			IBMQuery<Preferences> query = IBMQuery.queryForClass(Preferences.class);
			query.whereKeyEqualsTo("USER", uprefApplication.getUser().getEmail());
			System.out.println("User email: "+uprefApplication.getUser().getEmail());

			query.find().continueWith(new Continuation<List<Preferences>, Void>() {

				@Override
				public Void then(Task<List<Preferences>> task) throws Exception {
					final List<Preferences> objects = task.getResult();
					// Log if the find was cancelled.
					if (task.isCancelled()){
						Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
					}
					// Log error message, if the find task fails.
					else if (task.isFaulted()) {
						Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
					}							
					// If the result succeeds,
					else {
						if(objects.size()==2)
						{
							System.out.println("Preferences  set");
							System.out.println(objects.get(0).getPreference());
							System.out.println(objects.get(1).getPreference());
							if(objects.get(0).getPreference().equalsIgnoreCase("Timezone"))
							{
								System.out.println("0 is timezone");
								TextView txtTZ = (TextView) findViewById(R.id.txtTimezone);
								txtTZ.setText(objects.get(0).getValue());
								TextView txtCM = (TextView) findViewById(R.id.txtCommunication);
								txtCM.setText(objects.get(1).getValue());
							}
							else
							{
								System.out.println("0 is communication");
								TextView txtTZ = (TextView) findViewById(R.id.txtTimezone);
								txtTZ.setText(objects.get(1).getValue());
								TextView txtCM = (TextView) findViewById(R.id.txtCommunication);
								txtCM.setText(objects.get(0).getValue());
							}
							
							Button change_button = (Button) findViewById(R.id.change_button);
							uprefApplication.prefs = objects;
							change_button.setOnClickListener(new View.OnClickListener(){

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(PreferencesActivity.this, SetPrefsActivity.class);
									startActivity(intent);									
								}
								
							});
							
						}
						else
						{
							System.out.println("Preferences not set");
							Intent intent = new Intent(PreferencesActivity.this, SetPrefsActivity.class);
							startActivity(intent);
							finish();
						}
					}
					return null;
				}

			},Task.UI_THREAD_EXECUTOR);
		} catch (IBMDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preferences, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
