package Server;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.Any;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.TypeCode;
import org.omg.Dynamic.Parameter;
import org.omg.IOP.ServiceContext;
import org.omg.PortableInterceptor.InvalidSlot;
import org.omg.PortableInterceptor.ServerRequestInfoOperations;

import Client.ClientRMIInterface;;

public class ServerRMI  extends UnicastRemoteObject implements ServerRMIInterface 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServerLogic serverLogic;
	private String serverResponse;
	private Registry registro;
	private ArrayList<String> usernameClient;
	private ArrayList<ClientManagerRMI> client;
	private String serverIp;
	private String serverPort;
	private Server server;

	public ServerRMI(ServerLogic sv, String serverIp, String serverPort, Server s) throws RemoteException
	{
		super();
		registro = LocateRegistry.createRegistry(1999);;
		this.server = s;
		usernameClient = new ArrayList<String>();
		client = new ArrayList<ClientManagerRMI>();
		serverLogic = sv;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
	}

	@Override
	public synchronized String creaUtente(String username, String password) throws RemoteException 
	{
		serverResponse = serverLogic.add_new_user(username, password);
		
		return serverResponse;
	}

	@Override
	public synchronized String login(String username, String password) throws RemoteException 
	{
		serverResponse = serverLogic.login(username, password);
		
		return serverResponse;
	}

	@Override
	public synchronized String creaRazza(String token, String name, String type) throws RemoteException 
	{
		serverResponse = serverLogic.addNewSpecies(token, name, type);
		
		return serverResponse;
	}

	@Override
	public synchronized String accessoPartita(String token) throws RemoteException 
	{
		serverResponse = serverLogic.gameAccess(token);
		
		return serverResponse;
	}

	@Override
	public synchronized String uscitaPartita(String token) throws RemoteException
	{
		serverResponse = serverLogic.gameAccess(token);
		
		return serverResponse;
	}

	@Override
	public synchronized String[] listaGiocatori(String token) throws RemoteException 
	{
		serverResponse = serverLogic.playerList(token);
		
		return RMIMessageBroker.convertPlayerList(serverResponse);
	}

	@Override
	public synchronized ArrayList<String> classifica(String token) throws RemoteException 
	{
		serverResponse = serverLogic.ranking(token);
		
		return RMIMessageBroker.convertRanking(serverResponse);
	}

	@Override
	public synchronized String logout(String token) throws RemoteException 
	{
		serverResponse = serverLogic.logout(token);
		
		return serverResponse;
	}

	@Override
	public synchronized ArrayList<String> mappaGenerale(String token) throws RemoteException 
	{
		serverResponse = serverLogic.generalMap(token);
		
		return RMIMessageBroker.convertGeneralMap(serverResponse);
	}

	@Override
	public synchronized String[] listaDinosauri(String token) throws RemoteException 
	{
		serverResponse = serverLogic.dinosaursList(token);
		
		return RMIMessageBroker.convertDinoList(serverResponse);
	}

	@Override
	public synchronized ArrayList<String> vistaLocale(String token, String dinoId) throws RemoteException 
	{
		serverResponse = serverLogic.dinoZoom(token, dinoId);
		
		return RMIMessageBroker.convertDinoZoom(serverResponse);
	}

	@Override
	public synchronized String[] statoDinosauro(String token, String dinoId) throws RemoteException 
	{
		serverResponse = serverLogic.dinoState(token, dinoId);
		
		return RMIMessageBroker.convertDinoState(serverResponse);
	}

	@Override
	public synchronized String muoviDinosauro(String token, String dinoId, String row, String col) throws RemoteException 
	{
		serverResponse = serverLogic.dinoMove(token, dinoId, row, col);
		
		return serverResponse;
	}

	@Override
	public synchronized String cresciDinosauro(String token, String dinoId) throws RemoteException 
	{
		serverResponse = serverLogic.dinoGrowUp(token, dinoId);
		
		return serverResponse;
	}

	@Override
	public synchronized String deponiUovo(String token, String dinoId) throws RemoteException 
	{
		serverResponse = serverLogic.dinoNewEgg(token, dinoId);
		
		return serverResponse;
	}

	@Override
	public synchronized String confermaTurno(String token) throws RemoteException 
	{
		serverResponse = serverLogic.roundConfirm(token);
		
		return serverResponse;
	}

	@Override
	public synchronized String passaTurno(String token) throws RemoteException 
	{
		serverResponse = serverLogic.playerRoundSwitch(token);
		
		return serverResponse;
	}
	
	@Override
	public synchronized void notifyLogin(String username) throws RemoteException
	{
		server.addClientRMI(username);
	}
	
	@Override
	public synchronized void notifyLogout(String username) throws RemoteException
	{
		server.removeClientRMI(username);
	}

	@Override
	public void setGameAccess(boolean isInGame, String username) throws RemoteException
	{
		server.setGameAccessRMI(username, isInGame);
	}

	@Override
	public String getTokenOfCurrentPlayer() throws RemoteException 
	{
		return serverLogic.getTokenOfCurrentPlayer();
	}

	@Override
	public void changeRoundNotify() throws RemoteException 
	{
		serverLogic.changeRoundNotify();
	}

	@Override
	public String getUsernameOfCurrentPlayer() throws RemoteException 
	{
		return serverLogic.getuUsernameOfCurrentPlayer();
	}

	@Override
	public void registryClient(String username, String address, ClientRMIInterface client) throws RemoteException 
	{
		try {
			Naming.bind("rmi://127.0.0.1/" + username + ":1999",(Remote) client);
			//Naming.bind("rmi://" + address + "/" + username + ":1999",(Remote) client);
			//registro.rebind("rmi://127.0.0.1/server:1999",(Remote) new Server());
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Naming.rebind("rmi://127.0.0.1/" + username + ":1999",(Remote) client);
			System.out.println("Client RMI Avviato!");
			notifyLogin(username);
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
