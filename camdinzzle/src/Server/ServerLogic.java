package Server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.timer.Timer;
import javax.management.timer.TimerNotification;

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

	Thread counter30s;
	Thread counter2m;
	private static int timeForConfirm = 30000;   // in millisecondi
	private static int timeForPlay = 120000;
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
	
	
	
	public ServerLogic() {
		// TODO Auto-generated constructor stub

		
		
		players = new HashMap<String, Player>();
		loggedClientManager = new HashMap<String, ClientManager>();
		loggedPlayers = new HashMap<String, Player>();
		rank = new HashMap<String, Species>();
		currentSession = new Game();
		isTheFirstAccess = true;
		tokenOfCurrentPlayer = "";
		// Inizializzazione chiave per generazione del token
		keyForToken = this.generateKeyForToken();
		//System.out.println("<<SERVER>>--ENVIROMENT VARIABLES DEFINITED");
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
						/* Dal Client non bisogna permettere di creare una specie se ce ne ï¿½ giï¿½
						 * una avviata nella partita
						 */
						return ServerMessageBroker.createErroMessage("razzaGiaCreata");
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
		// CHiamat di questo metodo seguita dalla chiamata a changeRound
		synchronized (loggedPlayers) 
		{
			if(this.isLoggedUser(token))
			{
				if (currentSession.numberPlayersInGame() < currentSession.getMaxPlayers()) 
				{
					if (currentSession.getPlayer(token) == null)
					{
						currentSession.addPlayer(token, loggedPlayers.get(token));
						if(isTheFirstAccess)
						{
							tokenOfCurrentPlayer = token;
							isTheFirstAccess = false;
							//this.changeRound();
						}
						
						// Aggiungo i dinosauri del giocatore alla mappa
						Iterator dinosaurs = currentSession.getPlayer(token).getSpecie().getDinosaurs();
						while(dinosaurs.hasNext())
						{
							Map.Entry me = (Map.Entry)dinosaurs.next();
							
							Dinosaur current = ((Dinosaur)me.getValue());
							
							if((Game.getCell(current.getPosRow(), current.getPosCol()) instanceof Dinosaur) || (Game.getCell(current.getPosRow(), current.getPosCol()) instanceof Food) || ((Game.getCell(current.getPosRow(), current.getPosCol()) instanceof String) &&(Game.getCell(current.getPosRow(), current.getPosCol()).equals("a"))))
							{
								/* Se la cella in cui c'era il dinosauro è occupata allora viene messo in una posizione
								 * libera partendo dall'inizio della sua vista in alto a sinistra. Viene assunto che 
								 * sicuramente una casella libera nella sua vista c'è!!!!!!!
								 */
								for(int i = current.getPosRow() - current.getSizeLocalMap(); i<current.getPosRow() + current.getSizeLocalMap(); i++)
								{
									for(int j = current.getPosCol() - current.getSizeLocalMap(); j<current.getPosCol() + current.getSizeLocalMap(); j++)
									{
										if((Game.getCell(current.getPosRow(), current.getPosCol()) instanceof Dinosaur) == false)
											Game.setCellMap(current, current.getPosRow(), current.getPosCol());
										else if((Game.getCell(current.getPosRow(), current.getPosCol()) instanceof Food) == false)
											Game.setCellMap(current, current.getPosRow(), current.getPosCol());
										else if(((Game.getCell(current.getPosRow(), current.getPosCol()) instanceof String) &&(Game.getCell(current.getPosRow(), current.getPosCol()).equals("a"))) == false)
											Game.setCellMap(current, current.getPosRow(), current.getPosCol());
									}
								}
							}	
							else
								Game.setCellMap(current, current.getPosRow(), current.getPosCol());
						}
						currentSession.getPlayer(token).getSpecie().updateMap();
						
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
						// Tolgo i dinosauri del giocatore dalla mappa
						Iterator<Dinosaur> dinosaurs = loggedPlayers.get(token).getSpecie().getDinosaurs();
						while(dinosaurs.hasNext())
						{
							Map.Entry me = (Map.Entry) dinosaurs.next();
							
							if(me.getValue() instanceof Carnivorous)
							{
								if(((Carnivorous)me.getValue()).getVegetation() != null)
									Game.setCellMap(((Carnivorous)me.getValue()).getVegetation(), ((Carnivorous)me.getValue()).getPosRow(), ((Carnivorous)me.getValue()).getPosCol());
								else
									Game.setCellMap("t", ((Carnivorous)me.getValue()).getPosRow(), ((Carnivorous)me.getValue()).getPosCol());
							}
							else
							{
								if(((Vegetarian)me.getValue()).getCarrion() != null)
									Game.setCellMap(((Vegetarian)me.getValue()).getCarrion(), ((Vegetarian)me.getValue()).getPosRow(), ((Vegetarian)me.getValue()).getPosCol());
								else
									Game.setCellMap("t", ((Vegetarian)me.getValue()).getPosRow(), ((Vegetarian)me.getValue()).getPosCol());
							}
						}
						
						if(currentSession.numberPlayersInGame() == 0)
							isTheFirstAccess = true;
						// TODO: cambiare il tokenOfCurrentPlayer
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
						
						if(((Species)me.getValue()).getScore() > maxScore)
						{
							if(!alreadySpeciesRead.containsKey(((Species)me.getValue()).getName()))
							{
								maxScore = ((Species)me.getValue()).getScore();
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
				// TODO: cambio del tokenOfCurrentPlayer
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
						zoom.add(String.valueOf(dino.getPosRow()+dino.getSizeLocalMap()/2));
						zoom.add(String.valueOf(dino.getPosCol()-dino.getSizeLocalMap()/2));
						zoom.add(String.valueOf(dino.getSizeLocalMap()));
						zoom.add(String.valueOf(dino.getSizeLocalMap()));
						
						Object[][] localMap = dino.getLocalMap();
						
						for(int i = localMap.length-1; i>=0; i--)
						{
							for(int j = 0; j<localMap[i].length; j++)
							{
								if(localMap[i][j] instanceof Dinosaur )
									zoom.add("d," + ((Dinosaur)localMap[i][j]).getDinoId());
								else if(localMap[i][j] instanceof Carrion)
									zoom.add("c," + ((Carrion)localMap[i][j]).getPower());
								else if(localMap[i][j] instanceof Vegetation)
									zoom.add("v," + ((Vegetation)localMap[i][j]).getPower());
								else if(localMap[i][j] != null)
									zoom.add(localMap[i][j].toString());
								else
									zoom.add(null);
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
						state.add(String.valueOf(dino.getAge()));
						
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
			if(isLoggedUser(token))						//controlla se � loggato
			{
				if(currentSession.getPlayer(token) != null)			//controllo se � in partita
				{
		//manca non � il tuo turno
					if(currentSession.getPlayer(token).getSpecie().getDino(dinoId)!=null)		//controllo dinoId
					{
						Dinosaur dino = currentSession.getPlayer(token).getSpecie().getDino(dinoId);
						if(!currentSession.getPlayer(token).getSpecie().getDino(dinoId).getMoveTake())		//controllo che il dinosauro possa fare ancora mosse di movimento
						{
							if((dinoRow>=0)&&(dinoRow<Game.maxRow)&&(dinoCol>=0)&&(dinoCol<Game.maxCol))	//controllo che la destinazione sia nella mappa
							{
								if(Game.checkReachCell(dino.getPosRow(), dino.getPosCol(), dinoRow, dinoCol, dino.getDistMax()))	//controllo che il dinosauro possa arrivare a destinazione
								{
									if(!(((currentSession.getPlayer(token).getSpecie()).getType() == type.Vegetarian)&&(Game.getCell(dinoRow, dinoCol) instanceof Vegetarian)))		//se dino � vegetariano controllo che nn ci sia un vegetariano a destinazione
									{
										if(currentSession.getPlayer(token).getSpecie().getDino(dinoId).move(dinoRow, dinoCol))		//controlla che abbia abbastanza energia x muoversi e cambia le coordinate in dino
										{
											if((Game.getCell(dinoRow, dinoCol) instanceof Vegetarian)||(Game.getCell(dinoRow, dinoCol) instanceof Carnivorous))		//controlla se c'� un altro dinosauro nella cella di arrivo
											{
												if(currentSession.getPlayer(token).getSpecie().getDino(dinoId).fight(Game.getCell(dinoRow, dinoCol)))		//combatte
												{
													
/*		ricerca dino avversario						String specie = ((Dinosaur)Game.getCell(dinoRow, dinoCol)).getSpecie();
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
													
//aggiornare mappa generale del player che perde													
													if(((currentSession.getPlayer(token).getSpecie()).getType() == type.Vegetarian)&&(Game.getCell(dinoRow, dinoCol) instanceof Vegetation))		//controlla se il dino � vegetariano e se nella cella c'� vegetazione
													{
														((Vegetarian)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).eat(Game.getCell(dinoRow, dinoCol));								//quindi mangia
													}
													if(((currentSession.getPlayer(token).getSpecie()).getType() == type.Carnivorous)&&(Game.getCell(dinoRow, dinoCol) instanceof Carrion))	//controlla se il dino � carnivoro e se nella cella c'� carogna
													{
	/*mangiare altro dino*/								((Carnivorous)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).eat(Game.getCell(dinoRow, dinoCol));						//quindi mangia
													}
													
													if(((currentSession.getPlayer(token).getSpecie()).getType() == type.Vegetarian)&&(Game.getCell(dinoRow, dinoCol) instanceof Carrion))		//controlla se il dino � vegetariano e se nella cella c'� carogna
													{
														((Vegetarian)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).setCarrion((Carrion)Game.getCell(dinoRow, dinoCol));
													}
													if(((currentSession.getPlayer(token).getSpecie()).getType() == type.Carnivorous)&&(Game.getCell(dinoRow, dinoCol) instanceof Vegetation))	//controlla se il dino � carnivoro e se nella cella c'� vegetazione
													{
	/*mangiare altro dino*/								((Carnivorous)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).setVegetation((Vegetation)Game.getCell(dinoRow, dinoCol));
													}
													
													Game.setCellMap(currentSession.getPlayer(token).getSpecie().getDino(dinoId), dinoRow, dinoCol);
													currentSession.getPlayer(token).getSpecie().getDino(dinoId).setLocalMap();
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
												if(((currentSession.getPlayer(token).getSpecie()).getType() == type.Vegetarian)&&(Game.getCell(dinoRow, dinoCol) instanceof Vegetation))		//controlla se il dino � vegetariano e se nella cella c'� vegetazione
												{
													((Vegetarian)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).eat(Game.getCell(dinoRow, dinoCol));								//quindi mangia													
													((Vegetarian)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).setVegetation((Vegetation)Game.getCell(dinoRow, dinoCol));
													
												}
												if(((currentSession.getPlayer(token).getSpecie()).getType() == type.Carnivorous)&&(Game.getCell(dinoRow, dinoCol) instanceof Carrion))	//controlla se il dino � carnivoro e se nella cella c'� carogna
												{
													((Carnivorous)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).eat(Game.getCell(dinoRow, dinoCol));						//quindi mangia
													currentSession.repositionCarrion(dinoRow, dinoCol);																					//riposiziona carogna
												}
												if(((currentSession.getPlayer(token).getSpecie()).getType() == type.Vegetarian)&&(Game.getCell(dinoRow, dinoCol) instanceof Carrion))		//controlla se il dino � vegetariano e se nella cella c'� carogna
												{
													((Vegetarian)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).setCarrion((Carrion)Game.getCell(dinoRow, dinoCol));
												}
												if(((currentSession.getPlayer(token).getSpecie()).getType() == type.Carnivorous)&&(Game.getCell(dinoRow, dinoCol) instanceof Vegetation))	//controlla se il dino � carnivoro e se nella cella c'� vegetazione
												{
													((Carnivorous)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).setVegetation((Vegetation)Game.getCell(dinoRow, dinoCol));
												}
												Game.setCellMap(currentSession.getPlayer(token).getSpecie().getDino(dinoId), dinoRow, dinoCol);
												currentSession.getPlayer(token).getSpecie().getDino(dinoId).setLocalMap();
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
			if(isLoggedUser(token))						//controlla se � loggato
			{
				if(currentSession.getPlayer(token) != null)			//controllo token valido
				{
		//manca non � il tuo turno
					if(currentSession.getPlayer(token).getSpecie().getDino(dinoId)!=null)		//controllo dinoId
					{
						if(!currentSession.getPlayer(token).getSpecie().getDino(dinoId).getActionTake())		//controlla se l'azione � gia stata fatta
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
			if(isLoggedUser(token))						//controlla se � loggato
			{
				if(currentSession.getPlayer(token) != null)			//controllo token valido
				{
		//manca non � il tuo turno
					if(currentSession.getPlayer(token).getSpecie().getDino(dinoId)!=null)		//controllo dinoId
					{
						if(!currentSession.getPlayer(token).getSpecie().getDino(dinoId).getActionTake())		//controlla se l'azione � gia stata fatta
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
		// Dopo questo metodo chiamare la changeRound
		synchronized (loggedPlayers) 
		{
			if(isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) != null)
				{
					if(tokenOfCurrentPlayer.equals(token))
					{
						/* Fa in modo che il thread che contava i 30 secondi per dare la conferma non esegue nessuna 
						 * azione
						 */
						//counter30s.setIsJustUpdate(true);
						/*
						 * Starta il thread che conta i due minuti dopo i quali esegue il metodo updatePlayer e changeRound
						 */
						counter30s.interrupt();
						Counter counter = new Counter(this, timeForPlay);
						counter2m = new Thread(counter);
						counter2m.start();
						
						return ServerMessageBroker.createOkMessage();
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
	 * Esegue il passa turno da parte di un giocatore
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	/* La chiamata di questo metodo è seguita dalla chiamata al metodo chamngeRoundche crea il messaggio da mandare 
	 * in broadcast
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
					if(tokenOfCurrentPlayer.equals(token))
					{
						this.updatePlayer(token);
						counter2m.interrupt();
						return ServerMessageBroker.createOkMessage();
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
	 * Crea il messaggio di cambio del turno da mandare in broadcast a tutti i giocatori come notifica di cambio turno
	 * @return Messaggio da mandare ai client in broadcast
	 */
	public String changeRound()
	{
		Counter counter = new Counter(this, timeForConfirm);
		counter30s = new Thread(counter);
		counter30s.start();
		
		return ServerMessageBroker.createServerRoundSwitch(currentSession.getPlayer(tokenOfCurrentPlayer).getUserName());
	}
	
	/**
	 * Esegue il cambio del turno da un giocatore al prossimo e esegue le update su un giocatore(notifica in partita)
	 */
	public void updatePlayer(String token)
	{
		/* TODO forse
		 * - Fare l'update della mappa dei dinosauri coinvolti nei movimenti del giocatore in turno oppure se non 
		 *   possibile saperlo fare l'update di tutte le mappe di tutti i giocatori
		 */
		// Inizio aggiornamento stato giocatore
		currentSession.getPlayer(token).getSpecie().upDateDinosaurStatus();
		Iterator iter = currentSession.getPlayer(token).getSpecie().getDinosaurs();
		Map.Entry me;
		
		synchronized(iter)
		{
			// kill dei dinosauri con age = 0
			while(iter.hasNext())
			{
				me = (Map.Entry) iter.next();
				
				if(((Dinosaur)me.getValue()).getAge() == 0)
				{
					currentSession.getPlayer(token).getSpecie().killDino(((Dinosaur)me.getValue()));
				}
			}
			
			currentSession.getPlayer(token).getSpecie().updateMap();
			// End aggiornamento stato giocatore
			
			iter = currentSession.getPlayersList();
			int tableSize = 0;
			
			while(iter.hasNext())
			{
				me = (Map.Entry) iter.next();
				tableSize++;
				
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
					/* TODO : se arrivato qui significa che tutti i giocatori hanno giocato il server
					 * deve eseguire il metodo updateGame()
					 */
					this.updateGame();
					break;
				}
			}
		}
	}
	
	/**
	 * Esegue gli aggiornamenti sugli oggetti della mappa e sulle specie in partita. Da chiamare dopo che tutti hanno 
	 * giocato.
	 */
	public void updateGame()
	{
		/*
		 * - Diminuisco turni vissuti della specie: massimo 120 turni dopo i quali la specie muore(timeOfLive=0).
		 * 	 Nel caso di morte della specie, il riferimento della specie sul player deve essere 
		 * 	 settato a null. Il Client deve fare sapere all'utente la morte della sua specie.
		 * - Aumento energia vegetazione.
		 * - Diminuzione energia carogne.
		 * - Aggiornamento score dei tutte le specie in partita
		 */
		Iterator iter = currentSession.getPlayersList();
		
		synchronized (iter)
		{
			while(iter.hasNext())
			{
				Player currentPlayer = (Player)iter.next();
				Species currentSpecie = currentPlayer.getSpecie();			
				currentSpecie.updateTimeOfLive();
				currentSpecie.increaseScore();
				
				if(currentSpecie.getTimeOfLive() == 0)
				{
					currentPlayer.setSpecie(null);
					currentSession.removePlayer(currentPlayer.getToken());
				}
			}
			
			for(int i = 0; i<Game.maxRow; i++)
			{
				for(int j = 0; j<Game.maxCol; j++)
				{
					Object current = Game.getCell(i, j);
					
					if((current instanceof Vegetation) || (current instanceof Carrion))
						((Food)current).rebirth();
				}
			}
		}
	}
	
	
	public String getTokenOfCurrentPlayer() {
		return tokenOfCurrentPlayer;
	}

	public void setTokenOfCurrentPlayer(String tokenOfCurrentPlayer) {
		this.tokenOfCurrentPlayer = tokenOfCurrentPlayer;
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
