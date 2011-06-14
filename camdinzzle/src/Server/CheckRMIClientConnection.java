package Server;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CheckRMIClientConnection implements Runnable 
{
	private boolean run;
	private Server server;
	
	public CheckRMIClientConnection(Server server)
	{
		this.run = true;
		this.server = server;
	}
	
	@Override
	public void run() 
	{
		while(run)
		{
			try 
			{
				Thread.sleep(30000);
				
				Hashtable<String, ClientManagerRMI> clientTableRMI = server.getClientRMI();
				
				if(clientTableRMI.size() > 0)
				{			
					Set<Map.Entry<String, ClientManagerRMI>> set = clientTableRMI.entrySet();
					Iterator<Map.Entry<String, ClientManagerRMI>> iter = set.iterator();
					
					while(iter.hasNext())
					{
						Map.Entry<String, ClientManagerRMI> me = (Map.Entry<String, ClientManagerRMI>)iter.next();
						
						((ClientManagerRMI)me.getValue()).sendChangeRound("");
						
					}
				}
			} 
			catch (InterruptedException e)
			{
				
			}
			catch(Exception ex)
			{
				LogHelper.writeInfo("client RMI disconnesso.");
			}
		}
	}

}
