package Client;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
/**
 * 
 */

/**
 * @author Andrea
 *
 */
public class Client extends JFrame implements ActionListener, WindowListener{

	/**
	 * 
	 */
	
	private JLabel camdinzzle;
	private JLabel choice;
	private JPanel panel;
	private Dimension screenSize;
	private JRadioButton local;
	private JRadioButton rmi;
	private JRadioButton socket;
	private ButtonGroup radiogroup;
	private JButton next;
	private ConnectionManager connManager;
	private FrameLogin login;
	
	public Client(String Name) {
		super (Name);
		this.setVisible(true);
		radiogroup = new ButtonGroup();
		next = new JButton("Continue");
		next.setLocation(150,310);
		next.setSize(150, 30);
		local = new JRadioButton("Local");
		rmi = new JRadioButton("rmi");
		socket = new JRadioButton("socket");
		camdinzzle = new JLabel("Camdinzzle Project v1.0");
		choice = new JLabel("Select the method of connection to ServerLogic:");
		panel = new JPanel();
		panel.setLayout(null);
		this.setSize(400,400);
		panel.setSize(400,400);
		choice.setSize(350,30);
		camdinzzle.setSize(300,30);
		local.setSize(300,30);
		rmi.setSize(300,30);
		socket.setSize(300,30);
		radiogroup.add(local);
		radiogroup.add(rmi);
		radiogroup.add(socket);
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-300)/2);
		camdinzzle.setLocation(20,15);
		choice.setLocation(20,40);
		local.setLocation(20, 80);
		rmi.setLocation(20, 180);
		socket.setLocation(20, 280);
		
		panel.add(camdinzzle);
		panel.add(choice);
		panel.add(socket);
		panel.add(local);
		panel.add(rmi);
		panel.add(next);
		this.add(panel);
		this.addWindowListener(this);
		next.addActionListener(this);
		
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
		if(arg0.getSource() instanceof JFrame )
		{
			System.exit(0);
		}
		if(arg0.getSource() instanceof JButton)
		{
			if((local.isSelected()==false)&&(rmi.isSelected()==false)&&(socket.isSelected()==false))
			{
				System.out.println("You Can't Go Next!!!");
			}
			else
			{
				if(local.isSelected())
				{
					this.connManager = new ConnectionManagerLocal();
				}
				else if(rmi.isSelected())
				{
					this.connManager = new ConnectionManagerRMI();
				}
				else
				{
					this.setVisible(false);
					login = new FrameLogin("Login",this);
					
				}
			}
		}
		
		
	}
	
	public void setConnMann(ConnectionManagerSocket conn)
	{
		this.connManager = conn ;
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

}
