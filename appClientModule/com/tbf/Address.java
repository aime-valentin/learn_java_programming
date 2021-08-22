package com.tbf;

/**
 * 
 * @author anishimwe2
 * @Date 04.03.2020
 *	The address class models  a person's address
 *	From this class, a user can get a persons' street, city, state, zip, and country.
 */
public class Address {
	
	private String street;
	private String city;
	private String state;
	private String zip;
	private String country;
	
	public Address(String street, String city, String state, String zip, String country) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}

	public String getStreet() {
		return this.street;
	}

	public String getCity() {
		return this.city;
	}

	public String getState() {
		return this.state;
	}

	public String getZip() {
		return this.zip;
	}

	public String getCountry() {
		return this.country;
	}

	@Override
	public String toString() {
		return this.street + "\n" + this.city + "," + this.state + "," + this.country + this.zip  ;
	}
	
	
	
	
}
