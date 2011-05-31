package Client;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/* Da generare dopo che è stato fatto il login e quindi passare l'username e come address l'indirizzo del
 * server
 */

public class ClientRMI extends UnicastRemoteObject implements ClientRMIInterface
{
	private static final long serialVersionUID = 1L;
	private ClientRMI client;
	private String address;
	private String username;
	private boolean isInGame;
	private static final String port = "1099";
	private ConnectionManagerRMI cmRMI;
	
	public ClientRMI(String address, String username, ConnectionManagerRMI cmRMI) throws RemoteException 
	{
		super();
		this.cmRMI = cmRMI;
		this.address = address;
		this.username = username;
		isInGame = true;
	}

	@Override
	public boolean sendMessage(String msg) throws RemoteException 
	{
		if(ClientMessageBroker.manageChangeRound(msg) != null)
		{
			System.out.println("--> CAMBIO TURNO: " + msg);
			
			
			
			return true;
			/*
			if(ClientMessageBroker.manageChangeRound(msg).equals(username))
			{
				//TODO: chiamata al metodo del Client che mi lancia il popup di conferma del turno
			}
			else
			{
				//TODO: chiamata al metodo del Client che mi notifica il cambio del turno e mi evidenzia il giocatore
			}
			return true;
			*/
		}
		
		return false;
	}

	@Override
	public boolean getIsInGame() {
		return isInGame;
	}

	public void setInGame(boolean isInGame) {
		this.isInGame = isInGame;
	}

	
}
