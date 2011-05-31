/**
 * 
 */
package Client;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Andrea
 *
 */
public interface ConnectionManager {

	//TODO: modifica dei parametri
	/**
	 * Invia il messaggio di creazione utente al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String creaUtente(String username, String password);
	
	/**
	 * Invia il messaggio di login al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String login(String username, String password);
	
	/**
	 * Invia il messaggio di creazione razza al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String creaRazza(String name, String type);
	
	/**
	 * Invia il messaggio di accesso partita al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String accessoPartita();
	
	/**
	 * Invia il messaggio di uscita partita al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String uscitaPartita();
	
	/**
	 * Invia il messaggio di lista giocatori al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] listaGiocatori();
	
	/**
	 * Invia il messaggio di classifica al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public ArrayList<String> classifica();
	
	/**
	 * Invia il messaggio di logout al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String logout();
	
	/**
	 * Invia il messaggio di mappa generale al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public ArrayList<String> mappaGenerale();
	
	/**
	 * Invia il messaggio di lista dinosauri al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] listaDinosauri();
	
	/**
	 * Invia il messaggio di vista locale al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public ArrayList<String> vistaLocale(String dinoId);
	
	/**
	 * Invia il messaggio di stato dinosauro al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] statoDinosauro(String dinoId);
	
	/**
	 * Invia il messaggio di muovi dinosauro al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] muoviDinosauro(String dinoId, String row, String col);
	
	/**
	 * Invia il messaggio di cresci dinosauro al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] cresciDinosauro(String dinoId);
	
	/**
	 * Invia il messaggio di deponi uovo al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] deponiUovo(String dinoId);
	
	/**
	 * Invia il messaggio di conferma turno al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] confermaTurno();
	
	/**
	 * Invia il messaggio di passa turno al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] passaTurno();
}
