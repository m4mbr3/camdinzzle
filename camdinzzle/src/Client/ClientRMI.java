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

/* Da generare dopo che � stato fatto il login e quindi passare l'username e come address l'indirizzo del
 * server
 */

public class ClientRMI extends UnicastRemoteObject implements ClientRMIInterface
{
	private static final long serialVersionUID = 1L;
	private boolean isInGame;
	private ConnectionManagerRMI cmRMI;
	
	public ClientRMI(ConnectionManagerRMI cmRMI) throws RemoteException 
	{
		super();
		this.cmRMI = cmRMI;
		isInGame = true;
	}

	@Override
	public boolean sendMessage(String msg) throws RemoteException 
	{
		if(ClientMessageBroker.manageChangeRound(msg) != null)
		{
			System.out.println("--> CAMBIO TURNO: " + msg);
			
			cmRMI.setChangeRound(ClientMessageBroker.manageChangeRound(msg));
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean getIsInGame() {
		return isInGame;
	}
	
	@Override
	public void setInGame(boolean isInGame) {
		this.isInGame = isInGame;
	}

	
}
