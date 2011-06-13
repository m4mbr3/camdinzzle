/**
 * 
 */
package Client;

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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//import com.sun.medialib.mlib.Image;

/**
 * @author Andrea
 *
 */
public class FrameLogin extends JFrame implements ActionListener,WindowListener,ChangeListener, MouseListener{

	private static final long serialVersionUID = 1L;
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
	private JFrame new_userframe;
	private JButton exit;
	private boolean is_local;
	/**
	 * @throws HeadlessException
	 */
	public FrameLogin() throws HeadlessException 
	{
		
	}

	/**
	 * @param gc
	 */
	public FrameLogin(GraphicsConfiguration gc) 
	{
		super(gc);
	}

	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public FrameLogin(String title, Client client, boolean is_local) throws HeadlessException {
		super(title);
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.client = client;
		this.setVisible(true);
		this.setSize(330,335);
		this.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-300)/2);
		this.validate();
		this.repaint();
		new_user = new JLabel("<html> <u> Are you a new user? Click here!</u>");
		this.is_local = is_local;
		back = new JLabel("<html> <u>Come back to login</u>");
		camdinzzle = new JLabel("<html> <h3>Camdinzzle on Socket v1.0</h3>");
		username_label = new JLabel("Username :");
		password_label = new JLabel("Password :");
		username = new JTextField();
		password = new JPasswordField();
		exit = new JButton("Back Home");
		panel = new JPanel();
		panel_newUser = new JPanel();
/*		//CARICAMENTO IMMAGINI SFONDO
		ClassLoader cldr = this.getClass().getClassLoader();
		ImageIcon newUser = new ImageIcon(cldr.getResource("Images/NEW_USER.jpg"));
		JFrame newUserFrame = new JFrame();
		newUserFrame.setIconImage(newUser.getImage());
		this.add(newUserFrame);
		//
*/		send_newUser = new JButton("Register it");
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
		if(is_local == true) exit.setEnabled(false);
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
		panel.validate();
		exit.addMouseListener(this);
		this.addWindowListener(this);
		back.addMouseListener(this);
		send.addMouseListener(this);
		new_user.addMouseListener(this);
		send_newUser.addMouseListener(this);
		panel.repaint();
		this.add(panel);
		this.repaint();
		this.setResizable(false);
	}

	/**
	 * @param title
	 * @param gc
	 */
	public FrameLogin(String title, GraphicsConfiguration gc) 
	{
		super(title, gc);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
	
	}

	@Override
	public void windowActivated(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) 
	{
		if(!is_local){
			System.out.println("Exit clicked");
			client.setConnManager(null);
			client.setVisible(true);
			this.setVisible(false);
			if (new_userframe != null)
			new_userframe.setVisible(false);
			}
		else{
			System.exit(0);
			}
	}

	@Override
	public void windowClosing(WindowEvent arg0) 
	{
		if(!is_local){
			System.out.println("Exit clicked");
			client.setConnManager(null);
			client.setVisible(true);
			this.setVisible(false);
			if (new_userframe != null)
			new_userframe.setVisible(false);
			}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) 
	{
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) 
	{
		
	}

	@Override
	public void stateChanged(ChangeEvent arg0) 
	{
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
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
			if((!checkPassword(password.getPassword()))||(username.getText().contains("@"))||( username.getText().contains("<"))||(username.getText().contains(">"))||(username.getText().contains("'"))||(username.getText().contains("\""))||(username.getText().contains("?"))||(username.getText().contains("&"))||(username.getText().contains("="))||(username.getText().contains("!"))||(username.getText().contains("%")))	
			{
				JOptionPane.showMessageDialog(this,"Maybe you insert one or more special caracters!!!fix it and try again!!!", "Login Error", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
					this.setVisible(false);
					String login = client.getConnManager().login(username.getText(), getPasswordString(password));
					if(login != null)
					{
						if (ClientMessageBroker.manageLogin(login)[0].compareTo("ok")==0)
						{
							new FrameGameManager("ManagerPanel",client,is_local);
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
					}
			}
		}
		if (arg0.getComponent().equals(exit) && !is_local)
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
			if((!checkPassword(password.getPassword()))||(username.getText().contains("@"))||( username.getText().contains("<"))||(username.getText().contains(">"))||(username.getText().contains("'"))||(username.getText().contains("\""))||(username.getText().contains("?"))||(username.getText().contains("&"))||(username.getText().contains("="))||(username.getText().contains("!"))||(username.getText().contains("%")))
			{
				JOptionPane.showMessageDialog(this,"Maybe you insert one or more special caracters!!!fix it and try again!!!", "Login Error", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				new_userframe.setVisible(false);
				String newUser = client.getConnManager().creaUtente(username.getText(), getPasswordString(password));
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
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
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
	public void mouseExited(MouseEvent arg0) 
	{
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
	public void mousePressed(MouseEvent arg0) 
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		
	}
	
	private boolean checkPassword(char[] password)
	{
		for(int i=0; i<password.length; i++)
		{
			if(password[i] == '@')
				return false;
			else if(password[i] == '<')
				return false;
			else if(password[i] == '>')
				return false;
			else if(password[i] == '"')
				return false;
			else if(password[i] == '\'')
				return false;
			else if(password[i] == '?')
				return false;
			else if(password[i] == '&')
				return false;
			else if(password[i] == '=')
				return false;
			else if(password[i] == '!')
				return false;
			else if(password[i] == '%')
				return false;
		}
		return true;
	}
	
	private String getPasswordString(JPasswordField password)
	{
		char[] pass = password.getPassword();
		String ret = new String("");
		for(int i=0;i<pass.length;i++)
		{
			ret += pass[i];
		}
		return ret;
	}


	

}
