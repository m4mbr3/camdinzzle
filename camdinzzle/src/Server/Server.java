
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
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import Client.Client;

/**
 * Classe che rappresenta il server. Registra in remoto un server di nome "server" sulla porta 1099
 * per le connessioni RMI e aspetta le connessioni dei client in socket.
 */
public class Server implements Runnable 
{
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
	/*
	 * Lista dei client connessi via RMI. La chiave della tabella è l'username dell'utente.
	 */
	private Hashtable<String, ClientManagerRMI> clientTableRMI;
	/*
	 * Lista dei client connessi via Socket.
	 */
	private ArrayList<ClientManagerSocket> clientListSocket;
	
	private boolean is_run;
	/*
	 * Porta di default socket.
	 */
	private int port;
	/*
	 * Istanza di ServerLogic.
	 */
	private ServerLogic serverLogic;
	/*
	 * Lista dei client locali.
	 */
	private ArrayList<ClientManagerLocal> clientLocal;
	private Client client;
	private Registry registro;
	private CheckRMIClientConnection checkRMI;
	private static char command;
	
	/**
	 * Istanzia un nuovo server.
	 * @param port porta di ascolta per le connessioni socket
	 * @param serverLogic istanza di ServerLogic
	 * @param serverPort porta dove registrare il server RMI
	 * @param serverName nome del server RMI
	 */
	public Server(int port, ServerLogic serverLogic, String serverPort, String serverName)
	{
			clientLocal = new ArrayList<ClientManagerLocal>();
			this.port = port;
			serverLogic.setServer(this);
			this.serverLogic = serverLogic;
			this.registro = null;
			
	
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
				
				checkRMI = new CheckRMIClientConnection(this);
				(new Thread(checkRMI)).start();
			}
			catch (RemoteException e1) 
			{
				System.out.println("ERROR: Creation server RMI not occured.");
			}
			
			try 
			{
				//System.setSecurityManager(new RMISecurityManager());
				registro = LocateRegistry.createRegistry(Integer.parseInt(serverPort));
				registro.hashCode();
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
	
	/**
	 * Ritorna il numero dei giocatori in partita attualmente.
	 * @return numero dei giocatori in partita. Se va in errore viene tornato -1
	 */
	public int getPlayerInGame()
	{
		try
		{
			int playerInGame = 0;
			
			for (ClientManagerSocket client : clientListSocket) 
			{
				if(client.getIsInGame())
				{
					playerInGame++;
				}
			}
			
			for (ClientManagerLocal client : clientLocal) 
			{
				if(client.getIsInGame())
				{
					playerInGame++;
				}
			}
			
			Set<Map.Entry<String, ClientManagerRMI>> set = clientTableRMI.entrySet();
			Iterator<Map.Entry<String, ClientManagerRMI>> iter = set.iterator();
			
			while(iter.hasNext())
			{
				Map.Entry<String, ClientManagerRMI> me = (Map.Entry<String, ClientManagerRMI>)iter.next();
				
				if(me.getValue().getIsInGame())
				{
					playerInGame++;
				}
			}
			
			return playerInGame;
		}
		catch(Exception ex)
		{
			System.out.println("ERROR in Server.getPlayerInGame: " + ex.getMessage());
			return -1;
		}
	}
	
	public void stop()
	{
		is_run = false;
	}
	
	/**
	 * Aspetta le connessioni in socket da parte dei client.
	 */
	@Override
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
	 * Per ogni  Client in partita invia la notifica di cambio del turno.
	 * @param msg messaggio da mandare al Client
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
			Set<Map.Entry<String, ClientManagerRMI>> set = clientTableRMI.entrySet();
			Iterator<Map.Entry<String, ClientManagerRMI>> iter = set.iterator();
			
			while(iter.hasNext())
			{
				Map.Entry<String, ClientManagerRMI> me = (Map.Entry<String, ClientManagerRMI>)iter.next();
				/*
				if(((ClientManagerRMI)me.getValue()).getIsInGame())
				{*/
					isSend = ((ClientManagerRMI)me.getValue()).sendChangeRound(msg);
					
					if(!isSend)
					{
						removeClientRMI(((ClientManagerRMI)me.getValue()).getUsername());
					}
				//}
			}
		}
	}
	
	/**
	 * Aggiunge un client socket alla lista dei client connessi via socket.
	 * @param cms il client da aggiungere alla lista
	 */
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
	
	/**
	 * Rimuove un client connesso via socket dalla lista dei client connessi.
	 * @param cms rimuove il client dalla lista
	 */
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
	
	/**
	 * Aggiunge un client connesso al server via RMI alla lista dei client connessi via RMI.
	 * @param username username del client
	 * @param clientIp ip del PC del client
	 */
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
	
	/**
	 * Rimuove un client connesso via RMI dalla tabella dei client connessi via RMI.
	 * @param username l'username dell'utente da scollegare
	 */
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
	
	/**
	 * Assegna alla variabile di controllo in partita il parametro passato.
	 * @param username l'username del giocatore
	 * @param isInGame valore da assegnare alla variabile di gioco
	 */
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
	
	/**
	 * Ritorna la HashTable contenente i giocatori connessi via RMI.
	 * @return lista dei giocatori connessi via RMI
	 */
	public Hashtable<String, ClientManagerRMI> getClientRMI()
	{
		return clientTableRMI;
	}
	
	/**
	 * Rimuove un client locale dalla lista dei client locali.
	 * @param cmLocal il client da rimuovere
	 */
	public void removeClientLocal(ClientManagerLocal cmLocal)
	{
		try
		{
			clientLocal.remove(cmLocal);
		}
		catch(Exception ex)
		{
			System.out.println("ERROR: " + ex.getMessage());
		}
	}
	
	/**
	 * Crea un nuovo client locale e lo inserisce nella lista dei client locali.
	 */
	public void newClient()
	{
		clientLocal.add( new ClientManagerLocal(serverLogic, this));		
		client = new Client(clientLocal.get(clientLocal.size()-1));
		clientLocal.get(clientLocal.size()-1).setClient(client);
	}
	
	/**
	 * Stampa il menu di scelta iniziale.
	 */
	public static void print_help()
	{
		System.out.println("****************************************************************************************");
		System.out.println("Welcome to Camdinzzle Server v 1.0");
		System.out.println("Insert corrispondent character for these istructions :");
		System.out.println("-> c or C for create a new Client");
		System.out.println("-> s or S for save the status of server");
		System.out.println("-> e or E for Close the server");
		System.out.println("****************************************************************************************");
	}
	
	
	public static void main(String[] args) throws RemoteException {

		ServerLogic serverLogic = null;
		Server ss=null;
		
		try
		{
			serverLogic = new ServerLogic();
			
			try 
			{
				int socketPort = 4567;
				ss = new Server(socketPort, serverLogic, "1099", "server");
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
		
		
		Scanner scan = new Scanner(System.in);
		String letto=" ";
		boolean check;
		command = ' ';
			do{
				print_help();

				do
				{
					letto = scan.nextLine();
					if(((!(letto.equals("c")))&&(!(letto.equals("C")))&&(!(letto.equals("s")))&&(!(letto.equals("S")))&&(!(letto.equals("e")))&&((!(letto.equals("E"))))))
					{
						System.out.println("Comando non valido");
						check = false;
					}
					else
					{
						check = true;
					}
				}
				while(!check);
				
				try
				{
					command = letto.charAt(0);


					if(command == 'c' || command == 'C')
					{
						System.out.println("You have started a new Client in local!!!");
						ss.newClient();
					}
					else if(command == 's' || command == 'S')
					{
						if(ss.getPlayerInGame() == 0)
						{
							System.out.println("You have saved the server state");
							serverLogic.saveServerState();
						}
						else
						{
							System.out.println("There are players in game...Impossible saving");
						}
					}
					else if (command == 'e' || command =='E')
					{
						System.exit(0);
					}
				}
				catch(NoSuchElementException e)
				{
					System.out.println("See you soon");
				}
				
			}while(command != 'e' && command != 'E');
		}
	
	public void setCommand(char c)
	{
		command = c;
	}
}

