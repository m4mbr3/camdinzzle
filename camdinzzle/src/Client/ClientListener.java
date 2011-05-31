package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Server.ClientManagerSocket;

public class ClientListener implements Runnable 
{
	private Socket connection_with_server;
	private BufferedReader reader_on_socket;
	private String readSocket;
	private MonitorMessage mm;
	private String username;
	private int timelineServerNull;
	private boolean run;
	
	public ClientListener(MonitorMessage mm, Socket soc)
	{
		// TODO Auto-generated constructor stub
		
		this.mm = mm;
		this.readSocket = null;
		connection_with_server = soc;
		
		timelineServerNull = 0;
		run = true;
		
		try {
			reader_on_socket = new BufferedReader( new InputStreamReader(this.connection_with_server.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("<<CONN MANAGER>>--STARTING THREAD " );
		(new Thread(this)).start();
		System.out.println("<<CONN MANAGER>>--THREAD STARTED");
	}

	public void stop()
	{
		run = false;
	}
	
	@Override
	public void run() 
	{				
		while(run)
		{
			readSocket = null;
			
			try 
			{
				readSocket = reader_on_socket.readLine();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(readSocket != null)
			{
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
				{
					changeRoundNotify(readSocket);
				}
				else
				{
					mm.setMessage(readSocket);
				}
			}
			else
			{
				timelineServerNull++;
			}
		}
	}
	
	public void changeRoundNotify(String msg) throws ChangeRoundException, IsMyRoundException
	{
		if(ClientMessageBroker.manageChangeRound(msg) != null)
		{
			System.out.println("--> CAMBIO TURNO: " + msg);
			
			if(ClientMessageBroker.manageChangeRound(msg).equals(username))
			{
				throw new IsMyRoundException(this.username);
			}
			else
			{
				throw new ChangeRoundException(ClientMessageBroker.manageChangeRound(msg));
			}
		}
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
}
