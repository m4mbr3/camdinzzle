package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {

	/**
	 * @param args
	 */
	//Creation of a box for login
	private ServerSocket server;
	private Socket new_connection;
	private ServerLogic serverLogic;
	private boolean is_run;
	private int port;
	private ClientManagerSocket clientManagerSocket;
	private ArrayList<ClientManager> clientList;
		// TODO Auto-generated constructor stub
		
	public Server(int port, ServerLogic serverLogic)
	{
			this.port = port;
			try {
				this.server = new ServerSocket(this.port) ;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.serverLogic = serverLogic;
			clientList = new ArrayList<ClientManager>();
			this.new_connection = null;
			this.is_run = true;
			this.clientManagerSocket=null;
	}
	
	public void stop()
	{
		is_run = false;
	}
	
	public void run()
	{
		System.out.println("<<SERVER DAEMON>>--STARTED");
		while(is_run)
		{
			try {
				//waiting for connection
				System.out.println("<<SERVER DAEMON>>--WAITING FOR CONNECTIONS at "+ server.getLocalPort());
				new_connection = server.accept();
				System.out.println("<<SERVER DAEMON>>--CONNECTION INTERCEPTED");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clientManagerSocket = new ClientManagerSocket(new_connection,serverLogic);
			clientList.add(clientManagerSocket);
			System.out.println("<<SERVER DAEMON>>--STARTING EXECUTION LOGIN");
			(new Thread(clientManagerSocket)).start();
			System.out.println("<<SERVER DAEMON>>--EXECUTION LOGIN STARTED");
		}
		
	}
	
	public static void sendBroadcastMessage(String msg)
	{
		//TODO: ciclo che manda a tutti i Client connessi il messaggio di notifica del turno
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerLogic serverLogic = new ServerLogic();
		
		int port = 4567;
		(new Thread(new Server(port,serverLogic))).start();
		
	}

	
}
