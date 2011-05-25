/**
 * 
 */
package Client;

/**
 * @author Andrea
 *
 */
public interface ConnectionManager {

	/**
	 * Invia il messaggio di creazione utente al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String creaUtente(String msg);
	
	/**
	 * Invia il messaggio di login al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String login(String msg);
	
	/**
	 * Invia il messaggio di creazione razza al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String creaRazza(String msg);
	
	/**
	 * Invia il messaggio di accesso partita al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String accessoPartita(String msg);
	
	/**
	 * Invia il messaggio di uscita partita al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String uscitaPartita(String msg);
	
	/**
	 * Invia il messaggio di lista giocatori al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String listaGiocatori(String msg);
	
	/**
	 * Invia il messaggio di classifica al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String classifica(String msg);
	
	/**
	 * Invia il messaggio di logout al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String logout(String msg);
	
	/**
	 * Invia il messaggio di mappa generale al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String mappaGenerale(String msg);
	
	/**
	 * Invia il messaggio di lista dinosauri al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String listaDinosauri(String msg);
	
	/**
	 * Invia il messaggio di vista locale al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String vistaLocale(String msg);
	
	/**
	 * Invia il messaggio di stato dinosauro al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String statoDinosauro(String msg);
	
	/**
	 * Invia il messaggio di muovi dinosauro al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String muoviDinosauro(String msg);
	
	/**
	 * Invia il messaggio di cresci dinosauro al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String cresciDinosauro(String msg);
	
	/**
	 * Invia il messaggio di deponi uovo al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String deponiUovo(String msg);
	
	/**
	 * Invia il messaggio di conferma turno al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String confermaTurno(String msg);
	
	/**
	 * Invia il messaggio di passa turno al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server
	 */
	public String passaTurno(String msg);
}
