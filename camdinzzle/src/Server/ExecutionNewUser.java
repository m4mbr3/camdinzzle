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
import java.util.ArrayList;
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
	private Server server;
	private Socket connection_with_client;
	private String read_socket;
	private String[] splitted_message;
	private boolean is_an_free_username;
	private ArrayList<String> list_of_commands;
	
	
	public ExecutionNewUser(Socket connection_with_client, Server server ) {
		// TODO Auto-generated constructor stub
		this.server = server;
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
				
				
				//in this point there is a call to another static method for split and check a new string
				splitted_message = ServerMessageBroker.manageReceiveMessageSplit(read_socket);
				//control of login parameters 
				is_an_free_username = server.add_new_user(splitted_message[0],splitted_message[1]);
				
				if(is_an_free_username) 
				{
					list_of_commands.add("ok");
					writer_on_socket.print(ServerMessageBroker.createStandardMessage(list_of_commands));
					//at this point the client is official registered into Server Game and here starts the client connection
					//manager
					server.startClientConnectionManagerDaemon(connection_with_client, splitted_message[0]);
				}
				else 
				{
					writer_on_socket.print(ServerMessageBroker.createErroMessage("usernameOccupato"));
					//close the connection with client because is occurred an error during authentication
					try {
						connection_with_client.close();
					} catch (IOException e) {
								e.printStackTrace();
					}
				}
				
	}

}
