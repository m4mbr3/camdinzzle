package Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
/**
 * Classe di gestione del frame di manager che permette di ottenere informazioni non in partita.
 */
public class FrameGameManager extends JFrame implements WindowListener, MouseListener{

	private static final long serialVersionUID = 1L;
	/*
	 * Variabile che contiene le dimensioni dello schermo
	 */
	private Dimension screenSize;
	/*
	 * Pannello che lancia la crea Razza
	 */
	private BackPanel creaRazza;
	/*
	 * Pannello che lancia l'accesso partita
	 */
	private BackPanel accPartita;
	/*
	 * Pannello che lancia la lista dei giocatori
	 */
	private BackPanel lisGiocatori;
	/*
	 * Pannello che lancia il comando per la classifica
	 */
	private BackPanel classifica;
	/*
	 * Pannello che lancia il comando di logout
	 */
	private BackPanel logout;
	/*
	 * Pannello che contiene tutti gli altri pannelli
	 */
	private BackPanel backGround;
	/*
	 * URL all'immagine della creaRazza
	 */
	private URL creaRazzaImage;
	/*
	 * URL all'immagine della accesso Partita
	 */
	private URL accPartitaImage;
	/*
	 * URL all'immagine della Lista Giocatori
	 *
	 */
	private URL lisGiocatoriImage;
	/*
	 * URL all'immagine della classifica 
	 */
	private URL classificaImage;
	/*
	 * URL all'immagine del logout
	 */
	private URL logoutImage;
	/*
	 * URL all'immagine di sfondo
	 */
	private URL backGroundImage;
	/*
	 * Variabile che contiene un'istanza del client corrente
	 */
	private Client client;
	/*
	 * Frame per eseguire la creazione di una nuova razza
	 */
	private JFrame creaRazzaFrame;
	/*
	 * Etichetta con Il testo della finestra Razza
	 */
	private JLabel razza_testo;
	/*
	 * Etichetta che indica di dover eseguire una scelta
	 */
	private JLabel choice;
	/*
	 * Campo Testo per il nome della nuova razza
	 */
	private JTextField razza_valore;
	/*
	 * Opzione per una razza erbivora
	 */
	private JRadioButton Vege;
	/*
	 * Opzione per una razza carnivora
	 */
	private JRadioButton Carn;
	/*
	 * Oggetto per rendere mutuamente esclusiva la selezione del tipo di specie
	 */
	private ButtonGroup radiogroup;
	/*
	 * Bottone per confermare la creazione della nuova razza
	 */
	private JButton razza_button;
	/*
	 * Immagine per il bottone di creazione nuova razza
	 */
	private ImageIcon razza_button_image;
	/*
	 * URL dell'immagine per il razza_button_image
	 */
	private URL creaRazzaFrameImage;
	/*
	 * Pannello che contiene gli oggetti per la creazione della razza
	 */
	private BackPanel creaRazzaPanel;
	/*
	 * Frame per la visualizzazione della lista giocatori
	 */
	private JFrame listaGiocatori;
	/*
	 * Pannello della lista giocatori
	 */
	private JPanel pannelloGiocatori;
	/*
	 * Panel che mette lo sfondo nella lista dei giocatori in partita
	 */
	private BackPanel listaGiocatoriPanel;
	/*
	 * Posti della lista dei giocatori per la stampa dell'username
	 */
	private JLabel[] postiGiocatori;
	/*
	 * Titolo del frame Lista Giocatori
	 */
	private JLabel titoloGiocatori;
	/*
	 * Frame per la visualizzazione della classifica
	 */
	private JFrame ranking;
	/*
	 * Oggetto per scorrere la classifica
	 */
	private JScrollPane ranking2;
	/*
	 * Tabella per creare il layout della classifica
	 */
	private JTable ranking1;
	/*
	 * Icone dei dinosauri per la scelta prima dell'accesso in partita
	 */
	private ImageIcon[] iconChosenDino;
	/*
	 * Oggetto che contiene il frame di gioco
	 */
	private FrameGame frameGame;
	/*
	 * variabile che indica se il client è stand-Alone o in locale
	 */
	private boolean is_local;
	/*
	 * Immagine che indica un'operazione errata
	 */
	private ImageIcon error;
	/*
	 * Immagine che indica un'operazione corretta
	 */
	private ImageIcon ok;
	/*
	 * Indirizzo relativo all'immagine di sfondo della lista giocatori in partita 
	 */
	private URL listaGiocatoriImage;
	
	/**
	 * Costruttore della classe FrameGameManager
	 * @param title titolo della finestra
	 * @param client istanza del client loggato
	 * @param is_local variabile che identifica i client in locale
	 * @throws HeadlessException
	 */
	public FrameGameManager(String title, Client client, boolean is_local) throws HeadlessException {
		super(title);
		this.client = client;
		this.setResizable(false);
		this.setLayout(null);
		this.is_local = is_local; 
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		creaRazza = new BackPanel();
		accPartita = new BackPanel();
		lisGiocatori = new BackPanel();
		classifica = new BackPanel();
		logout = new BackPanel();
		backGround = new BackPanel();
		backGround.setLayout(null);
		razza_testo = new JLabel("Insert the Name");
		creaRazzaPanel = new BackPanel();
		choice = new JLabel("Select the type of new Species :");
		razza_valore = new JTextField();
		radiogroup = new ButtonGroup();
		Vege = new JRadioButton("Vegetarian");
		Carn = new JRadioButton("Carnivorous");
		Vege.setOpaque(false);
		Carn.setOpaque(false);
		razza_button = new JButton();
		radiogroup.add(Vege);
		radiogroup.add(Carn);
		choice.setSize(250,20);
		razza_button.setSize(331,50);
		razza_testo.setSize(130,50);
		razza_valore.setSize(120,25);
		Vege.setSize(130,20);
		Carn.setSize(130,20);
		Vege.setSelected(true);
		Carn.setSelected(false);
		razza_button.addMouseListener(this);
	
		//INIZIO CARICAMENTO IMMAGINI
		
		ClassLoader cldr = this.getClass().getClassLoader();
		// setta l'icona del frame
		ImageIcon logo = new ImageIcon(cldr.getResource("Images/icona.png"));
		this.setIconImage(logo.getImage());
        iconChosenDino = new ImageIcon[6];
        iconChosenDino[0] = new ImageIcon(cldr.getResource("Images/carn1.jpg"));
        iconChosenDino[1] = new ImageIcon(cldr.getResource("Images/car2.jpg"));
        iconChosenDino[2] = new ImageIcon(cldr.getResource("Images/car3.jpg"));
        iconChosenDino[3] = new ImageIcon(cldr.getResource("Images/veg1.jpg"));
        iconChosenDino[4] = new ImageIcon(cldr.getResource("Images/veg2.jpg"));
        iconChosenDino[5] = new ImageIcon(cldr.getResource("Images/veg3.jpg"));
        creaRazzaImage = cldr.getResource("Images/bottone1.jpg");
        accPartitaImage = cldr.getResource("Images/bottone2.jpg");
        lisGiocatoriImage = cldr.getResource("Images/bottone3.jpg");
        classificaImage = cldr.getResource("Images/bottone4.jpg");
        logoutImage = cldr.getResource("Images/bottone5.jpg");
        backGroundImage = cldr.getResource("Images/sfondo_manager_panel.jpg");
        razza_button_image = new ImageIcon(cldr.getResource("Images/create_new_species_tasto.jpg"));
        creaRazzaFrameImage = cldr.getResource("Images/create_new_species.jpg");
        error = new ImageIcon(cldr.getResource("Images/errore.jpg"));
        ok = new ImageIcon(cldr.getResource("Images/conferma.jpg"));
        listaGiocatoriImage = cldr.getResource("Images/fondo_playerList.jpg");

		//FINE CARICAMENTO IMMAGINI
		
		creaRazza.setSize(296, 71);
		accPartita.setSize(296, 71);
		lisGiocatori.setSize(296, 71);
		classifica.setSize(296, 71);
		logout.setSize(296, 71);
		backGround.setSize(319, 372);
		
		creaRazza.setLocation(11,8);
		accPartita.setLocation(11,79);
		lisGiocatori.setLocation(11,150);
		classifica.setLocation(11, 221);
		logout.setLocation(11, 292);
		backGround.setLocation(0, 0);
		
		this.setSize(319,395);
		this.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-400)/2);
		this.setVisible(true);
		creaRazza.addMouseListener(this);
		accPartita.addMouseListener(this);
		lisGiocatori.addMouseListener(this);
		classifica.addMouseListener(this);
		logout.addMouseListener(this);
		this.addWindowListener(this);
		
		creaRazza.setBackground(creaRazzaImage);
		accPartita.setBackground(accPartitaImage);
		lisGiocatori.setBackground(lisGiocatoriImage);
		classifica.setBackground(classificaImage);
		logout.setBackground(logoutImage);
		backGround.setBackground(backGroundImage);
		
		razza_button.setIcon(razza_button_image);
		creaRazzaPanel.setBackground(creaRazzaFrameImage);
		creaRazzaPanel.setSize(360, 230);
		creaRazzaPanel.setVisible(true);
		creaRazzaPanel.setLayout(null);
	
		
		backGround.add(creaRazza);
		backGround.add(accPartita);
		backGround.add(lisGiocatori);
		backGround.add(classifica);
		backGround.add(logout);
		this.add(backGround);
		this.repaint();
	}
	/**
	 * Metodo per refreshare il FrameGameManager
	 */
	public void refreshFrameGameManager()
	{
		creaRazza.setVisible(true);
		accPartita.setVisible(true);
		lisGiocatori.setVisible(true);
		classifica.setVisible(true);
		logout.setVisible(true);
		backGround.setVisible(true);
		creaRazza.repaint();
		accPartita.repaint();
		lisGiocatori.repaint();
		classifica.repaint();
		logout.repaint();
		backGround.repaint();
	}
	/**
	 * Metodo per ottenere l'informazione se è un client in locale o meno
	 * @return booleano true se è un client false viceversa
	 */
	public boolean getIsLocal()
	{
		return is_local;
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getComponent().equals(creaRazza))
		{
			//Frame con i campi per la creazione della razza
			creaRazzaFrame = new JFrame("Create new Species");
			ClassLoader cldr = this.getClass().getClassLoader();
			ImageIcon logo = new ImageIcon(cldr.getResource("Images/icona.png"));
			creaRazzaFrame.setIconImage(logo.getImage());
			creaRazzaFrame.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-200)/2);
			creaRazzaFrame.setLayout(null);
			creaRazzaFrame.setVisible(true);
			creaRazzaFrame.setSize(360,250);
			
			razza_button.setLocation(15, 165);
			razza_testo.setLocation(20,50);
			razza_valore.setLocation(180,60);
			Vege.setLocation(10,130);
			Carn.setLocation(150,130);
			choice.setLocation(20, 100);

			creaRazzaPanel.add(choice);
			creaRazzaPanel.add(Vege);
			creaRazzaPanel.add(Carn);
			creaRazzaPanel.add(razza_testo);
			creaRazzaPanel.add(razza_valore);
			creaRazzaPanel.add(razza_button);
			creaRazzaFrame.add(creaRazzaPanel);
		}
		else if (arg0.getComponent().equals(accPartita))
		{
			String gameAccess = client.getConnManager().accessoPartita();
			if(gameAccess != null)
			{
				String[] response = ClientMessageBroker.manageGameAccess(gameAccess);
				if(response == null)
				{
					JOptionPane.showMessageDialog(this,"You have sent an invalid message!!!", "Access Game Error", JOptionPane.ERROR_MESSAGE,error);
				}
				else if(response[0].compareTo("ok")==0)
				{
					
					this.setVisible(false);
					this.validate();
					int opt = chosenDinoImage();
					if((opt<0)||(opt>5))
						opt=0;
					frameGame = new FrameGame("Camdinzzle",client, this,opt);
					ChangeRoundThread.start();
					(new Thread(new ChangeRoundThread(client, frameGame))).start();
				}
				else if (response[0].compareTo("no")==0)
				{
					if (response[1].compareTo("troppiGiocatori")==0)
					{
						JOptionPane.showMessageDialog(this,"There aren't free spot!!!", "Access Game Error", JOptionPane.ERROR_MESSAGE,error);
					}
					else if(response[1].compareTo("tokenNonValido")==0)
					{
						JOptionPane.showMessageDialog(this,"Before access to game you must create a new specie!!!", "Access Game Error", JOptionPane.ERROR_MESSAGE,error);
					}
				}
			}
		}
		else if (arg0.getComponent().equals(lisGiocatori))
		{
			String[] response = client.getConnManager().listaGiocatori();
			if (response == null)
			{
				JOptionPane.showMessageDialog(this,"You have sent an invalid message!!!", "Lista Giocatori Error", JOptionPane.ERROR_MESSAGE,error);
			}
			else if (response[0].compareTo("null") == 0)
			{
				JOptionPane.showMessageDialog(this,"No players in game", "Lista Giocatori", JOptionPane.INFORMATION_MESSAGE,ok);
			}
			else if (response[0].compareTo("listaGiocatori")==0)
			{	
				if(listaGiocatori!=null)
				{
					listaGiocatori.removeAll();
					postiGiocatori = null;
					titoloGiocatori = null;
					listaGiocatoriPanel = null;
					pannelloGiocatori = null;
				}
				listaGiocatori = new JFrame("List of Players");
				ClassLoader cldr = this.getClass().getClassLoader();
				ImageIcon logo = new ImageIcon(cldr.getResource("Images/icona.png"));
				listaGiocatori.setIconImage(logo.getImage());
				listaGiocatoriPanel = new BackPanel();
				listaGiocatoriPanel.setBackground(listaGiocatoriImage);
				listaGiocatoriPanel.setLayout(new BorderLayout());
				listaGiocatori.setVisible(true);
				listaGiocatori.setSize(210, 621);
				listaGiocatori.setLocation(((int)(screenSize.getWidth())-210)/2,(int)((int)(screenSize.getHeight())-600)/2);
				titoloGiocatori = new JLabel("<html><h2>Players of this Game:</h2></html>");
				listaGiocatoriPanel.add(titoloGiocatori, BorderLayout.NORTH);
				titoloGiocatori.setSize(210,20);
				postiGiocatori = new JLabel[8];
				pannelloGiocatori = new JPanel();
				pannelloGiocatori.setOpaque(false);
				listaGiocatoriPanel.add(pannelloGiocatori,BorderLayout.CENTER);
				pannelloGiocatori.setLayout(new GridLayout(8,1));
				listaGiocatori.add(listaGiocatoriPanel);
				
				for (int i=1; i < response.length; i++)
				{
					postiGiocatori[i-1] = new JLabel();
					postiGiocatori[i-1].setText("<html><h3>&nbsp;&nbsp;"+i+"-  " + response[i] + "</h3></html>");
					postiGiocatori[i-1].setOpaque(false);
					pannelloGiocatori.add(postiGiocatori[i-1]);
				}	
				
				
				listaGiocatori.repaint();
			}
			else if (response[0].compareTo("no")==0)
			{
				JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "Lista Giocatori Error", JOptionPane.ERROR_MESSAGE,error);
			}
		}
		else if (arg0.getComponent().equals(classifica))
		{
			ArrayList<String> classifica = client.getConnManager().classifica();
			if(classifica!=null)
			{
				if(!classifica.contains("no"))
				{
					if(ranking==null)
					{
						ranking = new JFrame("Classifica");
						ClassLoader cldr = this.getClass().getClassLoader();
						ImageIcon logo = new ImageIcon(cldr.getResource("Images/icona.png"));
						ranking.setIconImage(logo.getImage());
						ranking2 = new JScrollPane();
					}
					else
					{
						ranking1 = null;
					}
					ranking.setVisible(true);
					ranking.setSize(new Dimension(350, 500));
					String[] columnNames = {"USERNAME","NOME SPECIE","PUNTEGGIO","IN PARTITA"};
					String[][] rowData = new String [classifica.size()/4][4];
					int j=0,z=0;
					for(int i=1;i<classifica.size();i++)
					{
						if(z>=4)
						{
							j++;
							z=0;
						}
						rowData[j][z] = classifica.get(i);
						z++;
					}
					ranking1 = new JTable(rowData, columnNames);
					ranking1.setVisible(true);
					ranking2.getViewport().add(ranking1);
					ranking.add(ranking2);
					ranking.setLocation(((int)(screenSize.getWidth())/2-175),((int)(screenSize.getHeight())/2-250));
				
				}
				else
				{
					 JOptionPane.showMessageDialog(this, "Azione non compiuta", "Error", JOptionPane.ERROR_MESSAGE,error);
				}
			}
			else
			{
				
			}
		}
		else if (arg0.getComponent().equals(logout))
		{
			int ritorno = JOptionPane.showConfirmDialog(
				    this,
				    "Do you really want to logout from Camdinzzle?",
				    "Exit Question",
				    JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,ok);
			if (ritorno == 0)
			{
				String check = client.getConnManager().logout();
				if(check!=null)
				{
					String[] response = ClientMessageBroker.manageLogout(check);
					if(response[0].compareTo("ok")==0)
					{
						this.setVisible(false);
						if(!is_local)
						client.setVisible(true);
						else
						{
							client.getLogin().setVisible(true);
							client.validate();
						}
						
					}
					if(response[0].compareTo("no")==0)
					{
						JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "Logout Error", JOptionPane.ERROR_MESSAGE,error);
					}
				}
			}
			else
			{
				this.setVisible(true);
			}
		}
		else if (arg0.getComponent().equals(razza_button))
		{
			 creaRazzaFrame.setVisible(false);
			 String type = null;
			 if(Vege.isSelected()) type = new String("e");
			 else  type = new String("c");
			 String newRace = client.getConnManager().creaRazza(razza_valore.getText(), type);
			 if(newRace != null)
			 {
				 String[] response = ClientMessageBroker.manageCreateSpecies(newRace);	 
				 if (response == null) JOptionPane.showMessageDialog(this,"You have sent an invalid message!!!", "New Species Error", JOptionPane.ERROR_MESSAGE,error);
				 else if(response[0].compareTo("ok")==0)
				 {
					 JOptionPane.showMessageDialog(this,"New Specie's been created!!!", "New Species", JOptionPane.INFORMATION_MESSAGE,ok);
				 }
				 else if (response[0].compareTo("no")==0)
				 {
					 if(response[1] != null)
					 {
						 if(response[1].compareTo("nomeRazzaOccupato")==0) JOptionPane.showMessageDialog(this,"Name busy!!! try with another name", "New Species Error", JOptionPane.ERROR_MESSAGE,error);
						 else if(response[1].compareTo("tokenNonValido")==0)JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "New Species Error", JOptionPane.ERROR_MESSAGE,error);
					 }
					 else JOptionPane.showMessageDialog(this,"You already have created another specie !!!", "New Species Error", JOptionPane.ERROR_MESSAGE,error);
				 }
			 }
		}
	}
	/**
	 * metodo che permette all'utente di scegliere l'icona con cui giocare dopo l'accesso in partita 
	 * @return un intero relativo alla scelta
	 */
	public int chosenDinoImage()
	{
		 return JOptionPane.showOptionDialog(this, "                                        scegli l'icona del dinosauro                                               ", "Chosen Icon Dinosaur", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, iconChosenDino, ok);	
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) 
	{

	}

	@Override
	public void windowClosing(WindowEvent arg0) 
	{
		int ritorno = JOptionPane.showConfirmDialog(
			    this,
			    "Do you really want to exit from Camdinzzle?",
			    "Exit Question",
			    JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,ok);
		if (ritorno == 0)
		{
			String check = client.getConnManager().logout();
			if(check!=null)
			{
				String[] response = ClientMessageBroker.manageLogout(check);
				if(response[0].compareTo("ok")==0)
				{
					this.setVisible(false);
					if(!is_local)
					client.setVisible(true);
					else{
						client.getLogin().setVisible(true);
						client.validate();
					}
				}
				if(response[0].compareTo("no")==0)
				{
					JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "Logout Error", JOptionPane.ERROR_MESSAGE,error);
				}
			}
		}
		else
		{
			this.setVisible(true);
		}
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

	

	@Override
	public void mouseEntered(MouseEvent arg0) {

		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {

		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

		
	}

}
