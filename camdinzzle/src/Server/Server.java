package Server;

import java.io.IOException;
import java.net.ServerSocket;
/**
 * 
 */
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrea
 * 
 */
public class Server {

	/**
	 * @author Andrea
	 * 
	 */
	// oggetti per sincronizzare i metodi sugli arraylist

	/**
	 * Object for managing the synchronization of operations on hashmap players
	 */
	private Object lock_players;

	/**
	 * Object for managing the synchronization of operations on hashmap
	 * logged_player
	 */
	private Object lock_logged_player;

	/**
	 * Object for managing the synchronization of operations on hashmap species
	 */
	private Object lock_species;
	// serversocket che aprono le connessioni con i socket del client

	/**
	 * ServerSocket that listens into thread login at port 4567. It is used to
	 * connect with client
	 */
	private ServerSocket deal_login;
	/**
	 * ServerSocket that listens into thread newUser at port 4566. It is used to
	 * create a new user by client
	 */
	private ServerSocket deal_newuser;

	/**
	 * Instance of CurrentSession of Game. It contains the info about the game
	 * and current maps used for gaming
	 */
	private Game currentSession;

	/**
	 * Hashmap that contains players instances. All players registered are here
	 * from first start of server
	 */
	private HashMap<String, Player> players;

	/**
	 * Hashmap that contains the instances of players current logged to server.
	 */
	private HashMap<String, ClientManager> loggedClientManager;

	/**
	 * HashMap che contiene delle istanze dei Player loggati. Chiave il token
	 */
	private HashMap<String, Player> loggedPlayers;
	/**
	 * Integer that contains the default port of login daemon
	 */
	private int port_login;

	/**
	 * Integer that contains the default port of new user daemon
	 */
	private int port_newuser;

	/**
	 * 
	 */
	private String keyForToken;
	
	/**	
	 * Object of Login Module daemon
	 */
	private Login login;
	
	/**
	 * Object of NewUser Module daemon
	 */
	private NewUser newuser;

	
	public Server() {
		// TODO Auto-generated constructor stub

		// Definition of default port login
		port_login = 4567;
		// Definition of default port NewUser
		port_newuser = 4566;
		// Inizializzazione chiave per generazione del token
		keyForToken = this.generateKeyForToken();

		// Definition of new Server login for passing it to startLogin and
		// launch login daemon
		try {
			deal_login = new ServerSocket(port_login);
			startLoginDaemon(deal_login);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Definition of new Server NewUser for passing it to startNewUser and
		// launch newuser daemon
		try {
			deal_newuser = new ServerSocket(port_newuser);
			startNewUserDaemon(deal_newuser);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Definition of new PlayerList empty
		players = new HashMap<String, Player>();
		loggedClientManager = new HashMap<String, ClientManager>();
		loggedPlayers = new HashMap<String, Player>();
		// species = new ArrayList<Species>();
		login = null;
		newuser = null;
		this.lock_logged_player = new Object();
		this.lock_players = new Object();
		this.lock_species = new Object();
	}

	public void controlAction() {
	}

	/**
	 * Method for starting 
	 */
	public void startLoginDaemon(ServerSocket server_socket) {

		/* The connection for login must be at this port : 4567 */
		login = new Login(server_socket, this);
		login.run();

	}

	public void startClientConnectionManagerDaemon(Socket socket_with_client,
			String token, String username) {
		ClientManagerSocket new_manager = new ClientManagerSocket(
				socket_with_client, this, username, token);
		// TODO Da rivedere
		loggedPlayers.put(token, players.get(username));
		new_manager.run();
		loggedClientManager.put(token, new_manager);
		new_manager = null;
	}

	public void startNewUserDaemon(ServerSocket server_socket) {
		newuser = new NewUser(server_socket, this);
		newuser.run();
	}

	/**
	 * Aggiunge un nuovo utente
	 * 
	 * @param msg
	 *            Messaggio del Client
	 * @return Messaggio da mandare al Client
	 */
	public String add_new_user(String msg) {
		String[] parameters = ServerMessageBroker
				.manageReceiveMessageSplit(msg);

		synchronized (players) {
			if (!players.containsKey(parameters[0])) {
				Player newPlayer = new Player(parameters[0], parameters[1]);
				players.put(parameters[0], newPlayer);

				return ServerMessageBroker.createOkMessage();
			} else
				return ServerMessageBroker
						.createErroMessage("usernameOccupato");
		}
	}

	/**
	 * Aggiunge il Player alla HashMap dei Player loggati
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String login(String msg)
	{
		String[] parameters = ServerMessageBroker.manageReceiveMessageSplit(msg);
		
		synchronized (players) 
		{
			if(players.containsKey(parameters[0]))
			{
				synchronized (loggedPlayers)
				{
					if(!loggedPlayers.containsValue(players.get(parameters[0])))
					{
						String token = this.generateToken(parameters[0], new Player("aznors", "kihidui"));
						if(!this.isLoggedUser(token))
						{
							loggedPlayers.put(token, players.get(parameters[0]));
							return ServerMessageBroker.createOkMessageWithOneParameter(token);
						}
					}
				}
			}
		}
		
		return ServerMessageBroker.createErroMessage("autenticazioneFallita");
	}

	/**
	 * Aggiunge la nuova specie al Player se non esiste gi�
	 * @param msg : messaggio del Client
	 * @return Messaggio da mandare al Client
	 */
	public String addNewSpecies(String msg) 
	{
		String[] parameters = ServerMessageBroker.manageReceiveMessageSplit(msg);
		
		synchronized (loggedPlayers) 
		{
			if ((this.isLoggedUser(parameters[0]))
					&& (currentSession.getPlayer(parameters[0]) != null)) 
			{
				Set set = loggedPlayers.entrySet();
				Iterator iter = set.iterator();
				boolean isRacePresent = false;

				while (iter.hasNext()) 
				{
					Map.Entry<String, Player> me = (Map.Entry<String, Player>) iter.next();
					if (me.getValue().getSpecie().equals(parameters[1]))
						isRacePresent = true;
				}

				if (!isRacePresent) 
				{
					Species new_specie;
					if (parameters[2].equals("c")) 
					{
						new_specie = new Species(parameters[1], Species.getCarnType());
					} 
					else 
					{
						new_specie = new Species(parameters[1], Species.getVegType());
					}

					loggedPlayers.get(parameters[0]).setSpecie(new_specie);
					return ServerMessageBroker.createOkMessage();
				} 
				else
					return ServerMessageBroker.createErroMessage("nomeRazzaOccupato");
			} 
			else
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}

	/**
	 * Controlla che il player possa accedere alla partita e se pu� lo fa accesere. Pu� accedere se c'�
	 * ancora posto nella partita(non � stato raggiunto il numero massimo di
	 * giocatori) e se il token � valido
	 * 
	 * @param username: username del giocatore
	 * @param token : token del giocatore
	 * @return Il comando da inviare al Client
	 */
	public String gameAccess(String msg) 
	{
		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		
		synchronized (loggedPlayers) 
		{
			if(this.isLoggedUser(token))
			{
				if (currentSession.numberPlayersInGame() < currentSession.getMaxPlayers()) 
				{
					if (currentSession.getPlayer(token) == null)
					{
						currentSession.addPlayer(token, loggedPlayers.get(token));
						return ServerMessageBroker.createOkMessage();
					} 
					else 
					{
						return ServerMessageBroker.createTokenNonValidoErrorMessage();
					}
				}
	
				return ServerMessageBroker.createErroMessage("troppiGiocatori");
			}
			else
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}

	/**
	 * Esegue l'uscita del giocatore dalla partita
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String gameExit(String msg)
	{
		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		
		synchronized (loggedPlayers) 
		{
			if(this.isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) != null)
				{
					if(currentSession.removePlayer(token))
						return ServerMessageBroker.createOkMessage();
				}
			}
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	
	
	
	
	public void sendCommand() {

	}

	public void takeFog() {

	}
	
	/**
	 * Controlla che un utente sia gi� loggato
	 * @param token : token del giocatore
	 * @return True se il giocatore � loggato, false altrimenti
	 */
	private boolean isLoggedUser(String token)
	{
		synchronized (loggedPlayers) 
		{
			if(loggedPlayers.get(token) != null)
				return true;
			else
				return false;
		}
	}
	
	/**
	 * Chiave per generazione dei token generata in modo casuale ad ogni avvio del Server
	 * @return Chiave generata
	 */
	private String generateKeyForToken() {
		// Lunghezza della chiave da 3 a 5
		int keyLength = (int) (Math.random() * 3 + 3);
		String key = new String("");

		// Contiene i numeri casuali da 0 a 7 presenti nella chiave univocamente
		HashMap<String, String> registeredPositions = new HashMap<String, String>();
		int i = 0;

		while (i < keyLength) {
			// Generazione casuale del numero da inserire nella chiave
			int singleCasual = (int) (Math.random() * keyLength);

			// Se non � gi� presente nella chiave, viene inserito
			if (!registeredPositions.containsKey(String.valueOf(singleCasual))) {
				registeredPositions.put(String.valueOf(singleCasual),
						String.valueOf(singleCasual));
				key += String.valueOf(singleCasual);
				i++;
			}
		}

		return key;
	}

	/**
	 * @return Chiave generata dal Server pe generare il token
	 */
	public String getKeyForToken() {
		return keyForToken;
	}
	
	/**
	 * Ricerca il minimo all'interno della chiave e lo ritorna 
	 * @param key : chiave generata dal Server
	 * @return Il minimo all'interno della chiave
	 */
	private int findMin(String key) {
		int min = key.length() + 1;

		for (int i = 0; i < key.length(); i++) {
			if (Integer.parseInt(String.valueOf(key.charAt(i))) < min) {
				min = Integer.parseInt(String.valueOf(key.charAt(i)));
			}
		}

		return min;
	}

	/**
	 * Token generato tramite l'applicazione sulla concatenazione di username e riferimento a questo 
	 * oggetto). Sulla concatenazione si applica un algoritmo di trasposizione.
	 * @param username : username del giocatore
	 * @return Token generato
	 */
	private String generateToken(String username, Player el) {
		String key = this.keyForToken;
		int length = key.length();
		String concatenateIdentifier = new String(username + el);
		String token = new String("");

		for (int j = 0; j < length; j++) {
			int min = findMin(key);
			int positionMin = key.indexOf(String.valueOf(min));

			key = key.replaceFirst(String.valueOf(min),
					String.valueOf(key.length()));

			for (int i = positionMin; i < concatenateIdentifier.length(); i += length) {
				token += concatenateIdentifier.charAt(i);
			}
		}

		return token;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
