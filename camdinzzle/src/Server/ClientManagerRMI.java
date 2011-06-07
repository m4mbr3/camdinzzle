/**
 * 
 */
package Server;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
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
	private String port;
	private boolean isInGame;
	
	public ClientManagerRMI(String username, String address, String port) throws MalformedURLException,
	NotBoundException, RemoteException, ConnectException
	{
		this.username = username;
		this.port = port;
		this.isInGame = false;
		
		client = (ClientRMIInterface)Naming.lookup("rmi://" + address + "/" + username + ":" + port);
		
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
		return isInGame;
		
	}

	@Override
	public void setIsInGame(boolean isInGame) {
		// TODO Auto-generated method stub
		this.isInGame = isInGame;
	}
	
	public void unregistryClient()
	{
		this.client = null;
	}
}
