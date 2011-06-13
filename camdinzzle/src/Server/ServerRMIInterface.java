package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Interfaccia che rappresenta i metodi messi a disposizione dal server ad un client connesso via RMI.
 */
public interface ServerRMIInterface extends Remote 
{
	/**
	 * Crea un nuovo utente con username e password.
	 * @param username l'username dell'utente da creare
	 * @param password la password dell'utente da creare
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String creaUtente(String username, String password) throws RemoteException;
	/**
	 * Esegue il login di un utente.
	 * @param username l'username dell'utente
	 * @param password la password dell'utente
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String login(String username, String password) throws RemoteException;
	/**
	 * Esegue la creazione di una specie.
	 * @param token il token dell'utente che vuole creare la specie
	 * @param name nome della specie
	 * @param type tipo della specie
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String creaRazza(String token, String name, String type) throws RemoteException;
	/**
	 * Esegue l'accesso alla partita di un utente.
	 * @param token il token dell'utente
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String accessoPartita(String token) throws RemoteException;
	/**
	 * Esegue l'uscita dalla partita di un utente.
	 * @param token il token dell'utente
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String uscitaPartita(String token) throws RemoteException;
	/**
	 * Crea la lista dei giocatori attualmente in partita.
	 * @param token il token dell'utente
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String[] listaGiocatori(String token) throws RemoteException;
	/**
	 * Crea la classifica di tutte le specie create.
	 * @param token il token dell'utente
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public ArrayList<String> classifica(String token) throws RemoteException;
	/**
	 * Esegue il logout di un utente.
	 * @param token il token dell'utente
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String logout(String token) throws RemoteException;
	/**
	 * Crea la mappa generale del giocatore.
	 * @param token il token del giocatore
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public ArrayList<String> mappaGenerale(String token) throws RemoteException;
	/**
	 * Crea la lista dei dinosauri del giocatore.
	 * @param token il token del giocatore
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String[] listaDinosauri(String token) throws RemoteException;
	/**
	 * Crea la vista locale del dinosauro richiesto da un giocatore.
	 * @param token il token del giocatore.
	 * @param dinoId l'id del dinosauro
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public ArrayList<String> vistaLocale(String token, String dinoId) throws RemoteException;
	/**
	 * Ritorna lo stato del dinosauro richiesto da un giocatore.
	 * @param token il token del giocatore
	 * @param dinoId l'id del dinosauro richiesto
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String[] statoDinosauro(String token, String dinoId) throws RemoteException;
	/**
	 * Esegue il movimento di un dinosauro di un giocatore.
	 * @param token il token del giocatore
	 * @param dinoId l'id del dinosauro da muovere
	 * @param row la riga di destinazione del movimento
	 * @param col la colonna di destinazione del movimento
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String muoviDinosauro(String token, String dinoId, String row, String col) throws RemoteException;
	/**
	 * Esegue la crescita di un dinosauro di un giocatore.
	 * @param token il token del giocatore
	 * @param dinoId l'id del dinosauro da far crescere
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String cresciDinosauro(String token, String dinoId) throws RemoteException;
	/**
	 * Esegue la deposizione di un uovo di un dinosauro di un giocatore.
	 * @param token il token del giocatore
	 * @param dinoId l'id del dinosauro su cui eseguire l'azione
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String deponiUovo(String token, String dinoId) throws RemoteException;
	/**
	 * Esegue la conferma del turno da parte di un giocatore
	 * @param token il token del giocatore
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String confermaTurno(String token) throws RemoteException;
	/**
	 * Esegue il passaggio del turno da parte di un giocatore.
	 * @param token il token del giocatore
	 * @return esito della richiesta
	 * @throws RemoteException
	 */
	public String passaTurno(String token) throws RemoteException;
	/**
	 * Notifica al server che un utente e' entrato in partita.
	 * @param username l'username dell'utente
	 * @param clientIp l'ip del client che si e' connesso
	 * @throws RemoteException
	 */
	public void notifyLogin(String username, String clientIp) throws RemoteException;
	/**
	 * Notifica al server che un utente ha eseguito il logout.
	 * @param username l'username dell'utente che ha eseguito il logout
	 * @throws RemoteException
	 */
	public void notifyLogout(String username) throws RemoteException;
	/**
	 * Setta la variabile di controllo se l'utente e' in partita.
	 * @param isInGame valore da assegnare alla variabile
	 * @param username l'username del giocatore entrato in partita
	 * @throws RemoteException
	 */
	public void setGameAccess(boolean isInGame, String username) throws RemoteException;
	/**
	 * Ritorna in token del giocatore attualmente abilitato ad effettuare le proprie mosse.
	 * @return il token del giocatore
	 * @throws RemoteException
	 */
	public String getTokenOfCurrentPlayer() throws RemoteException;
	/**
	 * Notifica il cambio del turno.
	 * @throws RemoteException
	 */
	public void changeRoundNotify() throws RemoteException;
	/**
	 * Ritorna l'username del giocatore abilitato a fare le proprie mosse.
	 * @return l'username del giocatore
	 * @throws RemoteException
	 */
	public String getUsernameOfCurrentPlayer() throws RemoteException;
	/**
	 * Esegue l'aggiornamento dello stato di un giocatore.
	 * @param token il token del gioatore
	 * @throws RemoteException
	 */
	public void updatePlayer(String token) throws RemoteException;
	/**
	 * Ritrona il token di un giocatore.
	 * @param username l'username del giocatore
	 * @return il token del giocatore richiesto
	 * @throws RemoteException
	 */
	public String getTokenOfPlayer(String username) throws RemoteException;
}
