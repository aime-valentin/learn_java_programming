package com.tbf;

/**
 * 
 * @author anishimwe2
 * @date 04.03.2020
 * DepositAccount models all deposit assets in the system
 */

public class DepositAccount extends Asset {
	private double apr; 
	private double balance;
	
	public DepositAccount(double apr, String assetCode, String label) {
		super (assetCode, label, "D");
		this.apr = apr/100.0;
		this.balance = 0.0;
	}
	
	//copy constructor 
	DepositAccount(DepositAccount da){
		super (da.assetCode,da.label,"D");
		apr = da.apr;
		balance = da.balance;
	}

	public double getApr() {
		return this.apr;
	}
	
	public void setNumericalValue(double balance) {
		this.balance = balance;
	}

	public double getTotalValue() {
		return balance;
	}
	
	/**
	 * @return annual percentage yield <br>
	 * for more info visit <a href= "https://www.investopedia.com/terms/a/apy.asp"> this link</a>
	 */
	public double annualReturn() {

		return (Math.exp(this.apr)-1)*this.getTotalValue();
	}
	
	public double riskValue() {
		return 0.0; //risk for deposit accounts is 0.0
	}
	

}

