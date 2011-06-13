package Client;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	protected ImageIcon icon;
	protected Image background;
	public BackPanel() 
	{
	    super();
	}
	
	public void paintComponent(Graphics g) 
	{
		g.drawImage(background, 0, 0, this);
	}
	
	public void setBackground(URL url)
	{
	    icon = new ImageIcon(url);
	    background = icon.getImage();
	}
}