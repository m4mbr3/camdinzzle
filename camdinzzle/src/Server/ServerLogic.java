package Server;

import java.io.IOException;
import java.net.ServerSocket;
/**
 * 
 */
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import Server.Species.type;

/**
 * @author Andrea
 * 
 */
public class ServerLogic {

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
	 * HashMap che contiene le specie estinte
	 */
	private HashMap<String, Species> rank;
	/**
	 * Stringa che contiene il token del giocatore che deve effettuare il turno
	 */
	private String tokenOfCurrentPlayer;
	
	private boolean isTheFirstAccess;
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

	
	public ServerLogic() {
		// TODO Auto-generated constructor stub

		
		
		players = new HashMap<String, Player>();
		loggedClientManager = new HashMap<String, ClientManager>();
		loggedPlayers = new HashMap<String, Player>();
		rank = new HashMap<String, Species>();
		currentSession = new Game();
		login = null;
		newuser = null;
		isTheFirstAccess = true;
		// Inizializzazione chiave per generazione del token
		keyForToken = this.generateKeyForToken();
		//System.out.println("<<SERVER>>--ENVIROMENT VARIABLES DEFINITED");

		
		this.lock_logged_player = new Object();
		this.lock_players = new Object();
		this.lock_species = new Object();
		this.add_new_user("@login,user=andrea,pass=andrea");
	}

	public void controlAction() {
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
		// TODO : gestione token perso
		
		String[] parameters = ServerMessageBroker.manageReceiveMessageSplit(msg);
		
		synchronized (players) 
		{
			if(players.containsKey(parameters[0]))
			{
				synchronized (loggedPlayers)
				{
					if(players.get(parameters[0]) != null)
					{
						if((((Player)players.get(parameters[0])).getUserName().equals(parameters[0])) && (((Player)players.get(parameters[0])).getPassword().equals(parameters[1])))
						{
							String token = this.generateToken(parameters[0], System.nanoTime());
							players.get(parameters[0]).setToken(token);
							
							if(!this.isLoggedUser(token))
							{
								loggedPlayers.put(token, players.get(parameters[0]));
								return ServerMessageBroker.createOkMessageWithOneParameter(token);
							}
						}
					}
				}
			}
		}
		
		return ServerMessageBroker.createErroMessage("autenticazioneFallita");
	}

	/**
	 * Aggiunge la nuova specie al Player se non esiste giï¿½
	 * @param msg : messaggio del Client
	 * @return Messaggio da mandare al Client
	 */
	public String addNewSpecies(String msg) 
	{
		String[] parameters = ServerMessageBroker.manageReceiveMessageSplit(msg);
		String token = parameters[0];
		
		synchronized (loggedPlayers) 
		{
			if (this.isLoggedUser(parameters[0])) 
			{
				boolean isRacePresent = false;
				
				synchronized(players)
				{
					/* Collection<Player> c = players.values(); 
					 * Iterator<Player> iter = c.iterator();
					 */
					/* Se il giocatore ha giï¿½ una specie ed ï¿½ uguale a quella richiesta
					 * significa che il giocatore entra in partita con una specie giï¿½ avviata ed ï¿½
					 * obbligato ad utilizzarla
					 */
					if(loggedPlayers.get(token).getSpecie() != null)
					{
						if(loggedPlayers.get(token).getSpecie().getName().equals(parameters[1]))
						{
							return ServerMessageBroker.createOkMessage();
						}
						/* Dal Client non bisogna permettere di creare una specie se ce ne ï¿½ giï¿½
						 * una avviata nella partita
						 */
						return ServerMessageBroker.createErroMessage("nomeRazzaOccupato");
					}
						
					Set set = players.entrySet();
					Iterator<Player> iter = set.iterator();
					isRacePresent = false;
					
					while (iter.hasNext()) 
					{
						Map.Entry<String, Player> me = (Map.Entry<String, Player>) iter.next();
						
						if(me.getValue().getSpecie() != null)
						{
							if (me.getValue().getSpecie().getName().equals(parameters[1]))
							{
								isRacePresent = true;
								break;
							}
						}
					}
				}
				if (!isRacePresent) 
				{
					Species new_specie;
					if (parameters[2].equals("c")) 
					{
						new_specie = new Species(parameters[1], Species.getCarnType(), loggedPlayers.get(parameters[0]).getUserName());
					} 
					else 
					{
						new_specie = new Species(parameters[1], Species.getVegType(), loggedPlayers.get(parameters[0]).getUserName());
					}

					rank.put(new_specie.getName(), new_specie);
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
	 * Controlla che il player possa accedere alla partita e se puo lo fa accedere. Puï¿½ accedere se c'ï¿½
	 * ancora posto nella partita(non ï¿½ stato raggiunto il numero massimo di
	 * giocatori) e se il token ï¿½ valido. Se il giocatore non ha ancora creato una propria specie, viene creata una specie di
	 * default che ha come nome l'username del player e come razza e.
	 * 
	 * @param username: username del giocatore
	 * @param token : token del giocatore
	 * @return Il comando da inviare al Client
	 */
	public String gameAccess(String msg) 
	{
		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		// TODO: imposizione sul client di creare la specie prima di avere un accesso alla partita
		synchronized (loggedPlayers) 
		{
			if(this.isLoggedUser(token))
			{
				/*
				if(loggedPlayers.get(token).getSpecie() == null)
				{
					loggedPlayers.get(token).setSpecie(new Species(loggedPlayers.get(token).getUserName(), Species.getVegType()));
				}
				*/
				if (currentSession.numberPlayersInGame() < currentSession.getMaxPlayers()) 
				{
					if (currentSession.getPlayer(token) == null)
					{
						currentSession.addPlayer(token, loggedPlayers.get(token));
						if(isTheFirstAccess)
						{
							tokenOfCurrentPlayer = token;
							isTheFirstAccess = false;
						}
								
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
					{
						if(currentSession.numberPlayersInGame() == 0)
							isTheFirstAccess = true;
						return ServerMessageBroker.createOkMessage();
					}
				}
				
				if(currentSession.numberPlayersInGame() == 0)
					isTheFirstAccess = true;
			}
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Esegue la creazione della lista di giocatori in partita.
	 * @param msg : messaggio del Client
	 * @return Messaggio da mandare al Client
	 */
	public String playerList(String msg)
	{
		ArrayList<String> parameters = new ArrayList<String>();
		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		parameters.add("listaGiocatori");
		
		synchronized (loggedPlayers) 
		{
			if(isLoggedUser(token))
			{
				if(loggedPlayers.get(token) != null)
				{
					Iterator<Player> iter = currentSession.getPlayersList();
					
					while(iter.hasNext())
					{
						Map.Entry me = (Map.Entry) iter.next();
						parameters.add(((Player)me.getValue()).getUserName());
					}
					
					return ServerMessageBroker.createStandardMessage(parameters);
				}
			}
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Genera la classifica di tutti gli utenti registrati
	 * @param msg : messaggio del Client
	 * @return Messaggio da mandare al Client
	 */
	public String ranking(String msg)
	{
		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		ArrayList<String> parameters = new ArrayList<String>();
		// TODO: gestione specie estinte e cambio punteggio nel player
		synchronized (loggedPlayers) 
		{
			HashMap<String, String> alreadySpeciesRead = new HashMap<String, String>();
			ArrayList<String> ranking = new ArrayList<String>();
			int maxScore;
			String speciesMaxScore = "";
			String usernameMaxScore = "";
			Map.Entry me = null;
			
			if(isLoggedUser(token))
			{
				for(int i = 0; i<rank.size(); i++)
				{
					Set set = rank.entrySet();
					Iterator  iter = set.iterator();
					maxScore = -10;
					
					while(iter.hasNext())
					{
						me = (Map.Entry) iter.next();
						
						if(((Species)me.getValue()).getPunteggio() > maxScore)
						{
							if(!alreadySpeciesRead.containsKey(((Species)me.getValue()).getName()))
							{
								maxScore = ((Species)me.getValue()).getPunteggio();
								speciesMaxScore = ((Species)me.getValue()).getName();
								usernameMaxScore = ((Species)me.getValue()).getPlayerUsername();
							}
						}
					}
					
					if(!speciesMaxScore.equals(""))
					{
						alreadySpeciesRead.put(speciesMaxScore, speciesMaxScore);
						
						ranking.add(usernameMaxScore);
						ranking.add(speciesMaxScore);
						ranking.add(String.valueOf(maxScore));
						if(currentSession.getPlayer(players.get(usernameMaxScore).getToken()) != null)
							ranking.add("s");
						else
							ranking.add("n");
					}
					
				}
				
				return ServerMessageBroker.createRankingList(ranking);
			}
			
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Esegue il logout di un giocatore loggato. Se il giocatore ï¿½ in partita, prima di eseguire il 
	 * logout deve uscire dalla partita
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String logout(String msg)
	{
		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		
		synchronized (loggedPlayers) 
		{
			if(isLoggedUser(token))
			{
				loggedPlayers.remove(token);
				
				if(currentSession.getPlayer(token) != null)
					currentSession.removePlayer(token);
				
				return ServerMessageBroker.createOkMessage();
			}
			else
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Crea la mappa generale da mandare a giocatori in partita
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String generalMap(String msg)
	{
		ArrayList<String> map = new ArrayList<String>();
		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		
		synchronized (loggedPlayers) 
		{
			if(isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) != null)
				{
					loggedPlayers.get(token).getSpecie().updateMap();
					Object[][] matrixMap = loggedPlayers.get(token).getSpecie().getPlayerMap();
					
					map.add(String.valueOf(Game.maxRow));
					map.add( String.valueOf(Game.maxCol));
					
					for(int i=0; i<Game.maxRow; i++)
					{
						for(int j=0; j<Game.maxCol; j++)
						{
							map.add((String)matrixMap[i][j]);
						}
					}
					
					return ServerMessageBroker.createGeneraleMap(map);
				}
				else
					return ServerMessageBroker.createErroMessage("nonInPartita");
			}
			else
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Crea la lista dei dinosauri di un giocatore
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String dinosaursList(String msg)
	{
		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		ArrayList<String> list = new ArrayList<String>();
		list.add("listaDinosauri");
		
		synchronized(loggedPlayers)
		{
			if(isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) != null)
				{
					Iterator<Dinosaur> dinos = currentSession.getPlayer(token).getSpecie().getDinosaurs();
					
					while(dinos.hasNext())
					{
						Map.Entry me = (Map.Entry) dinos.next();
						list.add(((Dinosaur)me.getValue()).getDinoId());
					}
					return ServerMessageBroker.createStandardMessage(list);
				}
				else
					return ServerMessageBroker.createErroMessage("nonInPartita");
			}
			else
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Crea la vista di un dinosauro dal suo id
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String dinoZoom(String msg)
	{
		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		String dinoId = ServerMessageBroker.manageReceiveMessageSplit(msg)[1];
		
		ArrayList<String> zoom = new ArrayList<String>();
		
		synchronized(loggedPlayers)
		{
			if(isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) != null)
				{
					Dinosaur dino = loggedPlayers.get(token).getSpecie().getDino(dinoId);
					if( dino != null)
					{
						zoom.add(String.valueOf(dino.getPosRow()));
						zoom.add(String.valueOf(dino.getPosCol()));
						zoom.add(String.valueOf(dino.getSizeLocalMap()));
						zoom.add(String.valueOf(dino.getSizeLocalMap()));
						
						Object[][] localMap = dino.getLocalMap();
						
						for(int i = 0; i<localMap.length; i++)
						{
							for(int j = 0; j<localMap[i].length; j++)
							{
								if(localMap[i][j] instanceof Dinosaur )
									zoom.add("d," + ((Dinosaur)localMap[i][j]).getDinoId());
								else if(localMap[i][j] instanceof Carrion)
									zoom.add("c," + ((Carrion)localMap[i][j]).getPower());
								else if(localMap[i][j] instanceof Vegetation)
									zoom.add("v," + ((Vegetation)localMap[i][j]).getPower());
								else
									zoom.add(localMap[i][j].toString());
							}
						}
						return ServerMessageBroker.createDinoZoom(zoom);
					}
					else
						return ServerMessageBroker.createErroMessage("idNonValido");
				}
				else
					return ServerMessageBroker.createErroMessage("nonInPartita");
			}
			else
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Crea lo stato di un dinosauro
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String dinoState(String msg)
	{
		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		String dinoId = ServerMessageBroker.manageReceiveMessageSplit(msg)[1];
		ArrayList<String> state = new ArrayList<String>();
		boolean isPossibleDino = false;
		Dinosaur possibleDino = null;
		
		synchronized(loggedPlayers)
		{
			if(isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) != null)
				{
					Dinosaur dino = loggedPlayers.get(token).getSpecie().getDino(dinoId);
					if( dino != null)
					{
						state.add(currentSession.getPlayer(token).getUserName());
						state.add(currentSession.getPlayer(token).getSpecie().getName());
						state.add(currentSession.getPlayer(token).getSpecie().getType().toString());
						state.add(String.valueOf(dino.getPosRow()));
						state.add(String.valueOf(dino.getPosCol()));
						state.add(String.valueOf(dino.getDinoDimension()));
						state.add(String.valueOf(dino.getEnergy()));
						state.add(String.valueOf(dino.getTurniVissuti()));
						
						return ServerMessageBroker.createDinoState(state);
					}
					else
					{
						/* Controllo che il dinosauro richiesto appartiene alla vista di uno dei dinosauri
						 * del giocatore
						 */
						
						Iterator<Dinosaur> dinosaurs = loggedPlayers.get(token).getSpecie().getDinosaurs();
						// Itera i dinosauri del giocatore
						while (dinosaurs.hasNext()) 
						{
							if(isPossibleDino)
								break;
							
							Map.Entry me = (Map.Entry) dinosaurs.next();
							/* TODO : da fare o no l'updateMap
							loggedPlayers.get(token).getSpecie().updateMap();
							*/
							Object[][] matrixMap = ((Dinosaur)me.getValue()).getLocalMap();
							// Cicla tutta la mappa e se trova un dinosauro con l'id richiesto esce dal ciclo
							for(int i = 0; i<matrixMap.length; i++)
							{
								if(isPossibleDino)
									break;
								
								for(int j = 0; j<matrixMap[i].length; j++)
								{
									if(matrixMap[i][j] instanceof Dinosaur)
									{
										if(((Dinosaur)matrixMap[i][j]).getDinoId().equals(dinoId))
										{
											isPossibleDino = true;
											possibleDino = (Dinosaur)matrixMap[i][j];
											break;
										}
									}
								}
							}
						}
						if((isPossibleDino) &&(possibleDino != null))
						{
							// cercare il dinosauro nei dinosauri di tutti gli altri utenti
							Iterator<Player> iter = currentSession.getPlayersList();
							
							while (iter.hasNext()) 
							{
								Map.Entry me = (Map.Entry) iter.next();
								dino = ((Player)me.getValue()).getSpecie().getDino(dinoId);
								
								if((dino != null) && (dino.equals(possibleDino)))
								{
									 state.add(((Player)me.getValue()).getUserName());
									 state.add(((Player)me.getValue()).getSpecie().getName());
									 state.add(((Player)me.getValue()).getSpecie().getType().toString());
									 state.add(String.valueOf(dino.getPosRow()));
									 state.add(String.valueOf(dino.getPosCol()));
									 state.add(String.valueOf(dino.getDinoDimension()));
									 
									 return ServerMessageBroker.createDinoState(state);
								}
							}
							
							return ServerMessageBroker.createErroMessage("idNonValido");
						}
						else
							return ServerMessageBroker.createErroMessage("idNonValido");
					}
				}
				else
					return ServerMessageBroker.createErroMessage("nonInPartita");
			}
			else
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	public String dinoMove(String msg)
	{
		String token = ServerMessageBroker.manageDinoMovement(msg)[0];
		String dinoId = ServerMessageBroker.manageDinoMovement(msg)[1];
		int	dinoRow = Integer.parseInt(ServerMessageBroker.manageDinoMovement(msg)[2]);
		int	dinoCol = Integer.parseInt(ServerMessageBroker.manageDinoMovement(msg)[3]);
		
		synchronized(loggedPlayers)
		{
			if(isLoggedUser(token))						//controlla se  loggato
			{
				if(currentSession.getPlayer(token) != null)			//controllo se  in partita
				{
		//manca non  il tuo turno
					if(currentSession.getPlayer(token).getSpecie().getDino(dinoId)!=null)		//controllo dinoId
					{
						Dinosaur dino = currentSession.getPlayer(token).getSpecie().getDino(dinoId);
						if(!currentSession.getPlayer(token).getSpecie().getDino(dinoId).getMoveTake())		//controllo che il dinosauro possa fare ancora mosse di movimento
						{
							if((dinoRow>=0)&&(dinoRow<Game.maxRow)&&(dinoCol>=0)&&(dinoCol<Game.maxCol))	//controllo che la destinazione sia nella mappa
							{
								if(Game.checkReachCell(dino.getPosRow(), dino.getPosCol(), dinoRow, dinoCol, dino.getDistMax()))	//controllo che il dinosauro possa arrivare a destinazione
								{
									if(!(((currentSession.getPlayer(token).getSpecie()).getType() == type.Vegetarian)&&(Game.getCell(dinoRow, dinoCol) instanceof Vegetarian)))		//se dino  vegetariano controllo che nn ci sia un vegetariano a destinazione
									{
										if(currentSession.getPlayer(token).getSpecie().getDino(dinoId).move(dinoRow, dinoCol))		//controlla che abbia abbastanza energia x muoversi e cambia le coordinate in dino
										{
											if((Game.getCell(dinoRow, dinoCol) instanceof Vegetarian)||(Game.getCell(dinoRow, dinoCol) instanceof Carnivorous))		//controlla se c' un altro dinosauro nella cella di arrivo
											{
												if(currentSession.getPlayer(token).getSpecie().getDino(dinoId).fight(Game.getCell(dinoRow, dinoCol)))		//combatte
												{
													
/*													String specie = ((Dinosaur)Game.getCell(dinoRow, dinoCol)).getSpecie();
													boolean check=false;
													Iterator iter = currentSession.getPlayersList();
													Map.Entry me;	
													do
													{
														me = (Map.Entry) iter.next();
														if((specie.compareTo(((Player)me.getValue()).getSpecie().getName()))==0)
														{
															check=true;
														}
													}while(!check);
*/													
													((Dinosaur)Game.getCell(dinoRow, dinoCol)).getSpecie().killDino((Dinosaur)Game.getCell(dinoRow, dinoCol));
													
													
													
													if(((currentSession.getPlayer(token).getSpecie()).getType() == type.Vegetarian)&&(Game.getCell(dinoRow, dinoCol) instanceof Carrion))		//controlla se il dino  vegetariano e se nella cella c' carogna
													{
														((Vegetarian)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).setCarrion((Carrion)Game.getCell(dinoRow, dinoCol));
													}
													if(((currentSession.getPlayer(token).getSpecie()).getType() == type.Carnivorous)&&(Game.getCell(dinoRow, dinoCol) instanceof Vegetation))	//controlla se il dino  carnivoro e se nella cella c' vegetazione
													{
														((Carnivorous)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).setVegetation((Vegetation)Game.getCell(dinoRow, dinoCol));
													}
													
													Game.setCellMap(currentSession.getPlayer(token).getSpecie().getDino(dinoId), dinoRow, dinoCol);
													return ServerMessageBroker.createOkMessageWithTwoParameter("combattimento", "v");
												}
												else
												{
													currentSession.getPlayer(token).getSpecie().killDino(currentSession.getPlayer(token).getSpecie().getDino(dinoId));
													return ServerMessageBroker.createOkMessageWithTwoParameter("combattimento", "p");
												}
											}
											else
											{
												if(((currentSession.getPlayer(token).getSpecie()).getType() == type.Vegetarian)&&(Game.getCell(dinoRow, dinoCol) instanceof Carrion))		//controlla se il dino  vegetariano e se nella cella c' carogna
												{
													((Vegetarian)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).setCarrion((Carrion)Game.getCell(dinoRow, dinoCol));
												}
												if(((currentSession.getPlayer(token).getSpecie()).getType() == type.Carnivorous)&&(Game.getCell(dinoRow, dinoCol) instanceof Vegetation))	//controlla se il dino  carnivoro e se nella cella c' vegetazione
												{
													((Carnivorous)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).setVegetation((Vegetation)Game.getCell(dinoRow, dinoCol));
												}
												Game.setCellMap(currentSession.getPlayer(token).getSpecie().getDino(dinoId), dinoRow, dinoCol);
												return ServerMessageBroker.createOkMessage();
											}
										}
										else
										{
											currentSession.getPlayer(token).getSpecie().killDino(currentSession.getPlayer(token).getSpecie().getDino(dinoId));
											return ServerMessageBroker.createErroMessage("mortePerInedia");
										}
									}
									else
									{
										return ServerMessageBroker.createErroMessage("destinazioneNonValida");
									}
								}
								else
								{
									return ServerMessageBroker.createErroMessage("destinazioneNonValida");
								}
							}
							else
							{
								return ServerMessageBroker.createErroMessage("destinazioneNonValida");
							}
						}
						else
						{
							return ServerMessageBroker.createErroMessage("raggiuntoLimiteMosseDinosauro");
						}
					}
					else
					{
						return ServerMessageBroker.createErroMessage("idNonValido");
					}
				}
				else
				{
					return ServerMessageBroker.createErroMessage("nonInPartita");
				}
			}
			else
			{
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
			}
		}
	}
	
	public String dinoGrowUp(String msg)
	{

		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		String dinoId = ServerMessageBroker.manageReceiveMessageSplit(msg)[1];
		
		synchronized(loggedPlayers)
		{
			if(isLoggedUser(token))						//controlla se  loggato
			{
				if(currentSession.getPlayer(token) != null)			//controllo token valido
				{
		//manca non  il tuo turno
					if(currentSession.getPlayer(token).getSpecie().getDino(dinoId)!=null)		//controllo dinoId
					{
						if(!currentSession.getPlayer(token).getSpecie().getDino(dinoId).getActionTake())		//controlla se l'azione  gia stata fatta
						{
							if(currentSession.getPlayer(token).getSpecie().getDino(dinoId).growUp())		//non ha abbastanza energia
							{
								return ServerMessageBroker.createOkMessage();
							}
							else
							{
								currentSession.getPlayer(token).getSpecie().killDino(currentSession.getPlayer(token).getSpecie().getDino(dinoId));
								return ServerMessageBroker.createErroMessage("mortePerInedia");
							}
						}
						else
						{
							return ServerMessageBroker.createErroMessage("raggiuntoLimiteMosseDinosauro");
						}
					}
					else
					{
						return ServerMessageBroker.createErroMessage("idNonValido");
					}
				}
				else
				{
					return ServerMessageBroker.createErroMessage("nonInPartita");
				}
			}
			else
			{
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
			}
		}
	}
	
	public String dinoNewEgg(String msg)
	{

		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		String dinoId = ServerMessageBroker.manageReceiveMessageSplit(msg)[1];
		
		synchronized(loggedPlayers)
		{
			if(isLoggedUser(token))						//controlla se  loggato
			{
				if(currentSession.getPlayer(token) != null)			//controllo token valido
				{
		//manca non  il tuo turno
					if(currentSession.getPlayer(token).getSpecie().getDino(dinoId)!=null)		//controllo dinoId
					{
						if(!currentSession.getPlayer(token).getSpecie().getDino(dinoId).getActionTake())		//controlla se l'azione  gia stata fatta
						{
							String idDino = currentSession.getPlayer(token).getSpecie().getDino(dinoId).newEgg();
							if(idDino !=null)		//non ha abbastanza energia
							{
								return ServerMessageBroker.createOkMessageWithOneParameter(idDino);//id dino nuovo
							}
							else
							{
								currentSession.getPlayer(token).getSpecie().killDino(currentSession.getPlayer(token).getSpecie().getDino(dinoId));
								return ServerMessageBroker.createErroMessage("mortePerInedia");
							}
						}
						else
						{
							return ServerMessageBroker.createErroMessage("raggiuntoLimiteMosseDinosauro");
						}
					}
					else
					{
						return ServerMessageBroker.createErroMessage("idNonValido");
					}
				}
				else
				{
					return ServerMessageBroker.createErroMessage("nonInPartita");
				}
			}
			else
			{
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
			}
		}
	}
	
	
	/**
	 * Esegue la conferma del turno
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String roundConfirm(String msg)
	{
		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		
		synchronized (loggedPlayers) 
		{
			if(isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) != null)
				{
					if(tokenOfCurrentPlayer == token)
						return ServerMessageBroker.createOkMessage();
					else
						return ServerMessageBroker.createErroMessage("nonIlTuoTurno");
				}
				else
					return ServerMessageBroker.createErroMessage("nonInPartita");
			}
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Esegue il passa turno da parte di un giocatore
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String playerRoundSwitch(String msg)
	{
		String token = ServerMessageBroker.manageReceiveMessageSplit(msg)[0];
		
		synchronized (loggedPlayers) 
		{
			if(isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) != null)
				{
					if(tokenOfCurrentPlayer == token)
					{
						Iterator iter = currentSession.getPlayersList();
						Map.Entry me;
						int tableSize = 0;
						
						while(iter.hasNext())
						{
							tableSize++;
							me = (Map.Entry) iter.next();
							
							if((((String) me.getKey()).equals(token)) && (tableSize < currentSession.numberPlayersInGame()))
							{
								me = (Map.Entry) iter.next();
								tokenOfCurrentPlayer = (String)me.getKey();
								return ServerMessageBroker.createOkMessage();
							}
							else if((((String) me.getKey()).equals(token)) && (tableSize == currentSession.numberPlayersInGame()))
							{
								// TODO : gestione del null ritornato
								tokenOfCurrentPlayer = currentSession.getFirstPlayer();
								return ServerMessageBroker.createOkMessage();
							}
						}
					}
					else
						return ServerMessageBroker.createErroMessage("nonIlTuoTurno");
				}
				else
					return ServerMessageBroker.createErroMessage("nonInPartita");
			}
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Esegue il cambio del turno(notifica in partita)
	 * @return Messaggio da mandare in broadcast ai Client per notificare che ï¿½ cambiato il turno. Il 
	 * messaggio contiene il comando e l'username del giocatore abilitato a fare le proprie mosse
	 */
	public String serverRoundSwitch()
	{
		Iterator iter = currentSession.getPlayersList();
		Map.Entry me;
		int tableSize = 0;
		
		while(iter.hasNext())
		{
			tableSize++;
			me = (Map.Entry) iter.next();
			
			if((((String) me.getKey()).equals(tokenOfCurrentPlayer)) && (tableSize < currentSession.numberPlayersInGame()))
			{
				me = (Map.Entry) iter.next();
				tokenOfCurrentPlayer = (String)me.getKey();
				break;
			}
			else if((((String) me.getKey()).equals(tokenOfCurrentPlayer)) && (tableSize == currentSession.numberPlayersInGame()))
			{
				// TODO : gestione del null ritornato
				tokenOfCurrentPlayer = currentSession.getFirstPlayer();
				break;
			}
		}
		
		synchronized(loggedPlayers)
		{
			String username = loggedPlayers.get(tokenOfCurrentPlayer).getUserName();
			return ServerMessageBroker.createServerRoundSwitch(username);
		}
	}
	
	
	public void sendCommand() {

	}

	public void takeFog() {

	}
	
	/**
	 * Controlla che un utente sia giï¿½ loggato
	 * @param token : token del giocatore
	 * @return True se il giocatore ï¿½ loggato, false altrimenti
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
	 * 
	 * 
	 * Chiave per generazione dei token generata in modo casuale ad ogni avvio del ServerLogic
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

			// Se non ï¿½ giï¿½ presente nella chiave, viene inserito
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
	 * @return Chiave generata dal ServerLogic pe generare il token
	 */
	public String getKeyForToken() {
		return keyForToken;
	}
	
	/**
	 * Ricerca il minimo all'interno della chiave e lo ritorna 
	 * @param key : chiave generata dal ServerLogic
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
	private String generateToken(String username, long el) {
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

		if(token.contains("@"))
			token = token.replaceAll("@", "#");
			
		return token;
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerLogic serverLogic = new ServerLogic();
	}

}
