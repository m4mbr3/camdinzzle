package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRMIInterface extends Remote 
{
	public boolean sendMessage(String msg) throws RemoteException;
	public boolean getIsInGame() throws RemoteException;
	public void setInGame(boolean isInGame) throws RemoteException;
}
