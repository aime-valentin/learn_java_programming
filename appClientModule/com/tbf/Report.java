package com.tbf;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * 
 * @author Aime Nishimwe
 * @Date 02.27.2020
 *
 */
public class Report {
	
	private List<Portfolio> portfolios;
	
	public Report (List<Portfolio> portfolios) {
		Comparator<Portfolio> cmp = new Comparator<Portfolio>() {

			@Override
			public int compare(Portfolio p1, Portfolio p2) {
				// TODO Auto-generated method stub
				return p1.getOwner().getName().compareTo(p2.getOwner().getName());
			}
			
		};
		 Collections.sort(portfolios, cmp);
		 this.portfolios = portfolios;
	}

	/**
	 * Writes a summary report of portfolio to a console
	 * Acts on Report.portfolios to write the report
	 */
	public void writeSummaryReport () {
		
		//Grand totals
		double grandTotalFees= 0.0;
		double grandTotalCommissions = 0.0;
		double grandTotalReturns = 0.0;
		double grandTotal = 0.0;
		
		//=========Header==========
		System.out.printf("%-10s %-20s %-20s %-10s %-15s %-15s %-10s %-10s \n","Portfolio","Owner","Manager","Fees($)", "Commissions($)","Weighted Risk","Return($)","Total($)");
		System.out.println("====================================================================================================================");
		//=========Body============
	
		for (Portfolio port : this.portfolios) {
			String portCode = port.getPortfolioCode();
			String summaryReport = String.format("%-10s %-20s %-20s %-10.2f %-15.2f %-15.4f %-10.2f %-10.2f \n", portCode,port.getOwner().getName(),
					port.getManager().getName() ,port.totalFees(port.getManager(), port.getAssetList()),
					port.totalCommissions(port.getManager(), port.getAssetList()),port.weightedRisk(port.getAssetList()),
					port.totalReturn(port.getAssetList()),port.totalValue(port.getAssetList()));
			System.out.print(summaryReport);
			
			grandTotalFees+=port.totalFees(port.getManager(), port.getAssetList());
			grandTotalCommissions+=port.totalCommissions(port.getManager(), port.getAssetList());
			grandTotalReturns+=port.totalReturn(port.getAssetList());
			grandTotal+=port.totalValue(port.getAssetList());
		}
		//=========Footer===========
		System.out.printf("%-50s %-15s \n","","----------------------------------------------------------------------");
		System.out.printf("%-10s %-20s %-20s %-10.2f %-15.2f %-15s %-10.2f %-10.2f \n\n\n"," ", " "," Total",grandTotalFees,grandTotalCommissions," ",
				grandTotalReturns,grandTotal);
	
	}
	
	/**
	 * Writes a full detailed report of portfolio to a console
	 * Acts on Report.portfolios to write the full report
	 */
	public void writeFullReport() {
		
		//=================header==============
		System.out.println("Portfolio Details");
		System.out.println("========================================================================================================================================");
	
		for (Portfolio port : this.portfolios) {
		//==================body================
			String portCode = port.getPortfolioCode();
			System.out.printf("Portfolio \t %s \n",portCode);
			System.out.println("=================================================");
			System.out.println("Owner:");
			System.out.println(port.getOwner().getName());
			
			if (port.getBeneficiary() != null) {
				System.out.println("Beneficary:");
				System.out.println(port.getBeneficiary().getName());
			}
			else {
				System.out.println(""); 
			}
			
			double totalRisk = port.weightedRisk(port.getAssetList());
			double totalAnnualReturn = 0.0;
			double totalValue = 0.0;
			
			System.out.println("Assets");
			System.out.printf("%-20s %-30s %-20s %-20s %-20s %-20s \n","Code","Asset","Return Rate (%)","Risk", "Annual Return ($)","Total Value($)");
			if(port.getAssetList() != null) {
				for (Asset asset : port.getAssetList()) {
				String summaryReport = String.format("%-20s %-30s %-20.2f %-20.2f %-20.2f %-20.2f \n",asset.getAssetCode(), asset.getLabel(),
						asset.rateOfReturn(), asset.riskValue(), asset.annualReturn(), asset.getTotalValue());
				System.out.print(summaryReport);
				
				totalAnnualReturn+=asset.annualReturn();
				totalValue +=asset.getTotalValue();
					
				}
			}else {
				String summaryReport = String.format("%-20s %-30s %-20.2f %-20.2f %-20.2f %-20.2f \n",0.0, 0.0,
						0.0, 0.0, 0.0, 0.0);
				System.out.print(summaryReport);
				
				totalAnnualReturn=0.0;
				totalValue =0.0;
			}
			
			//==============footer ==========
			System.out.printf("%-50s %-15s \n","","--------------------------------------------------------------------------");
			System.out.printf("%-20s %-30s %-20s %-20.4f %-20.2f %-20.2f \n"," ", " "," Total",totalRisk,totalAnnualReturn,totalValue);	
		}
	}
}

