package com.tbf;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Aime Nishimwe
 * @Date 04.03.2020
 * Broker class models a a broker object in the system 
 * 
 *
 */
public abstract class Broker extends Person {
	protected String secCode;
	private String rank;
		
	public Broker(String personalCode, String rank, String secCode, String lastName, String firstName, Address address,
			ArrayList<String> emails) {
		super(personalCode, lastName,firstName, address,emails);

		this.secCode = secCode;
		this.rank = rank;
	}
	
	public String getSecCode() {
		return secCode;
	}
	
	/**
	 * 
	 * @return E for expert broker, and J for junior broker
	 * rank can be used as a descriminator among different types of brokers
	 */
	public String getRank() {
		return this.rank;
	}
	
	public abstract double getCommission(List<Asset> portAssets);
	public abstract double getAnnualFee(List<Asset> portAssets);
}
