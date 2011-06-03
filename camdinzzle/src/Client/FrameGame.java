/**
 * 
 */
package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
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

	private Dimension screenSize;
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
	private boolean flagDinoList;
	private JTextArea dinoState;
	private JList playerList;
	private JPanel commandButtons;
	private JButton[] commandDinoButton;
	private JButton[] commandGameButton;
	private JLabel time;
	private JFrame ranking;
	private JTable ranking1;
	private JScrollPane ranking2;
	private Client client;
	private String dinoId;
	private int flag=0;
	private JScrollPane scrollPlayerList;
	private int timeGlobal;
	private boolean flagStop;
	
	private FrameGameManager frameGameManager;
	private final int widthControlPanel=300;
	private final int visibleRowCountDinoList=6;
	private final int visibleRowCountPlayerList=8;
	private final int maxAttempt = 5;
	private final Font fontDinoList = new Font("Serif", Font.PLAIN, 24); 
	private final Font fontDinoState = new Font("Serif", Font.PLAIN, 18);
	private final Font fontPlayerState = new Font("Serif", Font.PLAIN, 24);
	
	
	/**
	 * @param title
	 * @throws HeadlessException
	 * 
	 */
	
	public FrameGame(String title,Client client, FrameGameManager frameGameManager) throws HeadlessException{
		super(title);
		this.client=client;
		buttons = new JButton[row][col];
		iconVegetation = new ImageIcon("Images/vege.jpg");
		iconLand = new ImageIcon("Images/terra.jpg");
		iconWater = new ImageIcon("Images/acqua.jpg");
		iconDark = new ImageIcon("Images/red.jpg");
		iconCarrion = new ImageIcon("Images/carrion.jpg");
		iconLandDisable  = new ImageIcon("Images/terraDisable.jpg");
		iconWaterDisable  = new ImageIcon("Images/acquaDisable.jpg");
		iconVegetationDisable  = new ImageIcon("Images/vegeDisable.jpg");
		iconDino = new ImageIcon("Images/dino.jpg");
		this.setVisible(true);
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(0,0);
		this.frameGameManager = frameGameManager;
		flagStop = false;
		
		this.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
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
		panel.setPreferredSize(new Dimension((int)screenSize.getWidth()-widthControlPanel, (int)screenSize.getHeight()));
		panelControl.setPreferredSize(new Dimension(widthControlPanel, (int)screenSize.getHeight()));
		panelControlUp.setSize(new Dimension(widthControlPanel, (int)screenSize.getHeight()/14*6));
		panelControlDown.setSize(new Dimension(widthControlPanel, (int)screenSize.getHeight()/14*8));
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
		commandButtons.setPreferredSize(new Dimension(widthControlPanel-10, ((int)screenSize.getHeight()/14*4)));
		time = new JLabel();
		time.setPreferredSize(new Dimension(widthControlPanel-10, (int)screenSize.getHeight()/14));
		time.setVisible(true);
		panelControlDown.add(time,BorderLayout.SOUTH);
		for (int i=0; i < row; i++)
		{
			for(int j=0; j < col; j++)
			{
				buttons[i][j] = new JButton();
				buttons[i][j].setVisible(true);
				buttons[i][j].setSize(((int)screenSize.getWidth()-widthControlPanel)/col, ((int)screenSize.getHeight()/row));
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
		this.drawDinoList(msgDinoList);
		this.drawDinoState(msgDinoList[0], client.getConnManager().statoDinosauro(msgDinoList[0]));
		this.drawCommandButtons();
		this.drawPlayerList(client.getConnManager().listaGiocatori());
		this.repaint();
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
		/*AGGIORNAMENTO LISTA GIOCATORI
		 * dopo aver cliccato si aggiorna la lista giocatori
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
									String result;
									if(check[2].equals("v"))
										result = "vinto!";
									else
										result = "perso!";
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
				for(int i=0; i<2; i++)
				{
					commandDinoButton[i].setEnabled(true);
					drawDinoState(dinoId, client.getConnManager().statoDinosauro(dinoId));
				}
				flag=1;
			}
		}
	}

	@Override
	/*
	 * se il cursore entra in un bottone della mappa si crea un bordo nero
	 */
	public void mouseEntered(MouseEvent arg0) 
	{
		if ((arg0.getComponent() instanceof JButton)&&(!(arg0.getComponent().equals(commandDinoButton[0])))&&(!(arg0.getComponent().equals(commandDinoButton[1])))&&(!(arg0.getComponent().equals(commandGameButton[3])))&&(!(arg0.getComponent().equals(commandGameButton[0])))&&(!(arg0.getComponent().equals(commandGameButton[1])))&&(!(arg0.getComponent().equals(commandGameButton[2]))))
			((JButton) arg0.getComponent()).setBorder(BorderFactory.createLineBorder(Color.black));
	}

	@Override
	/*
	 * quando il cursore esce dal bottone della mappa il bordo si cancella
	 */
	public void mouseExited(MouseEvent arg0) 
	{
		if ((arg0.getComponent() instanceof JButton)&&(!(arg0.getComponent().equals(commandDinoButton[0])))&&(!(arg0.getComponent().equals(commandDinoButton[1])))&&(!(arg0.getComponent().equals(commandGameButton[3])))&&(!(arg0.getComponent().equals(commandGameButton[0])))&&(!(arg0.getComponent().equals(commandGameButton[1])))&&(!(arg0.getComponent().equals(commandGameButton[2]))))
			((JButton) arg0.getComponent()).setBorder(null);
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
						buttons[j][z].setDisabledIcon(iconVegetationDisable);
						buttons[j][z].setToolTipText(null);
					}
				else if(mapList.get(i).compareTo("t")==0)
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "land");
						buttons[j][z].setDisabledIcon(iconLandDisable);
						buttons[j][z].setToolTipText(null);
					}
				else if(mapList.get(i).compareTo("a")==0)
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "water");
						buttons[j][z].setDisabledIcon(iconWaterDisable);
						buttons[j][z].setToolTipText(null);
					}
				else if(mapList.get(i).compareTo("d")==0)
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "dinosaur");
						buttons[j][z].setDisabledIcon(iconLandDisable);
						buttons[j][z].setToolTipText(null);
					}
				else
					{
						buttons[j][z].setName(buttons[j][z].getName().substring(0, buttons[j][z].getName().indexOf(";")+1) + "land");
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
				errorMessageServer();
			}
			
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
				if(maxRow<0)
					maxRow=0;
				if(maxCol>=this.col)
					maxCol=this.col-1;
				int i=4;

				for(row=startRow;row>=maxRow;row--)
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
				for(int rowEnable=startRow; rowEnable>=maxRow; rowEnable--)
				{
					for(int colEnable=startCol; colEnable<=maxCol; colEnable++)
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
				if(dinoList==null)
				{
					dinoList = new JList(msgDinoList);
					flagDinoList=true;
				}
				dinoList.setListData(msgDinoList);
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
				dinoList.setPreferredSize(new Dimension(widthControlPanel-25,(int)screenSize.getHeight()/14*4));
				dinoList.setFont(fontDinoList);	
				if(flagDinoList)
				{
					panelControlUp.add(new JScrollPane(dinoList),BorderLayout.NORTH);
					flagDinoList=false;
				}
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
		String timeString = String.valueOf(timeInt);
		time.setText("Mancano " + timeString + " sec allo scadere del turno");
		time.setVisible(true);
		panelControlDown.repaint();
	}
	public int getTime()
	{
		return timeGlobal;
	}
	public void drawRound(String user)
	{
		time.setText("      ora e' il turno di:\n" + user);
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
			ranking = new JFrame("Classifica");
			ranking.setVisible(true);
			ranking.setSize(new Dimension(350, 500));
			ranking2 = new JScrollPane();
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
			ranking.setLocation((int)screenSize.getWidth()/2-175, (int)screenSize.getHeight()/2-250);
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
				if(dinoState==null)
					dinoState = new JTextArea(newMsgDinoState);
				else
					dinoState.setText(newMsgDinoState);
				dinoState.setVisible(true);
				dinoState.setPreferredSize(new Dimension(widthControlPanel,(int)screenSize.getHeight()/14*3));
				dinoState.setFont(fontDinoState);
				dinoState.setEditable(false);
				panelControlUp.add(dinoState,BorderLayout.SOUTH);
				panelControlUp.repaint();
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
				if(playerList!=null)
				{
					scrollPlayerList.remove(playerList);
					panelControlDown.remove(scrollPlayerList);
				}
				String[] newMsgPlayerList = new String[8];
				for(int i=0;i<msgPlayerList.length-1; i++)
					newMsgPlayerList[i]=msgPlayerList[i+1];
				playerList = new JList(newMsgPlayerList);
				playerList.setVisibleRowCount(visibleRowCountPlayerList);
				playerList.setVisible(true);
				playerList.setPreferredSize(new Dimension(widthControlPanel-20,(int)screenSize.getHeight()/14*2));
				playerList.setFont(fontPlayerState);
				scrollPlayerList = new JScrollPane(playerList);
				scrollPlayerList.setVisible(true);
				scrollPlayerList.setPreferredSize(new Dimension(widthControlPanel-5,(int)screenSize.getHeight()/14*2));
				panelControlDown.add(scrollPlayerList,BorderLayout.CENTER);
				panelControlDown.validate();
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
		 commandGameButton = new JButton[4];
		 commandGameButton[0] = new JButton("Classifica");
		 commandGameButton[0].setName("Classifica");
		 commandGameButton[1] = new JButton("Passa Turno");
		 commandGameButton[1].setName("Passa Turno");
		 commandGameButton[2] = new JButton("Esci dalla Partita");
		 commandGameButton[2].setName("Esci dalla Partita");
		 commandGameButton[3] = new JButton("Aggiorna lista giocatori");
		 commandGameButton[3].setName("Aggiorna lista giocatori");
		 
		 for(int i=0;i<2;i++)
		 {
			 commandDinoButton[i].setPreferredSize(new Dimension(widthControlPanel-10,(int)screenSize.getHeight()/14/5*3));
			 commandDinoButton[i].setVisible(true);
			 commandDinoButton[i].setEnabled(false);
			 commandDinoButton[i].addMouseListener(this);
			 commandButtons.add(commandDinoButton[i]);
		 }
		 for(int i=0;i<4;i++)
		 {
			 commandGameButton[i].setPreferredSize(new Dimension(widthControlPanel - 10, (int)screenSize.getHeight()/14/5*3));
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
	 
	 public void upDateFrameGame()
	 {
		extinctionSpecies();
		if(!flagStop)
		{
			this.drawMap(client.getConnManager().mappaGenerale());
			this.drawDinoList(client.getConnManager().listaDinosauri());
			this.drawPlayerList(client.getConnManager().listaGiocatori());
		}
	 }

}
