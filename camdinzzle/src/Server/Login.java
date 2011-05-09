package Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 */

/**
 * @author Andrea
 *
 */
public class Login implements Runnable{

	/**
	 * 
	 */
	//Creation of a box for login
	ServerSocket server_login;
	Socket new_connection;
	public Login(ServerSocket server_login) {
		// TODO Auto-generated constructor stub
		this.server_login =  server_login;
	}
	public void run()
	{
		//waiting for connection
		try {
			new_connection = server_login.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
