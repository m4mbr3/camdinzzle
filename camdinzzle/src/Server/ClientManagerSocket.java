/**
 * 
 */
package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import Client.ClientMessageBroker;
/**
 * @author Andrea
 *
 */
public class ClientManagerSocket extends ClientManager implements Runnable {

	private Socket connection_with_client;
	private ServerLogic serverLogic;
	private BufferedWriter writer_on_socket;
	private BufferedReader reader_on_socket;
	private String read_socket;
	private String command;
	private boolean is_run;
	
	public ClientManagerSocket(Socket connection_with_client, ServerLogic serverLogic) {
		// TODO Auto-generated constructor stub
		
		this.is_run = true;
		this.connection_with_client = connection_with_client;
		this.serverLogic = serverLogic;
		this.read_socket = null;
		
		try 
		{
			writer_on_socket = new BufferedWriter(new OutputStreamWriter(this.connection_with_client.getOutputStream()));
		} 
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try 
		{
			reader_on_socket = new BufferedReader( new InputStreamReader(this.connection_with_client.getInputStream()));
		} 
		catch (IOException e) 
		{
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
			try {
				read_socket = reader_on_socket.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("MESSAGGIO CLIENT ->" + read_socket);
			command = ServerMessageBroker.manageMessageType(read_socket);
			System.out.println("COMMAND ->" + command);
			//in this point there is a call to another static method for split and check a new string
			
			try
			{
				//control of login parameters 
				if(command.compareTo("creaUtente")==0)
				{
					writer_on_socket.write(serverLogic.add_new_user(read_socket));
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				else if (command.compareTo("login")==0)
				{
					String login = serverLogic.login(read_socket);
					this.setToken(ClientMessageBroker.manageLogin(login)[0]);
					writer_on_socket.write(login);
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				else if (command.compareTo("creaRazza") == 0)
				{	
					writer_on_socket.write(serverLogic.add_new_user(read_socket));
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				else if (command.compareTo("accessoPartita") == 0)
				{
					String msg = serverLogic.gameAccess(read_socket);
					
					if(ClientMessageBroker.checkMessage(msg))
						this.setIsInGame(true);
						
					writer_on_socket.write(msg);
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				else if (command.compareTo("uscitaPartita") == 0)
				{
					String msg = serverLogic.gameExit(read_socket);
					if(ClientMessageBroker.checkMessage(msg))
						this.setIsInGame(false);
					
					writer_on_socket.write(msg);
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				else if (command.compareTo("listaGiocatori") == 0)
				{
					writer_on_socket.write(serverLogic.playerList(read_socket));
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				else if (command.compareTo("classifica") == 0)
				{
					writer_on_socket.write(serverLogic.ranking(read_socket));
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				else if (command.compareTo("logout") == 0)
				{
					String msg = serverLogic.logout(read_socket);
					if(ClientMessageBroker.checkMessage(msg))
						this.setIsInGame(false);
						
					writer_on_socket.write(msg);
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				
				// Comandi in partita(Informazioni)
				
				else if(command.compareTo("mappaGenerale") == 0)
				{
					writer_on_socket.write(serverLogic.generalMap(read_socket));
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				else if(command.compareTo("listaDinosauri") == 0)
				{
					writer_on_socket.write(serverLogic.dinosaursList(read_socket));
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				else if(command.compareTo("vistaLocale") == 0)
				{
					writer_on_socket.write(serverLogic.dinoZoom(read_socket));
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				else if(command.compareTo("statoDinosauro") == 0)
				{
					writer_on_socket.write(serverLogic.dinoState(read_socket));
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				
				// End Comandi in partita(Informazioni)
				
				// Comandi in partita(Azioni)
				
				else if(command.compareTo("muoviDinosauro") == 0)
				{
					writer_on_socket.write(serverLogic.dinoMove(read_socket));
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				else if(command.compareTo("crescitaDinosauro") == 0)
				{
					writer_on_socket.write(serverLogic.dinoGrowUp(read_socket));
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				else if(command.compareTo("deponiUovo") == 0)
				{
					writer_on_socket.write(serverLogic.dinoNewEgg(read_socket));
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				
				// End Comandi in partita(Azioni)
				
				else if(command.compareTo("confermaTurno") == 0)
				{
					writer_on_socket.write(serverLogic.roundConfirm(read_socket));
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
				else if(command.compareTo("passaTurno") == 0)
				{
					writer_on_socket.write(serverLogic.playerRoundSwitch(read_socket));
					writer_on_socket.newLine();				
					writer_on_socket.flush();
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			read_socket = null;
		}
	}

	public boolean sendChangeRound(String msg)
	{
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			return true;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
