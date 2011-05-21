package Server;
/**
 * @author Forme
 * created 			28/04/2011
 * last modified	28/04/2011 
 */

public class Vegetation implements Food 
{

	private int power;
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
	public void rebirth() {
		power += maxPower/10;
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
		return "v";
	}
}
