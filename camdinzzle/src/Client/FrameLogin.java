package Client;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * Classe di gestione del frame di login.
 */
public class FrameLogin extends JFrame implements ActionListener,WindowListener,ChangeListener, MouseListener{

	private static final long serialVersionUID = 1L;
	/*
	 * Etichetta che indica il campo testo dell'username
	 */
	private JLabel username_label;
	/*
	 * Etichetta che indica il campo testo della password
	 */
	private JLabel password_label;
	/*
	 * Etichetta che indica il campo per l'immissione dell'username
	 */
	private JTextField username;
	/*
	 * Etichetta che indica il campo per l'immissione della password
	 */
	private JPasswordField password;
	/*
	 * Pannello che raccoglie tutti gli oggetti del FrameLogin e del FramenewUser
	 */
	private BackPanel panel;
	/*
	 * Bottone per inviare i valori di login al server
	 */
	private JButton send;
	/*
	 * Bottone per inviare i valori per un nuovo utente al server
	 */
	private JButton send_newUser;
	/*
	 * Variabile che contiene le informazioni sullo schermo dell'utente
	 */
	private Dimension screenSize;
	/*
	 * Variabile che contiene un'istanza del client corrente
	 */
	private Client client;
	/*
	 * Etichetta che permette di passare dalla finestra di login alla finestra di creazione del nuovo utente
	 */
	private JLabel new_user;
	/*
	 * Etichetta che permette di tornare alla finestra di login senza aver creato un nuovo utente
	 */
	private JLabel back;
	/*
	 * Frame per la visualizzazione del form per creare un nuovo utente
	 */
	private JFrame new_userframe;
	/*
	 * Bottone per tornare alla scelta del tipo di connessione
	 */
	private JButton exit;
	/*
	 * Variabile che mi indica se il client è standAlone oppure in locale
	 */
	private boolean is_local;
	/*
	 * 
	 */
	private ClassLoader cldr;
	/*
	 * URL dell'immagine di sfondo del login
	 */
	private URL loginImage;
	/*
	 * URL dell'immagine di sfondo del frame per il nuovo utente
	 */
	private URL newUserImage;
	/*
	 * Immagine per il bottone di invio delle info per il login
	 */
	private ImageIcon sendInfoImage;
	/*
	 * Immagine per il bottone di uscita
	 */
	private ImageIcon exitImage;
	/*
	 * Immagine per un'operazione non andata a buon fine
	 */
	private ImageIcon error;
	/*
	 * @throws HeadlessException
	 */
	public FrameLogin() throws HeadlessException 
	{
		
	}

	/**
	 * @param gc
	 */
	public FrameLogin(GraphicsConfiguration gc) 
	{
		super(gc);
	}

	/**
	 * Costruttore del FrameLogin
	 * @param title
	 * @throws HeadlessException
	 */
	public FrameLogin(String title, Client client, boolean is_local) throws HeadlessException {
		super(title);
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.client = client;
		this.setVisible(true);
		this.setSize(330,330);
		this.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-300)/2);
		this.validate();
		this.repaint();
		new_user = new JLabel("<html> <u> Are you a new user? Click here!</u>");
		this.is_local = is_local;
		back = new JLabel("<html> <u>Come back to login</u>");
		username_label = new JLabel("Username :");
		password_label = new JLabel("Password :");
		username = new JTextField();
		password = new JPasswordField();
		panel = new BackPanel();
		
		//CARICAMENTO IMMAGINI SFONDO
		cldr = this.getClass().getClassLoader();
		// setta l'icona del frame
		ImageIcon logo = new ImageIcon(cldr.getResource("Images/icona.png"));
		this.setIconImage(logo.getImage());
		loginImage = cldr.getResource("Images/login.jpg");
		newUserImage = cldr.getResource("Images/NEW_USER.jpg");
		sendInfoImage = new ImageIcon(cldr.getResource("Images/SEND_INFO.jpg"));
		exitImage = new ImageIcon(cldr.getResource("Images/BACK_HOME.jpg"));
        error = new ImageIcon(cldr.getResource("Images/errore.jpg"));
		//FINE CARICAMENTO IMMAGINI SFONDO
		
		send_newUser = new JButton(sendInfoImage);
		send = new JButton(sendInfoImage);
		exit = new JButton(exitImage);
		back.setSize(250,20);
		new_user.setSize(250,20);
		exit.setSize(131, 46);
		panel.setSize(330,310);
		send_newUser.setSize(131,46);
		send.setSize(131, 46);
		username_label.setSize(90,20);
		password_label.setSize(90,20);
		username.setSize(160,20);
		password.setSize(160,20);
		panel.setLayout(null);
		new_user.setVisible(true);
		back.setVisible(true);
		exit.setVisible(true);
		send_newUser.setVisible(true);
		send.setVisible(true);
		username_label.setVisible(true);
		password_label.setVisible(true);
		username.setVisible(true);
		password.setVisible(true);
		if(is_local == true) exit.setEnabled(false);
		panel.setVisible(true);
		back.setLocation(50,200);
		exit.setLocation(180,240);
		new_user.setLocation(50,200);
		send_newUser.setLocation(20,240);
		send.setLocation(20, 240);
		username_label.setLocation(50,110);
		password_label.setLocation(50,150);
		username.setLocation(140,110);
		password.setLocation(140, 150);
		panel.add(exit);
		panel.add(send);
		panel.add(password_label);
		panel.add(username_label);
		panel.add(password);
		panel.add(username);
		panel.add(new_user);
		panel.setBackground(loginImage);
		panel.validate();
		exit.addMouseListener(this);
		this.addWindowListener(this);
		back.addMouseListener(this);
		send.addMouseListener(this);
		new_user.addMouseListener(this);
		send_newUser.addMouseListener(this);
		panel.repaint();
		this.add(panel);
		this.repaint();
		this.setResizable(false);
	}

	/**
	 * @param title
	 * @param gc
	 */
	public FrameLogin(String title, GraphicsConfiguration gc) 
	{
		super(title, gc);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
	
	}

	@Override
	public void windowActivated(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) 
	{
		if(!is_local){
			client.setConnManager(null);
			client.setVisible(true);
			this.setVisible(false);
			if (new_userframe != null)
			new_userframe.setVisible(false);
			}
		else{
			System.exit(0);
			}
	}

	@Override
	public void windowClosing(WindowEvent arg0) 
	{
		if(!is_local){
			client.setConnManager(null);
			client.setVisible(true);
			this.setVisible(false);
			if (new_userframe != null)
			new_userframe.setVisible(false);
			}
		else
		{
			client.getConnManager().rmClientLocal();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) 
	{
		
	}

	@Override
	public void stateChanged(ChangeEvent arg0) 
	{
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		if(arg0.getComponent().equals(back))
		{
			new_userframe.setVisible(false);
			panel.remove(send_newUser);
			panel.add(send);
			panel.remove(back);
			panel.add(new_user);
			panel.setBackground(loginImage);
			this.add(panel);
			this.setVisible(true);
		}
		if(arg0.getComponent().equals(new_user))
		{																																																																														
				this.setVisible(false);
				new_userframe = new JFrame("New User");
				new_userframe.setResizable(false);
				new_userframe.setVisible(true);
				new_userframe.addWindowListener(this);
				new_userframe.setSize(330,330);
				new_userframe.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-300)/2);
				panel.remove(send);
				panel.add(back);
				panel.add(send_newUser);
				panel.remove(this.new_user);
				panel.setBackground(newUserImage);
				new_userframe.add(panel);			
		}
		if (arg0.getComponent().equals(send))
		{
			if((!checkPassword(password.getPassword()))||(username.getText().contains("@"))||( username.getText().contains("<"))||(username.getText().contains(">"))||(username.getText().contains("'"))||(username.getText().contains("\""))||(username.getText().contains("?"))||(username.getText().contains("&"))||(username.getText().contains("="))||(username.getText().contains("!"))||(username.getText().contains("%")))	
			{
				JOptionPane.showMessageDialog(this,"Maybe you insert one or more special caracters!!!fix it and try again!!!", "Login Error", JOptionPane.ERROR_MESSAGE,error);
			}
			else
			{
					this.setVisible(false);
					String login = client.getConnManager().login(username.getText(), getPasswordString(password));
					if(login != null)
					{
						if (ClientMessageBroker.manageLogin(login)[0].compareTo("ok")==0)
						{
							new FrameGameManager("ManagerPanel",client,is_local);
						}
						else if (ClientMessageBroker.manageLogin(login)[0].compareTo("no")==0)
						{
							this.setVisible(true);
							JOptionPane.showMessageDialog(this,"Authentication Failed!!!", "Login Error", JOptionPane.ERROR_MESSAGE,error);
						}
						else
						{
							this.setVisible(true);
							JOptionPane.showMessageDialog(this,"Generic Error during Login. Try Again!!!", "Message Error", JOptionPane.ERROR_MESSAGE,error);
						}
					}
			}
		}
		if (arg0.getComponent().equals(exit) && !is_local)
		{
			client.setConnManager(null);
			client.setVisible(true);
			this.setVisible(false);
			if (new_userframe != null)
			new_userframe.setVisible(false);
		}
		if (arg0.getComponent().equals(send_newUser))
		{
			if((!checkPassword(password.getPassword()))||(username.getText().contains("@"))||( username.getText().contains("<"))||(username.getText().contains(">"))||(username.getText().contains("'"))||(username.getText().contains("\""))||(username.getText().contains("?"))||(username.getText().contains("&"))||(username.getText().contains("="))||(username.getText().contains("!"))||(username.getText().contains("%")))
			{
				JOptionPane.showMessageDialog(this,"Maybe you insert one or more special caracters!!!fix it and try again!!!", "Login Error", JOptionPane.ERROR_MESSAGE,error);
			}
			else
			{
				new_userframe.setVisible(false);
				String newUser = client.getConnManager().creaUtente(username.getText(), getPasswordString(password));
				if(newUser!=null)
				{
					if (ClientMessageBroker.manageMessageType(newUser).compareTo("ok")==0)
					{	
						this.setVisible(true);
						panel.remove(send_newUser);
						panel.add(this.new_user);
						panel.remove(back);
						panel.setBackground(loginImage);
						panel.add(send);
						this.add(panel);
					}
					else
					{
						JOptionPane.showMessageDialog(this,"Username already exist", "NewUser Error", JOptionPane.ERROR_MESSAGE,error);
						new_userframe.setVisible(true);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this,"TIME OUT", "NewUser Error", JOptionPane.ERROR_MESSAGE,error);
				}
			}
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		if (arg0.getComponent().equals(this.new_user))
		{
			new_user.setCursor(new Cursor(Cursor.HAND_CURSOR));	
		}
		if (arg0.getComponent().equals(this.back))
		{
			back.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		if (arg0.getComponent().equals(this.new_user))
		{
			new_user.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			
		}
		if (arg0.getComponent().equals(this.back))
		{
			back.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		
	}
	/*
	 * Metodo che controlla i caratteri della password evitando i caratteri speciali
	 */
	private boolean checkPassword(char[] password)
	{
		for(int i=0; i<password.length; i++)
		{
			if(password[i] == '@')
				return false;
			else if(password[i] == '<')
				return false;
			else if(password[i] == '>')
				return false;
			else if(password[i] == '"')
				return false;
			else if(password[i] == '\'')
				return false;
			else if(password[i] == '?')
				return false;
			else if(password[i] == '&')
				return false;
			else if(password[i] == '=')
				return false;
			else if(password[i] == '!')
				return false;
			else if(password[i] == '%')
				return false;
		}
		return true;
	}
	
	/*
	 * Metodo che trasforma la password da array di caratteri a stringa
	 * @param password
	 * @return
	 */
	private String getPasswordString(JPasswordField password)
	{
		char[] pass = password.getPassword();
		String ret = new String("");
		for(int i=0;i<pass.length;i++)
		{
			ret += pass[i];
		}
		return ret;
	}


}
