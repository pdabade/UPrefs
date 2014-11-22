package com.ibm.db;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

@IBMDataObjectSpecialization("Preferences")
public class Preferences extends IBMDataObject
{
	private static final String USER = "USER";
	private static final String PREFERENCE = "PREFERENCE";
	private static final String VALUE = "VALUE";
	private static final String ACTIVE = "ACTIVE";
	
	public String getUser() {
		return (String) getObject(USER);
	}
	public void setUser(String user) {
		setObject(USER, (user != null) ? user : "");
	}
	public String getPreference() {
		return (String) getObject(PREFERENCE);
	}
	public void setPreference(String preference) {
		setObject(PREFERENCE, (preference != null) ? preference : "");
	}
	public String getValue() {
		return (String) getObject(VALUE);
	}
	public void setValue(String value) {
		setObject(VALUE, (value != null) ? value : "");
	}
	public String getActive() {
		return (String) getObject(ACTIVE);
	}
	public void setActive(String active) {
		setObject(ACTIVE, (active != null) ? active : "");
	}
	
}
