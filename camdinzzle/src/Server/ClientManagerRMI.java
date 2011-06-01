/**
 * 
 */
package Server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import Client.ClientRMIInterface;

/**
 * @author Andrea
 *
 */
public class ClientManagerRMI implements ClientManager 
{
	private String username;
	private ClientRMIInterface client;
	
	public ClientManagerRMI(String username, String address)
	{
		this.username = username;
		
		try {
			client = (ClientRMIInterface)Naming.lookup("rmi://" + address + "/" + username + ":1999");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean sendChangeRound(String msg) 
	{
		try {
			return client.sendMessage(msg);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean getIsInGame() 
	{
		try {
			return client.getIsInGame();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void setIsInGame(boolean isInGame) {
		// TODO Auto-generated method stub
		
	}

	
	
}
