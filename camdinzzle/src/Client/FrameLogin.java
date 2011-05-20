/**
 * 
 */
package Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;

/**
 * @author Andrea
 *
 */
public class FrameLogin extends JFrame implements ActionListener,WindowListener{

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
	private JButton send;
	private Dimension screenSize;
	private Client client;
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
		this.setSize(300,300);
		port = new JTextField();
		address = new JTextField();
		port_label = new JLabel("Port :");
		address_label = new JLabel("Address :");
		camdinzzle = new JLabel("Camdinzzle on Socket v1.0");
		username_label = new JLabel("Username :");
		password_label = new JLabel("Password :");
		username = new JTextField();
		password = new JPasswordField();
		panel = new JPanel();
		send = new JButton("Send Information");
		port_label.setSize(90,20);
		address_label.setSize(90,20);
		panel.setSize(300,300);
		send.setSize(180, 20);
		camdinzzle.setSize(200,70);
		username_label.setSize(90,20);
		password_label.setSize(90,20);
		port.setSize(90,20);
		address.setSize(90,20);
		username.setSize(90,20);
		password.setSize(90,20);
		panel.setLayout(null);
		
		port.setVisible(true);
		address.setVisible(true);
		port_label.setVisible(true);
		address_label.setVisible(true);
		camdinzzle.setVisible(true);
		send.setVisible(true);
		username_label.setVisible(true);
		password_label.setVisible(true);
		username.setVisible(true);
		password.setVisible(true);
		panel.setVisible(true);
		
		port.setLocation(140,80);
		address.setLocation(140,120);
		port_label.setLocation(50,80);
		address_label.setLocation(50,120);
		camdinzzle.setLocation(50,0);
		send.setLocation(50, 250);
		username_label.setLocation(50,160);
		password_label.setLocation(50,200);
		username.setLocation(140,160);
		password.setLocation(140, 200);
		
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
		
		send.addActionListener(this);
		
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
		if ( arg0.getSource() instanceof JButton)
		{
			this.setVisible(false);
			Integer port_i = new Integer(port.getText());
			client.setConnMann(new ConnectionManagerSocket(port_i.intValue(), address.getText(), username.getText(), password.getText()));
			
		}
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
	

}
