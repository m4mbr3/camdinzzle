package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

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

	public ServerRMI(ServerLogic sv) throws RemoteException
	{
		super();
		serverLogic = sv;
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
		serverResponse = serverLogic.gameExit(token);
		
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
	
	public synchronized void notifyGameAccess(ClientRMIInterface client) throws RemoteException
	{
		serverLogic.notifyGameAccess(client);
	}
}
