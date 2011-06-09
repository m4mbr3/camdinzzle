/**
 * 
 */
package Server;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import Client.ClientRMIInterface;

public class ClientManagerRMI implements ClientManager 
{
	private String username;
	private ClientRMIInterface client;
	private boolean isInGame;
	private ServerRMI serverRMI;
	
	public ClientManagerRMI(String username, String address, String port, ServerRMI serverRMI) throws MalformedURLException,
	NotBoundException, RemoteException, ConnectException
	{
		this.username = username;
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
		}while((!isSend) && (sendCounter < 2));
		if(isSend)
		{
			return true;
		}
		else
		{
			try 
			{
				if(this.isInGame)
				{
					serverRMI.uscitaPartita(serverRMI.getTokenOfPlayer(username));
				}
				serverRMI.logout(serverRMI.getTokenOfPlayer(username));
				serverRMI.removeClientRMI(username);
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
	public void setIsInGame(boolean isInGame) 
	{
		this.isInGame = isInGame;
	}
	
	public void unregistryClient()
	{
		this.client = null;
	}

	public String getUsername()
	{
		return username;
	}

}
