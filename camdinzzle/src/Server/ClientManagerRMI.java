/**
 * 
 */
package Server;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Andrea
 *
 */
public class ClientManagerRMI extends UnicastRemoteObject implements ServerRMIInterface {

	ServerLogic serverLogic;

	public ClientManagerRMI(ServerLogic sv) throws RemoteException
	{
		super();
		serverLogic = sv;
	}

	@Override
	public String add_new_user(String username, String password)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String login(String username, String password)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addNewSpecies(String name, String type)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String gameAccess() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String gameExit() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String playerList() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String ranking() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String logout() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generalMap() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dinosaursList() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dinoZoom(String dinoId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dinoState(String dinoId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dinoMove(String dinoId, String row, String col)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dinoGrowUp(String dinoId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dinoNewEgg(String dinoId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String roundConfirm() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String playerRoundSwitch() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeRound() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
