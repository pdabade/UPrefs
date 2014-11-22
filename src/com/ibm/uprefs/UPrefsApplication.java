package com.ibm.uprefs;

import java.util.List;
import java.util.Properties;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import com.ibm.db.Office_Locations;
import com.ibm.db.Preferences;
import com.ibm.db.User;
import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;

public final class UPrefsApplication extends Application {
	
	private static final String APP_ID = "applicationID";
	private static final String APP_SECRET = "applicationSecret";
	private static final String APP_ROUTE = "applicationRoute";
	private static final String PROPS_FILE = "uprefs.properties";
	public static final int EDIT_ACTIVITY_RC = 1;
	private static final String CLASS_NAME = UPrefsApplication.class.getSimpleName();
	
	User user = null;
	List<Preferences> prefs= null;
	
	public UPrefsApplication()
	{
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks()
		{

			@Override
			public void onActivityCreated(Activity activity,
					Bundle savedInstanceState) {
				Log.d(CLASS_NAME,"Activity created: " + activity.getLocalClassName());
				
			}

			@Override
			public void onActivityStarted(Activity activity) {
				Log.d(CLASS_NAME,"Activity started: " + activity.getLocalClassName());
				
			}

			@Override
			public void onActivityResumed(Activity activity) {
				Log.d(CLASS_NAME,"Activity resumed: " + activity.getLocalClassName());
				
			}

			@Override
			public void onActivityPaused(Activity activity) {
				Log.d(CLASS_NAME,"Activity paused: " + activity.getLocalClassName());
				
			}

			@Override
			public void onActivityStopped(Activity activity) {
				Log.d(CLASS_NAME,"Activity stopped: " + activity.getLocalClassName());
				
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity,
					Bundle outState) {
				Log.d(CLASS_NAME,"Activity saved instance state: " + activity.getLocalClassName());
				
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				Log.d(CLASS_NAME,"Activity destroyed: " + activity.getLocalClassName());				
			}
			
		});
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		// Read from properties file.
		Properties props = new java.util.Properties();
		Context context = getApplicationContext();
		try {
			AssetManager assetManager = context.getAssets();
			props.load(assetManager.open(PROPS_FILE));
			Log.i(CLASS_NAME, "Found configuration file: " + PROPS_FILE);
		} catch (Exception e) {
			Log.e(CLASS_NAME,"The uprefs.properties file could not be read properly.", e);
		}
		// Initialize the IBM core backend-as-a-service.
		IBMBluemix.initialize(this, props.getProperty(APP_ID), props.getProperty(APP_SECRET), props.getProperty(APP_ROUTE));
		// Initialize the IBM Data Service.
		IBMData.initializeService();
		// Register the User Specialization.
		User.registerSpecialization(User.class);
		Preferences.registerSpecialization(Preferences.class);
		Office_Locations.registerSpecialization(Office_Locations.class);
	}
	public User getUser() {
		return user;
	}
	public List<Preferences> getPreferences() {
		return prefs;
	}

}
