package Server;

import java.io.Serializable;

/**
 * Classe che rappresenta un utente all'interno del gioco.
 */

public class Player implements Serializable
{
	private static final long serialVersionUID = 1L;
	/*
	 * Password del giocatore.
	 */
	private String password;
	/*
	 * Username del giocatore.
	 */
	private String userName;
	/*
	 * Riferimento alla specie del giocatore.
	 */
	private Species specie;
	/*
	 * Token del giocatore.
	 */
	private String token;

	/**
	 * Istanzia un Playre con username e password.
	 * @param userName username del Player
	 * @param password pasword del Player
	 */
	public Player(String userName, String password) 
	{
		this.userName = userName;
		this.password = password;
		this.specie =  null;
	}

	/**
	 * Ritorna la password del Player
	 * @return password del Player
	 */
	public String getPassword() 
	{
		return password;
	}

	/**
	 * Assegna la password al Player
	 * @param password password da assegnare al Player
	 */
	public void setPassword(String password) 
	{
		this.password = password;
	}

	/**
	 * Ritorna l'username del Player.
	 * @return username del Player
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * Assegna l'username al Player.
	 * @param userName username del Player
	 */
	public void setUserName(String userName) 
	{
		this.userName = userName;
	}
	
	/**
	 * Assegna la specie al Player.
	 * @param specie la specie del Player
	 */
	public void setSpecie(Species specie)
	{
		this.specie = specie;
	}
	
	/**
	 * Ritorna la specie del Player.
	 * @return la specie del Player
	 */
	public Species getSpecie()
	{
		return this.specie;
	}
	
	/**
	 * Assegna il token al Player.
	 * @param t token del Player
	 */
	public void setToken(String t)
	{
		token = t;
	}
	
	/**
	 * Ritorna il token del Player.
	 * @return il token del player
	 */
	public String getToken()
	{
		return token;
	}

}
