package com.tbf;
import java.util.List;
/**
 * 
 * @author Aime Nishimwe
 * @Date 04.03.2020
 *
 */
public class Portfolio {
	private String portfolioCode;
	private Person owner;
	private Person manager;
	private Person beneficiary; 
	private List<Asset> assetList;
	
	public Portfolio(String portfolioCode, Person owner, Person manager, Person beneficiary,
			List<Asset> assetList2) {
		super();
		this.portfolioCode = portfolioCode;
		this.owner = owner;
		this.manager = manager;
		this.beneficiary = beneficiary;
		this.assetList = assetList2;
	}

	public String getPortfolioCode() {
		return portfolioCode;
	}

	public Person getOwner() {
		return owner;
	}

	public Person getManager() {
		return manager;
	}

	public Person getBeneficiary() {
		return beneficiary;
	}

	public List<Asset> getAssetList() {
		return assetList;
	}
	
	public double totalValue(List<Asset> assets) {
		double totalValue = 0.0;
		
		if(assets != null) {
			for (Asset asset : assets) {
			totalValue+=asset.getTotalValue();
		}
		}else {
			return 0.0;
		}
		return totalValue;
	}
	
	public double weightedRisk(List<Asset> assets) {
		if (assets == null) {
			return 0.0;
		}
		double totalValue = totalValue(assets);
		double aggregateValue = 0.0;
		for (Asset asset : assets) {
			aggregateValue += (asset.riskValue()*(asset.getTotalValue()/totalValue));
		}
		return aggregateValue;
	}
	
	public double totalReturn(List<Asset> assets) {
		double totalReturn = 0.0;
		
		if (assets != null) {
			for (Asset asset: assets) {
			totalReturn+=asset.annualReturn();
		}
		}else {
			return 0.0;
		}
		
		return totalReturn;
	}
	
	public double totalCommissions(Person person, List<Asset> portAssets) {
		Broker manager = (Broker) person ;
		return manager.getCommission(portAssets);
	}
	
	public double totalFees(Person person, List<Asset> portAssets) {
		Broker manager = (Broker) person ;
		return manager.getAnnualFee(portAssets);
	}

	@Override
	public String toString() {
		return "Portfolio [portfolioCode=" + portfolioCode + ", owner=" + owner + ", manager=" + manager
				+ ", beneficiary=" + beneficiary + ", assetList=" + assetList + "]";
	}
}
