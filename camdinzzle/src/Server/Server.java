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
import java.util.Map;
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
	
	/**
	 * Object for managing the synchronization of operations on hashmap players
	 */
	private Object lock_players;
	
	/**
	 * Object for managing the synchronization of operations on hashmap logged_player
	 */
	private Object lock_logged_player;
	
	/**
	 * Object for managing the synchronization of operations on hashmap species
	 */
	private Object lock_species;
	//serversocket che aprono le connessioni con i socket del client
	
	/**
	 * ServerSocket that listens into thread login at port 4567. 
	 * It is used to connect with client 
	 */
	private ServerSocket deal_login;
	/**
	 * ServerSocket that listens into thread newUser at port 4566.
	 * It is used to create a new user by client 
	 */
	private ServerSocket deal_newuser;
	
	
	/**
	 *	Instance of CurrentSession of Game. It contains the info about the game and current maps used for gaming
	 */
	private Game currentSession;
	
	/**
	 * Hashmap that contains players instances. All players registered are here from first start of server
	 */
	private HashMap<String, Player> players;
	
	/**
	 * Hashmap that contains the instances of players current logged to server.
	 */
	private HashMap<String, ClientManagerSocket> logged_players;
	
	/**
	 * Integer that contains the default port of login daemon
	 */
	private int port_login;
	
	/**
	 * Integer that contains the default port of new user daemon
	 */
	private int port_newuser;
	
	/**
	 * 
	 */
	private String keyForToken;
	private Login login;
	private NewUser newuser;
	
	public Server() 
	{
		// TODO Auto-generated constructor stub
		
		//Definition of default port login
		port_login = 4567;
		//Definition of default port NewUser 
		port_newuser = 4566;
		// Inizializzazione chiave per generazione del token
		keyForToken  = this.generateKeyForToken();
		
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
		logged_players = new HashMap<String, ClientManagerSocket>();
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
	
	
	
	public void startClientConnectionManagerDaemon(Socket socket_with_client, String token, String username)
	{
		ClientManagerSocket new_manager = new ClientManagerSocket(socket_with_client, this, username, token);
		new_manager.run();
		logged_players.put(token, new_manager);
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
				Iterator iter = set.iterator();
				
				while(iter.hasNext())
				{
					Map.Entry<String, Player> me = (Map.Entry<String, Player>) iter.next();
					if(me.getValue().getSpecie().equals(splitted_message[1]))
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
	 * Chiave per generazione dei token generata in modo casuale ad ogni avvio del Server
	 * @return Chiave generata
	 */
	private String generateKeyForToken()
	{
		// Lunghezza della chiave da 3 a 5
		int keyLength = (int) (Math.random() * 3 + 3);
		String key = new String("");
		
		// Contiene i numeri casuali da 0 a 7 presenti nella chiave univocamente
		HashMap<String, String> registeredPositions = new HashMap<String, String>();
		int i = 0;
		
		while(i<keyLength)
		{
			// Generazione casuale del numero da inserire nella chiave
			int singleCasual = (int) (Math.random() * keyLength); 
			
			// Se non � gi� presente nella chiave, viene inserito
			if(!registeredPositions.containsKey(String.valueOf(singleCasual)))
			{
				registeredPositions.put(String.valueOf(singleCasual), String.valueOf(singleCasual));
				key += String.valueOf(singleCasual);
				i++;
			}
		}
		
		return key;
	}
	
	public String getKeyForToken()
	{
		return keyForToken;
	}
	
	public void addTokenAtPlayer(String username, String token)
	{
		synchronized(lock_players)
		{
			if(players.containsKey(username))
			{
				players.get(username).setToken(token);
			}
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
