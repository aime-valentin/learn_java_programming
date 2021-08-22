package com.tbf;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author anishimwe2
 * @Date 04.03.2020
 * The Person class models person objects in the systme including 
 * 		managers (expert and junior brokers), owners, and beneficiaries.
 */

public abstract class Person {
	
    protected String personalCode;
    protected String lastName;
    protected String firstName;
    protected Address address;
    protected ArrayList<String> emails;
	
	public Person(String personalCode, String lastName, String firstName, Address address, ArrayList<String> emails) {
	
		this.personalCode = personalCode;
		this.lastName = lastName;
		this.firstName = firstName;
		this.address = address;
		
		this.emails = new ArrayList<String>();
		for (String email : emails) {
			this.emails.add(email);
		}
	}

	public String getPersonalCode() {
		return this.personalCode;
	}
	
	public String getName() {
		return this.lastName+", "+this.firstName;
	}

	public Address getAddress() {
		return this.address;
	}

	public ArrayList<String> getEmails() {
		return this.emails;
	}
	
	public static Person personOfCode(String code, List<Person> persons) {
		Person person = null;
		
		for(Person p : persons) {
			if (p.getPersonalCode().equals(code)) {
				person  = p;
			}
		}
		return person;
	}

	@Override
	public String toString() {
		return this.getName() + "\n" + this.getEmails() +"\n" + this.getAddress()+"\n";
	}	
}
	
