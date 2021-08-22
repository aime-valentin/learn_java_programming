package com.tbf;

/**
 * 
 * @author anishimwe2
 * @date 04.03.2020
 * 
 */

public class PrivateAccount extends Asset {

		private double totalAmount;
		private double omega;
		private double baseQuartelyDividend;
		private double baseRateOfReturn;
		
		//ADDED 
		private double stake;
		public PrivateAccount(double totalAmount, double omega, double baseQuartelyDividend, double baseRateOfReturn,
				String assetCode, String label) {
			
			super (assetCode, label, "P");
			
			this.totalAmount = totalAmount;
			this.omega = omega;
			this.baseQuartelyDividend = baseQuartelyDividend;
			this.baseRateOfReturn = baseRateOfReturn/100.0;
			this.stake = 0.0;
		}
		
		//copy constructor
		PrivateAccount (PrivateAccount pa){
			super(pa.assetCode,pa.label, "P");
			totalAmount = pa.totalAmount;
			omega = pa.omega;
			baseQuartelyDividend = pa.baseQuartelyDividend;
			baseRateOfReturn = pa.baseRateOfReturn;
			stake = pa.stake; 
		}
		public double getTotalValue() {
			return this.totalAmount*this.stake;
		}
		public double getOmega() {
			return this.omega;
		}
		public double getBaseQuartelyDividend() {
			return this.baseQuartelyDividend;
		}
		public double getBaseRateOfReturn() {
			return this.baseRateOfReturn;
		}
		
		public double getStake() {
			return stake;
		}
		public void setNumericalValue(double stake) {
			this.stake = stake/100.0;
		}
	
		public double annualReturn() {
			//return (this.getBaseRateOfReturn()*this.getTotalValue())+(this.getBaseQuartelyDividend()*4);
			return (this.baseRateOfReturn*(this.totalAmount)+(this.baseQuartelyDividend*4))*this.stake;
		}
		
		public double riskValue() {
			return (this.getOmega() + (Math.exp((-125500/this.totalAmount))));
		}
	
}
