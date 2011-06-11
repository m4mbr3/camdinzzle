package Server;

import java.io.Serializable;

/**
 * @author Forme
 * created 			28/04/2011
 * last modified	28/04/2011 
 */

public class Vegetation implements Food, Serializable
{
	private static final long serialVersionUID = 1L;
	/**
	 * energia della vegetazione
	 */
	private int power;
	/**
	 * energia massima della vegetazione
	 */
	private int maxPower;
	
	/**
	 * @param maxPower compreso fra 150 e 350 (250±40%)
	 */
	public Vegetation(int maxPower)
	{
		this.maxPower = maxPower;
		this.power = 10;
	}

	/**
	 * Ricrescita della vegetazione
	 */
	@Override
	public void rebirth() 
	{
		power += maxPower/10;
	}

	/**
	 * restituisce l'energia della vegetazione
	 * @return int
	 */
	public int getPower() 
	{
		return power;
	}

	/**
	 * setta l'energia della vegetzzione
	 * @param power
	 */
	public void setPower(int power) 
	{
		this.power = power;
	}

	/**
	 * ritorna l'energia massima
	 * @return
	 */
	public int getMaxPower() 
	{
		return maxPower;
	}

	/**
	 * setta l'energia massima
	 * @param maxPower
	 */
	public void setMaxPower(int maxPower) {
		this.maxPower = maxPower;
	}
	public String toString()
	{
		return "v";
	}
}
