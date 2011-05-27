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
	ArrayList<String> requestQueue;
	
	public ConnectionManagerSocket(int port, String address, String username, String password, MonitorMessage mm, Socket soc,
			ArrayList<String> requestQueue)
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
		this.requestQueue = requestQueue;
		
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

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub	
		while(true)
		{
			System.out.print("");
		}
	}	
	
	public synchronized String creaUtente(String username, String password) 
	{
		try
		{
			String msg = ClientMessageBroker.createUser(username, password);
			
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			while(mm.getMessage().equals(""))
			{
				System.out.print("");
			}
			
			String retStr = String.copyValueOf(mm.getMessage().toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			requestQueue.remove("creaUtente");
			
			return retStr;
			
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
	
	@Override
	public synchronized String login(String username, String password) 
	{
		try
		{
			String msg = ClientMessageBroker.createLogin(username, password);
			
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			while(mm.getMessage().equals(""))
			{
				System.out.print("");
			}
			
			String retStr = String.copyValueOf(mm.getMessage().toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			requestQueue.remove("login");
			
			if(ClientMessageBroker.checkMessage(retStr))
				token = ClientMessageBroker.manageLogin(retStr)[0];
			
			return retStr;
			
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

	@Override
	public synchronized String creaRazza(String name, String type) 
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createRace(token, name, type);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				
				System.out.println(retStr);
				
				requestQueue.remove("creaRazza");
				
				return retStr;
				
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
		else
			return null;
	}

	@Override
	public synchronized String accessoPartita() 
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createGameAccess(token);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				
				System.out.println(retStr);
				
				requestQueue.remove("accessoPartita");
				
				return retStr;
				
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
		else
			return null;
	}

	@Override
	public synchronized String uscitaPartita() 
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createGameExit(token);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				
				System.out.println(retStr);
				
				requestQueue.remove("uscitaPartita");
				
				return retStr;
				
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
		else
			return null;
	}

	@Override
	public synchronized String[] listaGiocatori()
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createPlayerList(token);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				
				System.out.println(retStr);
				
				requestQueue.remove("listaGiocatori");
				
				return ClientMessageBroker.managePlayerList(retStr);
				
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
		else
			return null;
	}
	
	@Override
	public synchronized ArrayList<String> classifica() 
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createRanking(token);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				
				System.out.println(retStr);
				
				requestQueue.remove("classifica");
				
				return ClientMessageBroker.manageRanking(retStr);
				
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
		else
			return null;
	}
	
	@Override
	public synchronized String logout() 
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createLogout(token);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				
				System.out.println(retStr);
				
				requestQueue.remove("logout");
				
				return retStr;
				
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
		else
			return null;
	}
	
	@Override
	public synchronized ArrayList<String> mappaGenerale()
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createGeneralMap(token);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				
				System.out.println(retStr);
				
				requestQueue.remove("mappaGenerale");
				
				return ClientMessageBroker.manageGeneralMap(retStr);
				
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
		else
			return null;
	}
	
	@Override
	public synchronized String[] listaDinosauri() 
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createDinoList(token);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				
				System.out.println(retStr);
				
				requestQueue.remove("listaDinosauri");
				
				return ClientMessageBroker.manageDinoList(retStr);
				
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
		else
			return null;
	}
	
	@Override
	public synchronized ArrayList<String> vistaLocale(String dinoId) 
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createDinoZoom(token, dinoId);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				
				System.out.println(retStr);
				
				requestQueue.remove("vistaLocale");
				
				return ClientMessageBroker.manageDinoZoom(retStr);
				
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
		else
			return null;
	}
	
	@Override
	public synchronized String[] statoDinosauro(String dinoId)
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createDinoState(token, dinoId);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				
				System.out.println(retStr);
				
				requestQueue.remove("statoDinosauro");
				
				return ClientMessageBroker.manageDinoState(retStr);
				
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
		else
			return null;
	}
	
	@Override
	public synchronized String[] muoviDinosauro(String dinoId, String row, String col) 
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createDinoMove(token, dinoId, row, col);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				requestQueue.remove("muoviDinosauro");
				
				return ClientMessageBroker.manageDinoMove(retStr);
				
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
		else
			return null;
	}
	
	@Override
	public synchronized String[] cresciDinosauro(String dinoId) 
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createDinoGrowUp(token, dinoId);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				requestQueue.remove("cresciDinosauro");
				
				return ClientMessageBroker.manageDinoGrowUp(retStr);
				
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
		else
			return null;
	}
	
	@Override
	public synchronized String[] deponiUovo(String dinoId) 
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createNewEgg(token, dinoId);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				requestQueue.remove("deponiUovo");

				return ClientMessageBroker.manageNewEgg(retStr);
				
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
		else
			return null;
	}
	
	@Override
	public synchronized String[] confermaTurno() 
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createRoundConfirmation(token);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				requestQueue.remove("confermaTurno");
				
				return ClientMessageBroker.manageRoundConfirm(retStr);
				
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
		else
			return null;
	}
	
	@Override
	public synchronized String[] passaTurno() 
	{
		if(!token.equals(""))
		{
			try
			{
				String msg = ClientMessageBroker.createPassOffRound(token);
				
				writer_on_socket.write(msg);
				writer_on_socket.newLine();				
				writer_on_socket.flush();
				
				while(mm.getMessage().equals(""))
				{
					System.out.print("");
				}
				
				String retStr = String.copyValueOf(mm.getMessage().toCharArray());
				mm.setMessage("");
				
				System.out.println(retStr);
				
				requestQueue.remove("passaTurno");
				
				return ClientMessageBroker.managePlayerChangeRound(retStr);
				
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
		else
			return null;
	}	
	
}
	
