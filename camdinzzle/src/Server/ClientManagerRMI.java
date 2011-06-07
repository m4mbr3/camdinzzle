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

import Client.Client;
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
	private ServerRMI serverRMI;
	
	public ClientManagerRMI(String username, String address, String port, ServerRMI serverRMI) throws MalformedURLException,
	NotBoundException, RemoteException, ConnectException
	{
		this.username = username;
		this.port = port;
		this.isInGame = false;
		this.serverRMI =serverRMI;
		
		client = (ClientRMIInterface)Naming.lookup("rmi://" + address + "/" + username + ":" + port);
		
	}
	
	@Override
	public boolean sendChangeRound(String msg) 
	{
		boolean isSend = false;
		int sendCounter = 0;
		
		do
		{
			try
			{
				if(client.sendMessage(msg))
					isSend = true;
			} 
			catch (RemoteException e) 
			{
				System.out.println("ERROR: " + e.getMessage());
				sendCounter++;
			}
		}while((!isSend) && (sendCounter <= 2));
		if(isSend)
		{
			return true;
		}
		else
		{
			try 
			{
				serverRMI.uscitaPartita(serverRMI.getTokenOfPlayer(username));
				serverRMI.logout(serverRMI.getTokenOfPlayer(username));
			}
			catch (RemoteException e) 
			{
				System.out.println("ERROR: " + e.getMessage());
			}
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


	@Override
	public Client getClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClient(Client client) {
		// TODO Auto-generated method stub
		
	}

	public String getUsername()
	{
		return username;
	}

}
