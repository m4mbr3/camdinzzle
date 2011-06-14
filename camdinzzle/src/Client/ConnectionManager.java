package Client;

import java.util.ArrayList;

public interface ConnectionManager {
	/**
	 * Invia il messaggio di creazione utente al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String creaUtente(String username, String password) ;
	/**
	 * Invia il messaggio di login al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String login(String username, String password) ;
	
	/**
	 * Invia il messaggio di creazione razza al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String creaRazza(String name, String type) ;
	
	/**
	 * Invia il messaggio di accesso partita al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String accessoPartita() ;
	
	/**
	 * Invia il messaggio di uscita partita al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String uscitaPartita() ;
	
	/**
	 * Invia il messaggio di lista giocatori al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] listaGiocatori() ;
	
	/**
	 * Invia il messaggio di classifica al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public ArrayList<String> classifica() ;
	
	/**
	 * Invia il messaggio di logout al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String logout() ;
	
	/**
	 * Invia il messaggio di mappa generale al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public ArrayList<String> mappaGenerale() ;
	
	/**
	 * Invia il messaggio di lista dinosauri al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] listaDinosauri() ;
	
	/**
	 * Invia il messaggio di vista locale al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public ArrayList<String> vistaLocale(String dinoId) ;
	
	/**
	 * Invia il messaggio di stato dinosauro al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] statoDinosauro(String dinoId) ;
	
	/**
	 * Invia il messaggio di muovi dinosauro al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] muoviDinosauro(String dinoId, String row, String col) ;
	
	/**
	 * Invia il messaggio di cresci dinosauro al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] cresciDinosauro(String dinoId) ;
	
	/**
	 * Invia il messaggio di deponi uovo al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] deponiUovo(String dinoId) ;
	
	/**
	 * Invia il messaggio di conferma turno al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] confermaTurno() ;
	
	/**
	 * Invia il messaggio di passa turno al Server e attende la risposta
	 * @param msg : messaggio da inviare al Server
	 * @return Risposta del Server, se va in errore ritorna null
	 */
	public String[] passaTurno() ;
	
	/**
	 * Torna lo stato del turno
	 * @return il valore della variabile changeRound
	 */
	public String getChangeRound();
	
	/**
	 * Setta lo stato del turno
	 * @param msg :nuovo valore della variabile changeRound
	 */
	public void setChangeRound(String msg);
	
	/**
	 * Torna il token dell'utente relativo alla connessione attuale
	 * @return una stringa contenente il token
	 */
	public String getToken();
	
	/**
	 * Torna l'username dell'utente loggato con questo client
	 * @return una stringa contenente l'useraname
	 */
	public String getUsername();

	/**
	 * Torna il client
	 * @return l'instanza del client stesso 
	 */
	public Client getClient();
	/**
	 * Setta l'istanza del client stesso
	 * @param salva l'instanza del client
	 */
	public void setClient(Client client);
	/**
	 * Rimuove la connessione con un clientLocal quando questo viene terminato
	 */
	public void rmClientLocal();
	
}
