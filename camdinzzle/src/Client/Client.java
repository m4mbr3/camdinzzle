package Client;
import javax.swing.JFrame;
/**
 * 
 */

/**
 * @author Andrea
 *
 */
public class Client extends JFrame {

	/**
	 * 
	 */
	
	int port;
	String address;
	ConnectionManagerSocket n;
	public Client(int port, String address) {
		// TODO Auto-generated constructor stub
		System.out.println("<<CLIENT>>--INITIALIZATION OF EVIROMENT VARIABLES");
		this.port = port;
		this.address = address;
		System.out.println("<<CLIENT>>--STARTING CONNECTION MANAGER SOCKET");
		n = new ConnectionManagerSocket(port,address, "alkjasladjfld","dkfkkdfkkkdk");
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
		Client s = new Client(4567, "localhost");
	}

}
