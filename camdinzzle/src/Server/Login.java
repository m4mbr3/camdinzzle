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


	//Creation of a box for login
	private ServerSocket server_login;
	private Socket new_connection;
	private Server server;
	private boolean is_run;
	private ExecutionLogin ex_login;
	public Login(ServerSocket server_login, Server server) {
		// TODO Auto-generated constructor stub
		this.server_login =  server_login;
		this.server = server;
		this.new_connection = null;
		this.is_run = true;
		this.ex_login = null;
	}
	public void stop()
	{
		is_run = false;
	}
	public void run()
	{

		while(is_run)
		{
			try {
				//waiting for connection
				new_connection = server_login.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ex_login = new ExecutionLogin(new_connection, server);
			ex_login.run();
		
		}
		
	}

}
