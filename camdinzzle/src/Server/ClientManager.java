package Server;

public abstract class ClientManager {
	
	private String token;
	private boolean isInGame;
	
	public ClientManager()
	{
		token ="";
		isInGame = false;
	}
	
	/**
	 * Scrive sul socket il messaggio del cambio turno
	 * @param msg : messaggio da mandare al Client
	 * @return true se il messaggio è stato inviato, false altrimenti
	 */
	public abstract boolean sendChangeRound(String msg);
	/**
	 * @return Token del Player a cui corrisponde il Client
	 */
	public String getToken()
	{
		return token;
	}
	
	public void setToken(String token)
	{
		this.token = token;
	}
	
	/**
	 * @return ture se il Player corrispondente a questo Client è in partita, false altrimenti
	 */
	public boolean getIsInGame()
	{
		return isInGame;
	}
	
	public void setIsInGame(boolean isInGame)
	{
		this.isInGame = isInGame;
	}
}
