package Server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import Server.Species.type;

public class ServerLogic 
{

	// oggetti per sincronizzare i metodi sugli arraylist

	private Server server = null;
	
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
	private Hashtable<String, Player> players;
	/**
	 * HashMap che contiene delle istanze dei Player loggati. Chiave il token
	 */
	private Hashtable<String, Player> loggedPlayers;
	/**
	 * HashMap che contiene le specie estinte
	 */
	private Hashtable<String, Species> rank;
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
	
	
	
	public ServerLogic() throws RemoteException {
		players = new Hashtable<String, Player>();
		loggedPlayers = new Hashtable<String, Player>();
		rank = new Hashtable<String, Species>();
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
	public String add_new_user(String username, String password) 
	{
		try
		{
			if (!players.containsKey(username)) 
			{
				Player newPlayer = new Player(username, password);
				players.put(username, newPlayer);
	
				return ServerMessageBroker.createOkMessage();
			} 
			else
				return ServerMessageBroker.createErroMessage("usernameOccupato");
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createErroMessage("usernameOccupato");
		}
	}

	/**
	 * Aggiunge il Player alla HashMap dei Player loggati
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String login(String username, String password)
	{
		try
		{
			if(players.containsKey(username))
			{
				if(!(loggedPlayers.containsValue(players.get(username))))
				{
					if((((Player)players.get(username)).getUserName().equals(username)) && (((Player)players.get(username)).getPassword().equals(password)))
					{
						String token = this.generateToken(username, System.nanoTime());
						players.get(username).setToken(token);
						
						if(!this.isLoggedUser(token))
						{
							loggedPlayers.put(token, players.get(username));
							return ServerMessageBroker.createOkMessageWithOneParameter(token);
						}
					}
				}
			}
			
			return ServerMessageBroker.createErroMessage("autenticazioneFallita");
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createErroMessage("autenticazioneFallita");
		}
	}

	/**
	 * Aggiunge la nuova specie al Player se non esiste giï¿½
	 * @param msg : messaggio del Client
	 * @return Messaggio da mandare al Client
	 */
	public String addNewSpecies(String token, String name, String type) 
	{
		try
		{
			if (this.isLoggedUser(token)) 
			{
				//oolean isRacePresent = false;
				
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
					return "@no";
				}
				/*	
				Set set = rank.entrySet();
				Iterator<Species> iter = set.iterator();
				isRacePresent = false;
				
				while (iter.hasNext()) 
				{
					Map.Entry me = (Map.Entry) iter.next();
					
					if(me.getValue() != null)
					{
						if (((Species)me.getValue()).getName().equals(name))
						{
							isRacePresent = true;
							break;
						}
					}
				}*/
				if (rank.get(name) == null) 
				{
					Species new_specie;
					if (type.equals("c")) 
					{
						new_specie = new Species(name, Species.getCarnType(), loggedPlayers.get(token).getUserName());
					} 
					else 
					{
						new_specie = new Species(name, Species.getVegType(), loggedPlayers.get(token).getUserName());
					}
	
					rank.put(name, new_specie);
					loggedPlayers.get(token).setSpecie(new_specie);
					return ServerMessageBroker.createOkMessage();
				} 
				else
					return ServerMessageBroker.createErroMessage("nomeRazzaOccupato");
			} 
			else
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
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
	public String gameAccess(String token) 
	{
		try
		{
			// Chiamata di questo metodo seguita dalla chiamata a changeRound
			if(this.isLoggedUser(token))
			{
				if (currentSession.numberPlayersInGame() < currentSession.getMaxPlayers())
				{
					if(loggedPlayers.get(token).getSpecie() == null)
					{
						return ServerMessageBroker.createTokenNonValidoErrorMessage();
					}
					
					if (currentSession.getPlayer(token) == null)
					{
						if(currentSession.numberPlayersInGame() == 0)
						{
							tokenOfCurrentPlayer = token;
							this.changeRound();
							//this.changeRoundNotify();
						}
						
						currentSession.addPlayer(token, loggedPlayers.get(token));
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
								for(int i = current.getPosRow() - current.getSizeRowLocalMap(); i<current.getPosRow() + current.getSizeRowLocalMap(); i++)
								{
									for(int j = current.getPosCol() - current.getSizeColLocalMap(); j<current.getPosCol() + current.getSizeColLocalMap(); j++)
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
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}

	/**
	 * Esegue l'uscita del giocatore dalla partita
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String gameExit(String token)
	{	
		try
		{
			if(this.isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) == null)
				{
					return ServerMessageBroker.createOkMessage();
				}
				else
				{
					if(currentSession.isPlayerInGame(token))
					{						
						if(loggedPlayers.get(token).getSpecie().getDinosaurs() != null)
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
						}
						
						if(tokenOfCurrentPlayer.equals(token))
						{
							if(counter2m != null)
								counter2m.interrupt();
							if(counter30s != null)
								counter30s.interrupt();
						}
						
						if(token.equals(tokenOfCurrentPlayer))
						{
							updatePlayer(token);
						}
						
						currentSession.removePlayer(token);
						return ServerMessageBroker.createOkMessage();
					}
				}
				
				if(currentSession.numberPlayersInGame() == 0)
					isTheFirstAccess = true;
			}
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Esegue la creazione della lista di giocatori in partita.
	 * @param msg : messaggio del Client
	 * @return Messaggio da mandare al Client
	 */
	public String playerList(String token)
	{
		try
		{
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add("listaGiocatori");
			
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
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Genera la classifica di tutti gli utenti registrati
	 * @param msg : messaggio del Client
	 * @return Messaggio da mandare al Client
	 */
	public String ranking(String token)
	{
		try
		{
			ArrayList<String> parameters = new ArrayList<String>();
			// TODO: gestione specie estinte e cambio punteggio nel player
			
			Hashtable<String, String> alreadySpeciesRead = new Hashtable<String, String>();
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
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Esegue il logout di un giocatore loggato. Se il giocatore ï¿½ in partita, prima di eseguire il 
	 * logout deve uscire dalla partita
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String logout(String token)
	{	
		try
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
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Crea la mappa generale da mandare a giocatori in partita
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String generalMap(String token)
	{
		try
		{
			ArrayList<String> map = new ArrayList<String>();
			
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
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Crea la lista dei dinosauri di un giocatore
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String dinosaursList(String token)
	{
		try
		{
			ArrayList<String> list = new ArrayList<String>();
			list.add("listaDinosauri");
			
			if(isLoggedUser(token))
			{
				if(loggedPlayers.get(token).getSpecie() == null)
				{
					return ServerMessageBroker.createStandardMessage(list);
				}
				if(currentSession.getPlayer(token) != null)
				{
					if(currentSession.getPlayer(token).getSpecie() != null)
					{
						if(currentSession.getPlayer(token).getSpecie().getDinosaurs() != null)
						{
							Iterator<Dinosaur> dinos = currentSession.getPlayer(token).getSpecie().getDinosaurs();
							
							while(dinos.hasNext())
							{
								Map.Entry me = (Map.Entry) dinos.next();
								list.add(((Dinosaur)me.getValue()).getDinoId());
							}
						}
					}
					
					return ServerMessageBroker.createStandardMessage(list);
				}
				else
					return ServerMessageBroker.createErroMessage("nonInPartita");
			}
			else
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Crea la vista di un dinosauro dal suo id
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String dinoZoom(String token, String dinoId)
	{		
		try
		{
			ArrayList<String> zoom = new ArrayList<String>();
			
			if(isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) != null)
				{
					Dinosaur dino = loggedPlayers.get(token).getSpecie().getDino(dinoId);
					if( dino != null)
					{
						int row = dino.getPosRow()+dino.getSizeRowLocalMap()/2;
						if(row>Game.maxRow)
							row=Game.maxRow;	
						zoom.add(String.valueOf(row));
						int col = dino.getPosCol()-dino.getSizeColLocalMap()/2;
						if(col<0)
							col=0;
						zoom.add(String.valueOf(col));
						zoom.add(String.valueOf(dino.getSizeRowLocalMap()));
						zoom.add(String.valueOf(dino.getSizeColLocalMap()));
						
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
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Crea lo stato di un dinosauro
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String dinoState(String token, String dinoId)
	{
		Dinosaur dino;
		try
		{
			ArrayList<String> state = new ArrayList<String>();			
			boolean isPossibleDino = false;
			Dinosaur possibleDino = null;
			
			if(isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) != null)
				{

					dino = loggedPlayers.get(token).getSpecie().getDino(dinoId);
					if( dino != null)
					{
						state.add(currentSession.getPlayer(token).getUserName());
						state.add(currentSession.getPlayer(token).getSpecie().getName());
						state.add(currentSession.getPlayer(token).getSpecie().getType().toString());
						state.add(String.valueOf(dino.getPosRow()));
						state.add(String.valueOf(dino.getPosCol()));
						state.add(String.valueOf(dino.getDinoDimension()));
						state.add(String.valueOf(dino.getEnergy()));
						state.add(String.valueOf(dino.getTimeOfLive()));
						
						return ServerMessageBroker.createDinoState(state);
					}
					else

					if(currentSession.getPlayer(token).getSpecie().getDinosaurs() != null)

					{
						dino = loggedPlayers.get(token).getSpecie().getDino(dinoId);
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
					return ServerMessageBroker.createDinoState(state);
				}
				else
					return ServerMessageBroker.createErroMessage("nonInPartita");
			}
			else
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	public String dinoMove(String token, String dinoId, String rowDest, String colDest)
	{
		try
		{
			int	dinoRow = Integer.parseInt(rowDest);
			int	dinoCol = Integer.parseInt(colDest);
			
			if(isLoggedUser(token))						//controlla se � loggato
			{
				if(currentSession.getPlayer(token) != null)			//controllo se � in partita
				{
					if(tokenOfCurrentPlayer.equals(token))
					{
						if(currentSession.getPlayer(token).getSpecie().getDino(dinoId)!=null)		//controllo dinoId
						{
							Dinosaur dino = currentSession.getPlayer(token).getSpecie().getDino(dinoId);
							if(!currentSession.getPlayer(token).getSpecie().getDino(dinoId).getMoveTake())		//controllo che il dinosauro possa fare ancora mosse di movimento
							{
								if((dinoRow>=0)&&(dinoRow<Game.maxRow)&&(dinoCol>=0)&&(dinoCol<Game.maxCol))	//controllo che la destinazione sia nella mappa
								{
									if(Game.checkReachCell(dino.getPosRow(), dino.getPosCol(), dinoRow, dinoCol, dino.getDistMax()))	//controllo che il dinosauro possa arrivare a destinazione
									{
										if(!((dino instanceof Vegetarian)&&(Game.getCell(dinoRow, dinoCol) instanceof Vegetarian)))		//se dino � vegetariano controllo che nn ci sia un vegetariano a destinazione
										{
											if(dino.move(dinoRow, dinoCol))		//controlla che abbia abbastanza energia x muoversi e cambia le coordinate in dino
											{
												if((Game.getCell(dinoRow, dinoCol) instanceof Vegetarian)||(Game.getCell(dinoRow, dinoCol) instanceof Carnivorous))		//controlla se c'� un altro dinosauro nella cella di arrivo
												{
													if(dino.fight(Game.getCell(dinoRow, dinoCol)))		//combatte
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
														
															
														if(dino instanceof Vegetarian)		//controlla se il dino e' vegetariano
														{
															if(Game.getCell(dinoRow, dinoCol) instanceof Vegetation)
															{
																dino.eat(Game.getCell(dinoRow, dinoCol));								//quindi mangia
															}
															else if(Game.getCell(dinoRow, dinoCol) instanceof Carrion)
															{
																((Vegetarian)dino).setCarrion((Carrion)Game.getCell(dinoRow, dinoCol));
															}
														}
														if(dino instanceof Carnivorous)	//controlla se il dino e' carnivoro
														{
															if(Game.getCell(dinoRow, dinoCol) instanceof Carrion)
															{
																dino.eat(Game.getCell(dinoRow, dinoCol));						//quindi mangia
															}
															else if(Game.getCell(dinoRow, dinoCol) instanceof Vegetation)
															{
																((Carnivorous)dino).setVegetation((Vegetation)Game.getCell(dinoRow, dinoCol));
															}
														}
														
														Game.setCellMap(dino, dinoRow, dinoCol);
														dino.setLocalMap();
														return ServerMessageBroker.createOkMessageWithTwoParameter("combattimento", "v");
													}
													else
													{
														currentSession.getPlayer(token).getSpecie().killDino(dino);
														return ServerMessageBroker.createOkMessageWithTwoParameter("combattimento", "p");
													}
												}
												else
												{
													if(dino instanceof Vegetarian)						//controlla se il dino
													{
														if(Game.getCell(dinoRow, dinoCol) instanceof Vegetation)
														{
															((Vegetarian)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).eat(Game.getCell(dinoRow, dinoCol));								//quindi mangia													
															((Vegetarian)currentSession.getPlayer(token).getSpecie().getDino(dinoId)).setVegetation((Vegetation)Game.getCell(dinoRow, dinoCol));
														}
														else if(Game.getCell(dinoRow, dinoCol) instanceof Carrion)
														{
															((Vegetarian)dino).setCarrion((Carrion)Game.getCell(dinoRow, dinoCol));
														}
												
													}
													if(dino instanceof Carnivorous)	//controlla se il dino � carnivoro
													{
														if(Game.getCell(dinoRow, dinoCol) instanceof Carrion)
														{
															((Carnivorous)dino).eat(Game.getCell(dinoRow, dinoCol));						//quindi mangia
															currentSession.repositionCarrion(dinoRow, dinoCol);																					//riposiziona carogna
														}
														else if(Game.getCell(dinoRow, dinoCol) instanceof Vegetation)
														{
															((Carnivorous)dino).setVegetation((Vegetation)Game.getCell(dinoRow, dinoCol));
														}
													}
													Game.setCellMap(dino, dinoRow, dinoCol);
													dino.setLocalMap();
													return ServerMessageBroker.createOkMessage();
												}
											}
											else
											{
												currentSession.getPlayer(token).getSpecie().killDino(dino);
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
						return ServerMessageBroker.createErroMessage("nonIlTuoTurno");
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
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	public String dinoGrowUp(String token, String dinoId)
	{	
		try
		{
			if(isLoggedUser(token))						//controlla se � loggato
			{
				if(currentSession.getPlayer(token) != null)			//controllo token valido
				{
					if(tokenOfCurrentPlayer.equals(token))
					{
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
						return ServerMessageBroker.createErroMessage("nonIlTuoTurno");
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
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	public String dinoNewEgg(String token, String dinoId)
	{	
		try
		{
			if(isLoggedUser(token))						//controlla se � loggato
			{
				if(currentSession.getPlayer(token) != null)			//controllo token valido
				{
					if(tokenOfCurrentPlayer.equals(token))
					{
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
						return ServerMessageBroker.createErroMessage("nonIlTuoTurno");
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
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	
	/**
	 * Esegue la conferma del turno
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	public String roundConfirm(String token)
	{
		try
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
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Esegue il passa turno da parte di un giocatore
	 * @param msg : messaggio ricevuto dal Client
	 * @return Messaggio da mandare al Client
	 */
	/* La chiamata di questo metodo è seguita dalla chiamata al metodo changeRound che crea il messaggio da mandare 
	 * in broadcast
	 */
	public String playerRoundSwitch(String token)
	{		
		try
		{
			if(isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) != null)
				{
					if(tokenOfCurrentPlayer.equals(token))
					{
						this.updatePlayer(token);
						if(counter2m != null)
						{
							counter2m.interrupt();
						}
						if(counter30s != null)
						{
							counter30s.interrupt();
						}
						this.changeRound();
						//this.changeRoundNotify();
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
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}
	
	/**
	 * Crea il thread dei 30 secondi di conferma
	 */
	public void changeRound()
	{
		try
		{
			Counter counter = new Counter(this, timeForConfirm);
			counter30s = new Thread(counter);
			counter30s.start();
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	public void changeRoundNotify()
	{
		try
		{
			server.sendBroadcastMessage(ServerMessageBroker.createServerRoundSwitch(currentSession.getPlayer(tokenOfCurrentPlayer).getUserName()));
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	public String getuUsernameOfCurrentPlayer()
	{
		return currentSession.getPlayer(tokenOfCurrentPlayer).getUserName();
	}
	
	/**
	 * Esegue il cambio del turno da un giocatore al prossimo e esegue le update su un giocatore(notifica in partita)
	 */
	public void updatePlayer(String token)
	{
		Iterator iter = null;
		Map.Entry me = null;
		
		try
		{
			/* TODO forse
			 * - Fare l'update della mappa dei dinosauri coinvolti nei movimenti del giocatore in turno oppure se non 
			 *   possibile saperlo fare l'update di tutte le mappe di tutti i giocatori
			 */
			// Inizio aggiornamento stato giocatore
			if(loggedPlayers.get(token).getSpecie().getDinosaurs() != null)
			{
				loggedPlayers.get(token).getSpecie().upDateDinosaurStatus();
				iter = loggedPlayers.get(token).getSpecie().getDinosaurs();
				
				// kill dei dinosauri con age = 0
				while(iter.hasNext())
				{
					me = (Map.Entry) iter.next();
					
					if(((Dinosaur)me.getValue()).getAge() == 0)
					{
						loggedPlayers.get(token).getSpecie().killDino(((Dinosaur)me.getValue()));
					}
				}
			}
			
			
			if(loggedPlayers.get(token).getSpecie() != null)
			{
				if(loggedPlayers.get(token).getSpecie().getDinosaurs() == null)
				{
					loggedPlayers.get(token).setSpecie(null);
				}
				else
				{
					loggedPlayers.get(token).getSpecie().updateMap();
				}
			}
			// End aggiornamento stato giocatore
			
			// Aggiornamento stato di altri giocatori
			
			if(currentSession.numberPlayersInGame() > 0)
			{
				iter = currentSession.getPlayersList();
				
				while(iter.hasNext())
				{
					me = (Map.Entry)iter.next();
					
					if(((Player)me.getValue()).getSpecie() != null)
					{
						if(((Player)me.getValue()).getSpecie().getDinosaurs() == null)
						{
							((Player)me.getValue()).setSpecie(null);
							currentSession.removePlayer(((Player)me.getValue()).getToken());
						}
					}
				}
			}
			
			// End
			
			if(currentSession.numberPlayersInGame() > 0)
			{
				iter = currentSession.getPlayersList();
				int tableSize = 0;
				
				// setta il token del prossimo giocatore a tokenOfCurrentPlayer
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
						this.updateGame(token);
						break;
					}
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Esegue gli aggiornamenti sugli oggetti della mappa e sulle specie in partita. Da chiamare dopo che tutti hanno 
	 * giocato.
	 */
	public void updateGame(String token)
	{
		try
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
			
			while(iter.hasNext())
			{
				Map.Entry me = (Map.Entry)iter.next();
				Player currentPlayer = (Player)me.getValue();
				Species currentSpecie = currentPlayer.getSpecie();			
				currentSpecie.updateTimeOfLive();
				currentSpecie.increaseScore();
				
				if(currentSpecie.getDinosaurs() == null)
				{
					currentSession.removePlayer(token);
				}
				
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
					
					if(current instanceof Vegetarian)
					{
						if(((Vegetarian)current).getVegetation() != null)
						{
							if(((Vegetarian)current).getVegetation().getMaxPower() > ((Vegetarian)current).getVegetation().getPower() + ((Vegetarian)current).getVegetation().getMaxPower() / 10)
							{
								((Vegetarian)current).getVegetation().rebirth();
							}
						}
						else
						{
							if(((Vegetarian)current).getCarrion() != null)
							{
								if(((Vegetarian)current).getCarrion().getMaxPower()- ((Vegetarian)current).getCarrion().getMaxPower() / 10 > 0)
								{
									((Vegetarian)current).getCarrion().rebirth();
								}
								else
								{
									int posRig;
									int posCol;
									
									do
									{
										posRig = (int) (Math.random() * 40);
										posCol = (int) (Math.random() * 40);
									}while(!((Game.getCell(posRig, posCol) instanceof String)&&(((String)Game.getCell(posRig, posCol)).compareTo("t")==0)));
									
									((Vegetarian)current).getCarrion().setPower(((Vegetarian)current).getCarrion().getMaxPower());
									Game.setCellMap(((Vegetarian)current).getCarrion(), posRig, posCol);
									((Vegetarian)current).setCarrion(null);
								}
							}
						}
					}
					else if(current instanceof Carnivorous)
					{
						if(((Carnivorous)current).getVegetation() != null)
						{
							if(((Carnivorous)current).getVegetation().getMaxPower() > ((Carnivorous)current).getVegetation().getPower() + ((Carnivorous)current).getVegetation().getMaxPower() / 10)
							{
								((Carnivorous)current).getVegetation().rebirth();
							}
						}
					}
					
					if(current instanceof Vegetation)
					{
						if(((Vegetation)current).getMaxPower() > ((Vegetation)current).getPower() + ((Vegetation)current).getMaxPower() / 10)
						{
							((Food)current).rebirth();
						}
					}
					else if(current instanceof Carrion)
					{
						if(((Carrion)current).getPower() - ((Carrion)current).getMaxPower() / 10 > 0)
						{
							((Food)current).rebirth();
						}
						else
						{
							// TODO: controllo se vegetazione interna ad un carnivoro o erbivoro e carogna su erbivoro
							int posRig;
							int posCol;
							
							do
							{
								posRig = (int) (Math.random() * 40);
								posCol = (int) (Math.random() * 40);
							}while(!((Game.getCell(posRig, posCol) instanceof String)&&(((String)Game.getCell(posRig, posCol)).compareTo("t")==0)));
							
							Game.setCellMap((Carrion)current, posRig, posCol);
							Game.setCellMap("t", i, j);
							((Carrion)current).setPower(((Carrion)current).getMaxPower());
						}
					}
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
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
		try
		{
			if(loggedPlayers.get(token) != null)
				return true;
			else
				return false;
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void setServer(Server s)
	{
		this.server = s;
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
}
