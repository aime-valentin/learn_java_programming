package com.tbf;
import java.util.List;
/**
 * Portfolio Management Interace
 * @author Aime Nishimwe
 * @Date 02.27.2020
 * this interface is implemented differently depending on the type of broker
 */
public interface PortManagement {
	public double getAnnualFee(List<Asset> portfolio);
	public double getCommission(List<Asset> portfolio);
}
