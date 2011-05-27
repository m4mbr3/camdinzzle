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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ServerLogic serverLogic;

	public ClientManagerRMI(ServerLogic sv) throws RemoteException
	{
		super();
		serverLogic = sv;
	}
	
	@Override
	public synchronized String add_new_user(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return "ciao";
	}

	@Override
	public String login(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addNewSpecies(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String gameAccess(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String gameExit(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String playerList(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String ranking(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String logout(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generalMap(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dinosaursList(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dinoZoom(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dinoState(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dinoMove(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dinoGrowUp(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dinoNewEgg(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String roundConfirm(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String playerRoundSwitch(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeRound() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	

}
