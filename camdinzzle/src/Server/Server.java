package Server;

import java.io.IOException;
import java.net.ServerSocket;
/**
 * 
 */
import java.net.Socket;

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
	private int port_login;
	public Server() 
	{
		// TODO Auto-generated constructor stub
		//Definition of a default port login
		port_login = 4567;
		//Definition of new Server login for passing it to startLogin 
		try {
			deal_login = new ServerSocket(port_login);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void controlAction()
	{
		
	}
	public void Startlogin()
	{
		/*The connection for login must be at this port : 4567	*/
		
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
