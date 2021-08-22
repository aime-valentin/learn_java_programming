package com.tbf;
import java.util.List;

/**
 * 
 * @author Aime Nishimwe
 * @Date 04.03.2020
 *
 */
public class PortfolioReport {
	/**
	 * Reads data person, asset, and portfololio data from database (anishimw)
	 * Creates parsers for assets, persons, and portfolios
	 * Parses assets, persons, and portfolios
	 * builds the report using the lists of assets, persons, and perfolios
	 * writes summary and full detailed reports
	 * @param args
	 */
	
	//private static Logger logger = LogManager.getLogger(PortfolioReport.class);
	
	public static void main(String[] args) {
		List<Asset> assets = ParseAsset.parse();
		List<Person> persons = ParsePerson.parse();
		List<Portfolio> portfolios = ParsePortfolio.parse(assets, persons);

		Report report = new Report(portfolios);
		report.writeSummaryReport();
		report.writeFullReport();	
		
	
	}
}
