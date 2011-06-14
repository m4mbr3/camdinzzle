package Server;

import java.io.Serializable;

/**
 * Classe che rappresenta una carogna all'interno del gioco.
 */
public class Carrion implements Food, Serializable
{
	private static final long serialVersionUID = 1L;
	/*
	 * Energia attuale della carogna.
	 */
	private int power;
	/*
	 * Energia massima della carogna.
	 */
	private int maxPower;
	
	/**
	 * Istanzia un oggetto Carrion con energia massima come parametro.
	 * @param maxPower compreso fra 350 e 650 (500±30%)
	 */
	public Carrion(int maxPower)
	{
		this.maxPower = maxPower;
		this.power = maxPower;
	}

	/**
	 * Esegue la perdita di energia della carogna.
	 */
	@Override
	public void rebirth() 
	{
		power -= maxPower/10;
	}
	
	/**
	 * Ritorna l'energia della carogna.
	 * @return energia della carogna
	 */
	public int getPower() 
	{
		return power;
	}

	/**
	 * Setta l'energia della carogna.
	 * @param power energia della carogna
	 */
	public void setPower(int power) 
	{
		this.power = power;
	}

	/**
	 * Ritorna l'energia massima della carogna.
	 * @return energia massima della carogna
	 */
	public int getMaxPower() 
	{
		return maxPower;
	}

	/**
	 * Setta l'energia massima della carogna.
	 * @param maxPower energia massima da assegnare alla carogna
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
