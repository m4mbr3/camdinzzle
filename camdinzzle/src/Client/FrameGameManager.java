/**
 * 
 */
package Client;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

/**
 * @author Andrea
 *
 */
public class FrameGameManager extends JFrame {

	/**
	 * @throws HeadlessException
	 */
	public FrameGameManager() throws HeadlessException {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param gc
	 */
	public FrameGameManager(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public FrameGameManager(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
		this.setSize(300,400);
		this.setVisible(true);
	}

	/**
	 * @param title
	 * @param gc
	 */
	public FrameGameManager(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
	}

}
