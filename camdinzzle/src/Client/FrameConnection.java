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

	private static final long serialVersionUID = 1L;
	private Dimension screenSize;
	private JPanel pannello;
	/**
	 * @throws HeadlessException
	 */
	public FrameConnection() throws HeadlessException 
	{
		
	}

	/**
	 * @param gc
	 */
	public FrameConnection(GraphicsConfiguration gc) 
	{
		super(gc);
	}

	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public FrameConnection(String title) throws HeadlessException {
		super(title);
		
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
	public FrameConnection(String title, GraphicsConfiguration gc) 
	{
		super(title, gc);
	}

}
