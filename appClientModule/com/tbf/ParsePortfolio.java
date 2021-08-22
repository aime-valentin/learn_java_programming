package com.tbf;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
/**
 * 
 * @author Aime Nishimwe
 * @Date 04.03.2020
 *
 */
public class ParsePortfolio extends Parser<Portfolio> {
	static Scanner sc = null;
	static List<Portfolio> portfolios = new ArrayList<Portfolio>();
	
	/**
	 * @param f representing portfolios in a flat file
	 * @return a list of portofolios
	 */
	public static List<Portfolio> parse(File f, List<Asset> assets, List<Person> persons){
		int totalRecord = 0;
		
		try {
			sc = new Scanner(f);
			totalRecord = Integer.parseInt(sc.nextLine()); //skips the first line
			while (sc.hasNextLine()) {
				String record = sc.nextLine();
				
				//Validation to skip empty records
				if (record.equals("")) {
					continue;
				}
				else {
					String[] recordArr = new String[5];
					recordArr = record.split(";");
					List<Asset> assetList = new ArrayList<>();
					String portfolioCode;
					Person owner;
					Person manager;
					Person beneficiary; 
					
					//Full record of a portfolio
					if (recordArr.length == 5) {
						
						portfolioCode = recordArr[0];
						owner = Person.personOfCode(recordArr[1],persons);
						manager = Person.personOfCode(recordArr[2],persons);
						beneficiary = Person.personOfCode(recordArr[3],persons);
						
						for(String asset: recordArr[4].split(",")) {
							ArrayList<String> assetToken = new ArrayList<String>();
							assetToken.add(asset.split(":")[0]); //assetCode
							assetToken.add(asset.split(":")[1]); //assetValue (total amount,number of shares, or stake depending on the asset)
							
							Double numValue = Double.parseDouble(assetToken.get(1));
							Asset updatedAsset = ParsePortfolio.getUpdatedAsset(assetToken.get(0), numValue, assets);
							assetList.add(updatedAsset);
							
							}
						Portfolio portfolio = new Portfolio(portfolioCode,owner,manager,beneficiary,assetList);
						ParsePortfolio.portfolios.add(portfolio);
					}
					
					//Portfolio with missing beneficiaryCode
					else if (recordArr.length == 4 && recordArr[3].indexOf(":")!= -1) {
						portfolioCode = recordArr[0];
						owner = Person.personOfCode(recordArr[1],persons);
						manager = Person.personOfCode(recordArr[2],persons);
						beneficiary = null; //explicitly set it to null value
						
						for(String asset: recordArr[4].split(",")) {
							ArrayList<String> assetToken = new ArrayList<String>();
							assetToken.add(asset.split(":")[0]); //assetCode
							assetToken.add(asset.split(":")[1]); //assetValue (total amount,number of shares, or stake depending on the asset)
							
							Double numValue = Double.parseDouble(assetToken.get(1));
							Asset updatedAsset = ParsePortfolio.getUpdatedAsset(assetToken.get(0), numValue, assets);
							assetList.add(updatedAsset);	
						}
						
						Portfolio portfolio = new Portfolio(portfolioCode,owner,manager,beneficiary,assetList);
						ParsePortfolio.portfolios.add(portfolio);
					}
					
					//Portfolio with beneficiaryCode and no asset
					else if (recordArr.length == 4 && recordArr[3].indexOf(":")== -1) {
						portfolioCode = recordArr[0];
						owner = Person.personOfCode(recordArr[1],persons);
						manager = Person.personOfCode(recordArr[2],persons);
						beneficiary = Person.personOfCode(recordArr[3],persons);
						assetList= null;
						//explicitly set to to null value
						//assetList is empty. assetList.size() == 0
						
						Portfolio portfolio = new Portfolio(portfolioCode,owner,manager,beneficiary,assetList);
						ParsePortfolio.portfolios.add(portfolio);
					}
					
					
					//Portfolio without beneficiaryCode and no assets
					else {
						portfolioCode = recordArr[0];
						owner = Person.personOfCode(recordArr[1],persons);
						manager = Person.personOfCode(recordArr[2],persons);
						beneficiary = null;
						assetList = null; //explicitly set to to null value
										 //assetList is empty. assetList.size() == 0
						
						Portfolio portfolio = new Portfolio(portfolioCode,owner,manager,beneficiary,assetList);
						ParsePortfolio.portfolios.add(portfolio);
					}
				}	
			}		
		}
		catch(FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		sc.close();	
		return ParsePortfolio.portfolios;	
	}
	
	/**
	 * Makes a connection to database, prepares and run a queyr to get data on portfolios
	 * @param assets in the database
	 * @param persons in the datase 
	 * @return a list of portfolios in the databse
	 */
	public static List<Portfolio> parse(List<Asset> assets, List<Person> persons){
		
		String portQuery  = "select port.portfolioId, port.portfolioCode,(select p.personCode from Person p where p.personId = port.owner) owner,\n" + 
				"(select p.personCode from Broker b join Person p on p.personid = b.personId where b.brokerId = port.manager) manager,\n" + 
				"(select p.personCode from Person p where p.personId = port.beneficiary)beneficiary\n" + 
				"from Portfolio port;";
	
		Connection conn;
		PreparedStatement prepared; 
		ResultSet portRS;
		try {
			
			conn = Parser.getConnection();		
			prepared = conn.prepareStatement(portQuery);
			portRS = prepared.executeQuery();
			while (portRS.next()){
				String portfolioId = Integer.toString(portRS.getInt("portfolioId"));
				
				String portfolioCode = portRS.getString("portfolioCode");
				Person owner = Person.personOfCode(portRS.getString("owner"),persons);
				Person manager = Person.personOfCode(portRS.getString("manager"),persons);
				Person beneficiary = Person.personOfCode(portRS.getString("beneficiary"),persons);
				List<Asset> portAssets = ParsePortfolio.getPortAssets(portfolioId, assets);
				
				Portfolio portfolio = new Portfolio(portfolioCode,owner,manager,beneficiary,portAssets);
				ParsePortfolio.portfolios.add(portfolio);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
		Parser.closeResources(conn, prepared, portRS);
		return ParsePortfolio.portfolios;
	}
	
	/**
	 * Gets connected to the database to retrieve assets of a particular portfolio
	 * @param portfolioId 
	 * @param assets which is a list of all the assets in the database
	 * @return list of assets within a portfolio with @param portofolioId
	 */
	
	public static List<Asset> getPortAssets(String portfolioId, List<Asset> assets){
		List<Asset> portAssets = new ArrayList<> ();
		String assetQuery = "select a.assetCode, pa.numValue from Portfolio port\n" + 
				"join PortfolioAsset pa on pa.portfolioId = port.portfolioId\n" + 
				"join Asset a on a.assetId = pa.assetId where port.portfolioId = ?;";
		
		Connection conn;
		PreparedStatement prepared;
		ResultSet assetRS;
		
		try {
			conn = Parser.getConnection();		
			prepared = conn.prepareStatement(assetQuery);
			prepared.setString(1, portfolioId);
			assetRS = prepared.executeQuery();
			while (assetRS.next()){
				String assetCode = assetRS.getString("assetCode");
				Double numValue = assetRS.getDouble("numValue");
				Asset copyOfAsset = ParsePortfolio.getUpdatedAsset(assetCode, numValue, assets);
				portAssets.add(copyOfAsset);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		Parser.closeResources(conn, prepared, assetRS);
		return portAssets;
	}
	
	/**
	 * Each asset has a differnt value on a portfolio
	 * 		number of shares for stocks 
	 * 		stake for private assets 
	 * 		total deposit for deposit assets
	 * getUpdatedAsset takes an asset from the database that is found on a portfolio 
	 * then, it updates it with a new value found in portfolio table
	 * @param assetCode
	 * @param numValue
	 * @param assets
	 * @return list of assets object that are updated
	 */
	
	private static Asset getUpdatedAsset (String assetCode, double numValue,List<Asset> assets) {
		Asset asset;
		
		asset = Asset.assetOfCode(assetCode, assets);
		
		if (asset instanceof DepositAccount ) {
			Asset copyOfAsset = new DepositAccount((DepositAccount)asset);
			copyOfAsset.setNumericalValue(numValue);
			return copyOfAsset;
		}else if(asset instanceof StockAccount) {
			Asset copyOfAsset = new StockAccount((StockAccount)asset);
			copyOfAsset.setNumericalValue(numValue);
			return copyOfAsset;
		}else if  (asset instanceof PrivateAccount){ 
			Asset copyOfAsset = new PrivateAccount((PrivateAccount)asset);
			copyOfAsset.setNumericalValue(numValue);
			return copyOfAsset;
		}else {
			return null;
		}
	}
	
	
}
