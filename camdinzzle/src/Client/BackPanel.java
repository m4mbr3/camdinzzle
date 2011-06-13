package Client;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * 
 * @author Marco
 *
 */
public class BackPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	/**
	 * sfondo pannello
	 */
	protected Image background;
	
	public BackPanel() 
	{
	    super();
	}
	
	public void paintComponent(Graphics g) 
	{
		g.drawImage(background, 0, 0, this);
	}
	
	/**
	 * Setta lo sfondo del pannello.
	 * @param url
	 */
	public void setBackground(URL url)
	{
	    background = (new ImageIcon(url)).getImage();
	}
}