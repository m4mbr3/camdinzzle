package Server;

import java.io.Serializable;

/**
 * Classe che rappresenta la vegetazione all'interno del gioco.
 */
public class Vegetation implements Food, Serializable
{
	private static final long serialVersionUID = 1L;
	/*
	 * Energia attuale della vegetazione.
	 */
	private int power;
	/*
	 * Energia massima della vegetazione.
	 */
	private int maxPower;
	
	/**
	 * Istanzia un oggetto vegetazione da aggiungere alla mappa del gioco.
	 * @param maxPower energia massima. Compresa fra 150 e 350 (250±40%)
	 */
	public Vegetation(int maxPower)
	{
		this.maxPower = maxPower;
		this.power = 10;
	}
	
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
	 * Ritorna l'energia massima della vegetazione.
	 * @return energia massima
	 */
	public int getMaxPower() 
	{
		return maxPower;
	}

	/**
	 * Setta l'energia massima della vegetazione.
	 * @param maxPower energia massima
	 */
	public void setMaxPower(int maxPower) {
		this.maxPower = maxPower;
	}
	
	public String toString()
	{
		return "v";
	}
}
