package com.ibm.uprefs;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import bolts.Continuation;
import bolts.Task;

import com.ibm.db.Office_Locations;
import com.ibm.db.Preferences;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;

public class SetPrefsActivity extends Activity {

	private static final String CLASS_NAME = "SetPreferencesActivity";
	String new_tz = null;
	String new_location = null;
	Button save_button;
	UPrefsApplication uprefApplication;
	String[] offices = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_prefs);
		uprefApplication = (UPrefsApplication) getApplication();
		save_button = (Button) findViewById(R.id.save_button);
		//save_button.setEnabled(false);
		String[] TZ = TimeZone.getAvailableIDs();
		ArrayList<String> timezonelist = new ArrayList<String>();
		ArrayList<String> locationlist = new ArrayList<String>();
		locationlist.add("LONDON");
		locationlist.add("HYDERABAD");
		locationlist.add("BANGALORE");
		locationlist.add("NEW YORK");

		for(int i = 0; i < TZ.length; i++) 
		{
			if(!(timezonelist.contains(TimeZone.getTimeZone(TZ[i]).getDisplayName()))) {
				timezonelist.add(TimeZone.getTimeZone(TZ[i]).getDisplayName());
			}
		}		

		ArrayAdapter<String> tz_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,timezonelist);
		final Spinner tz_spinner = (Spinner) findViewById(R.id.timezone_spinner);	
		tz_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tz_spinner.setAdapter(tz_adapter);
		
		ArrayAdapter<String> location_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,locationlist);
		final Spinner location_spinner = (Spinner) findViewById(R.id.location_spinner);	
		location_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		location_spinner.setAdapter(location_adapter);
		
		save_button.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				
				new_tz = (String) tz_spinner.getSelectedItem();
				new_location =  (String) location_spinner.getSelectedItem();
				if(uprefApplication.getPreferences()==null)
				{
					List<Preferences> preflist = new ArrayList<Preferences>();
					Preferences pref1 = new Preferences();
					pref1.setUser(uprefApplication.getUser().getEmail());
					pref1.setActive("Yes");
					pref1.setPreference("Timezone");
					pref1.setValue(new_tz);

					preflist.add(pref1);

					final Preferences pref2 = new Preferences();
					pref2.setUser(uprefApplication.getUser().getEmail());
					pref2.setActive("Yes");
					pref2.setPreference("Communication");
					pref2.setValue("E-mail");

					preflist.add(pref2);
					
					final Preferences pref3 = new Preferences();
					pref3.setUser(uprefApplication.getUser().getEmail());
					pref3.setActive("Yes");
					pref3.setPreference("Location");
					pref3.setValue(new_location);

					preflist.add(pref3);
					uprefApplication.prefs = preflist;

					pref1.save().continueWith(new Continuation<IBMDataObject, Void>() {

						@Override
						public Void then(Task<IBMDataObject> task) throws Exception {
							// Log if the save was cancelled.
		                    if (task.isCancelled()){
		                        Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
		                    }
							 // Log error message, if the save task fails.
							else if (task.isFaulted()) {
								Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
							}
							 // If the result succeeds
							else {
								pref2.save().continueWith(new Continuation<IBMDataObject, Void>() {

									@Override
									public Void then(Task<IBMDataObject> task) throws Exception {
										// Log if the save was cancelled.
					                    if (task.isCancelled()){
					                        Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
					                    }
										 // Log error message, if the save task fails.
										else if (task.isFaulted()) {
											Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
										}
										 // If the result succeeds
										else {
											pref3.save().continueWith(new Continuation<IBMDataObject, Void>() {

												@Override
												public Void then(Task<IBMDataObject> task) throws Exception {
													// Log if the save was cancelled.
								                    if (task.isCancelled()){
								                        Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
								                    }
													 // Log error message, if the save task fails.
													else if (task.isFaulted()) {
														Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
													}
													 // If the result succeeds
													else {
														Intent intent = new Intent(SetPrefsActivity.this, PreferencesActivity.class);
														startActivity(intent);
													}
													return null;
												}

											});	
										}
										return null;
									}

								});	
							}
							return null;
						}

					});	
					
				}
				else
				{
					System.out.println("Changing set prefs");
					int loc_i = 0;
					for(int i=0;i<3;i++)
					{
						if(uprefApplication.prefs.get(i).getPreference().equalsIgnoreCase("Location"))
							loc_i = i;
					}
					final int loc_k = loc_i;
					uprefApplication.prefs.get(loc_k).setValue(new_location);
					uprefApplication.prefs.get(loc_k).save().continueWith(new Continuation<IBMDataObject, Void>() {

						@Override
						public Void then(Task<IBMDataObject> task) throws Exception {
							// Log if the save was cancelled.							
		                    if (task.isCancelled()){
		                        Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
		                    }
							 // Log error message, if the save task fails.
							else if (task.isFaulted()) {
								Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
							}
							 // If the result succeeds..
							else {
								try {
									IBMQuery<Office_Locations> query = IBMQuery.queryForClass(Office_Locations.class);
									System.out.println(uprefApplication.prefs.get(loc_k).getValue());
									query.whereKeyEqualsTo("CITY", uprefApplication.prefs.get(loc_k).getValue());									
									query.find().continueWith(new Continuation<List<Office_Locations>, Void>() {

										@Override
										public Void then(Task<List<Office_Locations>> task) throws Exception {
											final List<Office_Locations> objects = task.getResult();
											// Log if the find was cancelled.
											if (task.isCancelled()){
												Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
											}
											// Log error message, if the find task fails.
											else if (task.isFaulted()) {
												Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
											}							
											// If the result succeeds..
											else {
												String office = "";
												if(objects.size()==0)
												{				
													offices = new String[1];
													offices[0] = "No IBM Offices here..";
												}
												else
												{
													System.out.println("Hello problem: "+objects.size());
													uprefApplication.office_locations = objects;
													System.out.println(objects.get(0).getCity());
													offices = new String[objects.size()];
													for(int i=0;i<objects.size();i++)
													{
														offices[i] = uprefApplication.getOffice_Locations().get(i).getCampusname()+" "+uprefApplication.office_locations.get(i).getAddress1()+" "+uprefApplication.office_locations.get(i).getAddress2()+" "+uprefApplication.office_locations.get(i).getCity()+" "+uprefApplication.office_locations.get(i).getCountry();
														office = office.concat(offices[i]+"<br>");
													}
												}

												String toMail =  uprefApplication.getUser().getEmail();
												String toName = uprefApplication.getUser().getName();
												String body = "<strong>Hello "+toName+", </strong>"+"<br>Your new "+uprefApplication.prefs.get(loc_k).getPreference()+" preference: "+uprefApplication.prefs.get(loc_k).getValue()+
														". <br>Offices in this area: <br>"+office;
												SendMailAsync sendmail = new SendMailAsync();
												sendmail.execute(toMail,toName,body);
												Intent intent = new Intent(SetPrefsActivity.this, LogoutActivity.class);
												startActivity(intent);
											}
											return null;
										}
									},Task.UI_THREAD_EXECUTOR);
								} catch (IBMDataException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							return null;
						}

					});	
				}		
				
			}

		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_prefs, menu);
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
