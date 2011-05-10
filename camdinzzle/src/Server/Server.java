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
	 * 
	 */
	private ServerSocket deal_server;
	private ServerSocket deal_login;
	private ServerSocket deal_newuser;
	private Socket deal_socket;
	private Game currentSession;
	private ArrayList<Player> player;
	private ArrayList<ClientManager> logged_player;
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
			startLogin(deal_login);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Definition of new Server NewUser for passing it to startNewUser  and launch newuser daemon
		try{
			deal_newuser = new ServerSocket(port_newuser);
			startNewUser(deal_newuser);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		//Definition of new PlayerList empty 
		player = new ArrayList<Player>();
		logged_player = new ArrayList<ClientManager>();
		login = null;
		newuser = null;
	}
	public void controlAction()
	{
		
	}
	public void startLogin(ServerSocket server_socket)
	{
	
		/*The connection for login must be at this port : 4567	*/
		login = new Login(server_socket, this);
		login.run();
		
	}
	public void startClientConnectionManager(Socket socket_with_client)
	{
		ClientManager new_manager = new ClientManager(socket_with_client);
		new_manager.run();
		logged_player.add(new_manager);
		new_manager = null;
	}
	public void startNewUser(ServerSocket server_socket)
	{
		newuser = new NewUser(server_socket, this);
		newuser.run();
	}
	public void sendCommand()
	{
		
	}
	public void takeFog()
	{
		
	}
	public boolean add_new_user(String user, String password)
	{
		boolean to_be_present = false;
		for( int i = 0 ; i < player.size(); i++)
		{
			if ((user.compareTo(player.get(i).getUserName())==0)&&(password.compareTo(player.get(i).getPassword())==0))
			{
				to_be_present = true;
			}
		}
		if (to_be_present == true)
			return false;
		else
		{
			Player new_player = new Player(user,password);
			player.add(new_player);
			return true;
		}
		
	}
	public boolean is_registered_player(String user,String password)
	{
		//method for search in array of registered player if user is registered
		for(int i = 0 ; i < player.size(); i++)
		{
			if ((user.compareTo(player.get(i).getUserName())==0)&&(password.compareTo(player.get(i).getPassword())==0))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
