package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMIInterface extends Remote {

	public String add_new_user(String username, String password) throws RemoteException;;
	public String login(String username, String password) throws RemoteException;;
	public String addNewSpecies(String name, String type) throws RemoteException;;
	public String gameAccess() throws RemoteException;;
	public String gameExit() throws RemoteException;;
	public String playerList() throws RemoteException;;
	public String ranking() throws RemoteException;;
	public String logout() throws RemoteException;;
	public String generalMap() throws RemoteException;;
	public String dinosaursList() throws RemoteException;;
	public String dinoZoom(String dinoId) throws RemoteException;;
	public String dinoState(String dinoId) throws RemoteException;;
	public String dinoMove(String dinoId, String row, String col) throws RemoteException;;
	public String dinoGrowUp(String dinoId) throws RemoteException;;
	public String dinoNewEgg(String dinoId) throws RemoteException;;
	public String roundConfirm() throws RemoteException;;
	public String playerRoundSwitch() throws RemoteException;;
	public void changeRound() throws RemoteException;;
}
