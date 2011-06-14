package Server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

public class LogHelper 
{
	private static final String logPath = "log";
	
	public synchronized static void writeInfo(String msg)
	{
		try
		{
			(new File(logPath)).mkdir();
			
			Calendar cal = Calendar.getInstance();
			int mese = cal.get(Calendar.MONTH) + 1;
			String ora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":" +
				String.valueOf(cal.get(Calendar.MINUTE) + ":" +String.valueOf(cal.get(Calendar.SECOND)));
			
			String data =  mese + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + 
			cal.get(Calendar.YEAR);
			
			File f = new File(logPath + "/log_" + data + ".txt");
			
			FileOutputStream fos = new FileOutputStream(f, true);
			PrintStream ps = new PrintStream(fos);
			
			ps.println("[" + ora + "] - INFO: " + msg);
			
			ps.close();
			fos.close();
		}
		catch(Exception ex)
		{
			
		}
	}
	
	public synchronized static void writeError(String msg)
	{
		try
		{
			(new File(logPath)).mkdir();
			
			Calendar cal = Calendar.getInstance();
			int mese = cal.get(Calendar.MONTH) + 1;
			String ora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":" +
			String.valueOf(cal.get(Calendar.MINUTE) + ":" +String.valueOf(cal.get(Calendar.SECOND)));
			
			String data =  mese + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + 
			cal.get(Calendar.YEAR);
			
			File f = new File(logPath + "/log_" + data + ".txt");
			
			FileOutputStream fos = new FileOutputStream(f, true);
			PrintStream ps = new PrintStream(fos);
			
			ps.println("[" + ora + "] - ERROR: " + msg);
			
			ps.close();
			fos.close();
		}
		catch(Exception ex)
		{
			
		}
	}
	
	public synchronized static void writeClientRequest(String msg)
	{
		try
		{
			(new File(logPath)).mkdir();
			
			Calendar cal = Calendar.getInstance();
			int mese = cal.get(Calendar.MONTH) + 1;
			String ora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":" +
			String.valueOf(cal.get(Calendar.MINUTE) + ":" +String.valueOf(cal.get(Calendar.SECOND)));
			
			String data =  mese + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + 
			cal.get(Calendar.YEAR);
			
			File f = new File(logPath + "/log_" + data + ".txt");
			
			FileOutputStream fos = new FileOutputStream(f, true);
			PrintStream ps = new PrintStream(fos);
			
			ps.println("[" + ora + "] - CLIENT REQUEST: " + msg);
			
			ps.close();
			fos.close();
		}
		catch(Exception ex)
		{
			
		}
	}
	
	public synchronized static void writeServerResponse(String msg)
	{
		try
		{
			(new File(logPath)).mkdir();
			
			Calendar cal = Calendar.getInstance();
			int mese = cal.get(Calendar.MONTH) + 1;
			String ora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":" +
			String.valueOf(cal.get(Calendar.MINUTE) + ":" +String.valueOf(cal.get(Calendar.SECOND)));
			
			String data =  mese + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + 
			cal.get(Calendar.YEAR);
			
			File f = new File(logPath + "/log_" + data + ".txt");
			
			FileOutputStream fos = new FileOutputStream(f, true);
			PrintStream ps = new PrintStream(fos);
			
			ps.println("[" + ora + "] - SERVER RESPONSE: " + msg);
			
			ps.close();
			fos.close();
		}
		catch(Exception ex)
		{
			
		}
	}
}
