package Server;

import java.io.Serializable;

public class Carrion implements Food, Serializable
{
	private static final long serialVersionUID = 1L;
	/**
	 * energia della carogna
	 */
	private int power;
	/**
	 * energia massima della carogna
	 */
	private int maxPower;
	
	/**
	 * @param maxPower compreso fra 350 e 650 (500±30%)
	 */
	public Carrion(int maxPower)
	{
		this.maxPower = maxPower;
		this.power = maxPower;
	}

	/**
	 * Perdita di energia
	 */
	@Override
	public void rebirth() 
	{
		power -= maxPower/10;
	}
	
	/**
	 * restituisce l'energia della carogna
	 * @return int
	 */
	public int getPower() 
	{
		return power;
	}

	/**
	 * setta l'energia della carogna
	 * @param power
	 */
	public void setPower(int power) 
	{
		this.power = power;
	}

	/**
	 * restituisce l'energia massima della carogna
	 * @return int
	 */
	public int getMaxPower() 
	{
		return maxPower;
	}

	/**
	 * setta l'energia massima della carogna
	 * @param maxPower
	 */
	public void setMaxPower(int maxPower) 
	{
		this.maxPower = maxPower;
	}
	
	public String toString()
	{
		return "c";
	}
}
