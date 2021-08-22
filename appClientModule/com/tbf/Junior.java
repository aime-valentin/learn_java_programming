package com.tbf;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Aime Nishimwe
 * @Date 04.03.2020
 * Junior Class implments Portfolio Management Interface to define how Junior brokers manage portfolios
 */
public class Junior extends Broker implements PortManagement {
	private static final double ANNUAL_FEE = 75; //in ($)
	private static final double RATE_COMMISSION = 0.0125;
	
	public Junior(String personalCode, String rank, String secCode, String lastName, String firstName, Address address,
			ArrayList<String> emails) {
		super(personalCode, rank, secCode, lastName, firstName, address, emails);
	}
	
	/**
	 * @param list of assets in a portfolio
	 * @return fees associated with the assets and managed by a junior broker
	 */
	public double getAnnualFee(List<Asset> portfolio) {
		double fees = 0.0;
		if (portfolio != null) {
			for (Asset asset : portfolio) {
			fees+=ANNUAL_FEE;
		}
		}else {
			return 0.0;
		}
		return fees;
	}
	
	/**
	 * @return total commissions on the all assets in a portfolio
	 * @param a list of assets in a portfolio
	 * For junior broker, there is a 1.25% commission on the total annual return of portfolio
	 */
	public double getCommission(List<Asset> portfolio) {
		double totalCommission = 0.0;
		if (portfolio != null) {
			for (Asset asset : portfolio) {
			totalCommission+=asset.annualReturn();
		}
		}else {
			return 0.0;
		}
		return totalCommission*RATE_COMMISSION;
	}
}
