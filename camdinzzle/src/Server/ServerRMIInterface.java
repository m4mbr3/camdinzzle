package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMIInterface extends Remote {

	public String creaUtente(String username, String password) throws RemoteException;
	public String login(String username, String password) throws RemoteException;
	public String creaRazza(String token, String name, String type) throws RemoteException;
	public String accessoPartita(String token) throws RemoteException;
	public String uscitaPartita(String token) throws RemoteException;
	public String listaGiocatori(String token) throws RemoteException;
	public String classifica(String token) throws RemoteException;
	public String logout(String token) throws RemoteException;
	public String mappaGenerale(String token) throws RemoteException;
	public String listaDinosauri(String token) throws RemoteException;
	public String vistaLocale(String token, String dinoId) throws RemoteException;
	public String statoDinosauro(String token, String dinoId) throws RemoteException;
	public String muoviDinosauro(String token, String dinoId, String row, String col) throws RemoteException;
	public String cresciDinosauro(String token, String dinoId) throws RemoteException;
	public String deponiUovo(String token, String dinoId) throws RemoteException;
	public String confermaTurno(String token) throws RemoteException;
	public String passaTurno(String token) throws RemoteException;
}
