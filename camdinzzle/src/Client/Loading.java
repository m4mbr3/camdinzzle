package Client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Classe di gestione della barra di caricamento iniziale del gioco.
 */

public class Loading extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JProgressBar progress;
	private AnimationPanel background;
	private Dimension screenSize;
	private JLabel progress_label;
	public Loading()
	{
		super("Loading");
		this.setUndecorated(true);
		this.setResizable(false);
		// setta l'icona del frame
		ClassLoader cldr = this.getClass().getClassLoader();
		ImageIcon logo = new ImageIcon(cldr.getResource("Images/icona.png"));
		this.setIconImage(logo.getImage());
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(602,230);
		this.setLocation((int)(screenSize.getWidth()-600)/2,(int)(screenSize.getHeight()-250)/2);
		this.setLayout(null);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		progress_label = new JLabel("Init Camdinzzle");
		progress_label.setSize(400,20);
		progress_label.setLocation(100,200);
		background = new AnimationPanel("Images/animazion_caricamento.gif");
		background.setSize(600, 250);
		background.setLayout(null);
		
		progress = new JProgressBar();
		progress.setSize(400,30);
		progress.setLocation(100,170);
		progress.setValue(100);
		background.add(progress);
		background.add(progress_label);
		this.getContentPane().add(background);
		this.setVisible(true);
		
		this.validate();
		this.repaint();
		int value =0;
		while (value<=100)
		{
			value = value + new Random().nextInt(15);
			this.inc(value);
			if(value <= 80)
				progress_label.setText("Starting environment");
			/*
			if(value < 20 && value > 5)
				progress_label.setText("Creating UI");
			if(value > 20 && value < 40)
				progress_label.setText("Testing Network");
			if(value > 40 && value < 60) 
				progress_label.setText("Searching for update");
			if(value > 60 && value <80)
				progress_label.setText("Finishing start");*/
			if(value > 80)
				progress_label.setText("Enjoy Camdinzzle");
			try 
			{
				Thread.sleep(350);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		this.setVisible(false);
	}
	public void inc(int value)
	{
		progress.setValue(value);
	}
}
