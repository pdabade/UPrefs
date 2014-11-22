package com.ibm.db;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

@IBMDataObjectSpecialization("User")
public class User extends IBMDataObject{
	
	private static final String ENTERPRISE = "ENTERPRISE";
	//private int id;
	private static final String NAME = "NAME";
	private static final String PASSWORD = "PASSWORD";
	private static final String ROLE = "ROLE";
	private static final String EMAIL = "EMAIL";
	
	public String getEnterprise() {
		return (String) getObject(ENTERPRISE);
	}
	public void setEnterprise(String enterprise) {
		setObject(ENTERPRISE, (enterprise != null) ? enterprise : "");
	}
	public String getName() {
		return (String) getObject(NAME);
	}
	public void setName(String name) {
		setObject(NAME, (name != null) ? name : "");
	}
	public String getPassword() {
		return (String) getObject(PASSWORD);
	}
	public void setPassword(String password) {
		setObject(PASSWORD, (password != null) ? password : "");
	}
	public String getRole() {
		return (String) getObject(ROLE);
	}
	public void setRole(String role) {
		setObject(ROLE, (role != null) ? role : "");
	}
	public String getEmail() {
		return (String) getObject(EMAIL);
	}
	public void setEmail(String email) {
		setObject(EMAIL, (email != null) ? email : "");
	}
}
