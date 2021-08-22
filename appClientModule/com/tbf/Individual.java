package com.tbf;
import java.util.ArrayList;

/**
 * 
 * @author Aime Nishimwe
 * @Date 04.03.2020
 * Individual can be owner or beneficiary
 * Designed to separate owners/beneficiaries from brokers
 */
public class Individual extends Person {

	public Individual(String personalCode, String lastName, String firstName, Address address,
			ArrayList<String> emails) {
		super(personalCode, lastName, firstName, address, emails);
	}
}
