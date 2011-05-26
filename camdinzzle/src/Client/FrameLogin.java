/**
 * 
 */
package Client;

import java.awt.Color;
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
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Andrea
 *
 */
public class FrameLogin extends JFrame implements ActionListener,WindowListener,ChangeListener, MouseListener{

	private JLabel username_label;
	private JLabel password_label;
	private JLabel camdinzzle;
	private JTextField username;
	private JPasswordField password;
	private JPanel panel;
	private JPanel panel_newUser;
	private JButton send;
	private JButton send_newUser;
	private Dimension screenSize;
	private Client client;
	private JLabel new_user;
	private JFrame new_userframe;
	/**
	 * @throws HeadlessException
	 */
	public FrameLogin() throws HeadlessException {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param gc
	 */
	public FrameLogin(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public FrameLogin(String title, Client client) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
		this.client = client;
		this.setVisible(true);
		this.setSize(330,300);
		this.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-300)/2);
		new_user = new JLabel("Are you a new user?Click here!");
		camdinzzle = new JLabel("Camdinzzle on Socket v1.0");
		username_label = new JLabel("Username :");
		password_label = new JLabel("Password :");
		username = new JTextField();
		password = new JPasswordField();
		panel = new JPanel();
		panel_newUser = new JPanel();
		send_newUser = new JButton("Register it!");
		send = new JButton("Send Information");
		new_user.setSize(250,20);
		panel_newUser.setSize(300,300);
		panel.setSize(300,300);
		send_newUser.setSize(180,20);
		send.setSize(180, 20);
		camdinzzle.setSize(200,70);
		username_label.setSize(90,20);
		password_label.setSize(90,20);
		
		username.setSize(160,20);
		password.setSize(160,20);
		panel.setLayout(null);
		
		new_user.setVisible(true);
		
		
		camdinzzle.setVisible(true);
		send_newUser.setVisible(true);
		send.setVisible(true);
		username_label.setVisible(true);
		password_label.setVisible(true);
		username.setVisible(true);
		password.setVisible(true);
		panel_newUser.setVisible(true);
		panel.setVisible(true);
		new_user.setLocation(50,200);
		camdinzzle.setLocation(50,0);
		send_newUser.setLocation(50,250);
		send.setLocation(50, 250);
		username_label.setLocation(50,80);
		password_label.setLocation(50,120);
		username.setLocation(140,80);
		password.setLocation(140, 120);
	
		
		panel.add(camdinzzle);
		panel.add(send);
		panel.add(password_label);
		panel.add(username_label);
		panel.add(password);
		panel.add(username);
		panel.add(new_user);
		

		send.addMouseListener(this);
		new_user.addMouseListener(this);
		send_newUser.addMouseListener(this);
		
		panel.repaint();
		this.add(panel);
		this.repaint();
	}

	/**
	 * @param title
	 * @param gc
	 */
	public FrameLogin(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		System.exit(0);
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
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getComponent().equals(new_user))
		{
			this.setVisible(false);
			new_userframe = new JFrame("New User");
			new_userframe.setVisible(true);
			new_userframe.addWindowListener(this);
			new_userframe.setSize(330,300);
			new_userframe.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-300)/2);
			panel.remove(send);
			panel.add(send_newUser);
			panel.remove(this.new_user);
			new_userframe.add(panel);
			
		}
		if (arg0.getComponent().equals(send))
		{
			
					this.setVisible(false);
					
					if (ClientMessageBroker.manageMessageType(client.getConnManager().login(ClientMessageBroker.createLogin(username.getText(), password.getText()))).compareTo("ok")==0)
					{
						client.startUI();
					}
					else
					{
						this.setVisible(true);
						JOptionPane.showMessageDialog(this,"Error in Data Login", "Login Error", JOptionPane.ERROR_MESSAGE);
					}
					//JOptionPane.showMessageDialog(this, "Eggs are not supposed to be green.");
			
		}
		if (arg0.getComponent().equals(send_newUser))
		{
			
				new_userframe.setVisible(false);
				if (ClientMessageBroker.manageMessageType(client.getConnManager().creaUtente(ClientMessageBroker.createUser(username.getText(), password.getText()))).compareTo("ok")==0)
				{	
					
					this.setVisible(true);
					panel.remove(send_newUser);
					panel.add(this.new_user);
					panel.add(send);
					this.add(panel);
					System.out.println("ciao----");
				}
				else
				{
					JOptionPane.showMessageDialog(this,"Username already exist", "NewUser Error", JOptionPane.ERROR_MESSAGE);
				}
		}
		
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
