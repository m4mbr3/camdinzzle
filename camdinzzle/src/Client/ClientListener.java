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

public class ClientListener implements Runnable {
	private Socket connection_with_server;
	private BufferedReader reader_on_socket;
	private String address;
	private int port;
	private String readSocket;
	private MonitorMessage mm;
	private String username;
	
	public ClientListener(int port, String address, String username, MonitorMessage mm, Socket soc)
	{
		// TODO Auto-generated constructor stub
		this.address = address;
		this.port = port;
		this.mm = mm;
		this.readSocket = null;
		connection_with_server = soc;
		this.username = username;
		
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

	@Override
	public void run() 
	{				
		while(true)
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
		}
	}
	
	public void changeRoundNotify(String msg)
	{
		if(ClientMessageBroker.manageChangeRound(msg) != null)
		{
			if(ClientMessageBroker.manageChangeRound(msg).equals(username))
			{
				//TODO: chiamata al metodo del Client che mi lancia il popup di conferma del turno
			}
			else
			{
				//TODO: chiamata al metodo del Client che mi notifica il cambio del turno e mi evidenzia il giocatore
			}
		}
	}
}
