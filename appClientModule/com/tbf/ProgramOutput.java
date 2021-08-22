package com.tbf;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
/**
 * 
 * @author Aime Nishimwe
 * @Date 02.27.2020
 *
 */
public abstract class ProgramOutput <T> {
	/**
	 * 
	 * @param list of objects 
	 * @param path where you want to save the .json file
	 */
	public void toJsonFile(List<T> list, String path) {
		Gson json = new GsonBuilder().setPrettyPrinting().create();
		String outAssetJson = json.toJson(list);
		
		try {
			PrintWriter pwJson = new PrintWriter(path);
			pwJson.write(outAssetJson);
			pwJson.close();	
		}
		catch(FileNotFoundException e) {
			throw new RuntimeException(e);
		}	
	}
	/**
	 * 
	 * @param list objects
	 * @param path where you want to save the .xml file 
	 */
	public void toXMLFile(List<T> list, String path) {
		XStream xstream = new XStream();
		String outIndividualsXml = xstream.toXML(list);
		
		try {	
			PrintWriter pXML = new PrintWriter(path);
			pXML.write(outIndividualsXml);
			pXML.close();
		}
		catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
	}
}
