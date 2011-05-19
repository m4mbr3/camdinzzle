/**
 * 
 */
package Client;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Andrea
 *
 */
public class FrameConnection extends JFrame {

	private Dimension screenSize;
	private JPanel pannello;
	/**
	 * @throws HeadlessException
	 */
	public FrameConnection() throws HeadlessException {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param gc
	 */
	public FrameConnection(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public FrameConnection(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
		
		this.setVisible(true);
		this.setSize(300,300);
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-300)/2);
		pannello.setSize(300,300);
		this.add(pannello);
		
	}

	/**
	 * @param title
	 * @param gc
	 */
	public FrameConnection(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
	}

}
