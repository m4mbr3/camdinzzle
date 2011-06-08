package Client;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ChangeRoundThread extends JFrame implements Runnable 
{
	private static final long serialVersionUID = 1L;
	private static boolean is_run;
	private Client client;
	private FrameGame frameGame;
	private JLabel msg;
	private Dimension screenSize;
	private JButton confirm;
	private JButton deny;
	private boolean is_my_turn;
	
	public ChangeRoundThread(String name,Client client,FrameGame frameGame)
	{
		super (name);
		this.client = client;
		this.is_run =true;
		this.frameGame = frameGame;
		this.msg = new JLabel();
		this.setSize(300, 200);
		this.setVisible(false);
		this.is_my_turn=false;
		confirm = new JButton("Confirm");
		deny = new JButton("Deny");
		//confirm.addMouseListener(this);
		//deny.addMouseListener(this);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		msg.setSize(200,20);
		msg.setLocation(5,50);
		confirm.setLocation(5, 130);
		confirm.setSize(100,30);
		deny.setLocation(150,130);
		deny.setSize(100,30);	
		this.setLocation((int)(screenSize.getWidth()-300)/2,(int)(screenSize.getHeight()-300)/2);
		this.setLayout(null);
		this.add(msg);		
		this.repaint();
	}
	public static void stop()
	{
		is_run = false;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (is_run)
		{
			if (!client.getConnManager().getChangeRound().equals(""))
			{
				frameGame.drawRound(client.getConnManager().getChangeRound());
				if(!client.getConnManager().getUsername().equals(client.getConnManager().getChangeRound()))
				{
					frameGame.upDateFrameGame();
					this.is_my_turn = false;
					client.getConnManager().setChangeRound("");
				}
				
				else
				{
					frameGame.upDateFrameGame();
					int response = TimeOutOptionPane.showTimeoutDialog(this, "Vuoi utilizzare il turno?", "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, new Object[]{"yes", "no"}, "no");
					optionpaneClicked(response);
					client.getConnManager().setChangeRound("");
					/*
					this.msg.setText("Ora tocca: "+ client.getConnManager().getChangeRound());
					this.add(confirm);
					this.add(deny);
					this.repaint();
					this.validate();
					this.setVisible(true);
					client.getConnManager().setChangeRound("");
					this.repaint();*/
					
				}
				client.getConnManager().setChangeRound("");
			}
			if(is_my_turn)
			{
				if(frameGame.getTime()!=0)
					frameGame.drawTime(frameGame.getTime()-1);
			}
			try{
				Thread.sleep(980);
			}
			catch(InterruptedException e)
			{
				System.out.println("ERROR: " + e.getMessage());
			}
		}
	}

	public void optionpaneClicked(int chosedOption) {
		// TODO Auto-generated method stub
		String[] response;
		if(chosedOption == 0)
		{ 
			this.setVisible(false);
			response=client.getConnManager().confermaTurno();
			if (response == null)
			{
				JOptionPane.showMessageDialog(this,"MessageIncorrect", "Turn Error", JOptionPane.ERROR_MESSAGE);
			}
			else if(response[0].equals("ok"))
			{
				this.is_my_turn=true;
				frameGame.drawTime(120);
			}
			else if (response[0].equals("no"))
			{
				if( response[1].equals("tokenNonValido")){
					JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (response[1].equals("nonInPartita")){
					JOptionPane.showMessageDialog(this,"You aren't in the game!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (response[1].equals("nonIlTuoTurno")){
					JOptionPane.showMessageDialog(this,"It's not Your turn!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
				else{
					JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
					
			}
			
				
		}
		else
		{
			this.setVisible(false);
			response=client.getConnManager().passaTurno();
			if (response == null)
			{
				JOptionPane.showMessageDialog(this,"MessageIncorrect", "Turn Error", JOptionPane.ERROR_MESSAGE);
			}
			else if(response[0].equals("ok"))
			{
				System.out.println("Turno passato");
			}
			else if (response[0].equals("no"))
			{
				if( response[1].equals("tokenNonValido")){
					JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (response[1].equals("nonInPartita")){
					JOptionPane.showMessageDialog(this,"You aren't in the game!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (response[1].equals("nonIlTuoTurno")){
					JOptionPane.showMessageDialog(this,"It's not Your turn!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
				else{
					JOptionPane.showMessageDialog(this,"You have an incorrect token!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}
	}
}
