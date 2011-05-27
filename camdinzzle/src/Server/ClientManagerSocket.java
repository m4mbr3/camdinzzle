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
public class ClientManagerSocket implements ClientManager, Runnable {

	private Socket connection_with_client;
	private ServerLogic serverLogic;
	private BufferedWriter writer_on_socket;
	private BufferedReader reader_on_socket;
	private String read_socket;
	private String command;
	private boolean is_run;
	private String token;
	private boolean isInGame;
	private int timeoutRequest;
	
	public ClientManagerSocket(Socket connection_with_client, ServerLogic serverLogic) {
		// TODO Auto-generated constructor stub
		token ="";
		isInGame = false;
		this.is_run = true;
		this.connection_with_client = connection_with_client;
		this.serverLogic = serverLogic;
		this.read_socket = null;
		timeoutRequest = 0;
		
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
			if(timeoutRequest == 40)
			{
				serverLogic.gameExit(token);
				serverLogic.logout(token);
				
				break;
			}
			
			try 
			{
				read_socket = reader_on_socket.readLine();
			} 
			catch (IOException e1) 
			{
				
			}
			
			if(read_socket != null)
			{
				System.out.println("MESSAGGIO CLIENT ->" + read_socket);
				command = ServerMessageBroker.manageMessageType(read_socket);
				System.out.println("COMMAND ->" + command);
				//in this point there is a call to another static method for split and check a new string
				
				try
				{
					// TODO: gestione connessioni null quando client si disconnette
					synchronized(writer_on_socket)
					{
						//control of login parameters 
						if(command.compareTo("creaUtente")==0)
						{
							String[] parameters = ServerMessageBroker.manageReceiveMessageSplit(read_socket);
							
							writer_on_socket.write(serverLogic.add_new_user(parameters[0], parameters[1]));
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						else if (command.compareTo("login")==0)
						{
							String[] parameters = ServerMessageBroker.manageReceiveMessageSplit(read_socket);
							
							String login = serverLogic.login(parameters[0], parameters[1]);
							this.setToken(ClientMessageBroker.manageLogin(login)[0]);
							writer_on_socket.write(login);
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						else if (command.compareTo("creaRazza") == 0)
						{	
							String[] parameters = ServerMessageBroker.manageReceiveMessageSplit(read_socket);
							
							writer_on_socket.write(serverLogic.addNewSpecies(parameters[0], parameters[1], parameters[2]));
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						else if (command.compareTo("accessoPartita") == 0)
						{
							String token = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[0];
							
							String msg = serverLogic.gameAccess(token);
							
							if(ClientMessageBroker.checkMessage(msg))
								this.setIsInGame(true);
								
							writer_on_socket.write(msg);
							writer_on_socket.newLine();				
							writer_on_socket.flush();
							
							if(token.equals(serverLogic.getTokenOfCurrentPlayer()))
								serverLogic.changeRoundNotify();
						}
						else if (command.compareTo("uscitaPartita") == 0)
						{
							String token = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[0];
							
							String msg = serverLogic.gameExit(token);
							if(ClientMessageBroker.checkMessage(msg))
								this.setIsInGame(false);
							
							writer_on_socket.write(msg);
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						else if (command.compareTo("listaGiocatori") == 0)
						{
							String token = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[0];
							
							writer_on_socket.write(serverLogic.playerList(token));
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						else if (command.compareTo("classifica") == 0)
						{
							String token = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[0];
							
							writer_on_socket.write(serverLogic.ranking(token));
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						else if (command.compareTo("logout") == 0)
						{
							String token = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[0];
							
							String msg = serverLogic.logout(token);
							if(ClientMessageBroker.checkMessage(msg))
								this.setIsInGame(false);
								
							writer_on_socket.write(msg);
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						
						// Comandi in partita(Informazioni)
						
						else if(command.compareTo("mappaGenerale") == 0)
						{
							String token = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[0];
							
							writer_on_socket.write(serverLogic.generalMap(token));
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						else if(command.compareTo("listaDinosauri") == 0)
						{
							String token = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[0];
							
							writer_on_socket.write(serverLogic.dinosaursList(token));
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						else if(command.compareTo("vistaLocale") == 0)
						{
							String token = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[0];
							String dinoId = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[1];
							
							writer_on_socket.write(serverLogic.dinoZoom(token, dinoId));
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						else if(command.compareTo("statoDinosauro") == 0)
						{
							String token = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[0];
							String dinoId = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[1];
							
							writer_on_socket.write(serverLogic.dinoState(token, dinoId));
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						
						// End Comandi in partita(Informazioni)
						
						// Comandi in partita(Azioni)
						
						else if(command.compareTo("muoviDinosauro") == 0)
						{
							String token = ServerMessageBroker.manageDinoMovement(read_socket)[0];
							String dinoId = ServerMessageBroker.manageDinoMovement(read_socket)[1];
							String rowDest = ServerMessageBroker.manageDinoMovement(read_socket)[2];
							String colDest = ServerMessageBroker.manageDinoMovement(read_socket)[3];
							
							writer_on_socket.write(serverLogic.dinoMove(token, dinoId, rowDest, colDest));
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						else if(command.compareTo("crescitaDinosauro") == 0)
						{
							String token = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[0];
							String dinoId = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[1];
							
							writer_on_socket.write(serverLogic.dinoGrowUp(token, dinoId));
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						else if(command.compareTo("deponiUovo") == 0)
						{
							String token = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[0];
							String dinoId = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[1];
							
							writer_on_socket.write(serverLogic.dinoNewEgg(token, dinoId));
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
						
						// End Comandi in partita(Azioni)
						
						else if(command.compareTo("confermaTurno") == 0)
						{
							String token = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[0];
							
							writer_on_socket.write(serverLogic.roundConfirm(token));
							writer_on_socket.newLine();				
							writer_on_socket.flush();
							
							serverLogic.changeRoundNotify();
						}
						else if(command.compareTo("passaTurno") == 0)
						{
							String token = ServerMessageBroker.manageReceiveMessageSplit(read_socket)[0];
							
							writer_on_socket.write(serverLogic.playerRoundSwitch(token));
							writer_on_socket.newLine();				
							writer_on_socket.flush();
							
							if(token.equals(serverLogic.getTokenOfCurrentPlayer()))
								serverLogic.changeRoundNotify();
						}
						else
						{
							writer_on_socket.write("@" + command);
							writer_on_socket.newLine();				
							writer_on_socket.flush();
						}
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			
				read_socket = null;
			}
			else
			{
				timeoutRequest++;
			}
		}
	}

	public boolean sendChangeRound(String msg)
	{
		try
		{
			synchronized(writer_on_socket)
			{
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				return true;
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @return Token del Player a cui corrisponde il Client
	 */
	public String getToken()
	{
		return token;
	}
	
	public void setToken(String token)
	{
		this.token = token;
	}
	
	/**
	 * @return true se il Player corrispondente a questo Client è in partita, false altrimenti
	 */
	public boolean getIsInGame()
	{
		return isInGame;
	}
	
	public void setIsInGame(boolean isInGame)
	{
		this.isInGame = isInGame;
	}
}
