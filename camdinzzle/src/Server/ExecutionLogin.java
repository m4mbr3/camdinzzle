import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.PrintStream;

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
	BufferedOutputStream _on_socket;
	PrintStream writer_on_socket;
	BufferedReader reader_on_socket;
	
	//Creation of a box for connection with client passed by Login
	Socket connection_with_client;
	
	//Variable for save the input from socket
	String read_socket;
	public ExecutionLogin(Socket connection_with_client) 
	{
		// TODO Auto-generated constructor stub
		
		this.connection_with_client = connection_with_client;
		_on_socket = new BufferedOutputStream(this.connection_with_client.getOutputStream());
		writer_on_socket = new PrintStream(_on_socket, false);
		reader_on_socket = new BufferedReader(this.connection_with_client.getInputStream());
	}
	public void run()
	{
		//Raed from socket the client string for login ex : @login,user=U,pass=P
		do{
			read_socket = reader_on_socket.readLine();
		}while(!read_socket.isEmpty());
		
		//in this point there is a call to another static method for split and check a new string
	}

}

