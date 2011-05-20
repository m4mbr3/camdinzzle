/**
 * 
 */
package Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
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
	private JLabel address_label;
	private JLabel port_label;
	private JTextField username;
	private JTextField port;
	private JTextField address;
	private JPasswordField password;
	private JPanel panel;
	private JPanel panel_newUser;
	private JButton send;
	private JButton send_newUser;
	private Dimension screenSize;
	private Client client;
	private JCheckBox enable_port;
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
		this.client = client;
		this.setVisible(true);
		this.setSize(330,300);
		new_user = new JLabel("Are you a new user?Click here!");
		port = new JTextField();
		enable_port = new JCheckBox("enable");
		address = new JTextField();
		port_label = new JLabel("Port :");
		address_label = new JLabel("Address :");
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
		port_label.setSize(90,20);
		address_label.setSize(90,20);
		panel_newUser.setSize(300,300);
		panel.setSize(300,300);
		send_newUser.setSize(180,20);
		send.setSize(180, 20);
		camdinzzle.setSize(200,70);
		enable_port.setSize(100,20);
		username_label.setSize(90,20);
		password_label.setSize(90,20);
		port.setSize(90,20);
		address.setSize(160,20);
		username.setSize(160,20);
		password.setSize(160,20);
		panel.setLayout(null);
		port.setText(new Integer(4567).toString());
		port.setVisible(true);
		port.enable(false);
		new_user.setVisible(true);
		address.setVisible(true);
		port_label.setVisible(true);
		address_label.setVisible(true);
		camdinzzle.setVisible(true);
		send_newUser.setVisible(true);
		send.setVisible(true);
		username_label.setVisible(true);
		password_label.setVisible(true);
		username.setVisible(true);
		password.setVisible(true);
		panel_newUser.setVisible(true);
		panel.setVisible(true);
		new_user.setLocation(50,222);
		enable_port.setLocation(230,80);
		port.setLocation(140,80);
		address.setLocation(140,120);
		port_label.setLocation(50,80);
		address_label.setLocation(50,120);
		camdinzzle.setLocation(50,0);
		send_newUser.setLocation(50,250);
		send.setLocation(50, 250);
		username_label.setLocation(50,160);
		password_label.setLocation(50,200);
		username.setLocation(140,160);
		password.setLocation(140, 200);
		enable_port.addChangeListener(this);
		panel.add(enable_port);
		panel.add(port);
		panel.add(address);
		panel.add(port_label);
		panel.add(address_label);
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
		if (arg0.getSource() instanceof JCheckBox)
			if(enable_port.isSelected())
			{
				port.setEnabled(true);
			}
			else
			{
				port.setEnabled(false);
			}
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
			panel.remove(send);
			panel.add(send_newUser);
			panel.remove(this.new_user);
			new_userframe.add(panel);
			
		}
		if (arg0.getComponent().equals(send))
		{
			this.setVisible(false);
			Integer port_i = new Integer(port.getText());
			client.setConnMann(new ConnectionManagerSocket(port_i.intValue(), address.getText(), username.getText(), password.getText()));
		}
		if (arg0.getComponent().equals(send_newUser))
		{
			new_userframe.setVisible(false);
			
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
