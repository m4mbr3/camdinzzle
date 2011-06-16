package Client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;


/**
 * Classe utlizzata per la notifica del cambio del turno in gioco.
 */
public class ChangeRoundThread implements Runnable, MouseListener , ActionListener
{
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * Variabile booleana che permette la terminazione del thread del ChangeRoundThread
	 */
	private static boolean is_run = true;
	
	/*
	 * Variabile che contiene l'istanza del client associato al ChangeRoundThread
	 */
	private Client client;
	
	/*
	 * Variabile che contiene l'istanza del Frame di gioco per permettere l'aggiornamento
	 * del conto alla rovescia visualizzato all'utente
	 */
	private FrameGame frameGame;
	
	/*
	 * Variabile che contiene determina il tipo di Info visualizzata all'utente
	 * in base a chi appartiene il turno corrente
	 */
	private boolean is_my_turn;
	
	/*
	 * Frame della conferma turno
	 */
	private JFrame changeRoundFrame;
	/*
	 * Bottone per la conferma turno
	 */
	private JButton yes;
	/*
	 * Bottone per rifiutare il turno
	 */
	private JButton no;
	/*
	 * controllo risposta utente
	 */
	private int response = 2;
	/*
	 * Variabile che contiene le dimensioni dello schermo
	 */
	private Dimension screenSize;
	/*
	 * Timer per la finestra cambio turno
	 */
	private Timer timer;
	/*
	 * Sfondo cambia turno
	 */
	private BackPanel changeRoundPanel;
	/*
	 * URL immagine sfondo cambia turno
	 */
	private URL changeRoundImageURL;
	/*
	 * Scritta per cambia turno
	 */
	private JLabel changeRoundText;
	
	/**
	 * Costruttore della classe ChangeRoundThread che inizializza le variabili legandole alle variabili
	 * del client corrente e al suo scherma di gioco
	 * @param client
	 * @param frameGame
	 */
	public ChangeRoundThread(Client client,FrameGame frameGame)
	{
		this.client = client;
		this.frameGame = frameGame;
		this.is_my_turn=false;
		
		no = new JButton("no");
		yes = new JButton("yes");
		no.addMouseListener(this);
		yes.addMouseListener(this);
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		ClassLoader cldr = this.getClass().getClassLoader();
		changeRoundImageURL = cldr.getResource("Images/sfondo_cambia_turno.jpg");
	}
	
	public static void start()
	{
		is_run = true;
	}
	/**
	 * Metodo statico che permette la terminazione del thread facendolo uscire dal ciclo di countdown
	 * Viene richiamata all'uscita dell'utente dalla partita
	 */
	public static void stop()
	{
		is_run = false;
	}
	/**
	 * Metodo statico che ritorna lo stato del thread
	 * @return true se il thread e' in esecuzione, false altrimenti
	 */
	public static boolean getIsRun()
	{
		return is_run;
	}
	/**
	 * Metodo chiamato al lancio del ChangeRoundthread che fa le notifiche all'utente riguardanti il cambio del turno
	 * e gestisce la scelta dell'utente di accettare o rifiutare il turno. In caso di accettazione del turno 
	 * il metodo provvede alla visualizzazione del contatore del tempo rimanente sull'interfaccia utente.
	 * In caso di turno altrui visualizza il nome del giocatore che sta giocando
	 */
	@Override
	public void run() 
	{
		while (is_run)
		{
			if (!client.getConnManager().getChangeRound().equals(""))
			{
				String round = client.getConnManager().getChangeRound();
				client.getConnManager().setChangeRound("");
				frameGame.drawRound(round);
				//frameGame.drawRound(client.getConnManager().getChangeRound());
				if(!client.getConnManager().getUsername().equals(round))
				{
					frameGame.upDateFrameGame();
					this.is_my_turn = false;
					//client.getConnManager().setChangeRound("");
				}
				else
				{
					frameGame.upDateFrameGame();
					//int response = frameGame.showTimeoutDialog(frameGame, "Do you want to use your round? You have 30 seconds to choose.", "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, new Object[]{"yes", "no"}, "no");
					if(changeRoundFrame!=null)
					{
						changeRoundFrame.removeAll();
						changeRoundFrame=null;
					}
					
					
					changeRoundFrame = new JFrame();
					changeRoundFrame.setAlwaysOnTop(true);
					changeRoundFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					changeRoundFrame.setUndecorated(true);
					changeRoundFrame.setResizable(false);
					changeRoundFrame.setSize(230, 150);
					changeRoundFrame.setVisible(true);
					changeRoundFrame.setLocation((int)(screenSize.getWidth()-330)/2,(int)(screenSize.getHeight()-330)/2);
					changeRoundFrame.setLayout(null);
					yes.setVisible(true);
					no.setVisible(true);
					yes.setSize(80, 30);
					no.setSize(80, 30);
					yes.setLocation(23, 100);
					no.setLocation(127, 100);
					changeRoundPanel=new BackPanel();
					changeRoundPanel.setBackground(changeRoundImageURL);
					changeRoundPanel.setVisible(true);
					changeRoundPanel.setSize(230,150);
					changeRoundPanel.setLayout(null);
					
					changeRoundText = new JLabel("<html>Do you want to use your round?<br /> You have 30 seconds to choose.</html>");
					changeRoundText.setSize(210, 70);
					changeRoundText.setLocation(10, 10);
					changeRoundText.setVisible(true);
					
					changeRoundPanel.add(changeRoundText);
					changeRoundPanel.add(yes);
					changeRoundPanel.add(no);
					changeRoundFrame.add(changeRoundPanel);
					changeRoundFrame.repaint();
					
					timer = new Timer(27000, this);
					timer.start();
					
					frameGame.setEnabled(false);
					//client.getConnManager().setChangeRound("");					
				}
				//client.getConnManager().setChangeRound("");
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
				
			}
		}
		is_run = true;
	}
	/**
	 * Metoro di gestione della scelta di accettazione o rifiuto del turno.
	 * Invia al server il messaggio corrispondente alla scelta
	 * @param chosedOption	server per indicare al metodo che tipo di decisione ha preso l'utente e quindi 
	 * il tipo di messaggio da inoltrare al server
	 */
	public void optionpaneClicked(int chosedOption) 
	{
		String[] response;
		if(chosedOption == 0)
		{ 
			response=client.getConnManager().confermaTurno();
			if (response == null)
			{
				JOptionPane.showMessageDialog(new JFrame(),"MessageIncorrect", "Turn Error", JOptionPane.ERROR_MESSAGE);
			}
			else if(response[0].equals("ok"))
			{
				this.is_my_turn=true;
				frameGame.drawTime(120);
			}
			else if (response[0].equals("no"))
			{
				if( response[1].equals("tokenNonValido")){
					JOptionPane.showMessageDialog(new JFrame(),"You have an incorrect token!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (response[1].equals("nonInPartita")){
					JOptionPane.showMessageDialog(new JFrame(),"You aren't in the game!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (response[1].equals("nonIlTuoTurno")){
					JOptionPane.showMessageDialog(new JFrame(),"It's not Your turn!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
				else{
					JOptionPane.showMessageDialog(new JFrame(),"You have an incorrect token!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			this.response=2;
		}
		else if(chosedOption == 1)
		{
			response=client.getConnManager().passaTurno();
			if (response == null)
			{
				JOptionPane.showMessageDialog(new JFrame(),"MessageIncorrect", "Turn Error", JOptionPane.ERROR_MESSAGE);
			}
			else if(response[0].equals("ok"))
			{
				
			}
			else if (response[0].equals("no"))
			{
				if( response[1].equals("tokenNonValido")){
					JOptionPane.showMessageDialog(new JFrame(),"You have an incorrect token!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (response[1].equals("nonInPartita")){
					JOptionPane.showMessageDialog(new JFrame(),"You aren't in the game!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (response[1].equals("nonIlTuoTurno")){
					JOptionPane.showMessageDialog(new JFrame(),"It's not Your turn!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
				else{
					JOptionPane.showMessageDialog(new JFrame(),"You have an incorrect token!!!", "Turn Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}
		this.response=2;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		if(arg0.getComponent().equals(yes))
		{
			response = 0;
			changeRoundFrame.dispose();
			timer.stop();
			frameGame.setEnabled(true);
			optionpaneClicked(response);
		}
		if(arg0.getComponent().equals(no))
		{
			response = 1;
			changeRoundFrame.dispose();
			timer.stop();
			frameGame.setEnabled(true);
			optionpaneClicked(response);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(changeRoundFrame!=null)
		{
			changeRoundFrame.setVisible(false);
		}
		if(timer!=null)
		{
			timer.stop();
		}
	}
}
