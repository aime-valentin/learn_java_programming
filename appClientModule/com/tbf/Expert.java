package com.tbf;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Aime Nishimwe
 * @Date 04.03.2020
 */
public class Expert extends Broker implements PortManagement {
	private static final double RATE_COMMISSION = 0.0375;
	
	public Expert(String personalCode, String rank, String secCode, String lastName, String firstName, Address address,
			ArrayList<String> emails) {
		super(personalCode, rank, secCode, lastName, firstName, address, emails);
		
	}
	
	public double getAnnualFee(List<Asset> portfolio) {
		return 0.0;
	}
	
	/**
	 * @return total commission for each portfolio
	 * @param list of assets in a portfolio
	 */
	public double getCommission(List<Asset> portfolio) {
		double totalCommission = 0.0;
		if (portfolio !=null) {
			for (Asset asset : portfolio) {
			totalCommission+=asset.annualReturn();
		}
		}else {
			return 0.0;
		}
		return totalCommission*RATE_COMMISSION;
	}
}
