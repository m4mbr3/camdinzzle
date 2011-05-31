/**
 * 
 */
package Client;

import Server.ServerLogic;
import Server.ServerRMIInterface;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;



/**
 * @author Andrea
 *
 */
public class ConnectionManagerRMI implements ConnectionManager 
{
	private String username;
	private String address;
	private String serverName;
	private String port;
	private ServerRMIInterface server;
	private String token;
	private ClientRMI client;
	
	public ConnectionManagerRMI(String address, String port, String serverName)
	{
		this.username = "";
		this.address = address;
		this.serverName = serverName;
		this.port = port;
		this.token = "";
		
		try {
			server = (ServerRMIInterface)Naming.lookup("rmi://" + address + "/" + serverName + ":" + port);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String creaUtente(String username, String password) 
	{
		String msg = null;
		
		try 
		{
			msg = server.creaUtente(username, password);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

	@Override
	public String login(String username, String password)
	{
		String msg = null;
		
		try 
		{
			msg = server.login(username, password);
			
			String[] response = ClientMessageBroker.manageLogin(msg);
			
			if(response[0].equals("ok"))
			{
				token = response[1];
				this.username = username;
			}
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

	@Override
	public String creaRazza(String name, String type) 
	{
		String msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.creaRazza(token, name, type);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

	@Override
	public String accessoPartita() 
	{
		String msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.accessoPartita(token);
			
			if(ClientMessageBroker.manageGameAccess(msg)[0].equals("ok"))
			{
				try {
					client = new ClientRMI(this.address, this.username);
					server.notifyGameAccess(client);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

	@Override
	public String uscitaPartita() 
	{
		String msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.uscitaPartita(token);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

	@Override
	public String[] listaGiocatori() 
	{
		String[] msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.listaGiocatori(token);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

	@Override
	public ArrayList<String> classifica() 
	{
		ArrayList<String> msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.classifica(token);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

	@Override
	public String logout() 
	{
		String msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.logout(token);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

	@Override
	public ArrayList<String> mappaGenerale() 
	{
		ArrayList<String> msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.mappaGenerale(token);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

	@Override
	public String[] listaDinosauri() 
	{
		String[] msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.listaDinosauri(token);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

	@Override
	public ArrayList<String> vistaLocale(String dinoId) 
	{
		ArrayList<String> msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.vistaLocale(token, dinoId);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

	@Override
	public String[] statoDinosauro(String dinoId) 
	{
		String[] msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.statoDinosauro(token, dinoId);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

	@Override
	public String[] muoviDinosauro(String dinoId, String row, String col) 
	{
		String msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.muoviDinosauro(token, dinoId, row, col);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(msg != null)
		{
			return ClientMessageBroker.manageDinoMove(msg);
		}
		return null;
	}

	@Override
	public String[] cresciDinosauro(String dinoId) 
	{
		String msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.cresciDinosauro(token, dinoId);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(msg != null)
		{
			return ClientMessageBroker.manageDinoGrowUp(msg);
		}
		return null;
	}

	@Override
	public String[] deponiUovo(String dinoId) 
	{
		String msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.deponiUovo(token, dinoId);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(msg != null)
		{
			return ClientMessageBroker.manageNewEgg(msg);
		}
		return null;
	}

	@Override
	public String[] confermaTurno() 
	{
		String msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.confermaTurno(token);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(msg != null)
		{
			return ClientMessageBroker.manageRoundConfirm(msg);
		}
		return null;
	}

	@Override
	public String[] passaTurno() 
	{
		String msg = null;
		
		try 
		{
			if(!token.equals(""))
				msg = server.passaTurno(token);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(msg != null)
		{
			return ClientMessageBroker.managePlayerChangeRound(msg);
		}
		return null;
	}

	
}
