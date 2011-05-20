/**
 * 
 */
package Server;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @author Andrea
 *
 */
public class ExecutionNewUser implements Runnable{

	/**
	 * 
	 */
	
	private BufferedOutputStream _on_socket;
	private PrintStream writer_on_socket;
	private BufferedReader reader_on_socket;
	private ServerLogic serverLogic;
	private Socket connection_with_client;
	private String read_socket;
	
	
	
	public ExecutionNewUser(Socket connection_with_client, ServerLogic serverLogic ) {
		// TODO Auto-generated constructor stub
		this.serverLogic = serverLogic;
		this.connection_with_client = connection_with_client;
		this.read_socket = null;
		try {
			_on_socket = new BufferedOutputStream(this.connection_with_client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer_on_socket = new PrintStream(_on_socket, false);
		try {
			reader_on_socket = new BufferedReader( new InputStreamReader(this.connection_with_client.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void run()
	{
		//Read from socket the client string for login example : @login,user=U,pass=P
				
				do{
					try {
						read_socket = reader_on_socket.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}while(read_socket.isEmpty());
				
			
				writer_on_socket.println(serverLogic.add_new_user(read_socket));
				
	}

}
