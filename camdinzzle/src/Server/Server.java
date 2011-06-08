
package Server;

import java.io.IOException;
import java.net.BindException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import Client.Client;


public class Server implements Runnable 
{

	/**
	 * @param args
	 */
	// RMI variables
	
	// End RMI variables
	
	// Socket variables
	private ServerSocket server;
	private Socket new_connection;
	// End Socket variables
	
	// ClientManager
	private ClientManagerSocket clientManagerSocket;
	// End ClientManager
	
	
	private ServerRMI serverRMI;
	
	private Hashtable<String, ClientManagerRMI> clientTableRMI;
	private ArrayList<ClientManagerSocket> clientListSocket;
	
	private boolean is_run;
	private int port;
	private ServerLogic serverLogic;
	private ArrayList<ClientManagerLocal> clientLocal;
	private Client client;
	public Server(int port, ServerLogic serverLogic, String serverPort, String serverName)
	{
			clientLocal = new ArrayList<ClientManagerLocal>();
			this.port = port;
			serverLogic.setServer(this);
			this.serverLogic = serverLogic;
			
			ClientManagerLocal cmLocal = new ClientManagerLocal(serverLogic);
			client = new Client(cmLocal);
			clientLocal.add(cmLocal);
			clientLocal.get(0).setClient(client);
			try 
			{
				this.server = new ServerSocket(this.port);
				clientListSocket = new ArrayList<ClientManagerSocket>();
				this.is_run = true;
				this.new_connection = null;
				this.clientManagerSocket=null;
			} 
			catch (BindException e)
			{
				System.out.println("ERROR: Another server is already running.");
				System.exit(0);
			}
			catch (IOException e) 
			{
				System.out.println("ERROR: Creation socket not occured.");
			}
			
			serverRMI = null;
			
			try
			{
				serverRMI = new ServerRMI(serverLogic, serverPort, this);
				clientTableRMI = new Hashtable<String, ClientManagerRMI>();
			}
			catch (RemoteException e1) 
			{
				System.out.println("ERROR: Creation server RMI not occured.");
			}
			
			try 
			{
				//System.setSecurityManager(new RMISecurityManager());
				Registry registro = LocateRegistry.createRegistry(Integer.parseInt(serverPort));
				Naming.bind("rmi://127.0.0.1/" + serverName + ":" + serverPort,(Remote) serverRMI);
			} 
			catch (AccessException e) 
			{
				System.out.println("ERROR: The server do not have permission to perform the action requested by the method call.");
			} 
			catch (RemoteException e) 
			{
				System.out.println("ERROR: Server RMI is down.");
			} 
			catch (AlreadyBoundException e)
			{
				System.out.println("ERROR: Server is just registrated.");
			}
			catch (MalformedURLException e) 
			{
				System.out.println("ERROR: Check the URL to bind the server RMI.");
			}
			try 
			{
				Naming.rebind("rmi://127.0.0.1/" + serverName + ":" + serverPort,(Remote) serverRMI);
				System.out.println("Server RMI Avviato!");
			}
			catch (AccessException e) 
			{
				System.out.println("ERROR: The server do not have permission to perform the action requested by the method call.");
			} 
			catch (RemoteException e) 
			{
				System.out.println("ERROR: Server RMI is down.");
			} 
			catch (MalformedURLException e)
			{
				System.out.println("ERROR: Check the URL to bind the server RMI.");
			}
	}
	
	public void stop()
	{
		is_run = false;
	}
	
	public void run()
	{
		System.out.println("<<SERVER DAEMON>>--STARTED");
		
		while(is_run)
		{
			try 
			{
				//waiting for connection
				System.out.println("<<SERVER DAEMON>>--WAITING FOR CONNECTIONS at " + server.getLocalPort());
				new_connection = server.accept();
				System.out.println("<<SERVER DAEMON>>--CONNECTION INTERCEPTED");
				clientManagerSocket = new ClientManagerSocket(new_connection,serverLogic, this);
				System.out.println("<<SERVER DAEMON>>--STARTING CLIENTMANAGER");
				(new Thread(clientManagerSocket)).start();
				System.out.println("<<SERVER DAEMON>>--EXECUTION CLIENTMANAGER STARTED");
			} 
			catch (IOException e) 
			{
				System.out.println("ERROR: " + e.getMessage());
			}
		}
		
	}
	
	/**
	 * Per ogni  Client in partita invia la notifica di cambio del turno
	 * @param msg : messaggio da mandare al Client
	 */
	public void sendBroadcastMessage(String msg)
	{
		boolean isSend;
		
		if(clientListSocket.size() > 0)
		{
			for (ClientManagerSocket client : clientListSocket) 
			{
				if(client.getIsInGame())
				{
					isSend = client.sendChangeRound(msg);
					if(!isSend)
					{
						clientListSocket.remove(client);
						client.stop();
					}
				}
			}
		}
		if (clientLocal.size() > 0)
		{
			for (ClientManagerLocal client : clientLocal) 
			{
				if(client.getIsInGame())
				{
					client.sendChangeRound(msg);
				}
			}
		}
		if(clientTableRMI.size() > 0)
		{			
			Set set = clientTableRMI.entrySet();
			Iterator iter = set.iterator();
			
			while(iter.hasNext())
			{
				Map.Entry me = (Map.Entry)iter.next();
				
				if(((ClientManagerRMI)me.getValue()).getIsInGame())
				{
					isSend = ((ClientManagerRMI)me.getValue()).sendChangeRound(msg);
					
					if(!isSend)
					{
						removeClientRMI(((ClientManagerRMI)me.getValue()).getUsername());
					}
				}
			}
		}
	}
	
	public void addClientSocket(ClientManagerSocket cms)
	{
		try
		{
			clientListSocket.add(cms);
		}
		catch(Exception ex)
		{
			System.out.println("ERROR: " + ex.getMessage());
		}
	}
	
	public void removeClientSocket(ClientManagerSocket cms)
	{
		try
		{
			clientListSocket.remove(cms);
		}
		catch(Exception ex)
		{
			System.out.println("ERROR: " + ex.getMessage());
		}
	}
	
	public void addClientRMI(String username, String clientIp)
	{
		try 
		{
			ClientManagerRMI cmRMI = new ClientManagerRMI(username, clientIp, "1099", serverRMI);
			clientTableRMI.put(username, cmRMI);
			System.out.println("Client scaricato!!");
		} 
		catch (MalformedURLException e) 
		{
			System.out.println("ERROR: Check the URL to bind the server RMI.");
		}
		catch (RemoteException e) 
		{
			System.out.println("ERROR: Client RMI is down.");
		}
		catch (Exception e)
		{
			System.out.println("ERROR: " + e.getMessage());
			String token = serverLogic.getTokenFromUsername(username);
			if(token != null)
			{
				serverLogic.logout(token);
			}
		}
	}
	
	public void removeClientRMI(String username)
	{
		try
		{
			if(clientTableRMI.get(username) != null)
			{
				clientTableRMI.get(username).unregistryClient();
				
				clientTableRMI.remove(username);
			}
		}
		catch(Exception ex)
		{
			System.out.println("ERROR: " + ex.getMessage());
		}
	}
	
	public void setGameAccessRMI(String username, boolean isInGame)
	{
		try
		{
			if(clientTableRMI.get(username) != null)
			{
				clientTableRMI.get(username).setIsInGame(isInGame);
			}
		}
		catch(Exception ex)
		{
			System.out.println("ERROR: " + ex.getMessage());
		}
	}
	
	
	public static void main(String[] args) throws RemoteException {
		// TODO Auto-generated method stub
		ServerLogic serverLogic = null;
		
		try
		{
			serverLogic = new ServerLogic();
			
			try 
			{
				int socketPort = 4567;
				Server ss = new Server(socketPort, serverLogic, "1099", "server");
				(new Thread(ss)).start();
			} 
			catch (Exception e) 
			{
				System.out.println("ERROR: " + e.getMessage());
			}
		}
		catch(Exception ex)
		{
			System.out.println("ERROR: serverLogic is not create.\n" + ex.getMessage());
		}
	}
}
