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
	private ClientListener clientListener;
	private String changeRound;
	public ConnectionManagerLocal(ClientManagerLocal manager)
	{
		this.manager = manager;
		this.username = "";
		changeRound = "";
		this.token = "";
	}
	@Override
	public String creaUtente(String username, String password) {
		// TODO Auto-generated method stub
		return manager.getServerLogic().add_new_user(username, password);
		
	}

	@Override
	public String login(String username, String password) {
		// TODO Auto-generated method stub
		String retStr = manager.getServerLogic().login(username, password);
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
	public String creaRazza(String name, String type) {
		// TODO Auto-generated method stub
		if (!token.equals(""))
		{
			String retStr = manager.getServerLogic().addNewSpecies(token, name, type);
			return retStr;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see Client.ConnectionManager#accessoPartita()
	 */
	@Override
	public String accessoPartita() {
		// TODO Auto-generated method stub
		if(!token.equals(""))
		{
			/*String retStr = server.gameAccess(token);
			if(ClientMessageBroker.manageGameAccess(retStr)[0].equals("ok"))
			{
				server.setGameAccess(true, username);
				
				if(token.equals(server.getTokenOfCurrentPlayer()))
				{
					server.changeRoundNotify();
				}
				else if(server.getTokenOfCurrentPlayer() != "")
				{
					client.sendMessage("@cambioTurno," + server.getUsernameOfCurrentPlayer());
				}
			}*/
		}
		return null;
	}

	@Override
	public String uscitaPartita() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] listaGiocatori() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> classifica() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String logout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> mappaGenerale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] listaDinosauri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> vistaLocale(String dinoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] statoDinosauro(String dinoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] muoviDinosauro(String dinoId, String row, String col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] cresciDinosauro(String dinoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] deponiUovo(String dinoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] confermaTurno() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] passaTurno() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChangeRound() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setChangeRound(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
