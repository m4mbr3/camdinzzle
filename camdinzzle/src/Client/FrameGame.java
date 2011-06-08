/**
 * 
 */
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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * @author Andrea
 *
 */
public class FrameGame extends JFrame implements MouseListener,Visual,ActionListener, WindowListener {

	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JPanel panelControl;
	private JPanel panelControlUp;
	private JPanel panelControlDown;
	final private int row = 40;
	final private int col = 40;
	private JButton[][] buttons;
	private ImageIcon iconVegetation;
	private ImageIcon iconWater;
	private ImageIcon iconCarrion;
	private ImageIcon iconDino;
	private ImageIcon iconDark;
	private ImageIcon iconLand;
	private ImageIcon iconVegetationDisable;
	private ImageIcon iconWaterDisable;
	private ImageIcon iconLandDisable;
	private JList dinoList;
	private JTextArea dinoState;
	private JScrollPane scrollDinoState;
//	private JList playerList;
	private JFrame listaGiocatori;
	private JLabel titoloGiocatori;
	private JLabel[] postiGiocatori;
	private JPanel pannelloGiocatori;
	private JPanel commandButtons;
	private JButton[] commandDinoButton;
	private JButton[] commandGameButton;
	private JLabel time;
	private int timeMin;
	private int timeSec;
	private JFrame rankingFrame;
	private JTable rankingTable;
	private JScrollPane rankingScroll;
	private Client client;
	private String dinoId;
	private int flag=0;
//	private JScrollPane scrollPlayerList;
	private int timeGlobal;
	private boolean flagStop;
	private String nameSpecie;
	private JButton cellClicked;
	
	private FrameGameManager frameGameManager;
	private final int widthControlPanel=300;
	private final int visibleRowCountDinoList=4;
//	private final int visibleRowCountPlayerList=8;
	private final int maxAttempt = 5;
	private final Font fontDinoList = new Font("Serif", Font.PLAIN, 24); 
	private final Font fontDinoState = new Font("Serif", Font.PLAIN, 15);
//	private final Font fontPlayerState = new Font("Serif", Font.PLAIN, 24);
	private int screenHeight;
	private int screenWidth;

	
	
	/**
	 * @param title
	 * @throws HeadlessException
	 * 
	 */
	
	public FrameGame(String title,Client client, FrameGameManager frameGameManager) throws HeadlessException{
		super(title);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();

		screenHeight = (int)g.getMaximumWindowBounds().getHeight();
		screenWidth = (int)g.getMaximumWindowBounds().getWidth();
		this.client=client;
		buttons = new JButton[row][col];
		iconVegetation = new ImageIcon("Images/vege.jpg");
		iconLand = new ImageIcon("Images/terra.jpg");
		iconWater = new ImageIcon("Images/acqua.jpg");
		iconDark = new ImageIcon("Images/buio.jpg");
		iconCarrion = new ImageIcon("Images/carrion.jpg");
		iconLandDisable  = new ImageIcon("Images/terraDisable.jpg");
		iconWaterDisable  = new ImageIcon("Images/acquaDisable.jpg");
		iconVegetationDisable  = new ImageIcon("Images/vegeDisable.jpg");
		iconDino = new ImageIcon("Images/T_REX.jpg");
		this.setVisible(true);
		this.setLocation(0,0);
		this.frameGameManager = frameGameManager;
		flagStop = false;
		
		this.setSize(screenWidth, screenHeight);
		this.addWindowListener(this);
		panel = new JPanel();
		panelControl = new JPanel();
		panelControlUp = new JPanel();
		panelControlDown = new JPanel();
		panel.setVisible(true);
		panelControl.setVisible(true);
		panelControlUp.setVisible(true);
		panelControlDown.setVisible(true);
		panel.setBorder(null);
		panel.setPreferredSize(new Dimension(screenWidth-widthControlPanel, screenHeight));
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
				buttons[i][j].setIcon(iconLand);
				buttons[i][j].setEnabled(false);
				panel.add(buttons[i][j]);
			}
		}	
		
		this.add(panel,BorderLayout.WEST);
		this.add(panelControl, BorderLayout.EAST);	
		this.validate();
		this.drawMap(client.getConnManager().mappaGenerale());
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
				frameGameManager.setVisible(true);
			}
		}
	}
	


	@Override
	public void actionPerformed(ActionEvent e) {}


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
					String[] option = {"yes","no"};
					int opt = JOptionPane.showOptionDialog(panel, "Vuoi muovere il dinosauro", "Muovi Dinosauro", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, option, "yes");
					((JButton)arg0.getComponent()).setBorder(null);
					if(opt==0)
					{
						cellClicked.setBorder(null);
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
									String result;
									if(check[2].equals("v"))
										result = " vinto!";
									else
										result = " perso!";
									JOptionPane.showMessageDialog(panel, "Combattimento" + result, "Combattimento", JOptionPane.INFORMATION_MESSAGE, null);
								}
								extinctionSpecies();
								if(!flagStop)
								{
									drawMap(client.getConnManager().mappaGenerale());
									drawDinoState(dinoId, client.getConnManager().statoDinosauro(dinoId));
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
				flag=0;
			}
			else if(((String)arg0.getComponent().getName()).contains("-"))
			{
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
						buttons[j][z].setIcon(iconVegetationDisable);
						buttons[j][z].setDisabledIcon(iconVegetationDisable);
						buttons[j][z].setToolTipText(null);
					}
				else if(mapList.get(i).compareTo("t")==0)
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "land");
						buttons[j][z].setIcon(iconLandDisable);
						buttons[j][z].setDisabledIcon(iconLandDisable);
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
						buttons[j][z].setIcon(iconLandDisable);
						buttons[j][z].setDisabledIcon(iconLandDisable);
						buttons[j][z].setToolTipText(null);
					}
				else
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "land");
						buttons[j][z].setIcon(iconLandDisable);
						buttons[j][z].setDisabledIcon(iconLandDisable);
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

				for(row=startRow;row>maxRow;row--)
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
								buttons[row][col].setIcon(iconLand);
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
									buttons[row][col].setToolTipText("id dinosauro: " + energySplit[1]);
									buttons[row][col].setIcon(iconDino);
								}
								else if(energySplit[0].compareTo("v")==0)
								{
									buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + "vegetation");
									buttons[row][col].setToolTipText("energia vegetazione: " + energySplit[1]);
									buttons[row][col].setIcon(iconVegetation);
								}
								else if(energySplit[0].compareTo("c")==0)
								{
									buttons[row][col].setName(buttons[row][col].getName().substring(0, buttons[row][col].getName().indexOf(";")+1) + "carrion");
									buttons[row][col].setToolTipText("energia carogna: " + energySplit[1]);
									buttons[row][col].setIcon(iconCarrion);
								}
							}
							//col++;
						i++;
						}
					}
				}
				for(int rowEnable=startRow; rowEnable>maxRow; rowEnable--)
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
	public void drawDinoList(String[] msgDinoList) 
	{
		if(msgDinoList!=null)
		{
			if(!msgDinoList[0].equals("no"))
			{
				if(dinoList!=null)
				{
					panelControlUp.remove(dinoList);
					dinoList = null;
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
						drawDinoState(dinoList.getSelectedValue().toString(), client.getConnManager().statoDinosauro(dinoList.getSelectedValue().toString()));
					}
				});
				dinoList.setVisible(true);
				dinoList.setPreferredSize(new Dimension(widthControlPanel-55,screenHeight/14*2));
				dinoList.setFont(fontDinoList);
				panelControlUp.add(new JScrollPane(dinoList),BorderLayout.NORTH);
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
		
		time.setText("Tempo allo scadere del turno  " + timeStringMin +":"+ timeStringSec);
		time.setVisible(true);
		panelControlDown.repaint();
	}
	public int getTime()
	{
		return timeGlobal;
	}
	public void drawRound(String user)
	{
		time.setText("      ora e' il turno di: \n" + user);
		time.setVisible(true);
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
			rankingFrame = new JFrame("Classifica");
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
		rankingTable = new JTable(rowData, columnNames);
		rankingTable.setVisible(true);
		rankingScroll.getViewport().add(rankingTable);
		rankingFrame.add(rankingScroll);
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
					msgDinoState[2]="Carnivorous";
				else
					msgDinoState[2]="Vegetarian";
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
				
				newMsgDinoState+="per muoversi selezionare il dinosauro\n";
				newMsgDinoState+="        e poi la destinazione";
				if(scrollDinoState!=null)
				{
					scrollDinoState.remove(dinoState);
					panelControlUp.remove(scrollDinoState);
				}
				dinoState = new JTextArea(newMsgDinoState);
				dinoState.setVisible(true);
				dinoState.setSize(new Dimension(widthControlPanel-50,screenHeight/14*3));
				dinoState.setFont(fontDinoState);
				dinoState.setEditable(false);
				scrollDinoState = new JScrollPane(dinoState);
				scrollDinoState.setSize(new Dimension(widthControlPanel-25,screenHeight/14*3));
				scrollDinoState.setVisible(true);
				scrollDinoState.validate();
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
				if(listaGiocatori==null)
				{
					listaGiocatori = new JFrame("List of Players");
				}
				else
				{
					listaGiocatori.removeAll();
					postiGiocatori = null;
					titoloGiocatori = null;
					pannelloGiocatori = null;
				}
				listaGiocatori.setLayout(new BorderLayout());
				listaGiocatori.setVisible(true);
				listaGiocatori.setSize(300, 600);
				listaGiocatori.setLocation((screenWidth-300)/2,(int)(screenHeight-600)/2);
				titoloGiocatori = new JLabel("<html><h2>Players of this Game:</h2></html>");
				listaGiocatori.add(titoloGiocatori, BorderLayout.NORTH);
				titoloGiocatori.setSize(300,20);
				postiGiocatori = new JLabel[8];
				pannelloGiocatori = new JPanel();
				listaGiocatori.add(pannelloGiocatori,BorderLayout.CENTER);
				pannelloGiocatori.setLayout(new GridLayout(8,1));
				
				for (int i=1; i < msgPlayerList.length; i++)
				{
					postiGiocatori[i-1] = new JLabel();
					postiGiocatori[i-1].setText(msgPlayerList[i]);
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
		 commandDinoButton[0] = new JButton("Cresci Dinosauro (Dimensione*500)");
		 commandDinoButton[0].setName("Cresci Dinosauro");
		 commandDinoButton[1] = new JButton("Deponi Uovo (1500)");
		 commandDinoButton[1].setName("Deponi Uovo");
		 commandGameButton = new JButton[5];
		 commandGameButton[0] = new JButton("Classifica");
		 commandGameButton[0].setName("Classifica");
		 commandGameButton[1] = new JButton("Passa Turno");
		 commandGameButton[1].setName("Passa Turno");
		 commandGameButton[2] = new JButton("Esci dalla Partita");
		 commandGameButton[2].setName("Esci dalla Partita");
		 commandGameButton[3] = new JButton("Lista giocatori");
		 commandGameButton[3].setName("Lista giocatori");
		 commandGameButton[4] = new JButton("Aggiorna Mappa");
		 commandGameButton[4].setName("Aggiorna Mappa");
		 
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
			 commandButtons.add(commandGameButton[i]);
		 }
		 panelControlDown.add(commandButtons, BorderLayout.NORTH);
		 

	 }
	 /**
	  * messaggio di errore in caso di messaggio di risposta illeggibile
	  */
	 public void errorMessage()
	 {
		 JOptionPane.showMessageDialog(panel, "Azione non compiuta", "Error", JOptionPane.ERROR_MESSAGE);
	 }
	 
	 /**
	  * messaggio di errore con l'Arrey di stringhe ricevuta dal server
	  * @param msg
	  */
	 public void errorMessageServer(String[] msg)
	 {
		 JOptionPane.showMessageDialog(panel, msg);
	 }
	 
	 /**
	  * messaggio di errore in caso di comunicazione non corretta con il server
	  */
	 public void errorMessageServer()
	 {
		 JOptionPane.showMessageDialog(panel, "Errore nella comunicazione col server");
	 }
	 
	 /**
	  * messaggio di errore con l'ArrayList ricevuto dal server
	  * @param msg
	  */
	 public void errorMessageServer(ArrayList<String> msg)
	 {
		 JOptionPane.showMessageDialog(panel, msg);
	 }
	 
	 /**
	  * messaggio di errore con la striga ricevuto dal server
	  * @param msg
	  */
	 public void errorMessageServer(String msg)
	 {
		 JOptionPane.showMessageDialog(panel, msg);
	 }
	 
	 private void extinctionSpecies()
	 {
		 String[] listaDino = client.getConnManager().listaDinosauri();
		 if(listaDino[0].equals("null"))
		 {
			 ChangeRoundThread.stop();
			 client.getConnManager().uscitaPartita();
			 this.setVisible(false);
			 frameGameManager.setVisible(true);
			 JOptionPane.showMessageDialog(getRootPane(), "La tua specie si e' estinta", "SPECIE ESTINTA", JOptionPane.INFORMATION_MESSAGE, null); 
			 flagStop = true;
		 }
	 }
	 private void extinctionSpecies(String[] listaDino)
	 {
		 if(listaDino[0].equals("null"))
		 {
			 ChangeRoundThread.stop();
			 client.getConnManager().uscitaPartita();
			 flagStop = true;
			 this.setVisible(false);
//			 this.repaint();
			 this.validate();
			 frameGameManager.setVisible(true);
			 JOptionPane.showMessageDialog(getRootPane(), "La tua specie si e' estinta", "SPECIE ESTINTA", JOptionPane.INFORMATION_MESSAGE, null); 
		 }
	 }
	 
	 public void upDateFrameGame()
	 {
		extinctionSpecies();
		if(!flagStop)
		{
			this.drawMap(client.getConnManager().mappaGenerale());
			this.drawDinoList(client.getConnManager().listaDinosauri());
		}
	 }
}
