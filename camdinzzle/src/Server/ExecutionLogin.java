package Server;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Andrea
 *
 */
public class ExecutionLogin implements Runnable
{
	private BufferedWriter writer_on_socket;
	private BufferedReader reader_on_socket;
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
		System.out.println("<<EX LOGIN>>--CREATING VARIABLES FOR SOCKET");

		this.connection_with_client = connection_with_client;
		try {
			new BufferedOutputStream(this.connection_with_client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		try {
			writer_on_socket = new BufferedWriter(new OutputStreamWriter(this.connection_with_client.getOutputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			reader_on_socket = new BufferedReader( new InputStreamReader(this.connection_with_client.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("<<EX LOGIN>>--CREATED VARIABLES FOR SOCKET");
		new ArrayList<String>();
	}
	public void run()
	{
		//Read from socket the client string for login example : @login,user=U,pass=P
		System.out.println("<<Ex LOGIN>>--InListening");
		
		try {
				read_socket = reader_on_socket.readLine();	
				//System.out.println("<<Ex LOGIN>>--"+read_socket);
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("<<Ex LOGIN>>-- ERROR IN READING");
			}
			catch (NullPointerException e)
			{
				System.out.println("<<Ex LOGIN>>-- ERROR NULL POINTER");
			}
			

		//System.out.println("<<Ex LOGIN> >--"+read_socket);

		ServerMessageBroker.manageReceiveMessageSplit(read_socket);
		server.login(read_socket);
		try {
			this.writer_on_socket.write(server.login(read_socket));
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//	server.startClientConnectionManagerDaemon(connection_with_client,token, splitted_message[0]);
		
	}
}

