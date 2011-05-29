package Server;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.net.InetAddress;
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
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

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
	private ServerManagerRMI serverManagerRMI;
	// End ClientManager
	
	private static ArrayList<ClientManager> clientList;
	
	private boolean is_run;
	private int port;
	private ServerLogic serverLogic;
	
	public Server(int port, ServerLogic serverLogic) throws RemoteException
	{
			this.port = port;
			try 
			{
				this.server = new ServerSocket(this.port) ;
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
	public static void sendBroadcastMessage(String msg)
	{
		//TODO: ciclo che manda a tutti i Client connessi il messaggio di notifica del turno
		if(clientList.size() > 0)
		{
			for (ClientManager client : clientList) 
			{
				if(client.getIsInGame())
				{
					// TODO: gestione se messaggio non inviato ad un client
					boolean result = client.sendChangeRound(msg);
				}
			}
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
		
		/*// RMI
		
		Registry registro = null;
		ClientManagerRMI cmRMI = null;
		
		try
		{
			cmRMI = new ClientManagerRMI(serverLogic);
		}
		catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			registro = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			Naming.bind("rmi://127.0.0.1/server:1099",(Remote) cmRMI);
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
			Naming.rebind("rmi://127.0.0.1/server:1099",(Remote) cmRMI);
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
		
		
		
		
		// End RMI
		*/
		
		try {
			int port = 4567;
			(new Thread(new Server(port,serverLogic))).start();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	
}
