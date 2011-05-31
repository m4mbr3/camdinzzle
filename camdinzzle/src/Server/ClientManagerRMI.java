/**
 * 
 */
package Server;

import java.rmi.RemoteException;
import java.util.ArrayList;

import Client.ClientRMIInterface;

/**
 * @author Andrea
 *
 */
public class ClientManagerRMI implements ClientManager 
{
	private ClientRMIInterface client;
	
	public ClientManagerRMI(ClientRMIInterface client)
	{
		this.client = client;
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
