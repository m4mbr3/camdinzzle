package Client;

import java.awt.Dimension;
import java.awt.Toolkit;


import javax.swing.JProgressBar;
import javax.swing.JFrame;

public class Loading extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JProgressBar progress;
	private AnimationPanel background;
	private Dimension screenSize;
	
	public Loading()
	{
		super("Loading");
		//this.setResizable(false);
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(600,250);
		this.setLocation((int)(screenSize.getWidth()-600)/2,(int)(screenSize.getHeight()-250)/2);
		this.setLayout(null);
		background = new AnimationPanel("Images/animazion_caricamento.gif");
		background.setSize(600, 250);
		background.setLayout(null);
	
		
		progress = new JProgressBar();
		progress.setSize(400,30);
		progress.setLocation(100,170);
		progress.setValue(100);
		background.add(progress);
		this.getContentPane().add(background);
		this.setVisible(true);
		
		this.validate();
		this.repaint();
		
	}
	public void inc(int value)
	{
		progress.setValue(value);
	}
	public static void main(String[] args) 
	{
		Loading load = new Loading();
		int value = 0;
		while (value<=100)
		{
			value += 5;
			load.inc(value);
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
