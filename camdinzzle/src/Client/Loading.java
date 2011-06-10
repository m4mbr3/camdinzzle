package Client;
import javax.swing.JProgressBar;
import javax.swing.JFrame;
public class Loading extends JFrame
{
	JProgressBar progress;
	public Loading()
	{
		super("Loading");
		//this.setResizable(false);
		this.setSize(600,250);
		this.setLayout(null);
		progress = new JProgressBar();
		progress.setSize(400,30);
		progress.setLocation(0,100);
		progress.setValue(100);
		this.setVisible(true);
		this.add(progress);
		
		this.validate();
		this.repaint();
		
	}
	public void inc(int value)
	{
		progress.setValue(value);
	}
	public static void main(String[] args) {
		Loading load = new Loading();
		int value = 0;
		while (value<=100)
		{
			value += 5;
			load.inc(value);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
