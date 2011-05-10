/**
 * 
 */
package Server;

import java.net.Socket;
/**
 * @author Andrea
 *
 */
public class ClientManager implements Runnable {

	/**
	 * 
	 */
	private Socket socket_with_client;
	
	public ClientManager(Socket socket_with_client) {
		// TODO Auto-generated constructor stub
		this.socket_with_client = socket_with_client;
	}
	public void run()
	{
		
	}

}
