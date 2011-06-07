
package Server;

import java.io.IOException;
import javax.swing.JOptionPane;
import java.io.ObjectInputStream.GetField;
import java.net.BindException;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import Client.Client;
import Client.ClientRMIInterface;
import Client.ConnectionManagerRMI;


public class Server implements Runnable {

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
	
	public Server(int port, ServerLogic serverLogic, String serverPort, String serverName)
	{
			this.port = port;
			
			serverLogic.setServer(this);
			this.serverLogic = serverLogic;
			
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
				Registry registro = LocateRegistry.createRegistry(Integer.parseInt(serverPort));
				Naming.bind("rmi://127.0.0.1/" + serverName + ":" + serverPort,(Remote) serverRMI);
			} 
			catch (AccessException e) 
			{
				System.out.println("ERROR: The server do not have permission to perform the action requested by the method call.");
			} 
			catch (RemoteException e) 
			{
				System.out.println("ERROR: Remote.");
			} 
			catch (AlreadyBoundException e)
			{
				System.out.println("ERROR: AlreadyBoundException in registry server RMI.");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (RemoteException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (MalformedURLException e)
			{
				System.out.println("Check the URL to bind the server RMI.");
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Per ogni  Client in partita invia la notifica di cambio del turno
	 * @param msg : messaggio da mandare al Client
	 */
	public void sendBroadcastMessage(String msg)
	{
		if(clientListSocket.size() > 0)
		{
			for (ClientManagerSocket client : clientListSocket) 
			{
				if(client.getIsInGame())
				{
					try
					{
						// TODO: gestione se messaggio non inviato ad un client
						boolean result = client.sendChangeRound(msg);
					}
					catch(Exception ex)
					{
						//clientList.remove(client);
					}
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
					((ClientManagerRMI)me.getValue()).sendChangeRound(msg);
				}
			}
		}
	}
	
	public void addClientSocket(ClientManagerSocket cms)
	{
		clientListSocket.add(cms);
	}
	
	public void removeClientSocket(ClientManagerSocket cms)
	{
		clientListSocket.remove(cms);
	}
	
	public void addClientRMI(String username, String clientIp)
	{
		try {
			ClientManagerRMI cmRMI = new ClientManagerRMI(username, clientIp, "1099");
			clientTableRMI.put(username, cmRMI);
			System.out.println("Client scaricato!!");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			String token = serverLogic.getTokenFromUsername(username);
			if(token != null)
			{
				serverLogic.logout(token);
			}
		}
	}
	
	public void removeClientRMI(String username)
	{
		if(clientTableRMI.get(username) != null)
		{
			clientTableRMI.get(username).unregistryClient();
			
			clientTableRMI.remove(username);
		}
	}
	
	public void setGameAccessRMI(String username, boolean isInGame)
	{
		if(clientTableRMI.get(username) != null)
		{
			clientTableRMI.get(username).setIsInGame(isInGame);
		}
	}
	
	
	public static void main(String[] args) throws RemoteException {
		// TODO Auto-generated method stub
		ServerLogic serverLogic = null;
		
		try
		{
			serverLogic = new ServerLogic();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		try {
			int socketPort = 4567;
			
			Server ss = new Server(socketPort, serverLogic, "1099", "server");
			//ServerForClientRMI sfcRMI = new ServerForClientRMI(serverLogic, ip, "server", "1099");

			(new Thread(ss)).start();
			//(new Thread(sfcRMI)).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Client client  = new Client(serverLogic);
	}

	
}
