package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;

import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
/**
 * Classe di gestione del frame di gioco.
 */
public class FrameGame extends JFrame implements MouseListener,Visual,ActionListener, WindowListener {

	private static final long serialVersionUID = 1L;
	/*
	 * Pannello di contenimento per la mappa
	 */
	private JPanel panel;
	/*
	 * Pannello di contenimento per i comandi sullo schema di gioco
	 */
	private BackPanel panelControl;
	/*
	 * Pannello di contenimento incluso in panelControl che contiene gli oggetti nella parte
	 * superiore
	 */
	private JPanel panelControlUp;
	/*
	 * Pannello di contenimento incluso in panelControl che contiene gli oggetti nella parte
	 * inferiore
	 */
	private JPanel panelControlDown;
	/*
	 * Costante che indica il numero di righe della mappa
	 */
	final private int row = 40;
	/*
	 * Costante che indica il numero di colonne della mappa
	 */
	final private int col = 40;
	/*
	 * Matrice di bottoni per definire lo schema di gioco
	 */
	private JButton[][] buttons;
	/*
	 * Array delle Immagini riguardanti la vegetazione
	 */
	private ImageIcon[] iconVegetation;
	/*
	 * Oggetto riguardante l'immagine dell'acqua
	 */
	private ImageIcon iconWater;
	/*
	 * Oggetto riguardante l'immagine delle carogne;
	 */
	private ImageIcon iconCarrion;
	/*
	 * Array delle immagini riguardanti i dinosauri
	 */
	private ImageIcon[] iconDino;
	/*
	 * Oggetto contenente l'immagine scelta dall'utente per quella specifica sessione di gioco
	 */
	private ImageIcon iconDinoPlayer;
	/*
	 * Oggetto riguardante l'immagine del buio
	 */
	private ImageIcon iconDark;
	/*
	 * Array delle immagini riguardanti i vari tipi di terra
	 */
	private ImageIcon[] iconLand;
	/*
	 * Array delle immagini riguardanti i vari tipi di vegetazione nella nebbia
	 */
	private ImageIcon[] iconVegetationDisable;
	/*
	 * Oggetto riguardante l'immagine dell'acqua nella nebbia
	 */
	private ImageIcon iconWaterDisable;
	/*
	 * Array delle immagini riguardanti i vari tipi di terra nella nebbia
	 */
	private ImageIcon[] iconLandDisable;
	/*
	 * Oggetto riguardante l'immagine del nemico
	 */
	private ImageIcon iconDinoEnemy;
	/*
	 * Oggetto che ricapitola i dinosauri della propria specie
	 */
	private JList dinoList;
	/*
	 * Oggetto che fa scorrere i dinosauri nella dinoList
	 */
	private JScrollPane dinoListScroll;
	/*
	 * Oggetto che ricapitola le caratteristiche di un particolare dino selezionato
	 * di una specie nell'dinoList
	 */
	private JTextArea dinoState;
	/*
	 * Oggetto che fa scorrere le caratteristiche di un particolare dino nella dinoState
	 */
	private JScrollPane scrollDinoState;
	/*
	 * Frame che visualizza la lista dei giocatori in partita
	 */
	private JFrame listaGiocatori;
	/*
	 * Panel che mette lo sfondo nella lista dei giocatori in partita
	 */
	private BackPanel listaGiocatoriPanel;
	/*
	 * Titolo del frame listaGiocatori
	 */
	private JLabel titoloGiocatori;
	/*
	 * "Slot" dove vengono visualizzati i giocatori in partita nel frame listaGiocatori
	 */
	private JLabel[] postiGiocatori;
	/*
	 * Pannello dove risiedono i postiGiocatori nel frame listaGiocatori
	 */
	private JPanel pannelloGiocatori;
	/*
	 * Pannello per i comandi
	 */
	private JPanel commandButtons;
	/*
	 * Lista di bottoni corrispondenti alle operazioni sul dinosauro
	 */
	private JButton[] commandDinoButton;
	/*
	 * Lista di bottoni corrispondenti alle operazioni di sessione partita che può fare l'utente 
	 */
	private JButton[] commandGameButton;
	/*
	 * Etichetta che stampa l'utente corrente o il timer del turno
	 */
	private JLabel time;
	/*
	 * Intero che rappresenta i minuti nel time
	 */
	private int timeMin;
	/*
	 * Intero che rappresenta i secondi nel time
	 */
	private int timeSec;
	/*
	 * finestra per la rappresentazione della classifica
	 */
	private JFrame rankingFrame;
	/*
	 * Tabella per il layout della classifica
	 */
	private JTable rankingTable;
	/*
	 * Oggetto per permettere di scorrere la classifica
	 */
	private JScrollPane rankingScroll;
	/*
	 * Oggetto che memorizza un'istanza del client che utilizza il FrameGame stesso
	 */
	private Client client;
	/*
	 * Oggetto per il salvataggio dell'id del dinosauro selezionato
	 */
	private String dinoId;
	/*
	 * Indica se è già stato selezionato un dinosauro da parte dell'utente
	 */
	private int flag=0;
	/*
	 * Tempo di durata turno in secondi
	 */
	private int timeGlobal;
	/*
	 * Flag per indicare che la specie è estinta
 	 */
	private boolean flagStop;
	/*
	 * Oggetto che salva il  nome della specie dell'utente corrente
	 */
	private String nameSpecie;
	/*
	 * Oggetto che fa riferimento alla cella cliccata
	 */
	private JButton cellClicked;
	/*
	 * Istanza di FrameGameManager per tornare allo stato di loggato fuori partita
	 */
	private FrameGameManager frameGameManager;
	/*
	 * Larghezza della sezione comandi
	 */
	private final int widthControlPanel=300;
	/*
	 * Numero di righe visibili nella dinolist
	 */
	private int visibleRowCountDinoList=6;
	/*
	 * Numero massimo di richieste con risposta negativa dal server prima dell'uscita direttamente
	 */
	private final int maxAttempt = 5;
	/*
	 * Font utilizzato per la dinoList
	 */
	private final Font fontDinoList = new Font("Serif", Font.PLAIN, 24);
	/*
	 * Font utilizzato per la dinostate
	 */
	private final Font fontDinoState = new Font("Serif", Font.PLAIN, 15);
	/*
	 * Valore che indica l'altezza dello schermo
	 */
	private int screenHeight;
	/*
	 * Valore che indica la larghezza dello schermo
	 */
	private int screenWidth;
	/*
	 * Oggetto relativo all'immagine di Messaggio operazione Errata
	 */
	private ImageIcon error;
	/*
	 * Oggetto relativo all'immagine di Messaggio operazione Confermata
	 */
	private ImageIcon ok;
	/*
	 * Indirizzo relativo all'immagine di sfondo della lista giocatori in partita 
	 */
	private URL listaGiocatoriImage;
	/*
	 * Oggetto relativo alla gif del combattimento
	 */
	private JFrame fight;
	/*
	 * Oggetto relativo alla gif del combattimento
	 */
	private AnimationPanel fightAnimation;
	/*
	 * Oggetto relativo alla gif del mangia
	 */
	private JFrame eat;
	/*
	 * Oggetto relativo alla gif del mangia
	 */
	private AnimationPanel eatAnimation;
	/*
	 * Timer per animazione
	 */
	private Timer timer;
	/*
	 * Energia del dinosauro
	 */
	private int energyDino;
	/*
	 * Tipo dinosauro
	 */
	private String dinosaursType;
	
	/**
	 * Costruttore di FrameGame
	 * @param title
	 * @throws HeadlessException
	 * 
	 */
	
	public FrameGame(String title,Client client, FrameGameManager frameGameManager, int intDino) throws HeadlessException{
		super(title);		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setResizable(false);
		this.setBackground(Color.black);
		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();

		screenHeight = (int)g.getMaximumWindowBounds().getHeight();
		screenWidth = (int)g.getMaximumWindowBounds().getWidth();
		this.client=client;
		buttons = new JButton[row][col];
		
		//INIZIO CARICAMENTO IMMAGINI
		
		iconVegetation = new ImageIcon[2];
		iconLand = new ImageIcon[2];
		iconDino = new ImageIcon[6];
		iconLandDisable = new ImageIcon[2];
		iconVegetationDisable = new ImageIcon[2];
		ClassLoader cldr = this.getClass().getClassLoader();
		// setta l'icona del frame
		ImageIcon logo = new ImageIcon(cldr.getResource("Images/icona.png"));
		this.setIconImage(logo.getImage());
		iconVegetation[0] = new ImageIcon(cldr.getResource("Images/vegetazione1.jpg"));
		iconVegetation[1] = new ImageIcon(cldr.getResource("Images/vegetazione2.jpg"));
		iconLand[0] = new ImageIcon(cldr.getResource("Images/terra1.jpg"));
		iconLand[1] = new ImageIcon(cldr.getResource("Images/terra2.jpg"));
		iconWater = new ImageIcon(cldr.getResource("Images/acqua.jpg"));
		iconDark = new ImageIcon(cldr.getResource("Images/buio.jpg"));
		iconCarrion = new ImageIcon(cldr.getResource("Images/carogna.jpg"));
		iconLandDisable[0]  = new ImageIcon(cldr.getResource("Images/terra1disabilitata.jpg"));
		iconLandDisable[1]  = new ImageIcon(cldr.getResource("Images/terra2disabilitata.jpg"));
		iconWaterDisable  = new ImageIcon(cldr.getResource("Images/acquaDisable.jpg"));
		iconVegetationDisable[0]  = new ImageIcon(cldr.getResource("Images/vegetazione1disabilitata.jpg"));
		iconVegetationDisable[1]  = new ImageIcon(cldr.getResource("Images/vegetazione2disabilitata.jpg"));
		iconDinoEnemy = new ImageIcon(cldr.getResource("Images/brontosauro.jpg"));
		iconDino[0] = new ImageIcon(cldr.getResource("Images/carn1map.jpg"));
		iconDino[1] = new ImageIcon(cldr.getResource("Images/car2map.jpg"));
		iconDino[2] = new ImageIcon(cldr.getResource("Images/car3map.jpg"));
		iconDino[3] = new ImageIcon(cldr.getResource("Images/veg1map.jpg"));
		iconDino[4] = new ImageIcon(cldr.getResource("Images/veg2map.jpg"));
		iconDino[5] = new ImageIcon(cldr.getResource("Images/veg3map.jpg"));
        error = new ImageIcon(cldr.getResource("Images/errore.jpg"));
        ok = new ImageIcon(cldr.getResource("Images/conferma.jpg"));
        listaGiocatoriImage = cldr.getResource("Images/fondo_playerList.jpg");

		//FINE CARICAMENTO IMMAGINI
		
		iconDinoPlayer = iconDino[intDino];

		this.setVisible(true);
		this.setLocation(0,0);
		this.frameGameManager = frameGameManager;
		flagStop = false;
		
		this.setSize(screenWidth, screenHeight);
		this.addWindowListener(this);
		panel = new JPanel();
		panelControl = new BackPanel();
		panelControlUp = new JPanel();
		panelControlDown = new JPanel();
		panel.setVisible(true);
		panelControl.setVisible(true);
		panelControlUp.setVisible(true);
		panelControlDown.setVisible(true);
		panel.setBorder(null);
		panel.setPreferredSize(new Dimension(screenWidth-widthControlPanel, screenHeight));
		panel.setLocation(0, 0);
		panelControl.setPreferredSize(new Dimension(widthControlPanel, screenHeight));
		panelControlUp.setSize(new Dimension(widthControlPanel, screenHeight/14*5));
		panelControlDown.setSize(new Dimension(widthControlPanel, screenHeight/14*9));
		panel.setLayout(new GridLayout(row,col));
		panelControl.setLayout(new BorderLayout());
		panelControlUp.setLayout(new BorderLayout());
		panelControlDown.setLayout(new BorderLayout());
		panelControlUp.validate();
		panelControlDown.validate();
		panelControl.validate();
		panelControlUp.setOpaque(false);
		panelControlDown.setOpaque(false);
		if(screenHeight>700)
			panelControl.setBackground(cldr.getResource("Images/fondo_gioco.jpg"));
		else
			panelControl.setBackground(cldr.getResource("Images/fondo_gioco_640.jpg"));
		panelControl.add(panelControlUp, BorderLayout.NORTH);
		panelControl.add(panelControlDown, BorderLayout.SOUTH);
		commandButtons = new JPanel();
		commandButtons.setVisible(true);
		commandButtons.setBorder(null);
		commandButtons.setPreferredSize(new Dimension(widthControlPanel-10, (screenHeight/14*5)));
		time = new JLabel();
		time.setPreferredSize(new Dimension(widthControlPanel-10, screenHeight/14));
		time.setVisible(true);
		panelControlDown.add(time,BorderLayout.SOUTH);
		for (int i=0; i < row; i++)
		{
			for(int j=0; j < col; j++)
			{
				buttons[i][j] = new JButton();
				buttons[i][j].setVisible(true);
				buttons[i][j].setSize((screenWidth-widthControlPanel)/col, (screenHeight/row));
				buttons[i][j].setName(i+","+j +";");
				buttons[i][j].addActionListener(this);
				buttons[i][j].addMouseListener(this);
				buttons[i][j].setBorder(null);
				int random = (int)(Math.random()*2);
				buttons[i][j].setIcon(iconLand[random]);
				buttons[i][j].setEnabled(false);
				panel.add(buttons[i][j]);
			}
		}	
		
		this.add(panel,BorderLayout.WEST);
		this.add(panelControl, BorderLayout.EAST);	
		this.validate();
		String[] msgDinoList = client.getConnManager().listaDinosauri();
		extinctionSpecies(msgDinoList);
		if(!flagStop)
		{
			nameSpecie = msgDinoList[0].substring(0,msgDinoList[0].indexOf("-")+1);
			this.drawDinoList(msgDinoList);
			this.drawDinoState(msgDinoList[0], client.getConnManager().statoDinosauro(msgDinoList[0]));
			this.drawCommandButtons();
			this.repaint();
		}
		this.drawMap(client.getConnManager().mappaGenerale());
	}

	@Override
	public void windowActivated(WindowEvent arg0) 
	{
		this.repaint();
	}

	@Override
	public void windowClosed(WindowEvent arg0) 
	{
		String check = client.getConnManager().uscitaPartita();
		if(check==null)
		{
			errorMessage();
		}
		else
		{
			if(check.equals("no"))
			{
				errorMessageServer(check);
			}
			else
			{
				
				ChangeRoundThread.stop();
				this.setVisible(false);
				frameGameManager = new FrameGameManager("Frame Game", client, frameGameManager.getIsLocal());
			}
		}
	}
	


	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(fight!=null)
			fight.setVisible(false);
		if(eat!=null)
			eat.setVisible(false);
		if(timer!=null)
			timer.stop();
	}


	@Override
	public void windowOpened(WindowEvent e) {}


	@Override
	public void windowClosing(WindowEvent e) 
	{
		String check = client.getConnManager().uscitaPartita();
		if(check==null)
		{
			errorMessage();
		}
		else
		{
			if(check.equals("no"))
			{
				errorMessageServer(check);
			}
			else
			{
				ChangeRoundThread.stop();
				this.setVisible(false);
				frameGameManager.setVisible(true);
			}
		}
	}


	@Override
	public void windowIconified(WindowEvent e) {}


	@Override
	public void windowDeiconified(WindowEvent e) {}


	@Override
	public void windowDeactivated(WindowEvent e) {}


	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		for(int i=0; i<2; i++)
		{
			commandDinoButton[i].setEnabled(false);
		}

		/*CRESCI DINOSAURO
		 * dopo aver cliccato una cella con un dinosauro si attiva il tasto
		 * se il messaggio non � null viene aggiornato la descrizione dello stato dino appena cresciuto
		 */
		if(arg0.getComponent().equals(commandDinoButton[0]))
		{
			if(cellClicked!=null)
				cellClicked.setBorder(null);
			
			String[] growUpDino = client.getConnManager().cresciDinosauro(dinoId);
			if(growUpDino==null)
			{
				errorMessage();
			}
			else
			{
				if(growUpDino[0].equals("no"))
				{
					errorMessageServer(growUpDino);
					extinctionSpecies();
				}
				else
				{
					drawDinoState(dinoId,client.getConnManager().statoDinosauro(dinoId));
					upDateFrameGame();
				}
			}	
		}
		
		/*DEPONI UOVO
		 *  dopo aver cliccato una cella con un dinosauro si attiva il tasto
		 *  dopo aver inviato la richiesta
		 *  se la risposta � diversa da null controlla il messaggio
		 *  se non � no ridisegna la mappa altrimenti crea un popup
		 */
		if(arg0.getComponent().equals(commandDinoButton[1]))
		{
			if(cellClicked!=null)
				cellClicked.setBorder(null);
			
			String[] newEgg = client.getConnManager().deponiUovo(dinoId);
			if(newEgg==null)
			{
				errorMessage();
			}
			else
			{
				if(newEgg[0].equals("no"))
				{
					errorMessageServer(newEgg);
					extinctionSpecies();
				}
				else
				{
					upDateFrameGame();
				}
			}

		}
		
		/*CLASSIFICA
		 * tasto sempre attivo
		 * se la risposta non � null e non no, crea un popup con la classifica
		 * altrimenti stampa l'errore
		 */
		if(arg0.getComponent().equals(commandGameButton[0]))
		{
			if(cellClicked!=null)
				cellClicked.setBorder(null);
			
			ArrayList<String> classifica = client.getConnManager().classifica();
			if(classifica!=null)
			{
				if(!classifica.contains("no"))
				{
					drawRanking(classifica);
				}
				else
				{
					errorMessageServer(classifica);
				}
			}
			else
			{
				errorMessage();
			}
		}
		
		/*PASSA TURNO
		 * tasto sempre attivo
		 * fa la chiamata a server
		 * se la risposta � null o no crea popup con errore
		 * altrimenti nulla
		 */
		if(arg0.getComponent().equals(commandGameButton[1]))
		{
			if(cellClicked!=null)
				cellClicked.setBorder(null);
			
			String[] check = client.getConnManager().passaTurno();
			if(check==null)
			{
				errorMessage();
			}
			else
			{
				if(check[0].equals("no"))
				{
					errorMessageServer(check);
				}
				else
				{
					upDateFrameGame();
				}
			}
		}
		
		/*ESCI DALLA PARTITA
		 * fa la chiamata a server
		 * se la risposta � null o no crea popup con errore
		 */
		if(arg0.getComponent().equals(commandGameButton[2]))
		{
			if(cellClicked!=null)
				cellClicked.setBorder(null);
			
			String check = client.getConnManager().uscitaPartita();
			if(check==null)
			{
				errorMessage();
			}
			else
			{
				if(check.equals("no"))
				{
					errorMessageServer(check);
				}
				else
				{
					ChangeRoundThread.stop();
					frameGameManager.setVisible(true);
					this.setVisible(false);
					
				}
			}
		}
		
		/*LISTA GIOCATORI
		 * dopo aver cliccato si apre la lista giocatori
		 * se @no viene creato un popup
		 */
		if(arg0.getComponent().equals(commandGameButton[3]))
		{
			if(cellClicked!=null)
				cellClicked.setBorder(null);
			
			String[] check = client.getConnManager().listaGiocatori();

			if(check==null)
			{
				errorMessage();
			}
			else
			{
				if(check.equals("no"))
				{
					errorMessageServer(check);
				}
				else
				{	
					drawPlayerList(check);				
				}
			}
		}
		
		/*AGGIORNA MAPPA
		 * dopo aver cliccato si aggiorna la mappa
		 * se @no viene creato un popup
		 */
		if(arg0.getComponent().equals(commandGameButton[4]))
		{
			if(cellClicked!=null)
				cellClicked.setBorder(null);
			
			ArrayList<String> check = client.getConnManager().mappaGenerale();
			if(check==null)
			{
				errorMessage();
			}
			else
			{
				if(check.equals("no"))
				{
					errorMessageServer(check);
				}
				else
				{	
					drawMap(check);				
				}
			}
		}
		
		/*BOTTONI MAPPA
		 * se viene cliccato un bottone contenente un dinosauro del giocatore allora
		 * attiva i pulsanti MOVIMENTO, CRESCI DINOSAURO, DEPONI UOVO
		 */
		if(arg0.getComponent() instanceof JButton)
		{
			if(flag==1)
			{
				String nameCell = arg0.getComponent().getName();
				if(nameCell.contains(";"))
				{
					if(!((String)arg0.getComponent().getName()).contains(nameSpecie))
					{
						String[] option = {"yes","no"};
						int opt = JOptionPane.showOptionDialog(panel, "Would you move "+ dinoId, "Dinosaur move", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, ok, option, "yes");
						cellClicked.setBorder(null);
						if(opt==0)
						{
							String[] nameDest = nameCell.split(";");
							String[] newNameDest = nameDest[0].split(",");
							String[] check = client.getConnManager().muoviDinosauro(dinoId, newNameDest[0], newNameDest[1]);
							if(check==null)
							{
								errorMessage();
							}
							else
							{
								if(!check[0].equals("no"))
								{
									if(check.length==3)
									{
										fight();
										String result;
										if(check[2].equals("v"))
											result = " win!";
										else
											result = " lose!";
										JOptionPane.showMessageDialog(panel, "Fight" + result, "Fight result", JOptionPane.INFORMATION_MESSAGE, ok);
									}
									extinctionSpecies();
									if(!flagStop)
									{
										String[] dinoState = client.getConnManager().statoDinosauro(dinoId);
										if(energyDino<Integer.parseInt(dinoState[6])&&(dinosaursType.equals("Vegetarian")))
										{
											eat();
										}
										drawMap(client.getConnManager().mappaGenerale());
										drawDinoState(dinoId, dinoState);
									}
								}
								else
								{
									errorMessageServer(check);
									extinctionSpecies();
								}
							}
						}
					}
					else
					{
						if(cellClicked!=null)
							cellClicked.setBorder(null);
						String[] idDino = arg0.getComponent().getName().split(";");
						dinoId = idDino[1];
						drawDinoState(dinoId, client.getConnManager().statoDinosauro(dinoId));
						if(((String)arg0.getComponent().getName()).contains(nameSpecie))
						{
							cellClicked = (JButton)arg0.getComponent();
							for(int i=0; i<2; i++)
							{
								commandDinoButton[i].setEnabled(true);	
							}
							((JButton)arg0.getComponent()).setBorder(BorderFactory.createLineBorder(Color.blue,2));
							flag=1;
						}
					}
						
				}
				flag=0;
			}
			else if(((String)arg0.getComponent().getName()).contains("-"))
			{
				String[] idDino = arg0.getComponent().getName().split(";");
				dinoId = idDino[1];
				String[] dinoState = client.getConnManager().statoDinosauro(dinoId);
				drawDinoState(dinoId, dinoState);
				if(((String)arg0.getComponent().getName()).contains(nameSpecie))
				{
					cellClicked = (JButton)arg0.getComponent();
					energyDino = Integer.parseInt(dinoState[6]);
					for(int i=0; i<2; i++)
					{
						commandDinoButton[i].setEnabled(true);	
					}
					((JButton)arg0.getComponent()).setBorder(BorderFactory.createLineBorder(Color.blue,2));
					flag=1;
				}
			}
			else
			{
				if(cellClicked!=null)
					cellClicked.setBorder(null);
			}
		}
	}

	@Override
	/*
	 * se il cursore entra in un bottone della mappa si crea un bordo nero
	 */
	public void mouseEntered(MouseEvent arg0) 
	{
		if ((arg0.getComponent() instanceof JButton)&&(!(arg0.getComponent().equals(commandDinoButton[0])))&&(!(arg0.getComponent().equals(commandDinoButton[1])))&&(!(arg0.getComponent().equals(commandGameButton[3])))&&(!(arg0.getComponent().equals(commandGameButton[0])))&&(!(arg0.getComponent().equals(commandGameButton[1])))&&(!(arg0.getComponent().equals(commandGameButton[2])))&&(!(arg0.getComponent().equals(commandGameButton[4]))))
			{
				if(!arg0.getComponent().equals(cellClicked))
				{
					((JButton) arg0.getComponent()).setBorder(BorderFactory.createLineBorder(Color.black));
				}
			}
	}

	@Override
	/*
	 * quando il cursore esce dal bottone della mappa il bordo si cancella
	 */
	public void mouseExited(MouseEvent arg0) 
	{
		if ((arg0.getComponent() instanceof JButton)&&(!(arg0.getComponent().equals(commandDinoButton[0])))&&(!(arg0.getComponent().equals(commandDinoButton[1])))&&(!(arg0.getComponent().equals(commandGameButton[3])))&&(!(arg0.getComponent().equals(commandGameButton[0])))&&(!(arg0.getComponent().equals(commandGameButton[1])))&&(!(arg0.getComponent().equals(commandGameButton[2])))&&(!(arg0.getComponent().equals(commandGameButton[4]))))
			{
				if(!arg0.getComponent().equals(cellClicked))
				{
					((JButton) arg0.getComponent()).setBorder(null);
				}
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
	
	@Override
	/**
	 * inizializza la mappa generale con le carateristiche inviategli nella mapList
	 * richiede le mappe locali di tutti i dinosauri del giocatore e le stampa su quella generale
	 * se il server risponde con errato messaggio mapList � null e viene rifatta la richiesta per un numero massimo uguale a maxAttempt
	 * @param mapList
	 */
	public void drawMap(ArrayList<String> mapList) 
	{
		if(mapList!=null)
		{
			int j=0;
			int z=0;
			for(int i = 2; i< mapList.size(); i++)
			{
				if(z==col)
				{
					z=0;
					j++;
				}
				buttons[j][z].setEnabled(false);
				if(mapList.get(i).compareTo("b")==0)
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "dark");
						buttons[j][z].setIcon(iconDark);
						buttons[j][z].setDisabledIcon(iconDark);
						buttons[j][z].setToolTipText(null);
					}
				else if(mapList.get(i).compareTo("v")==0)
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "vegetation");
						int random = (int)(Math.random()*2);
						buttons[j][z].setIcon(iconVegetationDisable[random]);
						buttons[j][z].setDisabledIcon(iconVegetationDisable[random]);
						buttons[j][z].setToolTipText(null);
					}
				else if(mapList.get(i).compareTo("t")==0)
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "land");
						int random = (int)(Math.random()*2);
						buttons[j][z].setIcon(iconLandDisable[random]);
						buttons[j][z].setDisabledIcon(iconLandDisable[random]);
						buttons[j][z].setToolTipText(null);
					}
				else if(mapList.get(i).compareTo("a")==0)
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "water");
						buttons[j][z].setIcon(iconWaterDisable);
						buttons[j][z].setDisabledIcon(iconWaterDisable);
						buttons[j][z].setToolTipText(null);
					}
				else if(mapList.get(i).compareTo("d")==0)
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "dinosaur");
						int random = (int)(Math.random()*2);
						buttons[j][z].setIcon(iconLandDisable[random]);
						buttons[j][z].setDisabledIcon(iconLandDisable[random]);
						buttons[j][z].setToolTipText(null);
					}
				else
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "land");
						int random = (int)(Math.random()*2);
						buttons[j][z].setIcon(iconLandDisable[random]);
						buttons[j][z].setDisabledIcon(iconLandDisable[random]);
						buttons[j][z].setToolTipText(null);
					}
				z++;
			}
			String[] msgDinoList = client.getConnManager().listaDinosauri();
			if(!msgDinoList[0].equals("null"))
			{
				if(msgDinoList[0].equals("no"))
				{
					errorMessageServer(msgDinoList);
				}
				else
				{
					for(int i=0; i<msgDinoList.length; i++)
					{
						drawDinoZoom(msgDinoList[i], client.getConnManager().vistaLocale(msgDinoList[i]));
					}
				}
			}
			else
			{
				extinctionSpecies(msgDinoList);
				if(!flagStop)
				{
					errorMessageServer();
				}
			}
			this.validate();
			
		}
		else
		{
			int count = 0;
			ArrayList<String> msg;
			do
			{
				msg = client.getConnManager().mappaGenerale();
				count++;
			}
			while((msg==null)&&(count<maxAttempt));
			if(msg==null)
			{
				errorMessageServer();
			}
		}
	}

	@Override
	/**
	 * Sovrascrive la mappa generale con la vista locale di un dinosauro
	 * nel caso in cui il messaggio � null viene rifatta la richiesta per maxAttempt volte
	 * se mapList contiente no viene creato un popup di errore
	 * @param dinoId 
	 * @param mapList : messaggio contenente vista locale
	 */
	public void drawDinoZoom(String dinoId, ArrayList<String> mapList) 
	{
		if(mapList!=null)
		{
			if(!mapList.get(0).equals("no"))
			{
				int startRow = Integer.parseInt(mapList.get(0));
				int startCol = Integer.parseInt(mapList.get(1));
				int maxRow = startRow - Integer.parseInt(mapList.get(2));
				int maxCol = Integer.parseInt(mapList.get(3)) + startCol;
				
				int row=startRow;
				int col=startCol;
				String[] energySplit = new String [2];
				if(startRow>=this.row)
					startRow=this.row-1;
				if(startCol<0)
					startCol=0;
				if(maxRow<=0)
					maxRow=-1;
				if(maxCol>=this.col)
					maxCol=this.col;
				int i=4;

				for(row=startRow;row>maxRow-1;row--)
				{
					for(col=startCol;col<maxCol;col++)
					{
						if(i<mapList.size())
						{
							if(mapList.get(i).compareTo("b")==0)
							{
								buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + "dark");
								buttons[row][col].setIcon(iconDark);
							}
							else if(mapList.get(i).compareTo("t")==0)
							{
								buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + "land");
								int random = (int)(Math.random()*2);
								buttons[row][col].setIcon(iconLand[random]);
							}
							else if(mapList.get(i).compareTo("a")==0)
							{
								buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + "water");
								buttons[row][col].setIcon(iconWater);
							}
								
							else if(mapList.get(i).indexOf(",") != -1)
							{
								energySplit = ((String)mapList.get(i)).split(",");
								if(energySplit[0].compareTo("d")==0)
								{
									buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + energySplit[1]);
									buttons[row][col].setToolTipText("Dinosaur id: " + energySplit[1]);
									String name = buttons[row][col].getName();
									if(name.contains(nameSpecie))
											buttons[row][col].setIcon(iconDinoPlayer);
									else
										buttons[row][col].setIcon(iconDinoEnemy);
								}
								else if(energySplit[0].compareTo("v")==0)
								{
									buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + "vegetation");
									buttons[row][col].setToolTipText("Vegetation energy: " + energySplit[1]);
									int random = (int)(Math.random()*2);
									buttons[row][col].setIcon(iconVegetation[random]);
								}
								else if(energySplit[0].compareTo("c")==0)
								{
									buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + "carrion");
									buttons[row][col].setToolTipText("Carrion energy: " + energySplit[1]);
									buttons[row][col].setIcon(iconCarrion);
								}
							}
							//col++;
						i++;
						}
					}
				}
				for(int rowEnable=startRow; rowEnable>maxRow-1; rowEnable--)
				{
					for(int colEnable=startCol; colEnable<maxCol; colEnable++)
					{
						if((rowEnable>=0)&&(rowEnable<this.row)&&(colEnable>=0)&&(colEnable<this.col))
						{
							buttons[rowEnable][colEnable].setEnabled(true);
						}
					}
				}
			}
			else
			{
				errorMessageServer(mapList);
			}
		}
		else
		{
			int count = 0;
			ArrayList<String> msg;
			do
			{
				msg = client.getConnManager().vistaLocale(dinoId);
				count++;
			}
			while((msg==null)&&(count<maxAttempt));
			if(msg==null)
			{
				errorMessageServer();
			}
		}
	}

	@Override
	/**
	 * prende la lista dinosauri e la stampa in alto a destra
	 * se msgDinoList � null fa la richiesta per un numero pari a maxAttempt dopo di che crea un popup di errore
	 * se il messaggio contiene no crea un popup di errore
	 * se il messaggio � valido stampa la lista dei dinosauri
	 * quando viene cliccato un dinosauro viene aggiornato il campo stato dinosauro
	 */
	public void drawDinoList(Object[] msgDinoList) 
	{
		if(msgDinoList!=null)
		{
			if(!msgDinoList[0].equals("no"))
			{
				if(dinoList!=null)
				{
					dinoListScroll.remove(dinoList);
					panelControlUp.remove(dinoListScroll);
					dinoList = null;
					dinoListScroll=null;
				}
				dinoList = new JList(msgDinoList);
				dinoList.setVisibleRowCount(visibleRowCountDinoList);
				dinoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				dinoList.addListSelectionListener(new ListSelectionListener() {
					
					@Override
					/**
					 * classe di ascolto inner anonima
					 */
					public void valueChanged(ListSelectionEvent e) 
					{
						if(dinoList.getSelectedValue() != null)
						{
							String[] msg = client.getConnManager().statoDinosauro(dinoList.getSelectedValue().toString());
							if(msg != null)
							{
								if(msg[0] != null)
									drawDinoState(dinoList.getSelectedValue().toString(), msg);
							}
						}
					}
				});
				dinoList.setVisible(true);
				dinoList.setFont(fontDinoList);
				dinoListScroll = new JScrollPane(dinoList);
				dinoListScroll.setPreferredSize(new Dimension(widthControlPanel-55,screenHeight/14*3));
				dinoList.setOpaque(false);
				dinoListScroll.setOpaque(false);
				panelControlUp.add(dinoListScroll,BorderLayout.NORTH);
				panelControlUp.repaint();
			}
			else
			{
				errorMessageServer(msgDinoList);
			}
		}
		else
		{
			int count = 0;
			String[] msg;
			do
			{
				msg = client.getConnManager().listaDinosauri();
				count++;
			}
			while((msg==null)&&(count<maxAttempt));
			if(msg==null)
			{
				errorMessageServer();
			}
		}
	}

	@Override
	public void drawTime(int timeInt) 
	{
		timeGlobal = timeInt;
		timeMin = timeInt/60;
		timeSec = timeInt%60;
		String timeStringSec;
		String timeStringMin;
		if(timeSec==0)
			timeStringSec = "00";
		else
		{
			if(timeSec < 10)
			{
				timeStringSec = "0" + String.valueOf(timeSec);
			}
			else
			{
				timeStringSec = String.valueOf(timeSec);
			}
		}
		if(timeMin == 0)
		{
			timeStringMin = "00";
		}
		else
		{
			timeStringMin = "0" + String.valueOf(timeMin);
		}
		
		time.setText("         Time to finish round  " + timeStringMin +":"+ timeStringSec);
		time.setVisible(true);
		time.setOpaque(false);
		panelControlDown.repaint();
	}
	public int getTime()
	{
		return timeGlobal;
	}
	public void drawRound(String user)
	{ 
		time.setText("         now is the turn of: \n" + user);
		time.setVisible(true);
		time.setOpaque(false);
		panelControlDown.repaint();
	}

	@Override
	public void drawConnectionState(String msg) 
	{

		
	}

	@Override
	/**STAMPA CLASSIFICA
	 * crea un popup con la classifica
	 */
	public void drawRanking(ArrayList<String> classifica) 
	{
		if(rankingFrame==null)
		{
			rankingFrame = new JFrame("Ranking");
			rankingScroll = new JScrollPane();
		}
		else
		{
			rankingTable = null;
		}
		rankingFrame.setVisible(true);
		rankingFrame.setSize(new Dimension(350, 500));
		rankingFrame.validate();
		rankingFrame.setAlwaysOnTop(true);
		String[] columnNames = {"USERNAME","NAME SPECIE","SCORE","IN GAME?"};
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
		rankingTable = new JTable(rowData, columnNames);
		rankingTable.setVisible(true);
		rankingScroll.getViewport().add(rankingTable);
		rankingFrame.add(rankingScroll);
		rankingTable.setEnabled(false);
		rankingFrame.setLocation(screenWidth/2-175, screenHeight/2-250);
	}

	@Override
	/**STAMPA LO STATO DINOSAURO
	 * 
	 */
	public void drawDinoState(String dinoId, String[] msgDinoState) 
	{
		if(msgDinoState!=null)
		{
			if(!msgDinoState[0].equals("no"))
			{
				if(msgDinoState[2].equals("c"))
				{
					msgDinoState[2]="Carnivorous";
					dinosaursType = "Carnivorous";
				}
				else
				{
					msgDinoState[2]="Vegetarian";
					dinosaursType = "Vegetarian";
				}
				String newMsgDinoState="";
				
				newMsgDinoState += "Dinosaur's state " + dinoId + "\n   of player " + msgDinoState[0] + ":\n";
				newMsgDinoState += "	race: " + msgDinoState[1] + "\n";
				newMsgDinoState += "	type: " + msgDinoState[2] + "\n";
				newMsgDinoState += "	dinosaur's position: \n          	    row:" + msgDinoState[3] + "\n          	    col:" + msgDinoState[4] + "\n";
				newMsgDinoState += "	dimension: " + msgDinoState[5] + "\n";
				
				// Se il dinosauro appartiene al giocatore allora ci sono delle informazioni aggiuntive
				if(msgDinoState.length > 6)
				{
					newMsgDinoState += "	energy: " + msgDinoState[6] + "\n";
					newMsgDinoState += "	round lived: " + msgDinoState[7] + "\n";
				}
				
				newMsgDinoState+="to move select your dinosaur\n";
				newMsgDinoState+="        then the destination";
				if(scrollDinoState!=null)
				{
					scrollDinoState.remove(dinoState);
					panelControlUp.remove(scrollDinoState);
				}
				dinoState = new JTextArea(newMsgDinoState);
				dinoState.setVisible(true);
				dinoState.setFont(fontDinoState);
				dinoState.setEditable(false);
				dinoState.setOpaque(false);
				scrollDinoState = new JScrollPane(dinoState);
				scrollDinoState.setPreferredSize(new Dimension(widthControlPanel-25,screenHeight/14*3));
				scrollDinoState.setVisible(true);
				scrollDinoState.validate();
				scrollDinoState.setOpaque(false);
				panelControlUp.add(scrollDinoState,BorderLayout.SOUTH);
				panelControlUp.validate();
			}
			else
			{
				errorMessageServer(msgDinoState);
			}
		}
		else
		{
			errorMessage();
		}	
	}
	@Override
	/**stampa la lista giocatori
	 * 
	 */
	public void drawPlayerList(String[] msgPlayerList)
	{
		if(msgPlayerList!=null)
		{
			if(!msgPlayerList[0].equals("no"))
			{	
				if(listaGiocatori!=null)
				{
					listaGiocatori.removeAll();
					postiGiocatori = null;
					titoloGiocatori = null;
					listaGiocatoriPanel = null;
					pannelloGiocatori = null;
					listaGiocatori = null;
				}
				listaGiocatori = new JFrame("List of Players");
				listaGiocatoriPanel = new BackPanel();
				listaGiocatoriPanel.setBackground(listaGiocatoriImage);
				listaGiocatoriPanel.setLayout(new BorderLayout());
				listaGiocatori.setVisible(true);
				listaGiocatori.setSize(210, 621);
				listaGiocatori.setLocation((screenWidth-210)/2,(int)(screenHeight-600)/2);
				titoloGiocatori = new JLabel("<html><h2>Players of this Game:</h2></html>");
				listaGiocatoriPanel.add(titoloGiocatori, BorderLayout.NORTH);
				titoloGiocatori.setSize(210,20);
				postiGiocatori = new JLabel[8];
				pannelloGiocatori = new JPanel();
				pannelloGiocatori.setOpaque(false);
				listaGiocatoriPanel.add(pannelloGiocatori,BorderLayout.CENTER);
				pannelloGiocatori.setLayout(new GridLayout(8,1));
				listaGiocatori.add(listaGiocatoriPanel);
				
				for (int i=1; i < msgPlayerList.length; i++)
				{
					postiGiocatori[i-1] = new JLabel();
					postiGiocatori[i-1].setText(msgPlayerList[i]);
					postiGiocatori[i-1].setOpaque(false);
					postiGiocatori[i-1].setText("<html><h3>&nbsp;&nbsp;"+i+"-  " + msgPlayerList[i] + "</h3></html>");
					pannelloGiocatori.add(postiGiocatori[i-1]);
				}	
				
				
				listaGiocatori.repaint();
			}
			else
			{
				errorMessageServer(msgPlayerList);
			}
		}
		else
		{
			errorMessage();
		}
	}
	/**
	 * istanzia i bottoni per comandare i dinosauri e il gioco
	 */
	 public void drawCommandButtons()
	 {
		 commandDinoButton = new JButton[2];
		 commandDinoButton[0] = new JButton("GrowUp Dinosars (Dimension*500)");
		 commandDinoButton[0].setName("GrowUp Dinosars");
		 commandDinoButton[1] = new JButton("Take Egg (1500)");
		 commandDinoButton[1].setName("Take Egg");
		 commandGameButton = new JButton[5];
		 commandGameButton[0] = new JButton("Ranking");
		 commandGameButton[0].setName("Ranking");
		 commandGameButton[1] = new JButton("Switch Round");
		 commandGameButton[1].setName("Switch Round");
		 commandGameButton[2] = new JButton("Game Exit");
		 commandGameButton[2].setName("Game Exit");
		 commandGameButton[3] = new JButton("Player List");
		 commandGameButton[3].setName("Player List");
		 commandGameButton[4] = new JButton("Map's UpDate");
		 commandGameButton[4].setName("Map's UpDate");
		 
		 for(int i=0;i<2;i++)
		 {
			 commandDinoButton[i].setPreferredSize(new Dimension(widthControlPanel-20,screenHeight/14/5*3));
			 commandDinoButton[i].setVisible(true);
			 commandDinoButton[i].setEnabled(false);
			 commandDinoButton[i].addMouseListener(this);
			 commandButtons.add(commandDinoButton[i]);
		 }
		 for(int i=0;i<5;i++)
		 {
			 commandGameButton[i].setPreferredSize(new Dimension(widthControlPanel - 20, screenHeight/14/5*3));
			 commandGameButton[i].setVisible(true);
			 commandGameButton[i].setEnabled(true);
			 commandGameButton[i].addMouseListener(this);
		 }
		 commandButtons.add(commandGameButton[4]);
		 commandButtons.add(commandGameButton[1]);
		 commandButtons.add(commandGameButton[3]);
		 commandButtons.add(commandGameButton[0]);
		 commandButtons.add(commandGameButton[2]);
		 commandButtons.setOpaque(false);
		 panelControlDown.add(commandButtons, BorderLayout.NORTH);
		 

	 }
	 /**
	  * messaggio di errore in caso di messaggio di risposta illeggibile
	  */
	 public void errorMessage()
	 {
		 JOptionPane.showMessageDialog(this, "Action not completed", "Error", JOptionPane.ERROR_MESSAGE,error);
	 }
	 
	 /**
	  * messaggio di errore con l'Arrey di stringhe ricevuta dal server
	  * @param msg
	  */
	 public void errorMessageServer(Object[] msg)
	 {
		 JOptionPane.showMessageDialog(this, msg,"Error",JOptionPane.ERROR_MESSAGE,error);
	 }
	 
	 /**
	  * messaggio di errore in caso di comunicazione non corretta con il server
	  */
	 public void errorMessageServer()
	 {
		 JOptionPane.showMessageDialog(this, "Error in server communication","Error",JOptionPane.ERROR_MESSAGE,error);
	 }
	 
	 /**
	  * messaggio di errore con l'ArrayList ricevuto dal server
	  * @param msg
	  */
	 public void errorMessageServer(ArrayList<String> msg)
	 {
		 JOptionPane.showMessageDialog(this, msg,"Error",JOptionPane.ERROR_MESSAGE,error);
	 }
	 
	 /**
	  * messaggio di errore con la striga ricevuto dal server
	  * @param msg
	  */
	 public void errorMessageServer(String msg)
	 {
		 JOptionPane.showMessageDialog(this, msg,"Error",JOptionPane.ERROR_MESSAGE,error);
	 }
	 /*
	  * Controlla che la lista dei dinosauri sia vuota per tornare alla frameGameManager
	  * @return Array di stringhe
	  */
	 private String[] extinctionSpecies()
	 {
		 String[] dinoList = client.getConnManager().listaDinosauri();
		 if(dinoList[0].equals("null"))
		 {
			 ChangeRoundThread.stop();
			 client.getConnManager().uscitaPartita();
			 this.setVisible(false);
			 frameGameManager.setVisible(true);
			 JOptionPane.showMessageDialog(getRootPane(), "La tua specie si e' estinta", "SPECIE ESTINTA", JOptionPane.INFORMATION_MESSAGE, error); 
			 flagStop = true;
			 return new String[] {""};
		 }
		 else
		 {
			 return dinoList;
		 }
	 }
	 /*
	  * Controlla che la lista dei dinosauri con l'ultima listadino richiesta che sia vuota per tornare alla frameGameManager
	  * @param listaDino lista dei dinosauri
	  */
	 private void extinctionSpecies(String[] listaDino)
	 {
		 if(listaDino[0].equals("null"))
		 {
			 ChangeRoundThread.stop();
			 client.getConnManager().uscitaPartita();
			 flagStop = true;
			 this.setVisible(false);
			 this.validate();
			 frameGameManager.setVisible(true);
			 JOptionPane.showMessageDialog(getRootPane(), "La tua specie si e' estinta", "SPECIE ESTINTA", JOptionPane.INFORMATION_MESSAGE, error); 
		 }
	 }
	 /**
	  * metodo che aggiorna il campo di gioco
	  */
	 public void upDateFrameGame()
	 {
		String [] dinoList = extinctionSpecies();
		if(!flagStop)
		{
			this.drawMap(client.getConnManager().mappaGenerale());
			this.drawDinoList(client.getConnManager().listaDinosauri());
			this.drawDinoState(dinoList[0], client.getConnManager().statoDinosauro(dinoList[0]));
		}
	 }
	 
	 public void fight()
	 {
		 if(fight!=null)
		 {
			 fight.removeAll();
			 fightAnimation = null;
			 fight = null;
		 }
		 fight = new JFrame();
		 fight.setSize(300, 102);
		 fight.setUndecorated(true);
		 fight.setLocation((screenWidth-300)/2, (screenHeight-102)/2);
		 fight.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		 fightAnimation = new AnimationPanel("Images/combattimento.gif");
		 fightAnimation.setLayout(null);
		 fightAnimation.setSize(300, 102);
		 fight.getContentPane().add(fightAnimation);
		 fight.setVisible(true);
		 timer = new Timer(4800, this);
		 timer.start();
	 }
	 
	 public void eat()
	 {
		 if(eat!=null)
		 {
			 eat.removeAll();
			 eatAnimation = null;
			 eat = null;
		 }
		 eat = new JFrame();
		 eat.setSize(148, 102);
		 eat.setUndecorated(true);
		 eat.setLocation((screenWidth-148)/2, (screenHeight-102)/2);
		 eat.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		 eatAnimation = new AnimationPanel("Images/mangia_erba.gif");
		 eatAnimation.setLayout(null);
		 eatAnimation.setSize(300, 102);
		 eat.getContentPane().add(eatAnimation);
		 eat.setVisible(true);
		 timer = new Timer(1800, this);
		 timer.start();
	 }
}
