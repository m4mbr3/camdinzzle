/**
 * 
 */
package Client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionManagerSocket implements ConnectionManager 
{
	
	private Socket connection_with_server;
	private BufferedWriter writer_on_socket;
	private String username;
	private String token;
	private MonitorMessage mm;
	private ClientListener clientListener;
	private String changeRound;
	
	public ConnectionManagerSocket(int port, String address, MonitorMessage mm) throws IOException
	{
		// TODO Auto-generated constructor stub
		this.mm = mm;
		changeRound = "";
 		token = "";
		connection_with_server = new Socket(address,port);
		clientListener = new ClientListener(mm, this.connection_with_server, this);
		
		writer_on_socket = new BufferedWriter(new OutputStreamWriter(connection_with_server.getOutputStream()));
		
	}
	@Override
	public void setChangeRound(String msg)
	{
		this.changeRound = msg;
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
					return null;
				}
			}
			
			if(mm.getMessage().equals("null"))
			{
				return null;
			}
			return mm.getMessage();
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			return null;
		} 
	}
	
	public String getChangeRound() 
	{
		return changeRound;
	}

	public synchronized String creaUtente(String username, String password) 
	{
		String msg = ClientMessageBroker.createUser(username, password);
		String writerSuccess = sendMessage(msg);
		
		if(writerSuccess == null)
		{
			return null;
		}
		
		String retStr = String.copyValueOf(writerSuccess.toCharArray());
		mm.setMessage("");
		
		System.out.println(retStr);
		
		return retStr;
	}
	
	@Override
	public synchronized String login(String username, String password) 
	{
		String msg = ClientMessageBroker.createLogin(username, password);
		String writerSuccess = sendMessage(msg);
		if(writerSuccess == null)
		{
			return null;
		}
		String retStr = String.copyValueOf(writerSuccess.toCharArray());
		String[] login =  ClientMessageBroker.manageLogin(retStr);
		mm.setMessage("");
		
		if(login != null)
		{
			if(login[0].equals("ok"))
			{
				token = ClientMessageBroker.manageLogin(retStr)[1];
				this.username = username;
			}
		}	
		System.out.println(retStr);
		
		return retStr;
	}

	@Override
	public synchronized String creaRazza(String name, String type) 
	{
		if(!token.equals(""))
		{
			String msg = ClientMessageBroker.createRace(token, name, type);
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			clientListener.stop();
			
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
				
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
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
			String writerSuccess = sendMessage(msg);
			
			if(writerSuccess == null)
			{
				return null;
			}
			
			String retStr = String.copyValueOf(writerSuccess.toCharArray());
			mm.setMessage("");
			
			System.out.println(retStr);
			
			mm.setMessage("");				
			
			return ClientMessageBroker.managePlayerChangeRound(retStr);
		}
		else
			return null;
	}	
	
	public void stopClientListener() 
	{
		clientListener.stop();
	}

	@Override
	public String getToken() {
		// TODO Auto-generated method stub
		return token;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public Client getClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClient(Client client) {
		// TODO Auto-generated method stub
		
	}

	
}
	
