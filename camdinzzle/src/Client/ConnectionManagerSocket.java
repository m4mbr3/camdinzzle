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
	private String token;
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
					changeRoundNotify(read_socket);
				}
			}
			
			read_socket = null;
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

	@Override
	public String creaUtente(String msg) 
	{
		String readSocket;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.equals("@ok")) || 
						!(readSocket.equals("@no,@usernameOccupato")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String login(String msg) 
	{
		String readSocket;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.contains("@ok")) || 
						!(readSocket.equals("@no,@autenticazioneFallita")) || 
						!((readSocket.contains("@ok")) && (readSocket.length() > 4)) ||
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			if(ClientMessageBroker.checkMessage(msg))
				token = ClientMessageBroker.manageLogin(readSocket)[0];
				
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String creaRazza(String msg) 
	{
		String readSocket;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.equals("@ok")) || 
						!(readSocket.equals("@no,@nomeRazzaOccupato")) || 
						!(readSocket.equals("@no,@tokenNonValido")) ||
						!(readSocket.equals("@no,@razzaGiaCreata")) ||
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String accessoPartita(String msg) 
	{
		String readSocket;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.equals("@ok")) || 
						!(readSocket.equals("@no,@troppiGiocatori")) || 
						!(readSocket.equals("@no,@tokenNonValido")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String uscitaPartita(String msg) 
	{
		String readSocket;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.equals("@ok")) || 
						!(readSocket.equals("@no,@tokenNonValido")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String listaGiocatori()
	{
		String readSocket;
		String msg = "@listaGiocatori,token=" + token;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.contains("@listagiocatori,")) ||
						!(readSocket.equals("@no,@tokenNonValido")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String classifica() 
	{
		String readSocket;
		String msg = "@classifica,token=" + token;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.contains("@classifica,")) ||
						!(readSocket.equals("@no,@tokenNonValido")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String logout() 
	{
		String readSocket;
		String msg = "@logout,token=" + token;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.equals("@ok,")) ||
						!(readSocket.equals("@no,@tokenNonValido")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String mappaGenerale()
	{
		String readSocket;
		String msg = "@mappaGenerale,token=" + token;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.contains("@mappaGenerale,")) ||
						!(readSocket.equals("@no,@tokenNonValido")) || 
						!(readSocket.equals("@no,@nonInPartita")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String listaDinosauri() 
	{
		String readSocket;
		String msg = "@listaDinosauri,token=" + token;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.contains("@listaDinosauri,")) ||
						!(readSocket.equals("@no,@tokenNonValido")) || 
						!(readSocket.equals("@no,@nonInPartita")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String vistaLocale(String dinoId) 
	{
		String readSocket;
		String msg = "@vistaLocale,token=" + token + ",idDino=" + dinoId;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.contains("@vistaLocale,")) ||
						!(readSocket.equals("@no,@tokenNonValido")) || 
						!(readSocket.equals("@no,@nonInPartita")) || 
						!(readSocket.equals("@no,@idNonValido")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String statoDinosauro(String dinoId)
	{
		String readSocket;
		String msg = "@statoDinosauro,token=" + token + ",idDino=" + dinoId;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.contains("@statoDinosauro,")) ||
						!(readSocket.equals("@no,@tokenNonValido")) || 
						!(readSocket.equals("@no,@nonInPartita")) || 
						!(readSocket.equals("@no,@idNonValido")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String muoviDinosauro(String dinoId, String row, String col) 
	{
		String readSocket;
		String msg = "@muoviDinosauro,token=" + token + ",idDino=" + dinoId + ",dest={" + row + "," + col + "}";
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.contains("@ok")) ||
						!(readSocket.equals("@no,@tokenNonValido")) || 
						!(readSocket.equals("@no,@idNonValido")) || 
						!(readSocket.equals("@no,@destinazioneNonValida")) ||
						!(readSocket.equals("@no,@raggiuntoLimiteMosseDinosauro")) ||
						!(readSocket.equals("@no,@mortePerInedia")) ||
						!(readSocket.equals("@no,@nonIlTuoTurno")) ||
						!(readSocket.equals("@no,@nonInPartita")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String cresciDinosauro(String dinoId) 
	{
		String readSocket;
		String msg = "@cresciDinosauro,token=" + token + ",idDino=" + dinoId;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.contains("@ok")) ||
						!(readSocket.equals("@no,@tokenNonValido")) || 
						!(readSocket.equals("@no,@idNonValido")) || 
						!(readSocket.equals("@no,@raggiuntoLimiteMosseDinosauro")) ||
						!(readSocket.equals("@no,@mortePerInedia")) ||
						!(readSocket.equals("@no,@nonIlTuoTurno")) ||
						!(readSocket.equals("@no,@nonInPartita")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String deponiUovo(String dinoId) 
	{
		String readSocket;
		String msg = "@deponiUovo,token=" + token + ",idDino=" + dinoId;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!((readSocket.contains("@ok,")) && (readSocket.length() > 4)) ||
						!(readSocket.equals("@no,@tokenNonValido")) || 
						!(readSocket.equals("@no,@idNonValido")) || 
						!(readSocket.equals("@no,@raggiuntoLimiteMosseDinosauro")) ||
						!(readSocket.equals("@no,@raggiuntoNumeroMaxDinosauri")) ||
						!(readSocket.equals("@no,@mortePerInedia")) ||
						!(readSocket.equals("@no,@nonIlTuoTurno")) ||
						!(readSocket.equals("@no,@nonInPartita")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String confermaTurno() 
	{
		String readSocket;
		String msg = "@confermaTurno,token=" + token;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.equals("@ok")) ||
						!(readSocket.equals("@no,@tokenNonValido")) ||
						!(readSocket.equals("@no,@nonIlTuoTurno")) ||
						!(readSocket.equals("@no,@nonInPartita")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String passaTurno() 
	{
		String readSocket;
		String msg = "@passaTurno,token=" + token;
		
		try
		{
			writer_on_socket.write(msg);
			writer_on_socket.newLine();				
			writer_on_socket.flush();
			
			do
			{
				/* Ciclo che controlla che il messaggio ricevuto non sia un cambio del turno. Se è un cambio del turno
				 * rimane in ascolto fino a che non riceve un messaggio valido
				 */
				do
				{
					// Ciclo che controlla se il messaggio è un messaggio possibile da ricevere 
					readSocket = null;
		
					do
					{
						// Ciclo che rimane in ascolto sul socket
						try 
						{
							readSocket = reader_on_socket.readLine();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}while(readSocket == null);
					
				}while(!(readSocket.equals("@ok")) ||
						!(readSocket.equals("@no,@tokenNonValido")) ||
						!(readSocket.equals("@no,@nonIlTuoTurno")) ||
						!(readSocket.equals("@no,@nonInPartita")) || 
						!(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
				if(ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno"))
					changeRoundNotify(readSocket);
			}while((ClientMessageBroker.manageMessageType(readSocket).equals("cambioTurno")));
			
			return readSocket;
		}
		catch(IOException e)
		{
			/*TODO: ogni volta che va in catch chiamare un metodo dell'interfaccia grafica che manda un popup di errore
			 * di comunicazione col socket 
			 */
			e.printStackTrace();
			return null;
		}
	}		
}
	
