package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Interfaccia utilizzata per definire un client RMI.
 */
public interface ClientRMIInterface extends Remote 
{
	/**
	 * Metodo da ridefinire per l'invio di messaggi al server.
	 * @param msg :messaggio per il server
	 * @return esito dell'operazione
	 * @throws RemoteException
	 */
	public boolean sendMessage(String msg) throws RemoteException;
	/**
	 * Metodo da ridefinire per verificare se l'utente è in gioco 
	 * @return esito dell'operazione
	 * @throws RemoteException
	 */
	public boolean getIsInGame() throws RemoteException;
	/**
	 * Metodo da ridefinire per settare un utente come in partita
	 * @param isInGame
	 * @throws RemoteException
	 */
	public void setInGame(boolean isInGame) throws RemoteException;
}
