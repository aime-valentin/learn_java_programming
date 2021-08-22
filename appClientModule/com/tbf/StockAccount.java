package com.tbf;
/**
 * 
 * @author Aime Nishimwe
 * @Date 02.27.2020
 *
 */

public class StockAccount extends Asset {
	private String stockSymbol;
	private double sharePrice;
	private double beta;
	private double baseQuartelyDividend;
	private double baseRateOfReturn;
	
	//ADDED
	private double numShares;
	
	public StockAccount(String stockSymbol, double sharePrice, double beta, double baseQuartelyDividend,
			double baseRateOfReturn, String assetCode, String label) {
		
		super (assetCode,label, "S");
	
		this.stockSymbol = stockSymbol;
		this.sharePrice = sharePrice;
		this.beta = beta;
		this.baseQuartelyDividend = baseQuartelyDividend;
		this.baseRateOfReturn = baseRateOfReturn;
		this.numShares = 0.0;
	}
	
	//copy constructor
	StockAccount(StockAccount sa){
		super(sa.assetCode,sa.label,"S");
		stockSymbol = sa.stockSymbol;
		sharePrice = sa.sharePrice;
		beta = sa.beta;
		baseQuartelyDividend = sa.baseQuartelyDividend;
		baseRateOfReturn = sa.baseRateOfReturn;
		numShares = sa.numShares;
	}

	public String getStockSymbol() {
		return this.stockSymbol;
	}

	public double getSharePrice() {
		return this.sharePrice;
	}

	public double getBeta() {
		return this.beta;
	}

	public double getBaseQuartelyDividend() {
		return this.baseQuartelyDividend;
	}

	public double getBaseRateOfReturn() {
		return this.baseRateOfReturn/100.0;
	}
	
	//ADDED
	public void setNumericalValue(double numShares) {
		this.numShares = numShares;
	}
	
	
	public double getTotalValue() {
		
		return this.numShares*this.sharePrice;
	}
	
	public double annualReturn() {
		return (this.getBaseRateOfReturn()*this.getTotalValue())+(this.getBaseQuartelyDividend()*4*this.numShares); //Removed *this.numShares
	}
	
	public double riskValue() {
		return this.getBeta();
	}
	
	public double getRateOfReturn() {
		return this.annualReturn()/this.getTotalValue();
	}
	
	
}
