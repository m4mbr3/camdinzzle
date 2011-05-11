package Server;

import java.io.IOException;
import java.net.ServerSocket;
/**
 * 
 */
import java.net.Socket;
import java.util.ArrayList;

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
	//all players registered to "camdinzzle", tutti gli utenti registrati
	private ArrayList<Player> players;
	//ArrayList of [1-8] player that playing into game
	private ArrayList<ClientManager> players_in_game; 
	//Arraylist degli utenti loggati ma che non stanno giocando
	private ArrayList<ClientManager> logged_players;
	private ArrayList<Species> species;
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
		players = new ArrayList<Player>();
		logged_players = new ArrayList<ClientManager>();
		species = new ArrayList<Species>();
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
		logged_players.add(new_manager);
		new_manager = null;
	}
	
	
	public void startNewUserDaemon(ServerSocket server_socket)
	{
		newuser = new NewUser(server_socket, this);
		newuser.run();
	}
	
	public void logout(String username)
	{
		for(int i = 0 ; i < logged_players.size(); i++)
		{
			if (logged_players.get(i).getUsername().compareTo(username) == 0)
			{
				logged_players.remove(i);
			}
		}
	}
	
	public boolean addNewSpecies(String[] splitted_message, String username)
	{
		//Species new_specie = new Species();
		boolean to_be_present = false;
		
		synchronized(lock_species)
		{
			for(int i = 0; i< species.size(); i++)
			{
				if(species.get(i).getName().compareTo(splitted_message[1]) == 0)
				{
					to_be_present = true;
				}
			}
			if (to_be_present)
			{
				return false;
			}
			else
			{
				Species new_specie;
				if(splitted_message[2].compareTo("c") == 0)
				{
					new_specie = new Species(splitted_message[1], Species.getCarnType());
				}
				else 
				{
					new_specie = new Species(splitted_message[1], Species.getVegType());
				}
				species.add(new_specie);
				for(int i = 0; i < players.size(); i++)
				{
					if (players.get(i).getUserName().compareTo(username) == 0)
					{
						players.get(i).setSpecie(splitted_message[1]);
					}
				}
				return true;
			}
			
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
			boolean to_be_present = false;
			for( int i = 0 ; i < players.size(); i++)
			{
				if ((user.compareTo(players.get(i).getUserName())==0)&&(password.compareTo(players.get(i).getPassword())==0))
				{
					to_be_present = true;
				}
			}
			if (to_be_present == true)
				return false;
			else
			{
				Player new_player = new Player(user,password);
				players.add(new_player);
				return true;
			}
		}	
	}
	
	
	
	
	
	public boolean is_registered_player(String user,String password)
	{
		//method for search in array of registered player if user is registered
		synchronized(lock_players)
		{
			for(int i = 0 ; i < players.size(); i++)
			{
				if ((user.compareTo(players.get(i).getUserName())==0)&&(password.compareTo(players.get(i).getPassword())==0))
				{
					return true;
				}
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
