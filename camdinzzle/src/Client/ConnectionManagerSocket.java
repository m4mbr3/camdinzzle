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
public class ConnectionManagerSocket implements ConnectionManager, Runnable  {
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
	
	public ConnectionManagerSocket(int port, String address, String username, String password, MonitorMessage mm, Socket soc)
	{
		// TODO Auto-generated constructor stub
		this.mm = mm;
		this.address = address;
		token = "";
		this.username = username;
		this.password = password;
		this.port = port;
		command = new String();
		connection_with_server = soc;
		run = true;
		
		try {
			writer_on_socket = new BufferedWriter(new OutputStreamWriter(connection_with_server.getOutputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("<<CONN MANAGER>>--STARTING THREAD " );
		(new Thread(this)).start();
		System.out.println("<<CONN MANAGER>>--THREAD STARTED");
	}

	public void stop()
	{
		run = false;
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub	
		while(run)
		{
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// TODO: scelta gestione comandoNonValido
	public String sendMessage(String msg)
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
	
	public synchronized String creaUtente(String username, String password) 
	{
		String msg = ClientMessageBroker.createUser(username, password);
		
		String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
		mm.setMessage("");
		
		System.out.println(retStr);
		
		return retStr;
	}
	
	@Override
	public synchronized String login(String username, String password) 
	{
		String msg = ClientMessageBroker.createLogin(username, password);
		
		String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
		mm.setMessage("");
		
		if(ClientMessageBroker.manageLogin(retStr)[0].equals("ok"))
			token = ClientMessageBroker.manageLogin(retStr)[1];
		
		System.out.println(retStr);
		
		return retStr;
	}

	@Override
	public synchronized String creaRazza(String name, String type) 
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
	public synchronized String accessoPartita() 
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
	public synchronized String uscitaPartita() 
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
	public synchronized String[] listaGiocatori()
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
	public synchronized ArrayList<String> classifica() 
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
	public synchronized String logout() 
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createLogout(token);
			
			String retStr = String.copyValueOf(sendMessage(msg).toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			return retStr;
		}
		else
			return null;
	}
	
	@Override
	public synchronized ArrayList<String> mappaGenerale()
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
	public synchronized String[] listaDinosauri() 
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
	public synchronized ArrayList<String> vistaLocale(String dinoId) 
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
	public synchronized String[] statoDinosauro(String dinoId)
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
	public synchronized String[] muoviDinosauro(String dinoId, String row, String col) 
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
	public synchronized String[] cresciDinosauro(String dinoId) 
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
	public synchronized String[] deponiUovo(String dinoId) 
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
	public synchronized String[] confermaTurno() 
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
	public synchronized String[] passaTurno() 
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
	
}
	
