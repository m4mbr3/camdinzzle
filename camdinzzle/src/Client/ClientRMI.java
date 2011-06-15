package Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
/**
 * Classe utilizzata per istanziare un client RMI per permettere al client di ricevere la notifica del cambio del turno
 * in partita.
 */
public class ClientRMI extends UnicastRemoteObject implements ClientRMIInterface
{
	private static final long serialVersionUID = 1L;
	/*
	 * Variabile che determina se è già stato fatto l'accesso in partita o meno
	 * serve per avere la notifica del cambia turno in caso di valore uguale  a true
	 */
	private boolean isInGame;
	/*
	 * Variabile contenente l'oggetto della connessione in RMI
	 */
	private ConnectionManagerRMI cmRMI;
	/**
	 * Costruttore della Classe ClientRMI che riceve l'oggetto relativo alla connessione con il server
	 * e ne salva un'istanza in cmRMI.
	 * @param cmRMI Oggetto della connessione in RMI
	 * @throws RemoteException
	 */
	public ClientRMI(ConnectionManagerRMI cmRMI) throws RemoteException 
	{
		super();
		this.cmRMI = cmRMI;
		isInGame = true;
	}
	/**
	 * Metodo per inviare la gestione del turno
	 * @param msg : Messaggio da inviare al server
	 */
	@Override
	public boolean sendMessage(String msg) throws RemoteException 
	{
		if(isInGame)
			if(ClientMessageBroker.manageChangeRound(msg) != null)
			{
				cmRMI.setChangeRound(ClientMessageBroker.manageChangeRound(msg));
				return true;
			}
		return false;
	}
	/**
	 * Metodo che torna lo stato del giocatore (è in gioco o no)
	 * @return Torna true se il clientRMI è in gioco no viceversa
	 */
	@Override
	public boolean getIsInGame() {
		return isInGame;
	}
	/**
	 * Metodo che setta lo stato del giocatore 
	 * @param isInGame : è il valore booleano che setta toglie o mette in gioco il client 
	 */
	@Override
	public void setInGame(boolean isInGame) {
		this.isInGame = isInGame;
	}	
}
