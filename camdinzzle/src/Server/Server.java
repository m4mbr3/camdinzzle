
package Server;

import java.io.IOException;
import javax.swing.JOptionPane;
import java.io.ObjectInputStream.GetField;
import java.net.BindException;
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
	private ServerRMI serverRMI;
	// End ClientManager
	
	
	private ServerRMI cmRMI;
	
	private static ArrayList<ClientManager> clientList;
	private static String address = "127.0.0.1";
	
	private boolean is_run;
	private int port;
	private ServerLogic serverLogic;
	
	public Server(int port, ServerLogic serverLogic, String serverIp, String serverPort, String serverName)
	{
			this.port = port;
			
			serverLogic.setServer(this);
			
			try 
			{
				this.server = new ServerSocket(this.port) ;
			} 
			catch (BindException e)
			{
				System.out.println("Another server is already running");
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.serverLogic = serverLogic;
			clientList = new ArrayList<ClientManager>();
			this.new_connection = null;
			this.is_run = true;
			this.clientManagerSocket=null;
			
			cmRMI = null;
			
			try
			{
				cmRMI = new ServerRMI(serverLogic, serverIp, serverPort);
			}
		
			
			catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				Registry registro = LocateRegistry.createRegistry(Integer.parseInt(serverPort));
				Naming.bind("rmi://" + serverIp + "/" + serverName + ":" + serverPort,(Remote) cmRMI);
				//registro.rebind("rmi://127.0.0.1/server:1999",(Remote) new Server());
			} catch (AccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AlreadyBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Naming.rebind("rmi://" + serverIp + "/" + serverName + ":" + serverPort,(Remote) cmRMI);
				System.out.println("Server RMI Avviato!");
			} catch (AccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			try {
				//waiting for connection
				System.out.println("<<SERVER DAEMON>>--WAITING FOR CONNECTIONS at " + server.getLocalPort());
				new_connection = server.accept();
				System.out.println("<<SERVER DAEMON>>--CONNECTION INTERCEPTED");
				clientManagerSocket = new ClientManagerSocket(new_connection,serverLogic);
				clientList.add(clientManagerSocket);
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
		if(clientList.size() > 0)
		{
			for (ClientManager client : clientList) 
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
		
		ArrayList<ClientManagerRMI> clientRMI = null;
		try {
			clientRMI = cmRMI.getClient();
			
			if(clientRMI != null)
			{
				for (ClientManagerRMI cl : clientRMI) 
				{
					cl.sendChangeRound(msg);
				}
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addClientRMI(ClientManagerRMI cmRMI)
	{
		try
		{
			clientList.add(cmRMI);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void removeClient(ClientManager cm)
	{
		clientList.remove(cm);
	}
	
	public static void main(String[] args) throws RemoteException {
		// TODO Auto-generated method stub
		ServerLogic serverLogic = null;
		String ip = "localhost";
		
		// Cerca l'IP del server
		try 
		{
			for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) 
			{
				  NetworkInterface iface = ifaces.nextElement();
				  for (Enumeration<InetAddress> addresses = iface.getInetAddresses(); addresses.hasMoreElements();) 
				  {
					  InetAddress address = addresses.nextElement();
					  if (address instanceof Inet4Address) 
					  {
						  if(!address.getHostAddress().equals("127.0.0.1"))
						  {
							  ip = address.getHostAddress();
						  }
				    }
				  }
				}
		} 
		catch (SocketException e) 
		{
			System.out.println("IP del server non trovato");
		}
		// End ricerca IP del server
		
		try
		{
			serverLogic = new ServerLogic();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		try {
			int port = 4567;
			
			Server ss = new Server(port, serverLogic, "127.0.0.1", "1099", "server");
			//ServerForClientRMI sfcRMI = new ServerForClientRMI(serverLogic, ip, "server", "1099");

			(new Thread(ss)).start();
			//(new Thread(sfcRMI)).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

	
}
