package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRMIInterface extends Remote 
{
	public boolean sendMessage(String msg) throws RemoteException;
}
