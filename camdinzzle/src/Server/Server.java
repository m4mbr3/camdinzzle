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
	private Socket deal_socket;
	private Game currentSession;
	private ArrayList<Player> player;
	private int port_login;
	private Login login;
	public Server() 
	{
		// TODO Auto-generated constructor stub
		
		//Definition of a default port login
		port_login = 4567;
		
		//Definition of new Server login for passing it to startLogin and launch login daemon 
		try {
			deal_login = new ServerSocket(port_login);
			startLogin(deal_login);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Definition of new PlayerList empty 
		player = new ArrayList<Player>();
		
		
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
	public void newUser()
	{
		
	}
	public void sendCommand()
	{
		
	}
	public void takeFog()
	{
		
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
