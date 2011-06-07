/**
 * 
 */
package Client;

import java.awt.Color;
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
import java.awt.*;
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
	private JLabel back;
	private FrameGameManager managerframe;
	private JFrame new_userframe;
	private JButton exit;
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
		this.setResizable(false);
		this.setSize(330,330);
		this.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-300)/2);
		new_user = new JLabel("<html> <u>Are you a new user? Click here!</u>");
		
		back = new JLabel("<html> <u>Come back to login</u>");
		camdinzzle = new JLabel("<html> <h3>Camdinzzle on Socket v1.0</h3>");
		username_label = new JLabel("Username :");
		password_label = new JLabel("Password :");
		username = new JTextField();
		password = new JPasswordField();
		exit = new JButton("Back Home");
		panel = new JPanel();
		panel_newUser = new JPanel();
		send_newUser = new JButton("Register it");
		send = new JButton("Send Info");
		back.setSize(250,20);
		new_user.setSize(250,20);
		exit.setSize(145, 20);
		panel_newUser.setSize(300,300);
		panel.setSize(300,300);
		send_newUser.setSize(145,20);
		send.setSize(145, 20);
		camdinzzle.setSize(220,70);
		username_label.setSize(90,20);
		password_label.setSize(90,20);
		
		username.setSize(160,20);
		password.setSize(160,20);
		panel.setLayout(null);
		
		new_user.setVisible(true);
		back.setVisible(true);
		exit.setVisible(true);
		camdinzzle.setVisible(true);
		send_newUser.setVisible(true);
		send.setVisible(true);
		username_label.setVisible(true);
		password_label.setVisible(true);
		username.setVisible(true);
		password.setVisible(true);
		panel_newUser.setVisible(true);
		panel.setVisible(true);
		back.setLocation(50,200);
		exit.setLocation(170,250);
		new_user.setLocation(50,200);
		camdinzzle.setLocation(50,0);
		send_newUser.setLocation(10,250);
		send.setLocation(10, 250);
		username_label.setLocation(50,80);
		password_label.setLocation(50,120);
		username.setLocation(140,80);
		password.setLocation(140, 120);
	
		panel.add(exit);
		panel.add(camdinzzle);
		panel.add(send);
		panel.add(password_label);
		panel.add(username_label);
		panel.add(password);
		panel.add(username);
		panel.add(new_user);
		
		exit.addMouseListener(this);
		this.addWindowListener(this);
		back.addMouseListener(this);
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
		System.out.println("Exit clicked");
		client.setConnManager(null);
		client.setVisible(true);
		this.setVisible(false);
		if (new_userframe != null)
		new_userframe.setVisible(false);
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Exit clicked");
		client.setConnManager(null);
		client.setVisible(true);
		this.setVisible(false);
		if (new_userframe != null)
		new_userframe.setVisible(false);
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
		if(arg0.getComponent().equals(back))
		{
			new_userframe.setVisible(false);
			panel.remove(send_newUser);
			panel.add(send);
			panel.remove(back);
			panel.add(new_user);
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
			new_userframe.add(panel);
			
		}
		if (arg0.getComponent().equals(send))
		{
			
					this.setVisible(false);
					String login = client.getConnManager().login(username.getText(), password.getText());
					if(login != null)
					{
						if (ClientMessageBroker.manageLogin(login)[0].compareTo("ok")==0)
						{
							managerframe = new FrameGameManager("ManagerPanel",client);
							
						}
						else if (ClientMessageBroker.manageLogin(login)[0].compareTo("no")==0)
						{
							this.setVisible(true);
							JOptionPane.showMessageDialog(this,"Authentication Failed!!!", "Login Error", JOptionPane.ERROR_MESSAGE);
						}
						else
						{
							this.setVisible(true);
							JOptionPane.showMessageDialog(this,"Generic Error during Login. Try Again!!!", "Message Error", JOptionPane.ERROR_MESSAGE);
						}
						//JOptionPane.showMessageDialog(this, "Eggs are not supposed to be green.");
					}
			
		}
		if (arg0.getComponent().equals(exit))
		{
			System.out.println("Exit clicked");
			client.setConnManager(null);
			client.setVisible(true);
			this.setVisible(false);
			if (new_userframe != null)
			new_userframe.setVisible(false);
		}
		if (arg0.getComponent().equals(send_newUser))
		{
			
				new_userframe.setVisible(false);
				String newUser = client.getConnManager().creaUtente(username.getText(), password.getText());
				if(newUser!=null)
				{
					if (ClientMessageBroker.manageMessageType(newUser).compareTo("ok")==0)
					{	
						this.setVisible(true);
						panel.remove(send_newUser);
						panel.add(this.new_user);
						panel.remove(back);
						panel.add(send);
						this.add(panel);
					}
					else
					{
						JOptionPane.showMessageDialog(this,"Username already exist", "NewUser Error", JOptionPane.ERROR_MESSAGE);
						new_userframe.setVisible(true);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this,"TIME OUT", "NewUser Error", JOptionPane.ERROR_MESSAGE);
				}
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
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
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
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
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
