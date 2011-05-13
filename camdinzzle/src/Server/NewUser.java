/**
 * 
 */
package Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * @author Andrea
 *
 */
public class NewUser implements Runnable{

	/**
	 * 
	 */
	private ServerSocket server_newuser;
	private Socket connection_with_client;
	private ExecutionNewUser ex_newuser;
	private Server server;
	private boolean is_run;
	
	
	public NewUser(ServerSocket server_newuser,Server server) {
		// TODO Auto-generated constructor stub
	
		this.server_newuser = server_newuser;
		this.server = server;
		is_run = true;
		connection_with_client = null;
		ex_newuser = null;
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
				
				connection_with_client = server_newuser.accept();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ex_newuser = new ExecutionNewUser(connection_with_client, server);
			ex_newuser.run();
		}
		
	}
}