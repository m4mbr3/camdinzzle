/**
 * 
 */
package Client;

import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;
import Server.ClientManagerLocal;

/**
 * @author Andrea
 *
 */

public class ConnectionManagerLocal implements ConnectionManager 
{	
	private ClientManagerLocal manager;
	private String address;
	private String username;
	private String password;
	private String token;
	private int port;
	private String command;
	private boolean run;
	private Client client;
	

	private String changeRound;
	public ConnectionManagerLocal(ClientManagerLocal manager)
	{
		this.manager = manager;
		this.username = "";
		changeRound = "";
		this.token = "";
	}
	@Override
	public String creaUtente(String username, String password) 
	{
		// TODO Auto-generated method stub
		return manager.creaUtente(username, password);
	}

	@Override
	public String login(String username, String password) 
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
	public String creaRazza(String name, String type) 
	{
		// TODO Auto-generated method stub
		if (!token.equals("")) return  manager.creaRazza(token, name, type);
		else return null;
	}

	
	@Override
	public String accessoPartita() {
		// TODO Auto-generated method stub
		if(!token.equals(""))
		{
			String retStr = manager.accessoPartita(token);
			if(ClientMessageBroker.manageGameAccess(retStr)[0].equals("ok"))
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
		}
		return null;
	}

	@Override
	public String uscitaPartita() {
		// TODO Auto-generated method stub
		if (!token.equals(""))return manager.uscitaPartita(token);
		else return null;
	}

	@Override
	public String[] listaGiocatori() {
		// TODO Auto-generated method stub
		if (!token.equals("")) return manager.listaGiocatori(token);
		else return null;
	}

	@Override
	public ArrayList<String> classifica() {
		// TODO Auto-generated method stub
		if (!token.equals("")) return manager.Classifica(token);
		else return null;
	}

	@Override
	public String logout() {
		// TODO Auto-generated method stub
		if(!token.equals("")) return manager.logout(token);
		else return null;
	}

	@Override
	public ArrayList<String> mappaGenerale() {
		// TODO Auto-generated method stub
		if(!token.equals("")) return manager.mappaGenerale(token);
		return null;
	}

	@Override
	public String[] listaDinosauri() {
		// TODO Auto-generated method stub
		if(!token.equals("")) return manager.listaDinosauri(token);
		else return null;
	}

	@Override
	public ArrayList<String> vistaLocale(String dinoId) {
		// TODO Auto-generated method stub
		if(!token.equals("")) return manager.vistaLocale(token, dinoId);
		else return null;
	}

	@Override
	public String[] statoDinosauro(String dinoId) {
		// TODO Auto-generated method stub
		if(!token.equals("")) return manager.statoDinosauro(token, dinoId);
		else return null;
	}

	@Override
	public String[] muoviDinosauro(String dinoId, String row, String col) {
		// TODO Auto-generated method stub
		if(!token.equals("")) return ClientMessageBroker.manageDinoMove(manager.muoviDinosauro(token, dinoId, row, col));
		else return null;
	}

	@Override
	public String[] cresciDinosauro(String dinoId) {
		// TODO Auto-generated method stub
		if(!token.equals("")) return ClientMessageBroker.manageDinoGrowUp(manager.cresciDinosauro(token, dinoId));
		else return null;
	}

	@Override
	public String[] deponiUovo(String dinoId) {
		// TODO Auto-generated method stub
		if(!token.equals("")) return ClientMessageBroker.manageNewEgg(manager.deponiUovo(token, dinoId));
		else return null;
	}

	@Override
	public String[] confermaTurno() {
		// TODO Auto-generated method stub
		if(!token.equals("")) return ClientMessageBroker.manageRoundConfirm(manager.confermaTurno(token));
		else return null;
	}

	@Override
	public String[] passaTurno() {
		// TODO Auto-generated method stub
		if(!token.equals("")) return ClientMessageBroker.managePlayerChangeRound(manager.passaTurno(token));
		else return null;
	}

	@Override
	public String getChangeRound() {
		// TODO Auto-generated method stub
		
		return changeRound;
	}

	@Override
	public void setChangeRound(String msg) {
		// TODO Auto-generated method stub
		msg = ClientMessageBroker.manageChangeRound(msg);
		if(msg != null)
			changeRound = msg;
		else 
			changeRound = ""; 
	}

	@Override
	public String getToken() {
		// TODO Auto-generated method stub
		return token;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}
	@Override
	public Client getClient() {
		// TODO Auto-generated method stub
		return this.client;
	}
	@Override
	public void setClient(Client client) {
		// TODO Auto-generated method stub
		this.client = client;
	}
	

}
