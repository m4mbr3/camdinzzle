package Server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
/**
 * Classe utilizzata per scrivere un file di log giornaliero. Vengono scritte le azioni 
 * compiute dal server, le richieste di creazione utente, di login, di accesso in partita, 
 * di uscita dalla partita e di logout per ogni utente che le richiese. Vengono inoltre scritti
 * gli eventuali errori che possono esserci.
 */
public class LogHelper 
{
	private static final String logPath = "log";
	
	/**
	 * Scrive un messaggio di informazione sul file.
	 * @param msg il messaggio di informazione
	 */
	public synchronized static void writeInfo(String msg)
	{
		try
		{
			(new File(logPath)).mkdir();
			
			Calendar cal = Calendar.getInstance();
			
			int mese_int = cal.get(Calendar.MONTH) + 1;
			String mese;
			if(mese_int < 10)
			{
				mese = "0" + String.valueOf(mese_int);
			}
			else
			{
				mese = String.valueOf(mese_int);
			}
			
			int giorno_int = cal.get(Calendar.DAY_OF_MONTH);
			String giorno;
			if(giorno_int < 10)
			{
				giorno = "0" + String.valueOf(giorno_int);
			}
			else
			{
				giorno = String.valueOf(giorno_int);
			}
			
			int ora_int = cal.get(Calendar.HOUR_OF_DAY);
			String ora;
			if(ora_int < 10)
			{
				ora = "0" + String.valueOf(ora_int);
			}
			else
			{
				ora = String.valueOf(ora_int);
			}
			
			int minuti_int = cal.get(Calendar.MINUTE);
			String minuti;
			if(minuti_int < 10)
			{
				minuti = "0" + String.valueOf(minuti_int);
			}
			else
			{
				minuti = String.valueOf(minuti_int);
			}
			
			int secondi_int = cal.get(Calendar.SECOND);
			String secondi;
			if(secondi_int < 10)
			{
				secondi = "0" + String.valueOf(secondi_int);
			}
			else
			{
				secondi = String.valueOf(secondi_int);
			}
			
			
			String hour = ora + ":" + minuti + ":" + secondi;
			
			String data =  mese + "-" + giorno + "-" + cal.get(Calendar.YEAR);
			
			File f = new File(logPath + "/log_" + data + ".log");
			
			FileOutputStream fos = new FileOutputStream(f, true);
			PrintStream ps = new PrintStream(fos);
			
			ps.println("[" + hour + "] - INFO:            " + msg);
			
			ps.close();
			fos.close();
		}
		catch(Exception ex)
		{
			
		}
	}
	
	/**
	 * Scrive un messaggio di errore sul file.
	 * @param msg il messaggio di errore
	 */
	public synchronized static void writeError(String msg)
	{
		try
		{
			(new File(logPath)).mkdir();
			
			Calendar cal = Calendar.getInstance();
			
			int mese_int = cal.get(Calendar.MONTH) + 1;
			String mese;
			if(mese_int < 10)
			{
				mese = "0" + String.valueOf(mese_int);
			}
			else
			{
				mese = String.valueOf(mese_int);
			}
			
			int giorno_int = cal.get(Calendar.DAY_OF_MONTH);
			String giorno;
			if(giorno_int < 10)
			{
				giorno = "0" + String.valueOf(giorno_int);
			}
			else
			{
				giorno = String.valueOf(giorno_int);
			}
			
			int ora_int = cal.get(Calendar.HOUR_OF_DAY);
			String ora;
			if(ora_int < 10)
			{
				ora = "0" + String.valueOf(ora_int);
			}
			else
			{
				ora = String.valueOf(ora_int);
			}
			
			int minuti_int = cal.get(Calendar.MINUTE);
			String minuti;
			if(minuti_int < 10)
			{
				minuti = "0" + String.valueOf(minuti_int);
			}
			else
			{
				minuti = String.valueOf(minuti_int);
			}
			
			int secondi_int = cal.get(Calendar.SECOND);
			String secondi;
			if(secondi_int < 10)
			{
				secondi = "0" + String.valueOf(secondi_int);
			}
			else
			{
				secondi = String.valueOf(secondi_int);
			}
			
			
			String hour = ora + ":" + minuti + ":" + secondi;
			
			String data =  mese + "-" + giorno + "-" + cal.get(Calendar.YEAR);
			
			File f = new File(logPath + "/log_" + data + ".log");
			
			FileOutputStream fos = new FileOutputStream(f, true);
			PrintStream ps = new PrintStream(fos);
			
			ps.println("[" + hour + "] - ERROR:           " + msg);
			
			ps.close();
			fos.close();
		}
		catch(Exception ex)
		{
			
		}
	}
	
	/**
	 * Scrive un messaggio di richiesta di un client sul file.
	 * @param msg il messaggio di richiesta
	 */
	public synchronized static void writeClientRequest(String msg)
	{
		try
		{
			(new File(logPath)).mkdir();
			
			Calendar cal = Calendar.getInstance();
			
			int mese_int = cal.get(Calendar.MONTH) + 1;
			String mese;
			if(mese_int < 10)
			{
				mese = "0" + String.valueOf(mese_int);
			}
			else
			{
				mese = String.valueOf(mese_int);
			}
			
			int giorno_int = cal.get(Calendar.DAY_OF_MONTH);
			String giorno;
			if(giorno_int < 10)
			{
				giorno = "0" + String.valueOf(giorno_int);
			}
			else
			{
				giorno = String.valueOf(giorno_int);
			}
			
			int ora_int = cal.get(Calendar.HOUR_OF_DAY);
			String ora;
			if(ora_int < 10)
			{
				ora = "0" + String.valueOf(ora_int);
			}
			else
			{
				ora = String.valueOf(ora_int);
			}
			
			int minuti_int = cal.get(Calendar.MINUTE);
			String minuti;
			if(minuti_int < 10)
			{
				minuti = "0" + String.valueOf(minuti_int);
			}
			else
			{
				minuti = String.valueOf(minuti_int);
			}
			
			int secondi_int = cal.get(Calendar.SECOND);
			String secondi;
			if(secondi_int < 10)
			{
				secondi = "0" + String.valueOf(secondi_int);
			}
			else
			{
				secondi = String.valueOf(secondi_int);
			}
			
			
			String hour = ora + ":" + minuti + ":" + secondi;
			
			String data =  mese + "-" + giorno + "-" + cal.get(Calendar.YEAR);
			
			File f = new File(logPath + "/log_" + data + ".log");
			
			FileOutputStream fos = new FileOutputStream(f, true);
			PrintStream ps = new PrintStream(fos);
			
			ps.println("[" + hour + "] - CLIENT REQUEST:  " + msg);
			                            
			ps.close();
			fos.close();
		}
		catch(Exception ex)
		{
			
		}
	}
	
	/**
	 * Scrive un messaggio di risposta del server ad un client.
	 * @param msg il messaggio di risposta
	 */
	public synchronized static void writeServerResponse(String msg)
	{
		try
		{
			(new File(logPath)).mkdir();
			
			Calendar cal = Calendar.getInstance();
			
			int mese_int = cal.get(Calendar.MONTH) + 1;
			String mese;
			if(mese_int < 10)
			{
				mese = "0" + String.valueOf(mese_int);
			}
			else
			{
				mese = String.valueOf(mese_int);
			}
			
			int giorno_int = cal.get(Calendar.DAY_OF_MONTH);
			String giorno;
			if(giorno_int < 10)
			{
				giorno = "0" + String.valueOf(giorno_int);
			}
			else
			{
				giorno = String.valueOf(giorno_int);
			}
			
			int ora_int = cal.get(Calendar.HOUR_OF_DAY);
			String ora;
			if(ora_int < 10)
			{
				ora = "0" + String.valueOf(ora_int);
			}
			else
			{
				ora = String.valueOf(ora_int);
			}
			
			int minuti_int = cal.get(Calendar.MINUTE);
			String minuti;
			if(minuti_int < 10)
			{
				minuti = "0" + String.valueOf(minuti_int);
			}
			else
			{
				minuti = String.valueOf(minuti_int);
			}
			
			int secondi_int = cal.get(Calendar.SECOND);
			String secondi;
			if(secondi_int < 10)
			{
				secondi = "0" + String.valueOf(secondi_int);
			}
			else
			{
				secondi = String.valueOf(secondi_int);
			}
			
			
			String hour = ora + ":" + minuti + ":" + secondi;
			
			String data =  mese + "-" + giorno + "-" + cal.get(Calendar.YEAR);
			
			File f = new File(logPath + "/log_" + data + ".log");
			
			FileOutputStream fos = new FileOutputStream(f, true);
			PrintStream ps = new PrintStream(fos);
			
			ps.println("[" + hour + "] - SERVER RESPONSE: " + msg);
			
			ps.close();
			fos.close();
		}
		catch(Exception ex)
		{
			
		}
	}
}
