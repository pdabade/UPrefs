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
import android.widget.EditText;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;

import com.ibm.db.User;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMQuery;

public class MainActivity extends Activity {

	public static final String CLASS_NAME="MainActivity";
	UPrefsApplication uprefApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		

		uprefApplication = (UPrefsApplication) getApplication();
		
		// User Login button
		Button btnLogin = (Button) findViewById(R.id.login_button);
		Button btnRegister = (Button) findViewById(R.id.register_button);


		// Login button click event
		btnLogin.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) {

				EditText txtusername = (EditText) findViewById(R.id.username);	
				String username = txtusername.getText().toString();

				EditText txtpassword = (EditText) findViewById(R.id.password);
				String password = txtpassword.getText().toString();

				try {
					IBMQuery<User> query = IBMQuery.queryForClass(User.class);
					query.whereKeyEqualsTo("EMAIL", username);
					System.out.println(username);
					query.whereKeyEqualsTo("PASSWORD", password);
					System.out.println(password);
					query.find().continueWith(new Continuation<List<User>, Void>() {

						@Override
						public Void then(Task<List<User>> task) throws Exception {
							final List<User> objects = task.getResult();
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
									Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_SHORT).show();
								}
								else if(objects.size()==1)
								{
									//Toast.makeText(getApplicationContext(), "Welcome...",Toast.LENGTH_SHORT).show();
									uprefApplication.user = objects.get(0);
									System.out.println(objects.get(0).getClass());
									Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
									startActivity(intent);
									finish();
								}
								else
								{
									Toast.makeText(getApplicationContext(), "Error. Please contact admin!", Toast.LENGTH_SHORT).show();
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

		});

		btnRegister.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
				startActivity(intent);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
