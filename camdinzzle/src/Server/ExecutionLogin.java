package Server;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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
	private ArrayList<String> list_of_commands;
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
		list_of_commands = new ArrayList<String>();
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
		//control of login parameters splitted_message[0] is user_name, splitted_message[1] is password
		is_an_exist_user = server.is_registered_player(splitted_message[0],splitted_message[1]);
		
		if(is_an_exist_user) 
		{
			list_of_commands.add("ok");
			String token = this.generateToken(splitted_message[0]);
			server.addTokenAtPlayer(splitted_message[0], token);
			list_of_commands.add(token);
			writer_on_socket.print(ServerMessageBroker.createStandardMessage(list_of_commands));
			//at this point the client is official logged into Server Game and here starts the client connection
			//manager
			server.startClientConnectionManagerDaemon(connection_with_client,token, splitted_message[0]);
		}
		else 
		{
			writer_on_socket.print(ServerMessageBroker.createErroMessage("autenticazioneFallita"));
			//close the connection with client because is occurred an error during authentication
			try {
				connection_with_client.close();
			} catch (IOException e) {
						e.printStackTrace();
			}
		}
	}

	
	/**
	 * Ricerca il minimo all'interno della chiave e lo ritorna
	 * @param key Chiave generata dal Server
	 * @return Il minimo all'interno della chiave
	 */
	private int findMin(String key)
	{
		int min = key.length() + 1;
		
		for(int i = 0; i<key.length(); i++)
		{
			if(Integer.parseInt(String.valueOf(key.charAt(i))) < min)
			{
				min = Integer.parseInt(String.valueOf(key.charAt(i)));
			}
		}
		
		return min;
	}
	
	/** Token generato tramite l'applicazione sulla concatenazione di username e hashcode(del riferimento 
	 * a questo oggetto). Sulla concatenazione di applica un algoritmo di trasposizione.
	 * @param username Username del giocatore
	 * @return Token generato
	 */
	private String generateToken(String username)
	{
		String key = server.getKeyForToken();
		int length = key.length();
		String concatenateIdentifier = new String(username + this.hashCode());
		String token = new String("");
		
		for(int j = 0; j<length; j++)
		{
			int min = findMin(key);
			int positionMin = key.indexOf(String.valueOf(min));
			
			key = key.replaceFirst(String.valueOf(min), String.valueOf(key.length()));

			for(int i = positionMin; i<concatenateIdentifier.length(); i += length)
			{
				token += concatenateIdentifier.charAt(i);
			}
		}
		
		return token;
	}
}

