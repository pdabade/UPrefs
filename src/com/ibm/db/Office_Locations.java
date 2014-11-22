package com.ibm.db;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

@IBMDataObjectSpecialization("Office_Locations")
public class Office_Locations extends IBMDataObject {
	
	private static final String CAMPUSNAME = "CAMPUSNAME";
	private static final String CITY = "CITY";
	private static final String ADDRESS1 = "ADDRESS1";
	private static final String ADDRESS2 = "ADDRESS2";
	private static final String COUNTRY = "COUNTRY";
	private static final String ZIPCODE = "ZIPCODE";
	private static final String TIMEZONE = "TIMEZONE";
	
	public String getCampusname() {
		return (String) getObject(CAMPUSNAME);
	}
	public void setCampusname(String campusname) {
		setObject(CAMPUSNAME, (campusname != null) ? campusname : "");
	}
	public String getCity() {
		return (String) getObject(CITY);
	}
	public void setCity(String city) {
		setObject(CITY, (city != null) ? city : "");
	}
	public String getAddress1() {
		return (String) getObject(ADDRESS1);
	}
	public void setAddress1(String address1) {
		setObject(ADDRESS1, (address1 != null) ? address1 : "");
	}
	public String getAddress2() {
		return (String) getObject(ADDRESS2);
	}
	public void setAddress2(String address2) {
		setObject(ADDRESS2, (address2 != null) ? address2 : "");
	}
	public String getCountry() {
		return (String) getObject(COUNTRY);
	}
	public void setCountry(String country) {
		setObject(COUNTRY, (country != null) ? country : "");
	}
	public String getZipcode() {
		return (String) getObject(ZIPCODE);
	}
	public void setZipcode(String zip) {
		setObject(ZIPCODE, (zip != null) ? zip : "");
	}
	public String getTimezone() {
		return (String) getObject(TIMEZONE);
	}
	public void setTimezone(String tz) {
		setObject(TIMEZONE, (tz != null) ? tz : "");
	}

}
