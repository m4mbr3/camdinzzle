package Client;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClientListener implements Runnable 
{
	/*
	 * Parametro che contiene il Socket per la comunicazione con il server
	 * In questo caso per la lettura dei messaggi del server 
	 */
	private Socket connection_with_server;
	/*
	 * Oggetto per Leggere dal Socket i messaggi del server
	 */
	private BufferedReader reader_on_socket;
	/*
	 * Stringa per il salvataggio del messaggio dal server
	 */
	private String readSocket;
	/*
	 * Oggetto Monitor per la comunicazione al client dei messaggi
	 */
	private MonitorMessage mm;
	/*
	 * Variabile per Limitare il numero di messaggi in validi dal server
	 * Serve per capire se eventualmente il server non è più in esecuzione
	 */
	private int timelineServerNull;
	/*
	 * Variabile che permette la terminazione del thread di ascolto sul socket
	 */
	private boolean run;
	/*
	 * Variabile che contiente l'oggetto di connessione con il server
	 */
	private ConnectionManagerSocket cms;
	/**
	 * Costruttore della classe ClientListener 
	 * Inizializza il thread all'ascolto sul socket
	 * @param mm Locazione comune per comunicare a terzi il messaggio
	 * @param soc Socket di connessione con il server
	 * @param cms Oggetto di connessione con il server
	 * @throws IOException
	 */
	public ClientListener(MonitorMessage mm, Socket soc, ConnectionManagerSocket cms) throws IOException
	{
		this.cms = cms;
		this.mm = mm;
		this.readSocket = null;
		connection_with_server = soc;
		timelineServerNull = 0;
		run = true;
		reader_on_socket = new BufferedReader( new InputStreamReader(this.connection_with_server.getInputStream()));
		(new Thread(this)).start();
	}
	/**
	 * Metodo che stoppa il thread di ascolto sul socket
	 */
	public void stop()
	{
		run = false;
	}
	/**
	 * Metodo che legge i messaggi del server
	 * Nel caso di cambio turno notifica al changeRoundListener il messaggio di cambio turno
	 * se no notifica al monitor message il messaggio per il Client
	 */
	@Override
	public void run() 
	{				
		while(run)
		{
			readSocket = null;
			if(timelineServerNull == 20)
			{
				mm.setMessage("null");
				JOptionPane.showMessageDialog(new JFrame(), "The server socket is down", "Server error", JOptionPane.ERROR_MESSAGE);
				
				for(int i = 0; i<Frame.getFrames().length; i++)
				{
					Frame.getFrames()[i].setVisible(false);
				}
				new Client("Cliente");
				this.stop();
			}
			try 
			{
				readSocket = reader_on_socket.readLine();
				timelineServerNull = 0;
			} 
			catch (IOException e) 
			{
				timelineServerNull++;
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
	/**
	 * Metodo per la notifica del cambio turno
	 */
	public void changeRoundNotify(String msg)
	{
		if(ClientMessageBroker.manageChangeRound(msg) != null)
		{			
			cms.setChangeRound(ClientMessageBroker.manageChangeRound(msg));
		}
	}
}
