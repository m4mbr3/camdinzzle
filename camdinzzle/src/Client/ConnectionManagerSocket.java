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

/**
 * @author Andrea
 *
 */
public class ConnectionManagerSocket implements ConnectionManager, Runnable  {
	//Class for client logging
	/**
	 * Socket for Managing the connection by socket with server
	 */
	Socket connection_with_server;
	
	private BufferedWriter writer_on_socket;
	private BufferedReader reader_on_socket;
	String address;
	String username;
	String password;
	int port;
	public ConnectionManagerSocket(int port, String address, String username, String password)
	{
		// TODO Auto-generated constructor stub
		this.address = address;
		this.username = username;
		this.password = password;
		this.port = port;
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
	public void run() {
		// TODO Auto-generated method stub
		String read_socket=null;
			
			
			try {
				this.writer_on_socket.write(ClientMessageBroker.createLogin(username, password));
				this.writer_on_socket.newLine();				
				this.writer_on_socket.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				read_socket = reader_on_socket.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if((read_socket != null)&&(ClientMessageBroker.checkMessage(read_socket)))
			{
				System.out.println("<<CONN MANAGER>>-- LOGIN ACCEPTED!!!");
			}
			else
			{
				System.out.println("<<CONN MANAGER>>-- LOGIN FAILD!!!");
			}
		
		
	}
	

}
