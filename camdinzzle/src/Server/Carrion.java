package Server;

import java.io.Serializable;

/**
 * @author Forme
 * created 			28/04/2011
 * last modified	28/04/2011 
 */

public class Carrion implements Food, Serializable
{
	private static final long serialVersionUID = 1L;
	private int power;
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
	public void rebirth() {
		power -= maxPower/10;
	}

	public int getPower() {
		return power;
	}

	/**
	 * @param power
	 */
	public void setPower(int power) {
		this.power = power;
	}

	public int getMaxPower() {
		return maxPower;
	}

	/**
	 * @param maxPower
	 */
	public void setMaxPower(int maxPower) {
		this.maxPower = maxPower;
	}
	public String toString()
	{
		return "c";
	}
}
