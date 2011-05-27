package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMIInterface extends Remote {

	public String add_new_user(String msg) throws RemoteException;;
	public String login(String msg) throws RemoteException;;
	public String addNewSpecies(String msg) throws RemoteException;;
	public String gameAccess(String msg) throws RemoteException;;
	public String gameExit(String msg) throws RemoteException;;
	public String playerList(String msg) throws RemoteException;;
	public String ranking(String msg) throws RemoteException;;
	public String logout(String msg) throws RemoteException;;
	public String generalMap(String msg) throws RemoteException;;
	public String dinosaursList(String msg) throws RemoteException;;
	public String dinoZoom(String msg) throws RemoteException;;
	public String dinoState(String msg) throws RemoteException;;
	public String dinoMove(String msg) throws RemoteException;;
	public String dinoGrowUp(String msg) throws RemoteException;;
	public String dinoNewEgg(String msg) throws RemoteException;;
	public String roundConfirm(String msg) throws RemoteException;;
	public String playerRoundSwitch(String msg) throws RemoteException;;
	public void changeRound() throws RemoteException;;
}
