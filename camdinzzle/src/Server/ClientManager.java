package Server;

public interface ClientManager 
{	
	/**
	 * Scrive sul socket il messaggio del cambio turno
	 * @param msg : messaggio da mandare al Client
	 * @return true se il messaggio � stato inviato, false altrimenti
	 */
	public boolean sendChangeRound(String msg);
	
	public boolean getIsInGame();
	
	public void setIsInGame(boolean isInGame);
}
