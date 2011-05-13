package Server;

import java.io.IOException;
import java.net.ServerSocket;
/**
 * 
 */
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Andrea
 *
 */
public class Server {

	
	/**
	 * @author Andrea
	 * 
	 */
	//oggetti per sincronizzare i metodi sugli arraylist
	private Object lock_players;
	private Object lock_logged_player;
	private Object lock_species;
	//serversocket che aprono le connessioni con i socket del client
	private ServerSocket deal_server;
	private ServerSocket deal_login;
	private ServerSocket deal_newuser;
	private Socket deal_socket;
	//oggetto che identifica la partita corrente
	private Game currentSession;
	private HashMap<String, Player> players;
	
	//HashMap degli utenti loggati ma che non stanno giocando. Chiave token
	private HashMap<String, ClientManager> logged_players;
	
	private int port_login;
	private int port_newuser;
	private Login login;
	private NewUser newuser;
	
	public Server() 
	{
		// TODO Auto-generated constructor stub
		
		//Definition of default port login
		port_login = 4567;
		//Definition of default port NewUser 
		port_newuser = 4566;
		
		//Definition of new Server login for passing it to startLogin and launch login daemon 
		try {
			deal_login = new ServerSocket(port_login);
			startLoginDaemon(deal_login);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Definition of new Server NewUser for passing it to startNewUser  and launch newuser daemon
		try{
			deal_newuser = new ServerSocket(port_newuser);
			startNewUserDaemon(deal_newuser);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		//Definition of new PlayerList empty 
		players = new HashMap<String, Player>();
		logged_players = new HashMap<String, ClientManager>();
		//species = new ArrayList<Species>();
		login = null;
		newuser = null;
		this.lock_logged_player = new Object();
		this.lock_players = new Object();
		this.lock_species = new Object();
	}
	
	
	public void controlAction()
	{ 	}
	
	
	
	
	
	public void startLoginDaemon(ServerSocket server_socket)
	{
	
		/*The connection for login must be at this port : 4567	*/
		login = new Login(server_socket, this);
		login.run();
	
	}
	
	
	
	public void startClientConnectionManagerDaemon(Socket socket_with_client, String username)
	{
		ClientManager new_manager = new ClientManager(socket_with_client, this, username);
		new_manager.run();
		logged_players.put(username, new_manager);
		new_manager = null;
	}
	
	
	public void startNewUserDaemon(ServerSocket server_socket)
	{
		newuser = new NewUser(server_socket, this);
		newuser.run();
	}
	
	public void logout(String username)
	{
		if(logged_players.containsKey(username))
			logged_players.remove(username);
	}
	
	public boolean addNewSpecies(String[] splitted_message, String username)
	{
		synchronized(lock_species)
		{
			if(players.containsKey(username))
			{
				Set set = players.entrySet();
				Iterator<String> iter = set.iterator();
				
				while(iter.hasNext())
				{
					if(players.get(iter).getSpecie().equals(splitted_message[1]))
						return false;
				}
				
				Species new_specie;
				if(splitted_message[2].equals("c"))
				{
					new_specie = new Species(splitted_message[1], Species.getCarnType());
				}
				else 
				{
					new_specie = new Species(splitted_message[1], Species.getVegType());
				}
				
				players.get(username).setSpecie(new_specie);
				return true;
			}
			else
				return false;
		}
	}
	
	public void sendCommand()
	{
		
	}
	
	
	
	public void takeFog()
	{
		
	}
	
	
	
	public boolean add_new_user(String user, String password)
	{
		synchronized(lock_players)
		{
			if(!players.containsKey(user))
			{
				if((players.get(user).getUserName().equals(user)) && (players.get(user).getPassword().equals(password)))
				{
					return false;
				}
				else
				{
					Player new_player = new Player(user,password);
					players.put(user, new_player);
					
					return true;
				}
			}
			else
				return false;
		}
	}
	
	
	
	
	
	public boolean is_registered_player(String user,String password)
	{
		//method for search in array of registered player if user is registered
		synchronized(lock_players)
		{
			if(players.containsKey(user))
			{
				if(players.get(user).getUserName().equals(user))
					return true;
				return false;
			}
			return false;
		}
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
