package com.tbf;

import java.util.List;

/**
 * 
 * @author aime nishimwe
 * @Date 04.03.2020
 * General representation of an asset (super class)
 *
 */
public abstract class Asset {
	protected String assetCode;
	protected String label;
	protected String symbol;

	public Asset(String assetCode, String label, String Symbol) {
		this.assetCode = assetCode;
		this.label = label;
		this.symbol = Symbol;
	}
	
	public String getAssetCode() {
		return assetCode;
	}

	public String getLabel() {
		return label;
	}

	public String getSymbol() {
		return symbol;
	}
	
	public double rateOfReturn() {
		return (this.annualReturn()/this.getTotalValue())*100;
	}
	
	/**
	 * 
	 * @param code
	 * @param assets
	 * @return an asset that corresponds to the given code
	 */
	public static Asset assetOfCode(String code, List<Asset> assets) {
		Asset asset  = null;
		
		for (Asset a : assets) {
			if(a.getAssetCode().equals(code)) {
				return a;
			}
		}
		return asset;
	}
	
	public abstract double annualReturn();
	public abstract double getTotalValue();
	public abstract double riskValue();
	
	/**
	 * 
	 * @param numericalValue : 
	 * 		number of shares for stock account
	 * 		stake of ownership of investment for private investment account
	 * 		total value/balance of on the account for deposit account 
	 * 
	 * sets the numerical value of (assetCode:"numericalValue") to the appropriate asset
	 */
	public abstract void setNumericalValue(double numericalValue); 		
}
