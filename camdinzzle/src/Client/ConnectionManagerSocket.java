/**
 * 
 */
package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.management.monitor.Monitor;
import javax.swing.JFrame;

/**
 * @author Andrea
 *
 */
public class ConnectionManagerSocket implements ConnectionManager {
	//Class for client logging
	/**
	 * Socket for Managing the connection by socket with server
	 */
	private Socket connection_with_server;
	private BufferedWriter writer_on_socket;
	private String address;
	private String username;
	private String password;
	private String token;
	private int port;
	private String command;
	private MonitorMessage mm;
	private boolean run;
	private ClientListener clientListener;
	
	public ConnectionManagerSocket(int port, String address, MonitorMessage mm) throws IOException
	{
		// TODO Auto-generated constructor stub
		this.mm = mm;
		this.address = address;
		token = "";
		
		this.port = port;
		command = new String();
		connection_with_server = new Socket(address,port);
		clientListener = new ClientListener(mm, this.connection_with_server);
		run = true;
		
		writer_on_socket = new BufferedWriter(new OutputStreamWriter(connection_with_server.getOutputStream()));
		
	}
	
	// TODO: scelta gestione comandoNonValido
	public String sendMessage(String msg) throws ChangeRoundException, IsMyRoundException
	{
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			while(mm.getMessage().equals(""))
			{
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return mm.getMessage();
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			
			e.printStackTrace();
			return null;
		} 
	}
	
	public synchronized String creaUtente(String username, String password) throws ChangeRoundException, IsMyRoundException
	{
		String msg = ClientMessageBroker.createUser(username, password);
		
		String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
		mm.setMessage("");
		
		System.out.println(retStr);
		
		return retStr;
	}
	
	@Override
	public synchronized String login(String username, String password) throws ChangeRoundException, IsMyRoundException
	{
		String msg = ClientMessageBroker.createLogin(username, password);
		
		String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
		mm.setMessage("");
		
		if(ClientMessageBroker.manageLogin(retStr)[0].equals("ok"))
		{
			token = ClientMessageBroker.manageLogin(retStr)[1];
			this.username = username;
			clientListener.setUsername(this.username);
		}
		
		System.out.println(retStr);
		
		return retStr;
	}

	@Override
	public synchronized String creaRazza(String name, String type) throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			
			String msg = ClientMessageBroker.createRace(token, name, type);
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			return retStr;
		}
		else
			return null;
	}

	@Override
	public synchronized String accessoPartita() throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createGameAccess(token);
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			return retStr;		
		}
		else
			return null;
	}

	@Override
	public synchronized String uscitaPartita() throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createGameExit(token);	
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			return retStr;
		}
		else
			return null;
	}

	@Override
	public synchronized String[] listaGiocatori() throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createPlayerList(token);
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			return ClientMessageBroker.managePlayerList(retStr);
		}
		else
			return null;
	}
	
	@Override
	public synchronized ArrayList<String> classifica() throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createRanking(token);
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			return ClientMessageBroker.manageRanking(retStr);
		}
		else
			return null;
	}
	
	@Override
	public synchronized String logout() throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createLogout(token);
			
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			clientListener.stop();
			
			return retStr;
		}
		else
			return null;
	}
	
	@Override
	public synchronized ArrayList<String> mappaGenerale() throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createGeneralMap(token);
			
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			return ClientMessageBroker.manageGeneralMap(retStr);
		}
		else
			return null;
	}
	
	@Override
	public synchronized String[] listaDinosauri() throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createDinoList(token);
			
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			return ClientMessageBroker.manageDinoList(retStr);
		}
		else
			return null;
	}
	
	@Override
	public synchronized ArrayList<String> vistaLocale(String dinoId) throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createDinoZoom(token, dinoId);
			
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			return ClientMessageBroker.manageDinoZoom(retStr);
		}
		else
			return null;
	}
	
	@Override
	public synchronized String[] statoDinosauro(String dinoId) throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createDinoState(token, dinoId);
			
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			return ClientMessageBroker.manageDinoState(retStr);
		}
		else
			return null;
	}
	
	@Override
	public synchronized String[] muoviDinosauro(String dinoId, String row, String col) throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createDinoMove(token, dinoId, row, col);
			
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			mm.setMessage("");
			
			return ClientMessageBroker.manageDinoMove(retStr);
		}
		else
			return null;
	}
	
	@Override
	public synchronized String[] cresciDinosauro(String dinoId) throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createDinoGrowUp(token, dinoId);
			
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			mm.setMessage("");
			
			return ClientMessageBroker.manageDinoGrowUp(retStr);
		}
		else
			return null;
	}
	
	@Override
	public synchronized String[] deponiUovo(String dinoId) throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{	
			String msg = ClientMessageBroker.createNewEgg(token, dinoId);
				
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			mm.setMessage("");

			return ClientMessageBroker.manageNewEgg(retStr);
		}
		else
			return null;
	}
	
	@Override
	public synchronized String[] confermaTurno() throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createRoundConfirmation(token);
			
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			mm.setMessage("");
			
			return ClientMessageBroker.manageRoundConfirm(retStr);
		}
		else
			return null;
	}
	
	@Override
	public synchronized String[] passaTurno() throws ChangeRoundException, IsMyRoundException
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createPassOffRound(token);
			
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			mm.setMessage("");				
			
			return ClientMessageBroker.managePlayerChangeRound(retStr);
		}
		else
			return null;
	}	
	
	public void stopClientListener() throws ChangeRoundException, IsMyRoundException
	{
		clientListener.stop();
	}
}
	
