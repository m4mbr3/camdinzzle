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
import java.util.HashMap;
/**
 * @author Andrea
 *
 */
public class ClientManager implements Runnable {

	/**
	 * 
	 */
	
	private Socket connection_with_client;
	private Server server;
	private BufferedOutputStream _on_socket;
	private PrintStream writer_on_socket;
	private BufferedReader reader_on_socket;
	private String read_socket;
	private String[] splitted_message; 
	private String username;
	private String command;
	private boolean is_run;
	
	public ClientManager(Socket connection_with_client, Server server, String username) {
		// TODO Auto-generated constructor stub
		this.is_run = true;
		this.connection_with_client = connection_with_client;
		this.server = server;
		this.username = username;
		this.read_socket = null;
		this.splitted_message = null;
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
	
	public void stop()
	{
		is_run =  false;
	}
	
	
	public void run()
	{
		//this is the daemon for one specify user that manage the processes "not in game" like "create Dinosaur "
		while(is_run)
		{
			do{
				try {
					read_socket = reader_on_socket.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}while(read_socket.isEmpty());
			command = ServerMessageBroker.manageMessageType(read_socket);
			//in this point there is a call to another static method for split and check a new string
			splitted_message = ServerMessageBroker.manageReceiveMessageSplit(read_socket);
			//control of login parameters 
			
			if (command.compareTo("creaRazza") == 0)
			{	
				boolean result = server.addNewSpecies(splitted_message, username);
				
				if (result == true)
				{
					ArrayList<String> list_of_commands = new ArrayList<String>();
					list_of_commands.add("ok");
					writer_on_socket.print(ServerMessageBroker.createStandardMessage(list_of_commands));
				}
				else
				{
					writer_on_socket.print(ServerMessageBroker.createErroMessage("nomeRazzaOccupato"));
				}
			}
			else if (command.compareTo("accessoPartita") == 0)
			{
				
			}
			else if (command.compareTo("uscitaPartita") == 0)
			{
				
			}
			else if (command.compareTo("listaGiocatori") == 0)
			{
				
			}
			else if (command.compareTo("classifica") == 0)
			{
			
			}
			else if (command.compareTo("logout") == 0)
			{
				if(splitted_message[0].compareTo("T") == 0)
				{
					server.logout(username);
					stop();
					ArrayList<String> list_of_commands = new ArrayList<String>();
					list_of_commands.add("ok");
					writer_on_socket.print(ServerMessageBroker.createStandardMessage(list_of_commands));
				}
				else
				{
					writer_on_socket.print(ServerMessageBroker.createErroMessage("tokenNonValido"));
				}
			}
			
			
			
		}
	}




	public String getUsername() {
		return username;
	}




	public void setUsername(String username) {
		this.username = username;
	}

}
