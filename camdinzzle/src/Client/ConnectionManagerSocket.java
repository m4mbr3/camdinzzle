/**
 * 
 */
package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

/**
 * @author Andrea
 *
 */
public class ConnectionManagerSocket extends JFrame implements ConnectionManager, Runnable  {
	//Class for client logging
	/**
	 * Socket for Managing the connection by socket with server
	 */
	private Socket connection_with_server;
	private BufferedWriter writer_on_socket;
	private BufferedReader reader_on_socket;
	private String address;
	private String username;
	private String password;
	private int port;
	private String command;
	private String read_socket;
	
	public ConnectionManagerSocket(int port, String address, String username, String password)
	{
		// TODO Auto-generated constructor stub
		this.address = address;
		this.username = username;
		this.password = password;
		this.port = port;
		command = new String();
		this.read_socket = null;
		
		try {
			System.out.println("<<CONN MANAGER>>--OPENING SOCKET WITH SERVER AT ADD "+address+" AND PORT " + port );
			connection_with_server = new Socket(this.address, this.port);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			writer_on_socket = new BufferedWriter(new OutputStreamWriter(connection_with_server.getOutputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		// TODO Auto-generated method stub
		String read_socket=null;
				
		while(true)
		{
			do
			{
				try 
				{
					read_socket = reader_on_socket.readLine();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}while(read_socket == null);
			
			if(read_socket != null)
			{
				if(ClientMessageBroker.manageMessageType(read_socket).equals("cambioTurno"))
				{
					if(ClientMessageBroker.manageChangeRound(read_socket) != null)
					{
						if(ClientMessageBroker.manageChangeRound(read_socket).equals(username))
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
			
			read_socket = null;
		}
	}

	@Override
	public String creaUtente(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String login(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creaRazza(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String accessoPartita(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String uscitaPartita(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String listaGiocatori(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String classifica(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String logout(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String mappaGenerale(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String listaDinosauri(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String vistaLocale(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String statoDinosauro(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String muoviDinosauro(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cresciDinosauro(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deponiUovo(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String confermaTurno(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String passaTurno(String msg) {
		// TODO Auto-generated method stub
		return null;
	}
			
		
		
}
	
