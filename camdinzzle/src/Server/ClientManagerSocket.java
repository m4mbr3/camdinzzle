/**
 * 
 */
package Server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
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
public class ClientManagerSocket extends ClientManager implements Runnable {

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
	private String token;
	private String command;
	private boolean is_run;
	private FileOutputStream f_out; 
	
	
	public ClientManagerSocket(Socket connection_with_client, Server server, String username, String token) {
		// TODO Auto-generated constructor stub
		
		this.is_run = true;
		this.connection_with_client = connection_with_client;
		this.server = server;
		this.username = username;
		this.token = token;
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
			
			//control of login parameters 
			
			if (command.compareTo("creaRazza") == 0)
			{	
				writer_on_socket.println(server.add_new_user(read_socket));
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
					writer_on_socket.println(ServerMessageBroker.createStandardMessage(list_of_commands));
				}
				else
				{
					writer_on_socket.println(ServerMessageBroker.createErroMessage("tokenNonValido"));
				}
			}
			
			// Comandi in partita(Informazioni)
			
			else if(command.compareTo("mappaGenerale") == 0)
			{
				
			}
			
			// End Comandi in partita(Informazioni)
			
			// Comandi in partita(Azioni)
			
			else if(command.compareTo("muoviDinosauro") == 0)
			{
				String[] dinoMovement = ServerMessageBroker.manageDinoMovement(command);
			}
			
			// End Comandi in partita(Azioni)
		}
	}




	public String getUsername() {
		return username;
	}




	public void setUsername(String username) {
		this.username = username;
	}

}
