package Client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
/**
 * 
 */
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import Server.*;

public class Client extends JFrame implements WindowListener,MouseListener,ChangeListener
{

	private static final long serialVersionUID = 1L;
	/**
	 * Etichetta per la visualizzazione del titolo/versione del gioco
	 */
	private JLabel camdinzzle;
	/**
	 * Etichetta per l'indicazione del dover fare una scelta del metodo di connessione
	 */
	private JLabel choice;
	/**
	 * Cotentitore di tutti gli oggetti riguardanti la scelta della connessione 
	 */
	private JPanel panel;
	/**
	 * Variabile contenente le dimensioni dello schermo
	 */
	private Dimension screenSize;
	/**
	 * Selezione per l'rmi
	 */
	private JRadioButton rmi;
	/**
	 * Selezione per i socket
	 */
	private JRadioButton socket;
	/**
	 * Variabile per rendere la scelta mutuamente esclusiva
	 */
	private ButtonGroup radiogroup;
	/**
	 * Pulsante per la conferma della scelta
	 */
	private JButton next;
	/**
	 * Variabile contenente l'oggetto di connessione 
	 * Rende indipendente le richieste verso il server dal metodo di connessione scelto con quest'ultimo
	 * Può contenere ConnectionManagerSocket, ConnectionManagerLocal e ConnectionManagerRMI
	 */
	private ConnectionManager connManager;
	/**
	 * Visualizza dopo la connessione con il server la finestra per il login o la creazione del nuovo utente
	 */
	private FrameLogin login;
	/**
	 * Etichetta per indicare lo spazio destinato all'immissione della porta
	 */
	private JLabel port_label;
	/**
	 * Etichetta per indicare lo spazio destinato all'indirizzo del server
	 */
	private JLabel address_label;
	/**
	 * Campo di testo per l'immissione della porta
	 * viene cambiata con quelle di default sia si scelga metodo socket sia per la scelta metodo rmi
	 * è possibile ma sconsigliata una modifica manuale della stessa
	 */
	private JTextField port;
	/**
	 * Campo di selezione per abilitare il cambio manuale del valore della porta da quello di default 
	 */
	private JCheckBox enable_port;
	/**
	 * Campo di testo per l'immissione dell'indirizzo del server
	 * ex localhost, 192.168.1.100, PC-SERVER
	 */
	private JTextField address;
	/**
	 * Etichetta che indica qual è il campo del nome del server nel caso RMI
	 */
	private JLabel nome_server;
	/**
	 * Campo di testo per l'immissione del nome del server 
	 * Valido e visibile solo per la connessione tramite RMI
	 */
	private JTextField server_value;
	/**
	 * Bottone che permette la disconnessione e terminazione del programma Camdinzzle
	 */
	private JButton exit;
	/**
	 * Variabile che determina il diverso funzionamento del Client Stand-alone dal Client in locale lanciato quindi
	 * dal server in esecuzione
	 * Ne limita alcune opzione di chiusura
	 */
	private boolean is_local;
	
	/**
	 * Costrutture della classe client dedicato alla versione Locale
	 * Non viene visualizzata in questo caso la finestra per la scelta della connessione ma direttamente
	 * il frame per effettuare il login
	 * @param clientLocal server per ottenere l'oggetto di connessione legato all'istanza del client
	 */
	public Client(ClientManagerLocal clientLocal)
	{
		this.connManager =new ConnectionManagerLocal(clientLocal);
		is_local = true;
		login = new FrameLogin("Login",this,is_local);
	}
	/**
	 * Costruttore della classe client dedicato alle versioni RMI e socket
	 * Visualizza la scelta di connessione
	 * @param Name Nome della finestra di scelta delle connessioni
	 */
	public Client(String Name) {
		super (Name);
		this.setResizable(false);
		this.setVisible(true);
		is_local = false;
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-300)/2);
		nome_server = new JLabel("Server: ");
		server_value = new JTextField("server");
		exit = new JButton("Exit");
		port_label = new JLabel("Port :");
		address_label = new JLabel("Address :");
		port = new JTextField("4567");
		enable_port = new JCheckBox("enable");
		address = new JTextField("localhost");
		radiogroup = new ButtonGroup();
		next = new JButton("Continue");
		rmi = new JRadioButton("rmi");
		socket = new JRadioButton("socket");
		camdinzzle = new JLabel("<html> <h3>Camdinzzle Project v1.0</h3>");
		choice = new JLabel("Select the method of connection to ServerLogic:");
		panel = new JPanel();
		exit.setSize(150,40);
		nome_server.setSize(90,20);
		server_value.setSize(160, 20);
		next.setSize(150, 40);
		this.setSize(400,400);
		panel.setSize(400,400);
		choice.setSize(350,30);
		camdinzzle.setSize(300,30);
		rmi.setSize(100,30);
		socket.setSize(100,30);
		port_label.setSize(90,20);
		address_label.setSize(90,20);
		port.setSize(90,20);
		address.setSize(160,20);
		enable_port.setSize(100,20);
		nome_server.setLocation(50,280);
		server_value.setLocation(140,280);
		exit.setLocation(30, 310);
		next.setLocation(200,310);
		choice.setLocation(20,60);
		rmi.setLocation(50, 110);
		socket.setLocation(170, 110);
		enable_port.setLocation(230,200);
		port.setLocation(140,200);
		address.setLocation(140,240);
		port_label.setLocation(50,200);
		address_label.setLocation(50,240);
		camdinzzle.setLocation(20,15);
		socket.setSelected(true);
		panel.setLayout(null);
		exit.setVisible(true);
		server_value.setVisible(true);
		nome_server.setVisible(true);
		nome_server.setVisible(false);
		server_value.setVisible(false);
		address.setEnabled(true);
		server_value.setEnabled(false);
		nome_server.setEnabled(true);
		enable_port.setEnabled(true);
		port.setEnabled(false);
		address.setEditable(true);
		port_label.setEnabled(true);
		address_label.setEnabled(true);
		radiogroup.add(rmi);
		radiogroup.add(socket);
		panel.add(exit);
		panel.add(nome_server);
		panel.add(server_value);
		panel.add(address);
		panel.add(address_label);
		panel.add(enable_port);
		panel.add(port);
		panel.add(port_label);
		panel.add(camdinzzle);
		panel.add(choice);
		panel.add(socket);
		panel.add(rmi);
		panel.add(next);
		this.add(panel);
		enable_port.addChangeListener(this);
		socket.addChangeListener(this);
		exit.addMouseListener(this);
		this.addWindowListener(this);
		next.addMouseListener(this);
		this.setResizable(false);
		this.validate();
		this.repaint();
	}
	/**
	 * 
	 * @return  Ritorna l'oggetto di connessione utilizzato dal Client
	 */
	public ConnectionManager getConnManager() {	
		return connManager;
	}
	/**
	 * Permette di settare l'oggetto di connessione del client
	 * @param connManager Oggetto per la comunciazione con il server (Locale, Socket, RMI)
	 */
	public void setConnManager(ConnectionManager connManager) {
		this.connManager = connManager;
	}	

	/**
	 * Programma Main del client che crea l'instanza del Client con la visualizzazione del primo frame 
	 * di dialogo
	 * @param args
	 */
	public static void main(String[] args) {
		new Client("Cliente");		
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
	}
	/**
	 * Gestisce l'evento di chiusura della finestra
	 * In caso di versione Stand-alone viene chiuso il programma
	 * In caso di versione Locale la finestra torna visibile
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		if (!is_local)
			System.exit(0);
		else 
			this.setVisible(true);
	}
	
	@Override
	public void windowDeactivated(WindowEvent arg0) {	
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
	}
	@Override
	public void windowOpened(WindowEvent arg0) {	
	}
	/**
	 * Metodo che gestisce le operazione dell'utente sull'interfaccia grafica
	 * Gestiesce il click da parte dell'utente sui pusanti:
	 * exit --> Chiude il programma
	 * next --> Effettua controlli sulla selezione dei vari parametri e se tutto è corretto tenta
	 * la connessione con il server  in caso di fallimento notifica all'utente il tipo di errore 
	 * e permette di ritentare
	 * @param arg0 Contiene l'oggetto con le informazioni riguardanti l'evento catturato dall'ascoltatore
	 * e permette quindi la determinazione dell'operazione svolta dall'utente
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getComponent().equals(exit))
		{
			System.exit(0);
		}
		else if(arg0.getComponent().equals(next))
		{
			if((rmi.isSelected()==false)&&(socket.isSelected()==false))
			{
				System.out.println("You Can't Go Next!!!");
			}
			else
			{
				 if(rmi.isSelected())
				 {
					this.setVisible(false);
					try{
						this.connManager = new ConnectionManagerRMI(address.getText(),port.getText(),server_value.getText());
						login = new FrameLogin("Login",this,is_local);
						System.out.println("Server scaricato!!");
					}
					catch(MalformedURLException e)
					{
						this.setVisible(true);
						JOptionPane.showMessageDialog(this, "Error in the url...check your data connect", "Malformed URL ", JOptionPane.ERROR_MESSAGE);
					}
					catch(AccessException e)
					{
						this.setVisible(true);
						JOptionPane.showMessageDialog(this, "Error during the access", "Access Exception", JOptionPane.ERROR_MESSAGE);
					}
					catch(AlreadyBoundException e)
					{
						this.setVisible(true);
						JOptionPane.showMessageDialog(this, "Object already in use", "AlreadyBoundException", JOptionPane.ERROR_MESSAGE);
					}
					catch(RemoteException e)
					{
						this.setVisible(true);
						JOptionPane.showMessageDialog(this, "Error during contact the remote server", "Remote Exception", JOptionPane.ERROR_MESSAGE);
					} 
					catch (Exception e) 
					{
						this.setVisible(true);
						JOptionPane.showMessageDialog(this, "Error during contact the remote server", "Remote Exception", JOptionPane.ERROR_MESSAGE);
					}					
				}	
				else
				{
					this.setVisible(false);
					Integer port_i = new Integer(port.getText());
					try{
							connManager = new ConnectionManagerSocket(port_i.intValue(), address.getText(), new MonitorMessage());
							login = new FrameLogin("Login",this, is_local);
						}
					catch(ConnectException e)
					{
						JOptionPane.showMessageDialog(this,"Please Check your connection data " +
								"maybe  Server is down","Error Connection ",JOptionPane.ERROR_MESSAGE);
						this.setVisible(true);
					}
					catch(UnknownHostException e)
					{
						JOptionPane.showMessageDialog(this, "Please Check the Address name", "Uknown Host", JOptionPane.ERROR_MESSAGE);
						this.setVisible(true);
					}
					catch(SocketException e)
					{
						JOptionPane.showMessageDialog(this, "Error of Connection","Socket Error", JOptionPane.ERROR_MESSAGE);
						this.setVisible(true);
					}
					catch(NumberFormatException e)
					{
						JOptionPane.showMessageDialog(this, "Check you port!!!", "Port Error", JOptionPane.ERROR_MESSAGE);
						this.setVisible(true);
					}
					catch(IOException e)
					{
						JOptionPane.showMessageDialog(this, "Generic IOError ", "IOException", JOptionPane.ERROR_MESSAGE);
						this.setVisible(true);
					}	
				}
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
	}
	public FrameLogin getLogin() {
		return login;
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	/**
	 * Metodo che gestisce l'evento di cambiata selezione e che quindi abilita e disabilita i vari componenti
	 * dell'interfaccia in base alla selezione dell'utente
	 * @param arg0 Contiene l'oggetto con le informazioni riguardanti l'evento catturato dall'ascoltatore
	 * e permette quindi la determinazione dell'operazione svolta dall'utente
	 */
	@Override
	public void stateChanged(ChangeEvent arg0) {
		if (arg0.getSource() instanceof JCheckBox)
			if(enable_port.isSelected())
			{
				port.setEnabled(true);
			}
			else
			{
				port.setEnabled(false);
			}
		if (arg0.getSource() instanceof JRadioButton)
		{
			if(socket.isSelected())
			{
				port.setText("4567");
				port.setVisible(true);
				server_value.setVisible(true);
				enable_port.setVisible(true);
				server_value.setVisible(false);
				nome_server.setVisible(false);
				port_label.setVisible(true);
				this.validate();
				this.repaint();
			}
			if(rmi.isSelected())
			{
				port.setText("1099");
				port.setVisible(false);
				port_label.setVisible(false);
				nome_server.setVisible(false);
				server_value.setVisible(false);
				enable_port.setVisible(false);
				this.validate();
				this.repaint();
			}
		}
	}
}
