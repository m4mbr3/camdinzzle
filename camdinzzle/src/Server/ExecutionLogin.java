package Server;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 
 */

/**
 * @author Andrea
 *
 */
public class ExecutionLogin implements Runnable
{

	/**
	 * 
	 */
	
	//Object for writing and reading on socket
	private BufferedOutputStream _on_socket;
	private PrintStream writer_on_socket;
	private BufferedReader reader_on_socket;
	private String[] splitted_message;
	private boolean is_an_exist_user;
	private Server server;
	//Creation of a box for connection with client passed by Login
	Socket connection_with_client;
	
	//Variable for save the input from socket
	String read_socket;
	public ExecutionLogin(Socket connection_with_client, Server server) 
	{
		// TODO Auto-generated constructor stub
		//Save an instance of server
		this.server = server;
		
		this.connection_with_client = connection_with_client;
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
		}while(!read_socket.isEmpty());
		
		//in this point there is a call to another static method for split and check a new string
		splitted_message = ServerMessageBroker.manageReceiveMessageSplit(read_socket);
		//control of login parameters 
		is_an_exist_user = server.is_registered_player(splitted_message[0],splitted_message[1]);
		
		if(is_an_exist_user) 
		{
			
		}
		else 
		{
			writer_on_socket.print()
		}
	}

}

