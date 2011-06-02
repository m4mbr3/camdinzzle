/**
 * 
 */
package Client;

import Server.ServerRMIInterface;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Enumeration;


public class ConnectionManagerRMI implements ConnectionManager 
{
	private String username;
	private ServerRMIInterface server;
	private String token;
	private ClientRMI client;
	private String changeRound;
	
	private static final String port = "1999";
	
	public ConnectionManagerRMI(String address, String port, String serverName) throws Exception
	{
		this.username = "";
		changeRound = "";
		this.token = "";
		
		server = (ServerRMIInterface)Naming.lookup("rmi://" + address + "/" + serverName + ":" + port);
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
			return null;
		}
		
		return msg;
	}

	@Override
	public String login(String username, String password)
	{
		String msg = null;
		String ip = null;
		
		try 
		{
			msg = server.login(username, password);
			
			String[] response = ClientMessageBroker.manageLogin(msg);
			
			if(response[0].equals("ok"))
			{
				token = response[1];
				this.username = username;
				
				try{
					client = new ClientRMI(this);
				}
			
				catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// Cerca l'IP del client
				try 
				{
					for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) 
					{
						  NetworkInterface iface = ifaces.nextElement();
						  for (Enumeration<InetAddress> addresses = iface.getInetAddresses(); addresses.hasMoreElements();) 
						  {
							  InetAddress address = addresses.nextElement();
							  if (address instanceof Inet4Address) 
							  {
								  if(!address.getHostAddress().equals("127.0.0.1"))
								  {
									  ip = address.getHostAddress();
								  }
						    }
						  }
						}
				} 
				catch (SocketException e) 
				{
					System.out.println("IP del server non trovato");
				}
				// End ricerca IP del client
				
				try {
					
					@SuppressWarnings("unused")
					Registry registro = LocateRegistry.createRegistry(Integer.parseInt(port));
					//Naming.bind("rmi://127.0.0.1/" + username + ":1999",(Remote) client);
					Naming.bind("rmi://127.0.0.1/" + username + ":" + port,(Remote) client);
					//registro.rebind("rmi://127.0.0.1/server:1999",(Remote) new Server());
				} 
				catch (AlreadyBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (AccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Naming.rebind("rmi://127.0.0.1/" + username + ":" + port,(Remote) client);
					//Naming.bind("rmi://" + address + "/" + username + ":1999",(Remote) client);
					System.out.println("Client RMI Avviato!");
					// per andare sulla stessa macchina:
					// ip = "127.0.0.1";
					server.notifyLogin(username, ip);
				} catch (AccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
			return null;
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
				server.setGameAccess(true, username);
				
				if(token.equals(server.getTokenOfCurrentPlayer()))
				{
					server.changeRoundNotify();
				}
				else if(server.getTokenOfCurrentPlayer() != "")
				{
					client.sendMessage("@cambioTurno," + server.getUsernameOfCurrentPlayer());
				}
			}
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
			if(ClientMessageBroker.manageGameExit(msg)[0].equals("ok"))
			{
				server.setGameAccess(false, username);
				
				if(token.equals(server.getTokenOfCurrentPlayer()))
				{
					server.changeRoundNotify();
				}
			}
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
			return null;
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
			return null;
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
			if(ClientMessageBroker.manageLogout(msg)[0].equals("ok"))
			{
				server.notifyLogout(username);
				client = null;
			}
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
			return null;
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
			return null;
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
			return null;
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
			return null;
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
			return null;
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
			return null;
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
			return null;
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
			return null;
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
			return null;
		}
		
		if(msg != null)
		{
			if(msg.equals("@ok"))
				try {
					server.changeRoundNotify();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return ClientMessageBroker.managePlayerChangeRound(msg);
		}
		return null;
	}

	@Override
	public String getChangeRound() 
	{
		return changeRound;
	}

	@Override
	public void setChangeRound(String msg) 
	{
		this.changeRound = msg;
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

	
}
