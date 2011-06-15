package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Classe che rappresenta la logica applicativa del gioco. 
 * Contiene la lista di tutti gli utenti registrati e la lista di tutte le specie create. 
 */

public class ServerLogic 
{
	/*
	 * Utilizzato per mandare il messaggio di cambio del turno ai client.
	 */
	private Server server = null;
	
	private Thread counter30s;
	private Thread counter2m;
	/*
	 * Tempo che un giocatore ha per confermare l'utilizzo di un turno di gioco (in millisecondi).
	 */
	private static int timeForConfirm = 30000; 
	/*
	 * Tempo che un giocatore ha per fare le mosse di un suo turno di gioco (in millisecondi).
	 */
	private static int timeForPlay = 120000;
	/*
	 * Istanza della sessione di gioco corrente. Contiene informazioni sulla sessione come i 
	 * giocatori e la mappa del gioco.
	 */
	private Game currentSession;
	/*
	 * Lista degli utenti registrati al gioco.
	 * La chiave dell'HashTable e' l'username del giocatore scelto al momento della registrazione.
	 */
	private Hashtable<String, Player> players;
	/*
	 * Lista degli utenti loggati.
	 * La chiave dell'HashTable e' il token generato al momento dell'invio dei dati di login.
	 */
	private Hashtable<String, Player> loggedPlayers;
	/*
	 * Lista di tutte le specie create nel gioco. Utilizzata per la classifica.
	 * La chiave dell'HashTable e' il nome della specie. 
	 */
	private Hashtable<String, Species> rank;
	/*
	 * Il token del giocatore che puo' effettuare le azioni di gioco.
	 */
	private String tokenOfCurrentPlayer;
	
	private String keyForToken;
	
	/**
	 * Istanzia un nuovo ServerLogic.
	 */
	public ServerLogic()
	{
		LogHelper.writeInfo("*****AVVIO DEL SERVER.*****");
		
		File f = new File("server.ser");
		boolean isCorrectLoadingFromFile = true;		
		
		if(f.exists())
		{
			try 
			{
				FileInputStream input = new FileInputStream("server.ser");
				ObjectInputStream ois = new ObjectInputStream(input);
			
				currentSession = new Game((Object[][])ois.readObject());
				players = new Hashtable<String, Player>();
				rank = new Hashtable<String, Species>();
				
				// Deserializzazione PlayerTable
				Object[][] playerMatrix = (Object[][])ois.readObject();
				
				for(int i = 0; i<playerMatrix.length; i++)
				{
					players.put((String)playerMatrix[i][0], (Player)playerMatrix[i][1]);
				}
				// End Deserializzazione playerTable
				
				// Deserializzazione RankTable
				Object[][] rankMatrix = (Object[][])ois.readObject();
				
				for(int i = 0; i<rankMatrix.length; i++)
				{
					rank.put((String)rankMatrix[i][0], (Species)rankMatrix[i][1]);
				}
				// End Deserializzazione RankTable
				
				ois.close(); 
				input.close();
				LogHelper.writeInfo("deserializzazione file 'server.ser' avvenuta con successo");
			} 
			catch (FileNotFoundException e) 
			{
				isCorrectLoadingFromFile = false;
				LogHelper.writeError("file 'server.ser' non trovato. Impossibile caricare lo stato del server.");
			}
			catch (ClassNotFoundException e) 
			{
				isCorrectLoadingFromFile = false;
				LogHelper.writeError("deserializzazione file 'server.ser' fallita.");
			}
			catch (IOException e) 
			{
				isCorrectLoadingFromFile = false;
				LogHelper.writeError("deserializzazione file 'server.ser' fallita.");
			}
				// END DESERIALIZZAZIONE
		}
		
		else if((!isCorrectLoadingFromFile) || (!f.exists()))
		{
			players = new Hashtable<String, Player>();
			rank = new Hashtable<String, Species>();
			currentSession = new Game(null);
		}
		
		loggedPlayers = new Hashtable<String, Player>();
		tokenOfCurrentPlayer = "";
		// Inizializzazione chiave per generazione del token
		keyForToken = this.generateKeyForToken();
	}

	//@ requires username != null && password != null (*Il controllo  dei caratteri speciali deve essere già eseguito*);
	//@ ensures if username != 
	//@							(\forall String i; i != null; players.conatinKey(i) == true)  ==> \result == "@ok";
	//@ 		else  \result == "@no,@usernameOccupato";
	//@ assignable players.put(username)
	//@ signals (Excetpion ex) \result == "@no";
	
	/**
	 * Aggiunge un nuovo utente alla lista degli utenti.
	 * @param username l'username dell'utente
	 * @param password la password dell'utente
	 * @return stringa di esito della richiesta 
	 */
	public String add_new_user(String username, String password) 
	{
		LogHelper.writeClientRequest("creaUtente --> " + username + ", " + password);
		
		try
		{
			if (!players.containsKey(username)) 
			{
				Player newPlayer = new Player(username, password);
				players.put(username, newPlayer);
	
				LogHelper.writeServerResponse("creaUtente " + username + " --> @ok");
				return ServerMessageBroker.createOkMessage();
			} 
			else
			{
				LogHelper.writeServerResponse("creaUtente " + username + " --> @no,@usernameOccupato");
				return ServerMessageBroker.createErroMessage("usernameOccupato");
			}
		}
		catch(Exception ex)
		{
			LogHelper.writeError(username + " creazione utente fallita.");
			return "@no";
		}
	}

	//@ requires username != null && password != null (*Il controllo dei caratteri speciali deve essere già eseguito*);
	//@ ensures if (players.countainsKey(username) == true && players.get(username).getPassword() == password ) 
	//@									==>  token == generateToken(username,System.nanoTime()) && \result == ServerMessageBroker.createOkMessageWithOneParameter(token);  
	//@ 		else  					\return == ServerMessageBroker.createErroMessage("autenticazioneFallita");
	//@ assignable LoggedPlayers.put(token, players.get(username));
	//@ signals (Exception ex) \return == "@no";
	/**
	 * Aggiunge un utente alla lista degli utenti loggati.
	 * @param username l'username dell'utente
	 * @param password la password dell'utente
	 * @return stringa di esito della richiesta
	 */
	public String login(String username, String password)
	{
		LogHelper.writeClientRequest("login --> " + username + ", " + password);
		
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
							
							LogHelper.writeServerResponse("login " + username + " --> @ok," + token);
							return ServerMessageBroker.createOkMessageWithOneParameter(token);
						}
					}
				}
			}
			
			LogHelper.writeServerResponse("login " + username + " --> @no,@autenticazioneFallita");
			return ServerMessageBroker.createErroMessage("autenticazioneFallita");
		}
		catch(Exception ex)
		{
			LogHelper.writeError(username + " login fallito.");
			return "@no";
		}
	}
	//@ requires token != null && name != null && (type == 'c' || type == 'e')
	//@	ensures if (!isLoggedUser(token)) ==> \result == ServerMessageBroker.createTokenNonValidoErrorMessage();
	//@			if (* la specie dell'utente non esiste e non c'è nessun'altra specie con quel nome *) ==> creo la specie && \return == ServerMessageBroker.createOkMessage();   
	//@			else ==> ServerMessageBroker.createErroMessage("nomeRazzaOccupato");
	//@			
	//@			else if (* la specie dell'utente esiste già *) ==> \return == "@no";
	//@	assignable rank.put(name, new_specie);
	//@ assignable loggedPlayers.get(token).setSpecie(new_specie);				
	//@ signals (Exception e) ServerMessageBroker.createTokenNonValidoErrorMessage();
	/**
	 * Aggiunge la nuova specie all'utente se non esiste gia' o se l'utente ha gi� una specie in gioco.
	 * @param token il token dell'utente
	 * @param name il nome della specie da creare
	 * @param type il tipo della specie ('c' o 'e')
	 * @return stringa di esito della richiesta
	 */
	public String addNewSpecies(String token, String name, String type) 
	{
		LogHelper.writeClientRequest("creaRazza --> " + token);
		
		try
		{
			if (this.isLoggedUser(token)) 
			{
				if(loggedPlayers.get(token).getSpecie() != null)
				{
					return "@no";
				}
				
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
					LogHelper.writeServerResponse("creaRazza " + token + " --> @ok");
					return ServerMessageBroker.createOkMessage();
				} 
				else
				{
					LogHelper.writeServerResponse("creaRazza " + token + " --> @no,@nomeRazzaOccupato");
					return ServerMessageBroker.createErroMessage("nomeRazzaOccupato");
				}
			} 
			else
			{
				LogHelper.writeServerResponse("creaRazza " + token + " --> @no,@tokenNonValido");
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
			}
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " creazione specie fallita.");
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
	}

	/**
	 * Aggiunge un utente alla partita corrente.
	 * Controlla che il player possa accedere alla partita e se puo lo fa accedere. Puo' accedere se 
	 * c'e' ancora posto nella partita (non se e' stato raggiunto il numero massimo di
	 * giocatori).
	 * @param username l'username dell'utente
	 * @param token il token dell'utente
	 * @return stringa di esito della richiesta
	 */
	public String gameAccess(String token) 
	{
		LogHelper.writeClientRequest("accessoPartita --> " + token);
		
		try
		{
			if(this.isLoggedUser(token))
			{
				if (currentSession.numberPlayersInGame() < currentSession.getMaxPlayers())
				{
					if(loggedPlayers.get(token).getSpecie() == null)
					{
						LogHelper.writeServerResponse("accessoPartita " + token + " --> @no,@tokenNonValido");
						return ServerMessageBroker.createTokenNonValidoErrorMessage();
					}
					
					if (currentSession.getPlayer(token) == null)
					{
						if(currentSession.numberPlayersInGame() == 0)
						{							
							tokenOfCurrentPlayer = token;
							this.changeRound();
						}
						
						currentSession.addPlayer(token, loggedPlayers.get(token));
						// Aggiungo i dinosauri del giocatore alla mappa
						Iterator<Map.Entry<String, Dinosaur>> dinosaurs = currentSession.getPlayer(token).getSpecie().getDinosaurs();
						boolean isPositionated = false;
						while(dinosaurs.hasNext())
						{
							Map.Entry<String, Dinosaur> me = (Map.Entry<String, Dinosaur>)dinosaurs.next();
							
							Dinosaur current = ((Dinosaur)me.getValue());
							if((Game.getCell(current.getPosRow(), current.getPosCol()) instanceof Dinosaur))
							{
								if(((Dinosaur)Game.getCell(current.getPosRow(), current.getPosCol())).getDinoId().equals(current.getDinoId()))
								{
									Game.setCellMap(current, current.getPosRow(), current.getPosCol());
									isPositionated = true;
									break;
								}
							}
							if((Game.getCell(current.getPosRow(), current.getPosCol()) instanceof Dinosaur) || (Game.getCell(current.getPosRow(), current.getPosCol()) instanceof Food) || ((Game.getCell(current.getPosRow(), current.getPosCol()) instanceof String) &&(Game.getCell(current.getPosRow(), current.getPosCol()).equals("a"))))
							{
								if(isPositionated == false)
								{
									/* Se la cella in cui c'era il dinosauro e occupata allora viene messo in una posizione
									 * libera partendo dall'inizio della sua vista in alto a sinistra.
									 */
									int offset = 1;
									do
									{
										for(int i = current.getPosRow() - offset; i<current.getPosRow() + offset; i++)
										{
											for(int j = current.getPosCol() - offset; j<current.getPosCol() + offset; j++)
											{
												if((i >= 0)&&(i<Game.maxRow)&&(j>=0)&&(j<Game.maxCol))
												{
													if((Game.getCell(i, j) instanceof String)&&(((String)Game.getCell(i, j)).compareTo("t")==0))
													{
														
														Game.setCellMap(current, i, j);
														current.posRow = i;
														current.posCol = j;
														isPositionated = true;
														break;
														
													}
												}
											}
											if(isPositionated)
											{
												break;
											}
										}
										offset++;
									}while(!isPositionated);
								}
							}	
							else
								Game.setCellMap(current, current.getPosRow(), current.getPosCol());
						}
						currentSession.getPlayer(token).getSpecie().updateMap();
						
						LogHelper.writeServerResponse("accessoPartita " + token + " --> @ok");
						return ServerMessageBroker.createOkMessage();
					} 
					else 
					{
						LogHelper.writeServerResponse("accessoPartita " + token + " --> @no,@tokenNonValido");
						return ServerMessageBroker.createTokenNonValidoErrorMessage();
					}
				}
	
				LogHelper.writeServerResponse("accessoPartita " + token + " --> @no,@troppiGiocatori");
				return ServerMessageBroker.createErroMessage("troppiGiocatori");
			}
			else
			{
				LogHelper.writeServerResponse("accessoPartita " + token + " --> @no,@tokenNonValido");
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
			}
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " accesso in partita fallito");
			return "@no";
		}
	}

	/**
	 * Esegue l'uscita dalla partita di un utente. 
	 * @param token il token dell'utente
	 * @return stringa di esito della richiesta
	 */
	public String gameExit(String token)
	{	
		LogHelper.writeClientRequest("uscitaPartita --> " + token);
		
		try
		{
			if(this.isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) == null)
				{
					LogHelper.writeServerResponse("uscitaPartita " + token + " --> @no,@tokenNonValido");
					return ServerMessageBroker.createTokenNonValidoErrorMessage();
				}
				else
				{
					if(currentSession.isPlayerInGame(token))
					{						
						if(loggedPlayers.get(token).getSpecie() != null)
						{
							if(loggedPlayers.get(token).getSpecie().getDinosaurs() != null)
							{
								// Tolgo i dinosauri del giocatore dalla mappa
								Iterator<Map.Entry<String, Dinosaur>> dinosaurs = loggedPlayers.get(token).getSpecie().getDinosaurs();
								while(dinosaurs.hasNext())
								{
									Map.Entry<String, Dinosaur> me = (Map.Entry<String, Dinosaur>) dinosaurs.next();
									
									if(me.getValue() instanceof Carnivorous)
									{
										if(((Carnivorous)me.getValue()).getVegetation() != null)
										{
											Game.setCellMap(((Carnivorous)me.getValue()).getVegetation(), ((Carnivorous)me.getValue()).getPosRow(), ((Carnivorous)me.getValue()).getPosCol());
											((Carnivorous)me.getValue()).setVegetation(null);
										}
										else
											Game.setCellMap("t", ((Carnivorous)me.getValue()).getPosRow(), ((Carnivorous)me.getValue()).getPosCol());
									}
									else
									{
										if(((Vegetarian)me.getValue()).getCarrion() != null)
										{
											Game.setCellMap(((Vegetarian)me.getValue()).getCarrion(), ((Vegetarian)me.getValue()).getPosRow(), ((Vegetarian)me.getValue()).getPosCol());
											((Vegetarian)me.getValue()).setCarrion(null);
										}
										else if(((Vegetarian)me.getValue()).getVegetation() != null)
										{
											Game.setCellMap(((Vegetarian)me.getValue()).getVegetation(), ((Vegetarian)me.getValue()).getPosRow(), ((Vegetarian)me.getValue()).getPosCol());
											((Vegetarian)me.getValue()).setVegetation(null);
										}
										else
											Game.setCellMap("t", ((Vegetarian)me.getValue()).getPosRow(), ((Vegetarian)me.getValue()).getPosCol());
									}
								}
							}
							else
							{
								currentSession.getPlayer(token).setSpecie(null);
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
						
						if(currentSession.numberPlayersInGame() == 0)
						{
							// SALVATAGGIO SERVER
							saveServerState();
							// END SALVATAGGIO SERVER
						}
						
						LogHelper.writeServerResponse("uscitaPartita " + token + " --> @ok");
						return ServerMessageBroker.createOkMessage();
					}
				}
			}
			LogHelper.writeServerResponse("uscitaPartita " + token + " --> @no,@tokenNonValido");
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " uscita partita fallita.");
			return "@no";
		}
	}
	
	/**
	 * Crea la lista degi giocatori attualmente in partita.
	 * @param token il token dell'utente che esegue la richiesta
	 * @return stringa di esito della richiesta
	 */
	public String playerList(String token)
	{
		LogHelper.writeClientRequest("listaGiocatori --> " + token);
		
		try
		{
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add("listaGiocatori");
			
			if(isLoggedUser(token))
			{
				if(loggedPlayers.get(token) != null)
				{
					Iterator<Map.Entry<String, Player>> iter = currentSession.getPlayersList();
					
					while(iter.hasNext())
					{
						Map.Entry<String, Player> me = (Map.Entry<String, Player>) iter.next();
						parameters.add(((Player)me.getValue()).getUserName());
					}
					
					String message = ServerMessageBroker.createStandardMessage(parameters);
					LogHelper.writeServerResponse("listaGiocatori " + token + " --> " + message);
					return message;
				}
			}
			LogHelper.writeServerResponse("listaGiocatori " + token + " --> @no,@tokenNonValido");
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " richiesta lista giocatori fallita.");
			return "@no";
		}
	}
	
	/**
	 * Genera la classifica di tutte le specie registrate.
	 * @param token il token dell'utente che eesegue la richiesta
	 * @return stringa di esito della richiesta
	 */
	public String ranking(String token)
	{
		LogHelper.writeClientRequest("classifica --> " + token);
		
		try
		{			
			Hashtable<String, String> alreadySpeciesRead = new Hashtable<String, String>();
			ArrayList<String> ranking = new ArrayList<String>();
			int maxScore;
			String speciesMaxScore = "";
			String usernameMaxScore = "";
			
			if(isLoggedUser(token))
			{
				for(int i = 0; i<rank.size(); i++)
				{
					Set<Map.Entry<String, Species>> set = rank.entrySet();
					Iterator<Map.Entry<String, Species>>  iter = set.iterator();
					maxScore = -10;
					
					while(iter.hasNext())
					{
						Map.Entry<String, Species> me = (Map.Entry<String, Species>) iter.next();
						
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
						if(players.get(usernameMaxScore).getSpecie() != null)
						{
							if(players.get(usernameMaxScore).getSpecie().getName().equals(speciesMaxScore))
								ranking.add("s");
							else
								ranking.add("n");
						}
						else
							ranking.add("n");
					}	
				}

				String message = ServerMessageBroker.createRankingList(ranking);
				LogHelper.writeServerResponse("classifica " + token + " --> " + message);
				return message;
			}
			
			LogHelper.writeServerResponse("classifica " + token + " --> @no,@tokenNonValido");
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " richiesta classifica fallita.");
			return "@no";
		}
	}
	
	/**
	 * Esegue il logout di un utente loggato. Se il giocatore e' in partita, prima di eseguire il 
	 * logout viene eseguita l'uscita dalla partita.
	 * @param token il token dell'utente che esegue la richiesta
	 * @return stringa di esito della richiesta
	 */
	public String logout(String token)
	{	
		LogHelper.writeClientRequest("logout --> " + token);
		
		try
		{
			if(isLoggedUser(token))
			{
				loggedPlayers.remove(token);
				
				if(currentSession.getPlayer(token) != null)
					currentSession.removePlayer(token);
				
				LogHelper.writeServerResponse("logout " + token + " --> @ok");
				return ServerMessageBroker.createOkMessage();
			}
			else
			{
				LogHelper.writeServerResponse("logout " + token + " --> @no,@tokenNonValido");
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
			}
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " logout fallito.");
			return "@no";
		}
	}
	
	/**
	 * Ritorna la mappa generale dell'utente che esegue la richiesta. L'utente deve essere in partita
	 * per ottenere la sua mappa generale.
	 * @param token il token dell'utente che esegue la richiesta
	 * @return stringa di esito della richiesta
	 */
	public String generalMap(String token)
	{
		LogHelper.writeClientRequest("mappaGenerale --> " + token);
		
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
					
					String message = ServerMessageBroker.createGeneraleMap(map);
					LogHelper.writeServerResponse("mappaGenerale " + token + " --> @ok");
					return message;
				}
				else
				{
					LogHelper.writeServerResponse("mappaGenerale " + token + " --> @no,@nonInPartita");
					return ServerMessageBroker.createErroMessage("nonInPartita");
				}
			}
			else
			{
				LogHelper.writeServerResponse("mappaGenerale " + token + " --> @no,@tokenNonValido");
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
			}
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " richiesta mappa generale fallita.");
			return "@no";
		}
	}
	
	/**
	 * Ritorna la lista dei dinosauri dell'utente che esegue la richiesta. L'utente deve essere in 
	 * partita per ottenere la lista dei suoi dinosauri.
	 * @param token il token dell'utente che esegue la richiesta
	 * @return stringa di esito della richiesta
	 */
	public String dinosaursList(String token)
	{
		LogHelper.writeClientRequest("listaDinosauri --> " + token);
		
		try
		{
			ArrayList<String> list = new ArrayList<String>();
			list.add("listaDinosauri");
			
			if(isLoggedUser(token))
			{
				if(loggedPlayers.get(token).getSpecie() == null)
				{
					String message = ServerMessageBroker.createStandardMessage(list);
					LogHelper.writeServerResponse("listaDinosauri " + token + " --> " + message);
					return message;
				}
				if(currentSession.getPlayer(token) != null)
				{
					if(currentSession.getPlayer(token).getSpecie() != null)
					{
						if(currentSession.getPlayer(token).getSpecie().getDinosaurs() != null)
						{
							Iterator<Map.Entry<String, Dinosaur>> dinos = currentSession.getPlayer(token).getSpecie().getDinosaurs();
							
							while(dinos.hasNext())
							{
								Map.Entry<String, Dinosaur> me = (Map.Entry<String, Dinosaur>) dinos.next();
								list.add(((Dinosaur)me.getValue()).getDinoId());
							}
						}
					}
					
					String message = ServerMessageBroker.createStandardMessage(list);
					LogHelper.writeServerResponse("listaDinosauri " + token + " --> " + message);
					return message;
				}
				else
				{
					LogHelper.writeServerResponse("listaDinosauri " + token + " --> @no,@nonInPartita");
					return ServerMessageBroker.createErroMessage("nonInPartita");
				}
			}
			else
			{
				LogHelper.writeServerResponse("listaDinosauri " + token + " --> @no,@tokenNonValido");
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
			}
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " richiesta lista dinosauri fallita.");
			return "@no";
		}
	}
	
	/**
	 * Ritorna la vista locale di un dinosauro. L'utente deve essere in partita per ottenere la vista
	 * locale di un dinosauro.
	 * @param token il token dell'utente che eesegue la richiesta
	 * @param dinoId l'id del dinosauro di cui e' richiesta la vista vlocale
	 * @return stringa di esito della richiesta
	 */
	public String dinoZoom(String token, String dinoId)
	{	
		LogHelper.writeClientRequest("vistaLocale --> " + token + ", " + dinoId);
		
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
						if((row-dino.getSizeRowLocalMap()+1)<0)
						{
							row -= row-dino.getSizeRowLocalMap()+1;
						}
						zoom.add(String.valueOf(row));
						int col = dino.getPosCol()-dino.getSizeColLocalMap()/2;
						if(col<0)
							col=0;
						if((col+dino.getSizeColLocalMap()-1)>=Game.maxCol)
						{
							col += Game.maxCol - (col+dino.getSizeColLocalMap());
						}
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
						
						String message = ServerMessageBroker.createDinoZoom(zoom);
						LogHelper.writeServerResponse("vistaLocale " + token + " --> " + message);
						return message;
					}
					else
					{
						LogHelper.writeServerResponse("vistaLocale " + token + " --> @no,@idNonValido");
						return ServerMessageBroker.createErroMessage("idNonValido");
					}
				}
				else
				{
					LogHelper.writeServerResponse("vistaLocale " + token + " --> @no,@nonInPartita");
					return ServerMessageBroker.createErroMessage("nonInPartita");
				}
			}
			else
			{
				LogHelper.writeServerResponse("vistaLocale " + token + " --> @no,@tokenNonValido");
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
			}
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " richiesta vista locale fallita.");
			return "@no";
		}
	}
	
	/**
	 * Ritorna lo stato di un dinosauro. L'utente che esegue la richiesta deve essere in partita.
	 * @param token il token dell'utente che esegue la richiesta
	 * @param dinoId l'id del dinosauro di cui e' richiesta lo stato
	 * @return stringa di esito della richiesta
	 */
	public String dinoState(String token, String dinoId)
	{
		LogHelper.writeClientRequest("statoDinosauro --> " + token + ", " + dinoId);
		
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
						if(currentSession.getPlayer(token).getSpecie().getType().toString().equals("Vegetarian"))
							state.add("e");
						else
							state.add("c");
						state.add(String.valueOf(dino.getPosRow()));
						state.add(String.valueOf(dino.getPosCol()));
						state.add(String.valueOf(dino.getDinoDimension()));
						state.add(String.valueOf(dino.getEnergy()));
						state.add(String.valueOf(dino.getTimeOfLive()));
						
						String message = ServerMessageBroker.createDinoState(state);
						LogHelper.writeServerResponse("statoDinosauro " + token + " --> " + message);
						return message;
					}
					else

					if(currentSession.getPlayer(token).getSpecie().getDinosaurs() != null)
					{
						dino = loggedPlayers.get(token).getSpecie().getDino(dinoId);
						if( dino != null)
						{
							state.add(currentSession.getPlayer(token).getUserName());
							state.add(currentSession.getPlayer(token).getSpecie().getName());
							if(currentSession.getPlayer(token).getSpecie().getType().toString().equals("Vegetarian"))
								state.add("e");
							else
								state.add("c");
							state.add(String.valueOf(dino.getPosRow()));
							state.add(String.valueOf(dino.getPosCol()));
							state.add(String.valueOf(dino.getDinoDimension()));
							state.add(String.valueOf(dino.getEnergy()));
							state.add(String.valueOf(dino.getAge()));
							
							String message = ServerMessageBroker.createDinoState(state);
							LogHelper.writeServerResponse("statoDinosauro " + token + " --> " + message);
							return message;
						}
						else
						{
							/* Controllo che il dinosauro richiesto appartiene alla vista di uno dei dinosauri
							 * del giocatore
							 */
							
							Iterator<Map.Entry<String, Dinosaur>> dinosaurs = loggedPlayers.get(token).getSpecie().getDinosaurs();
							// Itera i dinosauri del giocatore
							while (dinosaurs.hasNext()) 
							{
								if(isPossibleDino)
									break;
								
								Map.Entry<String, Dinosaur> me = (Map.Entry<String, Dinosaur>) dinosaurs.next();

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
								Iterator<Map.Entry<String, Player>> iter = currentSession.getPlayersList();
								
								while (iter.hasNext()) 
								{
									Map.Entry<String, Player> me = (Map.Entry<String, Player>) iter.next();
									dino = ((Player)me.getValue()).getSpecie().getDino(dinoId);
									
									if((dino != null) && (dino.equals(possibleDino)))
									{
										 state.add(((Player)me.getValue()).getUserName());
										 state.add(((Player)me.getValue()).getSpecie().getName());
										 state.add(((Player)me.getValue()).getSpecie().getType().toString());
										 state.add(String.valueOf(dino.getPosRow()));
										 state.add(String.valueOf(dino.getPosCol()));
										 state.add(String.valueOf(dino.getDinoDimension()));
										 
										 String message = ServerMessageBroker.createDinoState(state);
										 LogHelper.writeServerResponse("statoDinosauro " + token + " --> " + message);
										 return message;
									}
								}
								
								LogHelper.writeServerResponse("statoDinosauro " + token + " --> @no,@idNonValido");
								return ServerMessageBroker.createErroMessage("idNonValido");
							}
							else
							{
								LogHelper.writeServerResponse("statoDinosauro " + token + " --> @no,@idNonValido");
								return ServerMessageBroker.createErroMessage("idNonValido");
							}
						}
					}
					
					String message = ServerMessageBroker.createDinoState(state);
					LogHelper.writeServerResponse("statoDinosauro " + token + " --> " + message);
					return message;
				}
				else
				{
					LogHelper.writeServerResponse("statoDinosauro " + token + " --> @no,@nonInPartita");
					return ServerMessageBroker.createErroMessage("nonInPartita");
				}
			}
			else
			{
				LogHelper.writeServerResponse("statoDinosauro " + token + " --> @no,@tokenNonValido");
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
			}
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " richiesta stato dinosauro fallita.");
			return "@no";
		}
	}
	
	/**
	 * Esegue il movimento di un dinosauro. L'utente che esegue la richiesta deve essere in partita 
	 * per muovere un proprio dinosauro.
	 * @param token il token dell'utente che esegue la richiesta
	 * @param dinoId l'id del dinosauro che l'utente intende spostare
	 * @param rowDest la riga di destinazione del movimento
	 * @param colDest la colonna di destinazione del movimento
	 * @return stringa di esito della richiesta
	 */
	public String dinoMove(String token, String dinoId, String rowDest, String colDest)
	{
		LogHelper.writeClientRequest("muoviDinosauro --> " + token + ", " + dinoId + ", " + rowDest + ", " + colDest);
		
		try
		{
			int	dinoRow = Integer.parseInt(rowDest);
			int	dinoCol = Integer.parseInt(colDest);
			
			if(isLoggedUser(token))						
			{
				if(currentSession.getPlayer(token) != null)			
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
														((Dinosaur)Game.getCell(dinoRow, dinoCol)).getSpecie().killDino((Dinosaur)Game.getCell(dinoRow, dinoCol),false);
														
															
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
														LogHelper.writeServerResponse("muoviDinosauro " + token + " --> @ok,@combattimento,v");
														return ServerMessageBroker.createOkMessageWithTwoParameter("combattimento", "v");
													}
													else
													{
														currentSession.getPlayer(token).getSpecie().killDino(dino,true);
														LogHelper.writeServerResponse("muoviDinosauro " + token + " --> @ok,@combattimento,p");
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
															((Vegetation)Game.getCell(dinoRow, dinoCol)).setPower(10);
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
													LogHelper.writeServerResponse("muoviDinosauro " + token + " --> @ok");
													return ServerMessageBroker.createOkMessage();
												}
											}
											else
											{
												currentSession.getPlayer(token).getSpecie().killDino(dino,false);
												LogHelper.writeServerResponse("muoviDinosauro " + token + " --> @no,@mortePerInedia");
												return ServerMessageBroker.createErroMessage("mortePerInedia");
											}
										}
										else
										{
											LogHelper.writeServerResponse("muoviDinosauro " + token + " --> @no,@destinazioneNonValida");
											return ServerMessageBroker.createErroMessage("destinazioneNonValida");
										}
									}
									else
									{
										LogHelper.writeServerResponse("muoviDinosauro " + token + " --> @no,@destinazioneNonValida");
										return ServerMessageBroker.createErroMessage("destinazioneNonValida");
									}
								}
								else
								{
									LogHelper.writeServerResponse("muoviDinosauro " + token + " --> @no,@destinazioneNonValida");
									return ServerMessageBroker.createErroMessage("destinazioneNonValida");
								}
							}
							else
							{
								LogHelper.writeServerResponse("muoviDinosauro " + token + " --> @no,@raggiuntoLimiteMosseDinosauro");
								return ServerMessageBroker.createErroMessage("raggiuntoLimiteMosseDinosauro");
							}
						}
						else
						{
							LogHelper.writeServerResponse("muoviDinosauro " + token + " --> @no,@idNonValido");
							return ServerMessageBroker.createErroMessage("idNonValido");
						}
					}
					else
					{
						LogHelper.writeServerResponse("muoviDinosauro " + token + " --> @no,@nonIlTuoTurno");
						return ServerMessageBroker.createErroMessage("nonIlTuoTurno");
					}
				}
				else
				{
					LogHelper.writeServerResponse("muoviDinosauro " + token + " --> @no,@nonInPartita");
					return ServerMessageBroker.createErroMessage("nonInPartita");
				}
			}
			else
			{
				LogHelper.writeServerResponse("muoviDinosauro " + token + " --> @no,@tokenNonValido");
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
			}
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " movimento dinosauro fallito.");
			return "@no";
		}
	}
	
	/**
	 * Esegue la crescita di un dinosauro. L'utente che esegue la richiesta deve essere in partita per
	 * far crescere un proprio dinosauro.
	 * @param token il token dell'utente che esegue la richiesta
	 * @param dinoId l'id del dinosauro che l'utente intende far crescere
	 * @return stringa di esito della richiesta
	 */
	public String dinoGrowUp(String token, String dinoId)
	{	
		LogHelper.writeClientRequest("crescitaDinosauro --> " + token + ", " + dinoId);
		
		try
		{
			if(isLoggedUser(token))						//controlla se e' loggato
			{
				if(currentSession.getPlayer(token) != null)			//controllo se in partita
				{
					if(tokenOfCurrentPlayer.equals(token))
					{
						if(currentSession.getPlayer(token).getSpecie().getDino(dinoId)!=null)		//controllo dinoId
						{
							if(!currentSession.getPlayer(token).getSpecie().getDino(dinoId).getActionTake())		//controlla se l'azione � gia stata fatta
							{
								if(currentSession.getPlayer(token).getSpecie().getDino(dinoId).growUp())		//non ha abbastanza energia
								{
									LogHelper.writeServerResponse("crescitaDinosauro " + token + " --> @ok");
									return ServerMessageBroker.createOkMessage();
								}
								else
								{
									currentSession.getPlayer(token).getSpecie().killDino(currentSession.getPlayer(token).getSpecie().getDino(dinoId),false);
									LogHelper.writeServerResponse("crescitaDinosauro " + token + " --> @no,@mortePerInedia");
									return ServerMessageBroker.createErroMessage("mortePerInedia");
								}
							}
							else
							{
								LogHelper.writeServerResponse("crescitaDinosauro " + token + " --> @no,@raggiuntoLimiteMosseDinosauro");
								return ServerMessageBroker.createErroMessage("raggiuntoLimiteMosseDinosauro");
							}
						}
						else
						{
							LogHelper.writeServerResponse("crescitaDinosauro " + token + " --> @no,@idNonValido");
							return ServerMessageBroker.createErroMessage("idNonValido");
						}
					}
					else
					{
						LogHelper.writeServerResponse("crescitaDinosauro " + token + " --> @no,@nonIlTuoTurno");
						return ServerMessageBroker.createErroMessage("nonIlTuoTurno");
					}
				}
				else
				{
					LogHelper.writeServerResponse("crescitaDinosauro " + token + " --> @no,@nonInPartita");
					return ServerMessageBroker.createErroMessage("nonInPartita");
				}
			}
			else
			{
				LogHelper.writeServerResponse("crescitaDinosauro " + token + " --> @no,@tokenNonValido");
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
			}
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " crescita dinosauro fallita.");
			return "@no";
		}
	}
	
	/**
	 * Esegue la deposizione di un uovo di un dinosauro. L'utente che esegue la richiesta deve essere
	 * in partita per far deporre un uovo ad un proprio dinosauro.
	 * @param token il token dell'utente che esegue la richiesta
	 * @param dinoId l'id del dinosauro che l'utente intende far deporre un uovo
	 * @return stringa di esito della richiesta
	 */
	public String dinoNewEgg(String token, String dinoId)
	{	
		LogHelper.writeClientRequest("deposizioneUovo --> " + token + ", " + dinoId);
		
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
							if(currentSession.getPlayer(token).getSpecie().checkDinoNumber())
							{
								if(!currentSession.getPlayer(token).getSpecie().getDino(dinoId).getActionTake())		//controlla se l'azione � gia stata fatta
								{
									String idDino = currentSession.getPlayer(token).getSpecie().getDino(dinoId).newEgg();
									if(idDino !=null)		//non ha abbastanza energia
									{
										LogHelper.writeServerResponse("deponiUovo " + token + " --> @ok," + idDino);
										return ServerMessageBroker.createOkMessageWithOneParameter(idDino);//id dino nuovo
									}
									else
									{
										currentSession.getPlayer(token).getSpecie().killDino(currentSession.getPlayer(token).getSpecie().getDino(dinoId),false);
										LogHelper.writeServerResponse("deponiUovo " + token + " --> @no,@mortePerInedia");
										return ServerMessageBroker.createErroMessage("mortePerInedia");
									}
								}
								else
								{
									LogHelper.writeServerResponse("deponiUovo " + token + " --> @no,@raggiuntoLimiteMosseDinosauro");
									return ServerMessageBroker.createErroMessage("raggiuntoLimiteMosseDinosauro");
								}
							}
							else
							{
								LogHelper.writeServerResponse("deponiUovo " + token + " --> @no,@raggiuntoNumeroMaxDInosauri");
								return ServerMessageBroker.createErroMessage("raggiuntoNumeroMaxDinosauri");
							}
						}
						else
						{
							LogHelper.writeServerResponse("deponiUovo " + token + " --> @no,@idNonValido");
							return ServerMessageBroker.createErroMessage("idNonValido");
						}
					}
					else
					{
						LogHelper.writeServerResponse("deponiUovo " + token + " --> @no,@nonIlTuoTurno");
						return ServerMessageBroker.createErroMessage("nonIlTuoTurno");
					}
				}
				else
				{
					LogHelper.writeServerResponse("deponiUovo " + token + " --> @no,@nonInPartita");
					return ServerMessageBroker.createErroMessage("nonInPartita");
				}
			}
			else
			{
				LogHelper.writeServerResponse("deponiUovo " + token + " --> @no,@tokenNonValido");
				return ServerMessageBroker.createTokenNonValidoErrorMessage();
			}
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " deposizione uovo fallita.");
			return "@no";
		}
	}
	
	
	/**
	 * Esegue la conferma di un turno di gioco da parte di un utente. 
	 * @param token il token dell'utente che esegue la richiesta
	 * @return stringa di esito della richiesta
	 */
	public String roundConfirm(String token)
	{
		LogHelper.writeClientRequest("creaUtente --> " + token);
		
		try
		{
			if(isLoggedUser(token))
			{
				if(currentSession.getPlayer(token) != null)
				{
					if(tokenOfCurrentPlayer.equals(token))
					{
						/*
						 * Starta il thread che conta i due minuti dopo i quali esegue il metodo updatePlayer e changeRound
						 */
						counter30s.interrupt();
						Counter counter = new Counter(this, timeForPlay);
						counter2m = new Thread(counter);
						counter2m.start();
						
						LogHelper.writeServerResponse("conferaTurno " + token + " --> @ok");
						return ServerMessageBroker.createOkMessage();
					}
					else
					{
						LogHelper.writeServerResponse("confermaTurno " + token + " --> @no,@nonIlTuoTurno");
						return ServerMessageBroker.createErroMessage("nonIlTuoTurno");
					}
				}
				else
				{
					LogHelper.writeServerResponse("confermaTurno " + token + " --> @no,@nonInPartita");
					return ServerMessageBroker.createErroMessage("nonInPartita");
				}
			}
			LogHelper.writeServerResponse("confermaTurno " + token + " --> @no,@tokenNonValido");
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " conferma turno fallita.");
			return "@no";
		}
	}
	
	/**
	 * Esegue il cambio del turno di gioco.
	 * @param token il token dell'utente che esegue la richiesta
	 * @return stringa di esito della richiesta
	 */
	public String playerRoundSwitch(String token)
	{		
		LogHelper.writeClientRequest("passaTurno --> " + token);
		
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
						LogHelper.writeServerResponse("passaTurno " + token + " --> @ok");
						return ServerMessageBroker.createOkMessage();
					}
					else
					{
						LogHelper.writeServerResponse("passaTurno " + token + " --> @no,@nonIlTuoTurno");
						return ServerMessageBroker.createErroMessage("nonIlTuoTurno");
					}
				}
				else
				{
					LogHelper.writeServerResponse("passaTurno " + token + " --> @no,@nonInPartita");
					return ServerMessageBroker.createErroMessage("nonInPartita");
				}
			}
			LogHelper.writeServerResponse("passaTurno " + token + " --> @no,@tokenNonValido");
			return ServerMessageBroker.createTokenNonValidoErrorMessage();
		}
		catch(Exception ex)
		{
			LogHelper.writeError(token + " passa turno fallita.");
			return "@no";
		}
	}
	
	/**
	 * Crea il thread dei 30 secondi di tempo che un giocatore ha per confermare l'utilizzo del
	 * proprio turno di gioco.
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
			LogHelper.writeError("interruzione thread fallita.");
		}
	}
	
	/**
	 * Invia a tutti i giocatori il messaggio di cambio del turno.
	 */
	public void changeRoundNotify()
	{
		try
		{
			server.sendBroadcastMessage(ServerMessageBroker.createServerRoundSwitch(currentSession.getPlayer(tokenOfCurrentPlayer).getUserName()));
			LogHelper.writeInfo("messaggio cambio turno inviato.");
		}
		catch(Exception ex)
		{
			LogHelper.writeError("spedizione messaggio notifica in partita fallito.");
		}
	}
	
	/**
	 * Ritorna l'username del giocatore che e' abilitato ad eseguire le proprie mosse.
	 * @return username del giocatore
	 */
	public String getUsernameOfCurrentPlayer()
	{
		try
		{
			return currentSession.getPlayer(tokenOfCurrentPlayer).getUserName();
		}
		catch(Exception ex)
		{
			
		}
		return null;
	}
	
	/**
	 * Esegue l'aggiornamento dello stato di un giocatore ed esegue il cambio del turno.
	 */
	public void updatePlayer(String token)
	{		
		try
		{
			if(currentSession.getPlayer(token).getSpecie() != null)
			{
				// Inizio aggiornamento stato giocatore
				if(currentSession.getPlayer(token).getSpecie().getDinosaurs() != null)
				{
					currentSession.getPlayer(token).getSpecie().upDateDinosaurStatus();
					Iterator<Map.Entry<String, Dinosaur>> iter1 = currentSession.getPlayer(token).getSpecie().getDinosaurs();
					
					// kill dei dinosauri con age = 0
					while(iter1.hasNext())
					{
						Map.Entry<String, Dinosaur> me1 = (Map.Entry<String, Dinosaur>) iter1.next();
						
						if(((Dinosaur)me1.getValue()).getAge() == 0)
						{
							currentSession.getPlayer(token).getSpecie().killDino(((Dinosaur)me1.getValue()),false);
						}
					}
					
					if(loggedPlayers.get(token).getSpecie().getDinosaurs() == null)
					{
						currentSession.getPlayer(token).setSpecie(null);
					}
				}
			}
			
			
			if(currentSession.getPlayer(token).getSpecie() != null)
			{
				currentSession.getPlayer(token).getSpecie().updateMap();
			}
			// End aggiornamento stato giocatore
			
			// Aggiornamento stato di altri giocatori
			
			if(currentSession.numberPlayersInGame() > 0)
			{
				Iterator<Map.Entry<String, Player>> iter2 = currentSession.getPlayersList();
				
				while(iter2.hasNext())
				{
					Map.Entry<String, Player> me2 = (Map.Entry<String, Player>)iter2.next();
					
					if(((Player)me2.getValue()).getSpecie() != null)
					{
						if(((Player)me2.getValue()).getSpecie().getDinosaurs() == null)
						{
							((Player)me2.getValue()).setSpecie(null);
							currentSession.removePlayer(((Player)me2.getValue()).getToken());
						}
					}
				}
			}
			
			// End
			
			if(currentSession.numberPlayersInGame() > 0)
			{
				Iterator<Map.Entry<String, Player>> iter3 = currentSession.getPlayersList();
				int tableSize = 0;
				
				// setta il token del prossimo giocatore a tokenOfCurrentPlayer
				while(iter3.hasNext())
				{
					Map.Entry<String, Player> me3 = (Map.Entry<String, Player>) iter3.next();
					tableSize++;
					
					if((((String) me3.getKey()).equals(tokenOfCurrentPlayer)) && (tableSize < currentSession.numberPlayersInGame()))
					{
						me3 = (Map.Entry<String, Player>) iter3.next();
						tokenOfCurrentPlayer = (String)me3.getKey();
						break;
					}
					else if((((String) me3.getKey()).equals(tokenOfCurrentPlayer)) && (tableSize == currentSession.numberPlayersInGame()))
					{
						tokenOfCurrentPlayer = currentSession.getFirstPlayer();
						/* se arrivato qui significa che tutti i giocatori hanno giocato il server
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
			LogHelper.writeError("aggiornamento stato giocatore fallito.");
		}
	}
	
	/**
	 * Esegue gli aggiornamenti sugli oggetti della mappa e sulle specie in partita. Viene eseguita
	 * dopo che tutti i giocatori hanno giocato.
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
			Iterator<Map.Entry<String, Player>> iter = currentSession.getPlayersList();
			
			while(iter.hasNext())
			{
				Map.Entry<String, Player> me = (Map.Entry<String, Player>)iter.next();
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
					Iterator<Map.Entry<String, Dinosaur>> iter1 = currentSpecie.getDinosaurs();
					
					// kill dei dinosauri
					while(iter1.hasNext())
					{
						Map.Entry<String, Dinosaur> me1 = (Map.Entry<String, Dinosaur>) iter1.next();
						currentSpecie.killDino(((Dinosaur)me1.getValue()),false);
					}
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
			LogHelper.writeError("aggiornamento partita fallito.");
		}
	}
	
	/**
	 * Ritorna il token del giocatore abilitato a fare le proprie mosse.
	 * @return il token del giocatore
	 */
	public String getTokenOfCurrentPlayer() 
	{
		return tokenOfCurrentPlayer;
	}

	/**
	 * Setta il token del giocatore abilitato a fare le proprie mosse.
	 * @param tokenOfCurrentPlayer il token del giocatore abilitato a afre le proprie mosse
	 */
	public void setTokenOfCurrentPlayer(String tokenOfCurrentPlayer) 
	{
		this.tokenOfCurrentPlayer = tokenOfCurrentPlayer;
	}
	
	/*
	 * Controlla che un utente sia loggato.
	 * @param token il token dell'utente
	 * @return true se il giocatore e' loggato, false altrimenti
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
			return false;
		}
	}
	
	/**
	 * Viene settato il server.
	 * @param s il server
	 */
	public void setServer(Server s)
	{
		this.server = s;
	}
	
	/*
	 * Genera la chiave per generare i token degli utenti.
	 * @return chiave generata
	 */
	private String generateKeyForToken() 
	{
		// Lunghezza della chiave da 3 a 5
		int keyLength = (int) (Math.random() * 3 + 3);
		String key = new String("");

		// Contiene i numeri casuali da 0 a 7 presenti nella chiave univocamente
		HashMap<String, String> registeredPositions = new HashMap<String, String>();
		int i = 0;

		while (i < keyLength) 
		{
			// Generazione casuale del numero da inserire nella chiave
			int singleCasual = (int) (Math.random() * keyLength);

			// Se non e' presente nella chiave, viene inserito
			if (!registeredPositions.containsKey(String.valueOf(singleCasual))) 
			{
				registeredPositions.put(String.valueOf(singleCasual),
						String.valueOf(singleCasual));
				key += String.valueOf(singleCasual);
				i++;
			}
		}

		return key;
	}

	/**
	 * Ritorna la chiave per generare i token degli utenti.
	 * @return chiave
	 */
	public String getKeyForToken() 
	{
		return keyForToken;
	}
	
	private int findMin(String key) 
	{
		int min = key.length() + 1;

		for (int i = 0; i < key.length(); i++) {
			if (Integer.parseInt(String.valueOf(key.charAt(i))) < min) {
				min = Integer.parseInt(String.valueOf(key.charAt(i)));
			}
		}

		return min;
	}

	/*
	 * Genera il token di un utente tramite l'applicazione sulla concatenazione di username
	 * e i nanosecondi del server). Sulla concatenazione si applica un algoritmo di trasposizione.
	 * @param username username del giocatore
	 * @return token generato
	 */
	private String generateToken(String username, long el) 
	{
		String key = this.keyForToken;
		int length = key.length();
		String concatenateIdentifier = new String(username + el);
		String token = new String("");

		for (int j = 0; j < length; j++) 
		{
			int min = findMin(key);
			int positionMin = key.indexOf(String.valueOf(min));

			key = key.replaceFirst(String.valueOf(min), String.valueOf(key.length()));

			for (int i = positionMin; i < concatenateIdentifier.length(); i += length) 
			{
				token += concatenateIdentifier.charAt(i);
			}
		}

		if(token.contains("@"))
			token = token.replaceAll("@", "#");
			
		return token;
	}
	
	/*
	 * Salva lo stato del Server. Viene salvata la mappa, la lista dei giocatori registrati e la lista
	 * di tutte le specie. Il file di salvataggio si chiama 'server.ser'.
	 */
	public void saveServerState()
	{
		if ( currentSession.numberPlayersInGame() == 0)
		{
			try 
			{
				File f = new File("server.ser");
				if(f.exists())
				{
					f.delete();	
				}
				
				FileOutputStream out = new FileOutputStream("server.ser");
				ObjectOutputStream oos = new ObjectOutputStream(out);
				Object[][] map = currentSession.getGeneralMap();
				
				oos.writeObject(map);
				
				// Salvataggio PlayerTable
				Object[][] playerMatrix = new Object[players.size()][2];
				
				Set<Map.Entry<String, Player>> setPlayers = players.entrySet();
				Iterator<Map.Entry<String, Player>> iterPlayers = setPlayers.iterator();
				
				int i = 0;
				while(iterPlayers.hasNext())
				{
					Map.Entry<String, Player> mePlayers = (Map.Entry<String, Player>) iterPlayers.next();
					playerMatrix[i][0] = mePlayers.getKey();
					playerMatrix[i][1] = mePlayers.getValue();
					
					i++;
				}
				oos.writeObject(playerMatrix);
				// End salvataggio playerTable
				
				// Salvataggio rank
				Object[][] rankList = new Object[rank.size()][2];
				
				Set<Map.Entry<String, Species>> setRank = rank.entrySet();
				Iterator<Map.Entry<String, Species>> iterRank = setRank.iterator();
				
				i = 0;
				while(iterRank.hasNext())
				{
					Map.Entry<String, Species> meRank = (Map.Entry<String, Species>) iterRank.next();
					rankList[i][0] = meRank.getKey();
					rankList[i][1] = meRank.getValue();
					
					i++;
				}
				oos.writeObject(rankList);
				// End salvataggio rank
				
				oos.close();
				out.close();
				LogHelper.writeInfo("stato del server salvato con successo.");
			}
			catch (FileNotFoundException e) 
			{
				LogHelper.writeError("salvataggio stato del server non eseguito.");
			}
			catch (IOException e) 
			{
				LogHelper.writeError("salvataggio stato del server non eseguito.");
			}
		}
	}
	
	/**
	 * Ritorna il token di un giocatore tramite il suo username.
	 * @param username l'username dell'utente che esegue la richiesta
	 * @return token dell'utente che esegue la richiesta
	 */
	public String getTokenFromUsername(String username)
	{
		if(players.get(username) != null)
		{
			return players.get(username).getToken();
		}
		return null;
	}

	public Game getCurrentSession() 
	{
		return currentSession;
	}

	public void setCurrentSession(Game currentSession) 
	{
		this.currentSession = currentSession;
	}

	public Server getServer() 
	{
		return server;
	}

	public Hashtable<String, Player> getPlayers() 
	{
		return players;
	}

	public Hashtable<String, Player> getLoggedPlayers() 
	{
		return loggedPlayers;
	}
	
	public void setLoggedPlayers(Hashtable<String, Player> loggedPlayers)
	{
		this.loggedPlayers = loggedPlayers;
	}
	
	public Hashtable<String, Species> getRank() 
	{
		return rank;
	}

	public void setKeyForToken(String keyForToken) 
	{
		this.keyForToken = keyForToken;
	}
	
	public void setRank(Hashtable<String, Species> rank)
	{
		this.rank = rank;
	}

}
