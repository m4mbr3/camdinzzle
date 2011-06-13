package Server;

/**
 * Interfaccia che rappresenta il tramite tra la logica applicativa del server e la comunicazione con i client.
 */
public interface ClientManager 
{	
	/**
	 * Invia al client il messaggio di cambio del turno.
	 * @param msg messaggio di cambio del turno
	 * @return true se il messaggio e' stato inviato, false altrimenti
	 */
	public boolean sendChangeRound(String msg);
	
	/**
	 * Ritorna true se il client e' attualmente in partita, false altrimenti
	 * @return true se il client e' in partita, false altrimenti
	 */
	public boolean getIsInGame();
	
	/**
	 * Setta il valore della variabile se un utente e' attualmente in partita.
	 * @param isInGame valore da settare
	 */
	public void setIsInGame(boolean isInGame);
}
