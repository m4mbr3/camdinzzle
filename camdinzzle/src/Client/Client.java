package Client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
/**
 * 
 */
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import Server.*
;/**
 * @author Andrea
 *
 */
public class Client extends JFrame implements ActionListener, WindowListener,MouseListener,ChangeListener{

	/**
	 * 
	 */
	
	private JLabel camdinzzle;
	private JLabel choice;
	private JPanel panel;
	private Dimension screenSize;
	private JRadioButton rmi;
	private JRadioButton socket;
	private ButtonGroup radiogroup;
	private JButton next;
	private ConnectionManager connManager;
	private FrameLogin login;
	private JLabel port_label;
	private JLabel address_label;
	private JTextField port;
	private JCheckBox enable_port;
	private JTextField address;
	private JLabel nome_server;
	private JTextField server_value;
	
	public Client(ClientManagerLocal clientLocal)
	{
		this.connManager =new ConnectionManagerLocal(clientLocal);
		login = new FrameLogin("Login",this,true);
	}
	public Client(String Name) {
		super (Name);
		this.setResizable(false);
		this.setVisible(true);
		
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-300)/2);
		nome_server = new JLabel("Server: ");
		server_value = new JTextField("server");
		nome_server.setSize(90,20);
		server_value.setSize(160, 20);
		nome_server.setLocation(50,280);
		server_value.setLocation(140,280);
		port_label = new JLabel("Port :");
		address_label = new JLabel("Address :");
		port = new JTextField("");
		enable_port = new JCheckBox("enable");
		address = new JTextField("localhost");
		radiogroup = new ButtonGroup();
		next = new JButton("Continue");
		next.setLocation(150,310);
		next.setSize(150, 40);
		rmi = new JRadioButton("rmi");
		socket = new JRadioButton("socket");
		camdinzzle = new JLabel("<html> <h3>Camdinzzle Project v1.0</h3>");
		choice = new JLabel("Select the method of connection to ServerLogic:");
		panel = new JPanel();
		panel.setLayout(null);
		this.setSize(400,400);
		panel.setSize(400,400);
		choice.setSize(350,30);
		camdinzzle.setSize(300,30);
		rmi.setSize(100,30);
		socket.setSize(100,30);
		port_label.setSize(90,20);
		address_label.setSize(90,20);
		port.setSize(90,20);
		address.setSize(160,20);
		enable_port.setSize(100,20);
		enable_port.addChangeListener(this);
		socket.addChangeListener(this);
		radiogroup.add(rmi);
		radiogroup.add(socket);
		
		camdinzzle.setLocation(20,15);
		choice.setLocation(20,60);
		rmi.setLocation(50, 110);
		socket.setLocation(170, 110);
		enable_port.setLocation(230,200);
		port.setLocation(140,200);
		address.setLocation(140,240);
		port_label.setLocation(50,200);
		address_label.setLocation(50,240);
		server_value.setVisible(true);
		nome_server.setVisible(true);
		address.setEnabled(true);
		server_value.setEnabled(false);
		nome_server.setEnabled(true);
		enable_port.setEnabled(true);
		port.setEnabled(false);
		address.setEditable(true);
		port_label.setEnabled(true);
		address_label.setEnabled(true);
		
		panel.add(nome_server);
		panel.add(server_value);
		panel.add(address);
		panel.add(address_label);
		panel.add(enable_port);
		panel.add(port);
		panel.add(port_label);
		panel.add(camdinzzle);
		panel.add(choice);
		panel.add(socket);
		panel.add(rmi);
		panel.add(next);
		this.add(panel);
		this.addWindowListener(this);
		next.addActionListener(this);
		this.setResizable(false);
		this.validate();
		this.repaint();
	}
	public ConnectionManager getConnManager() {
		
		return connManager;
	}
	public void setConnManager(ConnectionManager connManager) {
		this.connManager = connManager;
	}
	public void createCommand()
	{
		
	}
	public void sendCommand()
	{
		
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client client = new Client("Cliente");		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() instanceof JButton)
		{
			if((rmi.isSelected()==false)&&(socket.isSelected()==false))
			{
				System.out.println("You Can't Go Next!!!");
			}
			else
			{
				 if(rmi.isSelected())
				 {
					this.setVisible(false);
					try{
						this.connManager = new ConnectionManagerRMI(address.getText(),port.getText(),server_value.getText());
						login = new FrameLogin("Login",this,false);
						System.out.println("Server scaricato!!");
					}
					catch(MalformedURLException e)
					{
						this.setVisible(true);
						JOptionPane.showMessageDialog(this, "Error in the url...check your data connect", "Malformed URL ", JOptionPane.ERROR_MESSAGE);
					}
					catch(AccessException e)
					{
						this.setVisible(true);
						JOptionPane.showMessageDialog(this, "Error during the access", "Access Exception", JOptionPane.ERROR_MESSAGE);
					}
					catch(AlreadyBoundException e)
					{
						this.setVisible(true);
						JOptionPane.showMessageDialog(this, "Object already in use", "AlreadyBoundException", JOptionPane.ERROR_MESSAGE);
					}
					catch(RemoteException e)
					{
						this.setVisible(true);
						JOptionPane.showMessageDialog(this, "Error during contact the remote server", "Remote Exception", JOptionPane.ERROR_MESSAGE);
					} 
					catch (Exception e) 
					{
						this.setVisible(true);
						JOptionPane.showMessageDialog(this, "Error during contact the remote server", "Remote Exception", JOptionPane.ERROR_MESSAGE);
					}
		
					
				}	
				else
				{
					this.setVisible(false);
					Integer port_i = new Integer(port.getText());
					try{
							connManager = new ConnectionManagerSocket(port_i.intValue(), address.getText(), new MonitorMessage());
							login = new FrameLogin("Login",this, false);
						}
					catch(ConnectException e)
					{
						JOptionPane.showMessageDialog(this,"Please Check your connection data " +
								"maybe  Server is down","Error Connection ",JOptionPane.ERROR_MESSAGE);
						this.setVisible(true);
					}
					catch(UnknownHostException e)
					{
						JOptionPane.showMessageDialog(this, "Please Check the Address name", "Uknown Host", JOptionPane.ERROR_MESSAGE);
						this.setVisible(true);
					}
					catch(SocketException e)
					{
						JOptionPane.showMessageDialog(this, "Error of Connection","Socket Error", JOptionPane.ERROR_MESSAGE);
						this.setVisible(true);
					}
					catch(NumberFormatException e)
					{
						JOptionPane.showMessageDialog(this, "Check you port!!!", "Port Error", JOptionPane.ERROR_MESSAGE);
						this.setVisible(true);
					}
					catch(IOException e)
					{
						JOptionPane.showMessageDialog(this, "Generic IOError ", "IOException", JOptionPane.ERROR_MESSAGE);
						this.setVisible(true);
					}
					
						
				}
			}
		}
		
		
	}
	
	public void setConnMann(ConnectionManagerSocket conn)
	{
		this.connManager = conn;
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
		if (arg0.getSource() instanceof JRadioButton)
		{
			if(socket.isSelected())
			{
				port.setText("4567");
				server_value.setEnabled(false);
				server_value.setEditable(false);
				this.validate();
				this.repaint();
			}
			if(rmi.isSelected())
			{
				port.setText("1099");
				server_value.setEnabled(true);
				server_value.setEditable(true);
				this.validate();
				this.repaint();
			}
		}
	}

}
