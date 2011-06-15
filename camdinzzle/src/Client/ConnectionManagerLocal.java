package Client;

import java.util.ArrayList;
import Server.ClientManagerLocal;
/**
 * Classe utilizzata per la gestione dello scambio di messaggi con il server in locale.
 */
public class ConnectionManagerLocal implements ConnectionManager 
{	
	/*
	 * Contiene l'oggetto della connessione da locale
	 */
	private ClientManagerLocal manager;
	/*
	 * Contiene l'username del client collegato tramite locale
	 */
	private String username;
	/*
	 * Contiene la stringa del token relativo alla connessione corrente
	 */
	private String token;
	/*
	 * Conetiene l'istanza del client relativa alla connessione corrente
	 */
	private Client client;
	/*
	 * Serve per notificare il cambio del turno alla changeRoundThread
	 */
	private String changeRound;
	
	/**
	 * Costruttore della classe ConnectionManagerLocal
	 * @param manager :gestore della connessione su server
	 */
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
		return manager.creaUtente(username, password);
	}
	@Override
	public synchronized String login(String username, String password) 
	{
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
		if (!token.equals("")) return  manager.creaRazza(token, name, type);
		else return null;
	}

	
	@Override
	public synchronized String accessoPartita() {
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
	public synchronized void rmClientLocal()
	{
		manager.rmClient();
	}
	@Override
	public synchronized String uscitaPartita() {
		if (!token.equals(""))return manager.uscitaPartita(token);
		else return null;
	}

	@Override
	public synchronized String[] listaGiocatori() {
		if (!token.equals("")) return manager.listaGiocatori(token);
		else return null;
	}

	@Override
	public synchronized ArrayList<String> classifica() {
		if (!token.equals("")) return manager.Classifica(token);
		else return null;
	}

	@Override
	public synchronized String logout() {
		if(!token.equals("")) return manager.logout(token);
		else return null;
	}

	@Override
	public synchronized ArrayList<String> mappaGenerale() {
		if(!token.equals("")) return manager.mappaGenerale(token);
		return null;
	}

	@Override
	public synchronized String[] listaDinosauri() {
		if(!token.equals("")) return manager.listaDinosauri(token);
		else return null;
	}

	@Override
	public synchronized ArrayList<String> vistaLocale(String dinoId) {
		if(!token.equals("")) return manager.vistaLocale(token, dinoId);
		else return null;
	}

	@Override
	public synchronized String[] statoDinosauro(String dinoId) {
		if(!token.equals("")) return manager.statoDinosauro(token, dinoId);
		else return null;
	}

	@Override
	public synchronized String[] muoviDinosauro(String dinoId, String row, String col) {
		if(!token.equals("")) return ClientMessageBroker.manageDinoMove(manager.muoviDinosauro(token, dinoId, row, col));
		else return null;
	}

	@Override
	public synchronized String[] cresciDinosauro(String dinoId) {
		if(!token.equals("")) return ClientMessageBroker.manageDinoGrowUp(manager.cresciDinosauro(token, dinoId));
		else return null;
	}

	@Override
	public synchronized String[] deponiUovo(String dinoId) {
		if(!token.equals("")) return ClientMessageBroker.manageNewEgg(manager.deponiUovo(token, dinoId));
		else return null;
	}

	@Override
	public synchronized String[] confermaTurno() {
		if(!token.equals("")) return ClientMessageBroker.manageRoundConfirm(manager.confermaTurno(token));
		else return null;
	}

	@Override
	public synchronized String[] passaTurno() {
		String msg = manager.passaTurno(token);
		if(!token.equals("")) return ClientMessageBroker.managePlayerChangeRound(msg);
		else return null;
	}

	@Override
	public synchronized String getChangeRound() {
		return changeRound;
	}

	@Override
	public synchronized void setChangeRound(String msg) 
	{
		this.changeRound = msg;
	}

	@Override
	public synchronized String getToken() {
		return token;
	}

	@Override
	public synchronized String getUsername() {
		return username;
	}
	@Override
	public synchronized Client getClient() {
		return this.client;
	}
	@Override
	public synchronized void setClient(Client client) {
		this.client = client;
	}
}
