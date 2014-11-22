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
import com.ibm.sendgrid.SendGrid;
import com.ibm.sendgrid.SendGrid.Email;

public class SetPrefsActivity extends Activity {

	private static final String CLASS_NAME = "SetPreferencesActivity";
	String new_tz = null;
	Button save_button;
	UPrefsApplication uprefApplication;
	String offices = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_prefs);
		uprefApplication = (UPrefsApplication) getApplication();

		save_button = (Button) findViewById(R.id.save_button);
		//save_button.setEnabled(false);
		String[] TZ = TimeZone.getAvailableIDs();
		ArrayList<String> timezonelist = new ArrayList<String>();

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
		
		save_button.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				
				new_tz = (String) tz_spinner.getSelectedItem();
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
				else
				{
					System.out.println("Changing set prefs");
					int i = 0;
					System.out.println("Pref 0 :"+uprefApplication.prefs.get(0).getPreference());
					if(uprefApplication.prefs.get(0).getPreference().equalsIgnoreCase("Communication"))
					{
						System.out.println("Pref 0 :"+uprefApplication.prefs.get(0).getPreference());
						i=1;
					}
					final int k = i;
					uprefApplication.prefs.get(i).setValue(new_tz);
					uprefApplication.prefs.get(i).save().continueWith(new Continuation<IBMDataObject, Void>() {

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
									query.whereKeyEqualsTo("TIMEZONE", uprefApplication.prefs.get(k).getValue());
									
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
											// If the result succeeds, load the list.
											else {

												if(objects.size()==0)
												{
													offices = "No IBM Offices here.";
												}
												else
												{
													offices = objects.toString();
													//Toast.makeText(getApplicationContext(), "Error. Please contact admin!", Toast.LENGTH_SHORT).show();
												}
											}
											return null;
										}
									},Task.UI_THREAD_EXECUTOR);
								} catch (IBMDataException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								String toEmail = uprefApplication.getUser().getEmail();
								String toName = uprefApplication.getUser().getName();
								String body = "<strong>Hello "+toName+", </strong>"+"<br>Your new "+uprefApplication.prefs.get(k).getPreference()+" preference: "+uprefApplication.prefs.get(k).getValue()+
										". <br>Offices in this area: <br>"+offices;
								sendEmail(toEmail,toName,body);
								Intent intent = new Intent(SetPrefsActivity.this, LogoutActivity.class);
								startActivity(intent);
							}
							return null;
						}

						private void sendEmail(String toEmail, String toName, String body) {
							String sendgrid_username  = "uprefs";
							String sendgrid_password  = "UserPrefs12";

							try
							{
								SendGrid sendgrid = new SendGrid(sendgrid_username, sendgrid_password);
								Email email = new SendGrid.Email();
								email.addTo(toEmail);
								email.addToName(toName);
								email.setFromName("User Prefs App");
								email.setSubject("Preferences Changed!");
								email.setFrom("UPrefs@in.ibm.com");
								email.setHtml(body); 

								SendGrid.Response response = sendgrid.send(email);
						        String msgResponse = response.getMessage();

						        Log.d(CLASS_NAME, msgResponse);
							}catch (Exception e) {
								Log.e(CLASS_NAME, e.toString());
							}
							
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
