package Client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Classe utilizzat per l'animazione di introduzione del gioco.
 */

public class AnimationPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Image img;
	private Timer timer;
	private boolean finish=false;
	private int count;
	
	public AnimationPanel(String urlImage)
	{
		super();
		ClassLoader cldr = this.getClass().getClassLoader();
		img = (new ImageIcon(cldr.getResource(urlImage))).getImage();
		try 
		{
			MediaTracker track = new MediaTracker(this);
			track.addImage(img, 0);
			track.waitForID(0);
		} 
		catch (InterruptedException e) 
		{
			
		}
		count=0;
		timer = new Timer(1, this);
		timer.start();
	}

	@Override
	public void actionPerformed (ActionEvent ae) 
	{
		repaint();
		count++;
		if(count==5)
		{
			finish = true;
		}
	}
	
	protected void paintComponent(Graphics g) 
	{
	   setOpaque(false);
	   g.drawImage(img, 0, 0, null);
	   super.paintComponent(g);
	}
	
	public boolean getFinish()
	{
		return finish;
	}
}