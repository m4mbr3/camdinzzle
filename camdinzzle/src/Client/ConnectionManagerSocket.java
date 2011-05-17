/**
 * 
 */
package Client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
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
	private BufferedOutputStream _on_socket;
	private BufferedWriter writer_on_socket;
	private BufferedReader reader_on_socket;
	
	public ConnectionManagerSocket(int port, String address) 
	{
		// TODO Auto-generated constructor stub
		
		try {
			System.out.println("<<CONN MANAGER>>--OPENING SOCKET WITH SERVER AT ADD "+address+" AND PORT " + port );
			connection_with_server = new Socket(address, port);
			
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
		int i = 0 ;
		String read_socket=null;
		while(true)
		{	
			
			try {
				this.writer_on_socket.write("@login,user=F,pass=N");
				this.writer_on_socket.newLine();				
				this.writer_on_socket.flush();
				System.out.println("@login,user=F,pass=N");
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
		
		}
	}
	

}
