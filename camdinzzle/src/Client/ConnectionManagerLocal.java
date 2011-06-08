/**
 * 
 */
package Client;

import java.util.ArrayList;
import Server.ClientManagerLocal;

/**
 * @author Andrea
 *
 */

public class ConnectionManagerLocal implements ConnectionManager 
{	
	private ClientManagerLocal manager;
	private String username;
	private String token;
	private Client client;
	

	private String changeRound;
	public ConnectionManagerLocal(ClientManagerLocal manager)
	{
		this.manager = manager;
		this.username = "";
		this.changeRound = "";
		this.token = "";
	}
	@Override
	public synchronized String creaUtente(String username, String password) 
	{
		// TODO Auto-generated method stub
		return manager.creaUtente(username, password);
	}

	@Override
	public synchronized String login(String username, String password) 
	{
		// TODO Auto-generated method stub
		String retStr = manager.Login(username, password);
		String[] response = ClientMessageBroker.manageLogin(retStr);
		
		if(response !=  null)
		{
			if(response[0].equals("ok"))
			{
				token = response[1];
				this.username = username;
			}
		}
		else return null;
		return retStr;
	}

	@Override
	public synchronized String creaRazza(String name, String type) 
	{
		// TODO Auto-generated method stub
		if (!token.equals("")) return  manager.creaRazza(token, name, type);
		else return null;
	}

	
	@Override
	public synchronized String accessoPartita() {
		// TODO Auto-generated method stub
		if(!token.equals(""))
		{
			String retStr = manager.accessoPartita(token);
			String[] ret = ClientMessageBroker.manageGameAccess(retStr);
			if(ret[0].equals("ok"))
			{
				manager.setIsInGame(true);
				
				if(token.equals(manager.getTokenOfCurrentPlayer()))
				{
					manager.changeRoundNotify();
				}
				else if(manager.getTokenOfCurrentPlayer() != "")
				{
					manager.sendMessage("@cambioTurno," + manager.getUsernameOfCurrentPlayer());
				}
				return retStr;
			}
			else if(!((ret[0].equals("no"))&&(ret[1].equals("COMANDO NON ESEGUITO"))))
			{
				return retStr;
				
			}
			else
				return null;
		}
		return null;
	}

	@Override
	public synchronized String uscitaPartita() {
		// TODO Auto-generated method stub
		if (!token.equals(""))return manager.uscitaPartita(token);
		else return null;
	}

	@Override
	public synchronized String[] listaGiocatori() {
		// TODO Auto-generated method stub
		if (!token.equals("")) return manager.listaGiocatori(token);
		else return null;
	}

	@Override
	public synchronized ArrayList<String> classifica() {
		// TODO Auto-generated method stub
		if (!token.equals("")) return manager.Classifica(token);
		else return null;
	}

	@Override
	public synchronized String logout() {
		// TODO Auto-generated method stub
		if(!token.equals("")) return manager.logout(token);
		else return null;
	}

	@Override
	public synchronized ArrayList<String> mappaGenerale() {
		// TODO Auto-generated method stub
		if(!token.equals("")) return manager.mappaGenerale(token);
		return null;
	}

	@Override
	public synchronized String[] listaDinosauri() {
		// TODO Auto-generated method stub
		if(!token.equals("")) return manager.listaDinosauri(token);
		else return null;
	}

	@Override
	public synchronized ArrayList<String> vistaLocale(String dinoId) {
		// TODO Auto-generated method stub
		if(!token.equals("")) return manager.vistaLocale(token, dinoId);
		else return null;
	}

	@Override
	public synchronized String[] statoDinosauro(String dinoId) {
		// TODO Auto-generated method stub
		if(!token.equals("")) return manager.statoDinosauro(token, dinoId);
		else return null;
	}

	@Override
	public synchronized String[] muoviDinosauro(String dinoId, String row, String col) {
		// TODO Auto-generated method stub
		if(!token.equals("")) return ClientMessageBroker.manageDinoMove(manager.muoviDinosauro(token, dinoId, row, col));
		else return null;
	}

	@Override
	public synchronized String[] cresciDinosauro(String dinoId) {
		// TODO Auto-generated method stub
		if(!token.equals("")) return ClientMessageBroker.manageDinoGrowUp(manager.cresciDinosauro(token, dinoId));
		else return null;
	}

	@Override
	public synchronized String[] deponiUovo(String dinoId) {
		// TODO Auto-generated method stub
		if(!token.equals("")) return ClientMessageBroker.manageNewEgg(manager.deponiUovo(token, dinoId));
		else return null;
	}

	@Override
	public synchronized String[] confermaTurno() {
		// TODO Auto-generated method stub
		if(!token.equals("")) return ClientMessageBroker.manageRoundConfirm(manager.confermaTurno(token));
		else return null;
	}

	@Override
	public synchronized String[] passaTurno() {
		// TODO Auto-generated method stub
		String msg = manager.passaTurno(token);
		if(!token.equals("")) return ClientMessageBroker.managePlayerChangeRound(msg);
		else return null;
	}

	@Override
	public synchronized String getChangeRound() {
		// TODO Auto-generated method stub
		
		return changeRound;
	}

	@Override
	public synchronized void setChangeRound(String msg) 
	{
		/*
		if(msg.equals(""))
		{
			changeRound = ""; 
		}
		else
		{*/
			/*
			msg = ClientMessageBroker.manageChangeRound(msg);
			if(msg != null)
			{*/
				this.changeRound = msg;
			//}
		//}
	}

	@Override
	public synchronized String getToken() {
		// TODO Auto-generated method stub
		return token;
	}

	@Override
	public synchronized String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}
	@Override
	public synchronized Client getClient() {
		// TODO Auto-generated method stub
		return this.client;
	}
	@Override
	public synchronized void setClient(Client client) {
		// TODO Auto-generated method stub
		this.client = client;
	}
	

}
