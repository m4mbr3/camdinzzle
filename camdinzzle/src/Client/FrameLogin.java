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

	JLabel username_label;
	JLabel password_label;
	JLabel camdinzzle;
	JTextField username;
	JPasswordField password;
	JPanel panel;
	JButton send;
	Dimension screenSize;
	
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
	public FrameLogin(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
		this.setVisible(true);
		this.setSize(300,300);
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-300)/2);
		camdinzzle = new JLabel("Camdinzzle");
		username_label = new JLabel("Username :");
		password_label = new JLabel("Password :");
		username = new JTextField();
		password = new JPasswordField();
		panel = new JPanel();
		send = new JButton("Send Information");
		panel.setSize(300,300);
		send.setSize(180, 20);
		camdinzzle.setSize(90,70);
		username_label.setSize(90,20);
		password_label.setSize(90,20);
		username.setSize(90,20);
		password.setSize(90,20);
		panel.setLayout(null);
		
		camdinzzle.setVisible(true);
		send.setVisible(true);
		username_label.setVisible(true);
		password_label.setVisible(true);
		username.setVisible(true);
		password.setVisible(true);
		panel.setVisible(true);
		
		camdinzzle.setLocation(50,0);
		send.setLocation(50, 250);
		username_label.setLocation(50,98);
		password_label.setLocation(50,200);
		username.setLocation(140,98);
		password.setLocation(140, 200);
		
			
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
			new ConnectionManagerSocket(4567, "localhost", username.getText(), password.getText());
			System.exit(0);
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
