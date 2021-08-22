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
 * @Date 02.27.2020
 *
 */
public class ParsePerson extends Parser<Person>  {

	 static Scanner sc = null;
	 static List<Person> persons= new ArrayList<Person>();

	/**
	 * 
	 * @param f representing a file object containing persons data in a flat file
	 * @return a list of persons
	 * <ol> Persons can be:
	 * <li> Individual (Owner/Beneficary)</li>
	 * <li>Broker (Expert-E, Junior-J)</></ol>
	 */
	public static List<Person> parse(File f){
		
		int totalRecord = 0;
		
		try {
			sc = new Scanner(f);
			totalRecord = Integer.parseInt(sc.nextLine()); //skips the first line
			while (sc.hasNextLine()) {
				String record = sc.nextLine();
				String[] recordArr = record.split(";");
				
				//validation for empty record
				if (record.equals("")) {
					continue;
				}
				
				else {
						//Broker
					if (!recordArr[1].equals("")) {
						ArrayList<String> emails = new ArrayList<String>();
						
						if (record.indexOf('@')!= -1) {
							for (String email : recordArr[4].split(",")) {
								emails.add(email);
							}
						}
						String personalCode = recordArr[0];
						String secCode = recordArr[1].split(",")[1];
						String rank = recordArr[1].split(",")[0];
						String lastName = recordArr[2].split(",")[0];
						String firstName = recordArr[2].split(",")[1];
						
						//Make address object
						String street = recordArr[3].split(",")[0];
						String city = recordArr[3].split(",")[1];
						String state = recordArr[3].split(",")[2];
						String zip = recordArr[3].split(",")[3];
						String country = recordArr[3].split(",")[4];
						Address address = new Address(street,city,state,zip,country);
						
						//Make expertBroker object
						if (rank.equals("E")) {
							Broker expert = new Expert(personalCode,rank,secCode,lastName,firstName,address,emails);
							ParsePerson.persons.add(expert);
						}
						
						else {
							Broker junior = new Junior(personalCode,rank,secCode,lastName,firstName,address,emails);
							ParsePerson.persons.add(junior);
						}
						
					}
					
					//Individual (Owner/beneficiary)
					else {
						ArrayList<String> emails = new ArrayList<String>();
						
						if (record.indexOf('@')!= -1) {
							for (String email : recordArr[4].split(",")) {
								emails.add(email);
							}
						}
						
						String personalCode = recordArr[0];
						String lastName = recordArr[2].split(",")[0];
						String firstName = recordArr[2].split(",")[1];
						
						//Make address object
						String street = recordArr[3].split(",")[0];
						String city = recordArr[3].split(",")[1];
						String state = recordArr[3].split(",")[2];
						String zip = recordArr[3].split(",")[3];
						String country = recordArr[3].split(",")[4];
						Address address = new Address(street,city,state,zip,country);
						
						Individual notBroker  = new Individual(personalCode,lastName,firstName,address,emails);
						ParsePerson.persons.add(notBroker);
				    }
				}
			}	
		}
		
		catch(FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		sc.close();
		return ParsePerson.persons;
	}
	/**
	 * makes a connection to database, prepares and executre a query to get data for people 
	 * @return
	 */
	public static List<Person> parse(){
	
		String personQuery  = "select p.personId, p.personCode, p.firstName, p.lastName,a.street, a.city, a.state,a.zip, a.country, b.secCode, b.brokerType\n" + 
				"from Person p\n" + 
				"left join Address a on p.personId = a.personId\n" + 
				"left join Broker b on p.personId = b.personId\n" + 
				"order by b.brokerId;";
	
		Connection conn;
		PreparedStatement prepared; 
		ResultSet personRS;
		try {
			
			conn = Parser.getConnection();		
			prepared = conn.prepareStatement(personQuery);
			personRS = prepared.executeQuery();
			while (personRS.next()){
				
				String secCode = personRS.getString("secCode");
				String personCode = personRS.getString("personCode");
				String brokerType = personRS.getString("brokerType");
				String firstName = personRS.getString("firstName");
				String lastName = personRS.getString("lastName");
				String street = personRS.getString("street");
				String city = personRS.getString("city");
				String state = personRS.getString("state");
				String zip = personRS.getString("zip");
				String country = personRS.getString("country");
				Address address = new Address(street,city,state,zip,country);
				ArrayList<String> emails = ParsePerson.getEmails(personRS.getString("personId"));
				
				if (secCode == null) { //individuals
					Person individual  = new Individual(personCode,lastName,firstName,address,emails);
					ParsePerson.persons.add(individual);
				} else if (brokerType.equals("E")) {
					Broker expert = new Expert(personCode,brokerType,secCode,lastName,firstName,address,emails);
					ParsePerson.persons.add(expert);
				} else if (brokerType.equals("J")){
					Broker junior = new Junior(personCode,brokerType,secCode,lastName,firstName,address,emails);
					ParsePerson.persons.add(junior);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
		Parser.closeResources(conn, prepared, personRS);
		return ParsePerson.persons;
	}
	/**
	 * Helper method to get emails for a particular person from database
	 * @param personId
	 * @return
	 */
	public static ArrayList<String> getEmails (String personId){
		ArrayList<String> emails = new ArrayList<String> ();
		String emailQuery = "select emailAdress from Email em where em.personId = ?";
		
		Connection conn;
		PreparedStatement prepared;
		ResultSet emailRS;
		
		try {
			conn = Parser.getConnection();		
			prepared = conn.prepareStatement(emailQuery);
			prepared.setString(1, personId);
			emailRS = prepared.executeQuery();
			while (emailRS.next()){
				String email = emailRS.getString("emailAdress");
				emails.add(email);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		Parser.closeResources(conn, prepared, emailRS);
		return emails;
	}
	

}
