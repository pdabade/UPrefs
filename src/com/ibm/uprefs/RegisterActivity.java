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
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;

public class RegisterActivity extends Activity {
	
	public static final String CLASS_NAME="RegisterActivity";
	UPrefsApplication uprefApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		

		uprefApplication = (UPrefsApplication) getApplication();        
		
		Button btnRegister = (Button) findViewById(R.id.register_button);
        
        btnRegister.setOnClickListener(new View.OnClickListener() 
        {

			@Override
			public void onClick(View v) {
				
				EditText txtName = (EditText) findViewById(R.id.textPersonName);				
				final String name = txtName.getText().toString();
				
				EditText txtPassword = (EditText) findViewById(R.id.textPassword);
				final String password = txtPassword.getText().toString();
		        
		        EditText txtemail = (EditText) findViewById(R.id.textEmailAddress);
		        final String email = txtemail.getText().toString();
		        
		        EditText txtenterprise = (EditText) findViewById(R.id.txtEnterprise);
		        final String enterprise = txtenterprise.getText().toString();
		        
		        try {
					IBMQuery<User> query = IBMQuery.queryForClass(User.class);
					query.whereKeyEqualsTo("EMAIL", email);
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
									User user = new User();
						        	user.setName(name);
						        	user.setEmail(email);
						        	user.setPassword(password);
						        	user.setRole("User");
						        	user.setEnterprise(enterprise);
						        	user.save().continueWith(new Continuation<IBMDataObject, Void>() {

										@Override
										public Void then(Task<IBMDataObject> task) throws Exception {
											
											if (task.isCancelled())
											{
						                        Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
						                    }
											 // Log error message, if the save task fails.
											else if (task.isFaulted()) 
											{
												Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
											}
											else
											{
												Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
												startActivity(intent);
												finish();
											}
											return null;
										}
						        		
						        	},Task.UI_THREAD_EXECUTOR);
								}
								else if(objects.size()==1)
								{
									Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_SHORT).show();
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
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
