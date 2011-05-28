/**
 * 
 */
package Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
	
	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public FrameGameManager(String title, Client client) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
		
		this.setLayout(null);
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		titleframe = new JLabel("ManagerPanel v 1.0");
		creaRazza = new JLabel("Create new Species");
		accPartita = new JLabel("Access to Game");
		lisGiocatori = new JLabel("Give List of Players");
		classifica = new JLabel("Give the Classific");
		logout = new JLabel("Exit to Camdinzzle");
		
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

	/**
	 * @param title
	 * @param gc
	 */
	public FrameGameManager(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
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
	public void mouseClicked(MouseEvent arg0) {
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
