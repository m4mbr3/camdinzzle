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
	private ServerLogic serverLogic;
	//Creation of a box for connection with client passed by Login
	Socket connection_with_client;
	
	//Variable for save the input from socket
	String read_socket;
	public ExecutionLogin(Socket connection_with_client, ServerLogic serverLogic) 
	{
		// TODO Auto-generated constructor stub
		//Save an instance of serverLogic
		this.serverLogic = serverLogic;
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
		String message_from_server;
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

		
		
		message_from_server = serverLogic.login(read_socket);
		if ( ServerMessageBroker.manageMessageType(message_from_server).compareTo("ok")==0 )
		{
			try {
				this.writer_on_socket.write(message_from_server);
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//dal message devo tirare fuori il token e avviare la tiritera del client manager
			String[] for_token = ServerMessageBroker.manageReceiveMessageSplit(message_from_server);
			String[] for_username = ServerMessageBroker.manageReceiveMessageSplit(read_socket);
			System.out.println(for_token[0] +" "+ for_username[0]);
			//serverLogic.startClientConnectionManagerDaemon(connection_with_client, for_token[0], for_username[0]);
		}
		else
		{
			try{
				this.writer_on_socket.write(message_from_server);
				this.connection_with_client.close();
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
			
		
		
		//	serverLogic.startClientConnectionManagerDaemon(connection_with_client,token, splitted_message[0]);
		
	}
}

