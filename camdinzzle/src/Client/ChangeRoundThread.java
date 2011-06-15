package Client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChangeRoundThread  implements Runnable 
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
	 * @return
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
					int response = TimeOutOptionPane.showTimeoutDialog(new JFrame(), "Vuoi utilizzare il turno?", "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, new Object[]{"yes", "no"}, "no");
					optionpaneClicked(response);
					client.getConnManager().setChangeRound("");					
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
		}
		else
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
	}
}
