/**
 * 
 */
package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
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
 * @author Andrea
 *
 */


public class FrameGameManager extends JFrame implements WindowListener, MouseListener{

	private static final long serialVersionUID = 1L;
	private Dimension screenSize;
	private JLabel titleframe;
	private JLabel creaRazza;
	private JLabel accPartita;
	private JLabel lisGiocatori;
	private JLabel classifica;
	private JLabel logout;
	private Client client;
	
	private JFrame creaRazzaFrame;
	private JLabel razzatitle;
	private JLabel razza_testo;
	private JLabel choice;
	private JTextField razza_valore;
	private JRadioButton Vege;
	private JRadioButton Carn;
	private ButtonGroup radiogroup;
	private JButton razza_button;
	
	private JFrame listaGiocatori;
	private JPanel pannelloGiocatori;
	private JLabel[] postiGiocatori;
	private JLabel titoloGiocatori;
	
	private JFrame ranking;
	private JScrollPane ranking2;
	private JTable ranking1;
	
	private JFrame dinoChosenFrame;
	private JPanel dinoChosenPanel;
	private ImageIcon[] iconChosenDino;
	private JButton[] buttonDino;
	private boolean setImage;
	
	private FrameGame frameGame;
	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public FrameGameManager(String title, Client client) throws HeadlessException {
		super(title);

		this.client = client;
		this.setResizable(false);
		this.setLayout(null);
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		titleframe = new JLabel("ManagerPanel v 1.0");
		creaRazza = new JLabel("Create new Species");
		accPartita = new JLabel("Access to Game");
		lisGiocatori = new JLabel("Give List of Players");
		classifica = new JLabel("Give the Classific");
		logout = new JLabel("Exit to Camdinzzle");
		razzatitle = new JLabel("Create new Species");
		razza_testo = new JLabel("Insert the Name");
		choice = new JLabel("Select the type of new Species :");
		razza_valore = new JTextField();
		radiogroup = new ButtonGroup();
		Vege = new JRadioButton("Vegetarian");
		Carn = new JRadioButton("Carnivorous");
		razza_button = new JButton("Create new Species");
		radiogroup.add(Vege);
		radiogroup.add(Carn);
		razza_button.setSize(180, 20);
		razzatitle.setSize(200,20);
		razza_testo.setSize(130,20);
		razza_valore.setSize(120,20);
		Vege.setSize(130,20);
		Carn.setSize(130,20);
		razzatitle.setLocation(10,0);
		razza_testo.setLocation(10,50);
		razza_valore.setLocation(180, 50);
		Vege.setLocation(10,130);
		Vege.setSelected(true);
		Carn.setLocation(150,130);
		Carn.setSelected(false);
		razza_button.setLocation(10,190);
		razza_button.addMouseListener(this);
		
		iconChosenDino = new ImageIcon[7];
		iconChosenDino[0] = new ImageIcon("Images/sauro.jpg");
		iconChosenDino[1] = new ImageIcon("Images/T_REX.jpg");
		iconChosenDino[2] = new ImageIcon("Images/Saurolophus.jpg");
		iconChosenDino[3] = new ImageIcon("Images/stegosauro.jpg");
		iconChosenDino[4] = new ImageIcon("Images/triceratopo.jpg");
		iconChosenDino[5] = new ImageIcon("Images/velociraptor.jpg");
		iconChosenDino[6] = new ImageIcon("Images/brontosauro.jpg");
		
		
		creaRazza.setSize(300, 76);
		accPartita.setSize(300,76);
		lisGiocatori.setSize(300,76);
		classifica.setSize(300,76);
		logout.setSize(300,76);
		
		creaRazza.setLocation(0,20);
		accPartita.setLocation(0,96);
		lisGiocatori.setLocation(0,172);
		classifica.setLocation(0, 248);
		logout.setLocation(0, 324);
		
		creaRazza.setBorder(BorderFactory.createLineBorder(Color.black));
		accPartita.setBorder(BorderFactory.createLineBorder(Color.black));
		lisGiocatori.setBorder(BorderFactory.createLineBorder(Color.black));
		classifica.setBorder(BorderFactory.createLineBorder(Color.black));
		logout.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setSize(300,450);
		this.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-400)/2);
		this.setVisible(true);
		titleframe.setSize(300,20);
		titleframe.setLocation(10,0);
		
		creaRazza.addMouseListener(this);
		accPartita.addMouseListener(this);
		lisGiocatori.addMouseListener(this);
		classifica.addMouseListener(this);
		logout.addMouseListener(this);
		this.addWindowListener(this);
		
		this.add(titleframe);
		this.add( creaRazza);
		this.add(accPartita);
		this.add(lisGiocatori);
		this.add(classifica);
		this.add(logout);
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getComponent().equals(creaRazza))
		{
			//Frame con i campi per la creazione della razza
			creaRazzaFrame = new JFrame("Create new Species");
			creaRazzaFrame.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-200)/2);
			creaRazzaFrame.setLayout(null);
			creaRazzaFrame.setVisible(true);
			creaRazzaFrame.setSize(360,250);
			creaRazzaFrame.add(this.razza_button);
			creaRazzaFrame.add(this.Vege);
			creaRazzaFrame.add(this.Carn);
			creaRazzaFrame.add(this.razzatitle);
			creaRazzaFrame.add(this.razza_testo);
			creaRazzaFrame.add(this.razza_valore);
		}
		else if (arg0.getComponent().equals(accPartita))
		{
			String gameAccess = client.getConnManager().accessoPartita();
			if(gameAccess != null)
			{
				String[] response = ClientMessageBroker.manageGameAccess(gameAccess);
				if(response == null)
				{
					JOptionPane.showMessageDialog(this,"You have sent an invalid message!!!", "Access Game Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(response[0].compareTo("ok")==0)
				{
					
					this.setVisible(false);
					this.validate();
					int opt = chosenDinoImage();
					if(opt==-1)
						opt=0;
					frameGame = new FrameGame("Isola dei Dinosauri",client, this,iconChosenDino[opt]);
					ChangeRoundThread changeRoundThread = new ChangeRoundThread("Change Round", client, frameGame);
						
					(new Thread(changeRoundThread)).start();
					
				}
				else if (response[0].compareTo("no")==0)
				{
					if (response[1].compareTo("troppiGiocatori")==0)
					{
						JOptionPane.showMessageDialog(this,"There aren't free spot!!!", "Access Game Error", JOptionPane.ERROR_MESSAGE);
					}
					else if(response[1].compareTo("tokenNonValido")==0)
					{
						JOptionPane.showMessageDialog(this,"Before access to game you must create a new specie!!!", "Access Game Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		else if (arg0.getComponent().equals(lisGiocatori))
		{
			String[] response = client.getConnManager().listaGiocatori();
			if (response == null)
			{
				JOptionPane.showMessageDialog(this,"You have sent an invalid message!!!", "Lista Giocatori Error", JOptionPane.ERROR_MESSAGE);
			}
			else if (response[0].compareTo("null") == 0)
			{
				JOptionPane.showMessageDialog(this,"No players in game", "Lista Giocatori", JOptionPane.INFORMATION_MESSAGE);
			}
			else if (response[0].compareTo("listaGiocatori")==0)
			{
				
				listaGiocatori = new JFrame("List of Players");
				listaGiocatori.setLayout(new BorderLayout());
				listaGiocatori.setVisible(true);
				listaGiocatori.setSize(300, 600);
				listaGiocatori.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-600)/2);
				titoloGiocatori = new JLabel("Players of this Game:");
				listaGiocatori.add(titoloGiocatori, BorderLayout.NORTH);
				titoloGiocatori.setSize(300,20);
				postiGiocatori = new JLabel[8];
				pannelloGiocatori = new JPanel();
				listaGiocatori.add(pannelloGiocatori,BorderLayout.CENTER);
				pannelloGiocatori.setLayout(new GridLayout(8,1));
				
				for (int i=1; i < response.length; i++)
				{
					postiGiocatori[i-1] = new JLabel();
					postiGiocatori[i-1].setText(response[i]);
					pannelloGiocatori.add(postiGiocatori[i-1]);
				}	
				
				
				listaGiocatori.repaint();
			}
			else if (response[0].compareTo("no")==0)
			{
				JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "Lista Giocatori Error", JOptionPane.ERROR_MESSAGE);
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
					 JOptionPane.showMessageDialog(this, "Azione non compiuta", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				//errorMessage();
			}
		}
		else if (arg0.getComponent().equals(logout))
		{
			int ritorno = JOptionPane.showConfirmDialog(
				    this,
				    "Do you really want to exit from Camdinzzle?",
				    "Exit Question",
				    JOptionPane.YES_NO_OPTION);
			if (ritorno == 0){
				String[] response = ClientMessageBroker.manageLogout(client.getConnManager().logout());
				if(response[0].compareTo("ok")==0)
				{
					System.exit(0);
				}
				if(response[0].compareTo("no")==0)
				{
					JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "Logout Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else if (arg0.getComponent().equals(razza_button))
		{
			 creaRazzaFrame.setVisible(false);
			 String type = null;
			 if(Vege.isSelected()) type = new String("e");
			 else  type = new String("c");
			 System.out.println("sonoprima");
			 String newRace = client.getConnManager().creaRazza(razza_valore.getText(), type);
			 if(newRace != null)
			 {
				 String[] response = ClientMessageBroker.manageCreateSpecies(newRace);	 
				 if (response == null) JOptionPane.showMessageDialog(this,"You have sent an invalid message!!!", "New Species Error", JOptionPane.ERROR_MESSAGE);
				 else if(response[0].compareTo("ok")==0)
				 {
					 JOptionPane.showMessageDialog(this,"New Specie's been created!!!", "New Species", JOptionPane.INFORMATION_MESSAGE);
				 }
				 else if (response[0].compareTo("no")==0)
				 {
					 if(response[1] != null)
					 {
						 if(response[1].compareTo("nomeRazzaOccupato")==0) JOptionPane.showMessageDialog(this,"Name busy!!! try with another name", "New Species Error", JOptionPane.ERROR_MESSAGE);
						 else if(response[1].compareTo("tokenNonValido")==0)JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "New Species Error", JOptionPane.ERROR_MESSAGE);
					 }
					 else JOptionPane.showMessageDialog(this,"You already have created another specie !!!", "New Species Error", JOptionPane.ERROR_MESSAGE);
				 }
			 }
		}
	}
	
	public int chosenDinoImage()
	{
		 return JOptionPane.showOptionDialog(this, "                                        scegli l'icona del dinosauro                                               ", "Chosen Icon Dinosaur", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, iconChosenDino, null);	
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) 
	{
		int ritorno = JOptionPane.showConfirmDialog(
			    this,
			    "Do you really want to exit from Camdinzzle?",
			    "Exit Question",
			    JOptionPane.YES_NO_OPTION);
		if (ritorno == 0){
			String[] response = ClientMessageBroker.manageLogout(client.getConnManager().logout());
			if(response[0].compareTo("ok")==0)
			{
				System.exit(0);
			}
			if(response[0].compareTo("no")==0)
			{
				JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "Logout Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) 
	{
		int ritorno = JOptionPane.showConfirmDialog(
			    this,
			    "Do you really want to exit from Camdinzzle?",
			    "Exit Question",
			    JOptionPane.YES_NO_OPTION);
		if (ritorno == 0){
			String[] response = ClientMessageBroker.manageLogout(client.getConnManager().logout());
			if(response[0].compareTo("ok")==0)
			{
				System.exit(0);
			}
			if(response[0].compareTo("no")==0)
			{
				JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "Logout Error", JOptionPane.ERROR_MESSAGE);
			}
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
