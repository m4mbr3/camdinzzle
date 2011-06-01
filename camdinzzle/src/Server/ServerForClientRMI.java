package Server;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;

public class ServerForClientRMI implements Runnable 
{
	private ServerLogic serverLogic;
	private String serverIp;
	private String serverName;
	private String serverPort;
	private boolean run;
	
	public ServerForClientRMI(ServerLogic serverLogic, String serverIp, String serverName, String serverPort)
	{
		Registry registro = null;
		ServerRMI cmRMI = null;
		this.serverIp = serverIp;
		this.serverName = serverName;
		this.serverPort = serverPort;
		run =true;
		
		try
		{
			cmRMI = new ServerRMI(serverLogic);
		}
	
		catch(ExportException e)
		{
			System.out.println("Port already in use: 1099!!!");
		}
		catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			registro = LocateRegistry.createRegistry(Integer.parseInt(serverPort));
			Naming.bind("rmi://" + serverIp + "/" + serverName + ":" + serverPort,(Remote) cmRMI);
			//registro.rebind("rmi://127.0.0.1/server:1999",(Remote) new Server());
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
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
			Naming.rebind("rmi://" + serverIp + "/" + serverName + ":" + serverPort,(Remote) cmRMI);
			System.out.println("Server RMI Avviato!");
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
	
	public void stop()
	{
		run = false;
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		while(run)
		{
			
		}
	}

}
