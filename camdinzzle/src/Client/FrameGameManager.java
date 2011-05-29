/**
 * 
 */
package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * @author Andrea
 *
 */


public class FrameGameManager extends JFrame implements WindowListener, MouseListener{

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
	private FrameGame game;
	
	private JFrame listaGiocatori;
	private JPanel pannelloGiocatori;
	private JLabel[] postiGiocatori;
	private JLabel titoloGiocatori;
	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public FrameGameManager(String title, Client client) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
		this.client = client;
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
		// TODO Auto-generated method stub
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
			String[] response = ClientMessageBroker.manageGameAccess(client.getConnManager().accessoPartita());
			if(response == null)
			{
				JOptionPane.showMessageDialog(this,"You have sent an invalid message!!!", "Access Game Error", JOptionPane.ERROR_MESSAGE);
			}
			else if(response[0].compareTo("ok")==0)
			{
				game = new FrameGame("Game",client);
				this.setVisible(false);
				game.setVisible(true);
			}
			else if (response[0].compareTo("no")==0)
			{
				if (response[1].compareTo("troppiGiocatori")==0)
				{
					JOptionPane.showMessageDialog(this,"There aren't free spot!!!", "Access Game Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(response[1].compareTo("tokenNonValido")==0)
				{
					JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "Access Game Error", JOptionPane.ERROR_MESSAGE);
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
			else if (response[0].compareTo("listaGiocatori")==0)
			{
				System.out.println("ciaoasdasdas");
				listaGiocatori = new JFrame("List of Players");
				listaGiocatori.setLayout(new BorderLayout());
				listaGiocatori.setVisible(true);
				listaGiocatori.setSize(300, 600);
				listaGiocatori.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-600)/2);
				titoloGiocatori = new JLabel("Players of this Game:");
				listaGiocatori.add(titoloGiocatori, BorderLayout.NORTH);
				postiGiocatori = new JLabel[8];
				pannelloGiocatori = new JPanel();
				listaGiocatori.add(pannelloGiocatori,BorderLayout.SOUTH);
				pannelloGiocatori.setLayout(new GridLayout(1,8));
				int j;
				for (int i=1; i < response.length; i++)
				{
					postiGiocatori[i-1].setText(response[i]);
				}
				for (int i=0; i < postiGiocatori.length; i++)
				{
					if (postiGiocatori[i].getText() == "") postiGiocatori[i].setText("Spot Empty");
					pannelloGiocatori.add(postiGiocatori[i]);
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
			 if(Vege.isSelected()) type = new String("c");
			 else  type = new String("e");
			 System.out.println("sonoprima");
			 String[] response = ClientMessageBroker.manageCreateSpecies(client.getConnManager().creaRazza(razza_valore.getText(), type));	 
			 if (response == null) JOptionPane.showMessageDialog(this,"You have sent an invalid message!!!", "New Species Error", JOptionPane.ERROR_MESSAGE);
			 else if(response[0].compareTo("ok")==0)
			 {
				 JOptionPane.showMessageDialog(this,"New Specie's been created!!!", "New Species", JOptionPane.INFORMATION_MESSAGE);
			 }
			 else if (response[0].compareTo("no")==0)
			 {
				if(response[1].compareTo("nomeRazzaOccupato")==0) JOptionPane.showMessageDialog(this,"Name busy!!! try with another name", "New Species Error", JOptionPane.ERROR_MESSAGE);
				else if(response[1].compareTo("tokenNonValido")==0)JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "New Species Error", JOptionPane.ERROR_MESSAGE);
				else if(response[1].compareTo("razzaGiaCreata")==0)JOptionPane.showMessageDialog(this,"You already have created another specie !!!", "New Species Error", JOptionPane.ERROR_MESSAGE);
			 }
			 
		}
	}
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}