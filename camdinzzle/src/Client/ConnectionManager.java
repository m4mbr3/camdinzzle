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
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String creaUtente(String msg);
	
	/**
	 * Invia il messaggio di login al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String login(String msg);
	
	/**
	 * Invia il messaggio di creazione razza al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String creaRazza(String msg);
	
	/**
	 * Invia il messaggio di accesso partita al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String accessoPartita(String msg);
	
	/**
	 * Invia il messaggio di uscita partita al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String uscitaPartita(String msg);
	
	/**
	 * Invia il messaggio di lista giocatori al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String listaGiocatori(String msg);
	
	/**
	 * Invia il messaggio di classifica al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String classifica(String msg);
	
	/**
	 * Invia il messaggio di logout al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String logout(String msg);
	
	/**
	 * Invia il messaggio di mappa generale al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String mappaGenerale(String msg);
	
	/**
	 * Invia il messaggio di lista dinosauri al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String listaDinosauri(String msg);
	
	/**
	 * Invia il messaggio di vista locale al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String vistaLocale(String msg);
	
	/**
	 * Invia il messaggio di stato dinosauro al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String statoDinosauro(String msg);
	
	/**
	 * Invia il messaggio di muovi dinosauro al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String muoviDinosauro(String msg);
	
	/**
	 * Invia il messaggio di cresci dinosauro al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String cresciDinosauro(String msg);
	
	/**
	 * Invia il messaggio di deponi uovo al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String deponiUovo(String msg);
	
	/**
	 * Invia il messaggio di conferma turno al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String confermaTurno(String msg);
	
	/**
	 * Invia il messaggio di passa turno al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String passaTurno(String msg);
}
