package com.ibm.db;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

@IBMDataObjectSpecialization("Preferences")
public class Office_Locations extends IBMDataObject {
	
	private static final String CAMPUSNAME = "CAMPUSNAME";
	private static final String STATE = "STATE";
	private static final String CITY = "CITY";
	private static final String ADDRESS1 = "ADDRESS1";
	private static final String ADDRESS2 = "ADDRESS2";
	private static final String COUNTRY = "COUNTRY";
	private static final String ZIPCODE = "ZIPCODE";
	private static final String LATITUDE = "LATITUDE";
	private static final String LONGITUDE = "LONGITUDE";
	private static final String TIMEZONE = "TIMEZONE";
	private static final String ICUTIMEZONE = "ICUTIMEZONE";
	
	public String getCampusname() {
		return CAMPUSNAME;
	}
	public void setCampusname(String campusname) {
		setObject(CAMPUSNAME, (campusname != null) ? campusname : "");
	}
	public String getState() {
		return STATE;
	}
	public void setState(String state) {
		setObject(STATE, (state != null) ? state : "");
	}
	public String getCity() {
		return CITY;
	}
	public void setCity(String city) {
		setObject(CITY, (city != null) ? city : "");
	}
	public String getAddress1() {
		return ADDRESS1;
	}
	public void setAddress1(String address1) {
		setObject(ADDRESS1, (address1 != null) ? address1 : "");
	}
	public String getAddress12() {
		return ADDRESS2;
	}
	public void setAddress2(String address2) {
		setObject(ADDRESS2, (address2 != null) ? address2 : "");
	}
	public String getCountry() {
		return COUNTRY;
	}
	public void setCountry(String country) {
		setObject(COUNTRY, (country != null) ? country : "");
	}
	public String getZipcode() {
		return ZIPCODE;
	}
	public void setZipcode(String zip) {
		setObject(ZIPCODE, (zip != null) ? zip : "");
	}
	public String getLatitude() {
		return LATITUDE;
	}
	public void setLatitude(String lat) {
		setObject(LATITUDE, (lat != null) ? lat : "");
	}
	public String getLongitude() {
		return LONGITUDE;
	}
	public void setLongitude(String lon) {
		setObject(LONGITUDE, (lon != null) ? lon : "");
	}
	public String getTimezone() {
		return TIMEZONE;
	}
	public void setTimezone(String tz) {
		setObject(TIMEZONE, (tz != null) ? tz : "");
	}
	public String getIcutimezone() {
		return ICUTIMEZONE;
	}
	public void setIcutimezone(String icutimezone) {
		setObject(ICUTIMEZONE, (icutimezone != null) ? icutimezone : "");
	}	

}
