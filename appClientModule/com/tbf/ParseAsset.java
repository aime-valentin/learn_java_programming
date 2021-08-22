package com.tbf;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * 
 * @author Aime Nishimwe
 * @Date 04.03.2020
 *
 */
public class ParseAsset extends Parser<Asset> {
	private static Scanner sc = null;
	private static List<Asset> assets = new ArrayList<Asset>();
	
	/**
	 * 
	 * @param f representing a file object containing assets in a flat file
	 * @return a list of assets from the flat file 
	 * <ol>type of assets 
	 * <li>Private Investment</li>
	 * <li>Deposit Account</li>
	 * <li>Stock Account</li>
	 * </ol>
	 */
	public static List<Asset> parse(File f) {
		int totalRecord = 0;
		try {
			sc = new Scanner(f);
			totalRecord = Integer.parseInt(sc.nextLine()); //skips the first line and stores number of records
			while (sc.hasNextLine()) {
				String record = sc.nextLine();
				String[] recordArr = record.split(";");
				
				//Validation for empty records
				if (record.equals("")) {
					continue;
				}
				else {
						if (recordArr[1].equals("S")) {
						double sharePrice = Double.parseDouble(recordArr[7]);
						double beta = Double.parseDouble(recordArr[5]);
						double baseQuartelyDividend = Double.parseDouble(recordArr[3]);
						double baseRateOfReturn = Double.parseDouble(recordArr[4]);
						
						Asset stAccount = new StockAccount(recordArr[6],sharePrice, beta,
								baseQuartelyDividend,baseRateOfReturn,recordArr[0],recordArr[2] );
						ParseAsset.assets.add(stAccount);
					}
					else if (recordArr[1].equals("P")) {
						double totalAmount = Double.parseDouble(recordArr[6]);
						double omega = Double.parseDouble(recordArr[5]);
						double baseQuartelyDividend = Double.parseDouble(recordArr[3]);
						double baseRateOfReturn = Double.parseDouble(recordArr[4]);
						
						Asset priAccount = new PrivateAccount(totalAmount,omega,baseQuartelyDividend,
								baseRateOfReturn,recordArr[0], recordArr[2]);
						ParseAsset.assets.add(priAccount);
					}
					else {
						double apr = Double.parseDouble(recordArr[3]);
						Asset depAccount = new DepositAccount(apr,recordArr[0],recordArr[2]);
						ParseAsset.assets.add(depAccount);
					}
				}
			}		
		}
		catch(FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		sc.close();
		return ParseAsset.assets;
	}
	
	/**
	 * parse() method calls Parser.getConnection() to make a connection to the database 
	 * Prepares statements to execute queries that resut in asset data stored in database
	 * Each type of asset is retrieved indepdently of the others
	 * 		For ex: Private, stock, and deposit assets are retrieved using three different statements
	 * Makes asset objects
	 * 
	 * @return a list of asset objects
	 */
	
	public static List<Asset> parse(){
	
	//Deposits Assets
	String depQuery = "select * from Asset a\n" + 
			"join DepositAccount da on da.assetId = a.assetId";
	
	Connection conn;
	PreparedStatement preparedDep; 
	ResultSet depositRS;
	try {
		
		conn = Parser.getConnection();		
		preparedDep = conn.prepareStatement(depQuery);
		depositRS = preparedDep.executeQuery();
		while (depositRS.next()){
			double apr = depositRS.getDouble("apr");
			String assetCode = depositRS.getString("assetCode");
			String assetLabel = depositRS.getString("assetLabel");
			
			Asset depAccount = new DepositAccount(apr,assetCode, assetLabel);
			ParseAsset.assets.add(depAccount);			
		}
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}
	Parser.closeResources(preparedDep, depositRS);

		
	//Stocks Assets
	String stkQuery = "select * from Asset a\n" + 
			"join StockAccount sa  on sa.assetId = a.assetId";
	PreparedStatement preparedStk;
	ResultSet stkRS;
	try {
		preparedStk = conn.prepareStatement(stkQuery);
		stkRS = preparedStk.executeQuery();
		while (stkRS.next()){
			double sharePrice = stkRS.getDouble("sharePrice");
			double beta = stkRS.getDouble("betaMeasure");
			double baseQuartelyDividend = stkRS.getDouble("quartelyDividend");
			double baseRateOfReturn = stkRS.getDouble("baseRateOfReturn");
			String assetCode = stkRS.getString("assetCode");
			String assetLabel = stkRS.getString("assetLabel");
			String stockSymbol = stkRS.getString("stockSymbol"); 
			
			Asset stAccount = new StockAccount(stockSymbol,sharePrice, beta,
					baseQuartelyDividend,baseRateOfReturn,assetCode,assetLabel );
			ParseAsset.assets.add(stAccount);
			
		}
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}
	Parser.closeResources(preparedStk, stkRS);
	
	//Private Assets
	String pvtQuery = "select * from Asset a\n" + 
			"join PrivateAccount pa  on pa.assetId = a.assetId";
	
	PreparedStatement preparedPvt;
	ResultSet pvtRS;
	try {
		preparedPvt = conn.prepareStatement(pvtQuery);
		pvtRS = preparedPvt.executeQuery();
		while (pvtRS.next()){
			double omega= pvtRS.getDouble("baseOmegaMeasure");
			double baseQuartelyDividend = pvtRS.getDouble("quartelyDividend");
			double baseRateOfReturn = pvtRS.getDouble("baseRateOfReturn");
			String assetCode = pvtRS.getString("assetCode");
			String assetLabel = pvtRS.getString("assetLabel");
			double totalAmount = pvtRS.getDouble("totalValue"); 
			
			Asset priAccount = new PrivateAccount(totalAmount,omega,baseQuartelyDividend,
					baseRateOfReturn,assetCode, assetLabel);
			ParseAsset.assets.add(priAccount);
		}
			
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}
	
	Parser.closeResources(preparedPvt, pvtRS);
	return ParseAsset.assets;	
	}
}
