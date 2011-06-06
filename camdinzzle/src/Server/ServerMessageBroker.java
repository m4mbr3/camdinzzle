/**
 * @author Forme
 * created 			06/05/2011
 * last modified	09/05/2011
 */ 

package Server;

import java.util.ArrayList;

/**
 * classe utilizzata per la gestione delle stringhe dei messaggi proveniente dal client. I metodi che iniziano con 
 * "manage" gestiscono le stringhe dei messaggi proveniente dal Client; i metodi che iniziano con "create" 
 * creano le stringhe dei messaggi da mandare al Client
 */
public class ServerMessageBroker 
{
	// Gestione messaggi in entrata
	
	/**
	 * Divide il messaggio in parametri
	 * @param msg
	 * @return array contenente il messaggio diviso per parametri
	 */
	private static String[] splitMessage(String msg)
	{
		String[] splittedMessage = msg.split(",");
		
		return splittedMessage;
	}
	
	/**
	 * Gestisce il nome del comando ricevuto
	 * @param msg
	 * @return nome del comando ricevuto
	 */
	public static String manageMessageType(String msg)
	{
		String[] splittedMessage;
		
		if(msg.contains(","))
		{
			splittedMessage = msg.split(",");
			return splittedMessage[0].substring(1);
		}
		else
			return "comandoNonValido";
	}
	
	/**
	 * Gestisce la divisione del messaggio in parametri
	 * @param msg
	 * @return array contenente il messaggio diviso per parametri tranne il nome del comando
	 */
	private static String[] manageReceiveMessageSplit(String msg)
	{
		String validMessage = msg.substring(msg.indexOf(',')+1);
		String[] returnValues = validMessage.split(",");
		
		return deleteEqualSymbol(returnValues);
	}
	
	public static String[] manageCreateUser(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 3)
			{
				if(splitMessage[0].equals("@creaUtente"))
				{
					if((splitMessage[1].contains("=")) && (splitMessage[2].contains("=")))
					{
						if((splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("user"))
								&& (splitMessage[2].substring(0, splitMessage[2].indexOf("=")).equals("pass")))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] manageLogin(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 3)
			{
				if(splitMessage[0].equals("@login"))
				{
					if((splitMessage[1].contains("=")) && (splitMessage[2].contains("=")))
					{
						if((splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("user"))
								&& (splitMessage[2].substring(0, splitMessage[2].indexOf("=")).equals("pass")))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] manageCreateRace(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 4)
			{
				if(splitMessage[0].equals("@creaRazza"))
				{
					if((splitMessage[1].contains("=")) && (splitMessage[2].contains("=")) && (splitMessage[3].contains("=")))
					{
						if((splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
								&& (splitMessage[2].substring(0, splitMessage[2].indexOf("=")).equals("nome"))
								&& (splitMessage[3].substring(0, splitMessage[3].indexOf("=")).equals("tipo")))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] manageGameAccess(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 2)
			{
				if(splitMessage[0].equals("@accessoPartita"))
				{
					if(splitMessage[1].contains("="))
					{
						if(splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] manageGameExit(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 2)
			{
				if(splitMessage[0].equals("@uscitaPartita"))
				{
					if(splitMessage[1].contains("="))
					{
						if(splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] managePlayerList(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 2)
			{
				if(splitMessage[0].equals("@listaGiocatori"))
				{
					if(splitMessage[1].contains("="))
					{
						if(splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] manageRanking(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 2)
			{
				if(splitMessage[0].equals("@classifica"))
				{
					if(splitMessage[1].contains("="))
					{
						if(splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] manageLogout(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 2)
			{
				if(splitMessage[0].equals("@logout"))
				{
					if(splitMessage[1].contains("="))
					{
						if(splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] manageGeneralMap(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 2)
			{
				if(splitMessage[0].equals("@mappaGenerale"))
				{
					if(splitMessage[1].contains("="))
					{
						if(splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] manageDinoList(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 2)
			{
				if(splitMessage[0].equals("@listaDinosauri"))
				{
					if(splitMessage[1].contains("="))
					{
						if(splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] manageDinoZoom(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 3)
			{
				if(splitMessage[0].equals("@vistaLocale"))
				{
					if((splitMessage[1].contains("=")) && (splitMessage[2].contains("=")))
					{
						if((splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
								&& (splitMessage[2].substring(0, splitMessage[2].indexOf("=")).equals("idDino")))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] manageDinoState(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 3)
			{
				if(splitMessage[0].equals("@statoDinosauro"))
				{
					if((splitMessage[1].contains("=")) && (splitMessage[2].contains("=")))
					{
						if((splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
								&& (splitMessage[2].substring(0, splitMessage[2].indexOf("=")).equals("idDino")))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] manageDinoGrowUp(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 3)
			{
				if(splitMessage[0].equals("@cresciDinosauro"))
				{
					if((splitMessage[1].contains("=")) && (splitMessage[2].contains("=")))
					{
						if((splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
								&& (splitMessage[2].substring(0, splitMessage[2].indexOf("=")).equals("idDino")))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] manageNewEgg(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 3)
			{
				if(splitMessage[0].equals("@deponiUovo"))
				{
					if((splitMessage[1].contains("=")) && (splitMessage[2].contains("=")))
					{
						if((splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
								&& (splitMessage[2].substring(0, splitMessage[2].indexOf("=")).equals("idDino")))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] manageRoundConfirm(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 2)
			{
				if(splitMessage[0].equals("@confermaTurno"))
				{
					if(splitMessage[1].contains("="))
					{
						if(splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String[] managePlayerRoundSwitch(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 2)
			{
				if(splitMessage[0].equals("@passaTurno"))
				{
					if(splitMessage[1].contains("="))
					{
						if(splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
						{
							return manageReceiveMessageSplit(msg);
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Gestisce il messaggio di movimento di un dinosauro
	 * @param msg
	 * @return array contenente il messaggio diviso per parametri. Le coordinate sono in ultima e penultima 
	 * posizione
	 */
	public static String[] manageDinoMovement(String msg)
	{
		if(msg.contains(","))
		{
			String[] splitMessage = msg.split(",");
			
			if(splitMessage.length == 5)
			{
				if(splitMessage[0].equals("@muoviDinosauro"))
				{
					if((splitMessage[1].contains("=")) && (splitMessage[2].contains("=")) && (splitMessage[3].contains("=")))
					{
						if((splitMessage[1].substring(0, splitMessage[1].indexOf("=")).equals("token"))
								&& (splitMessage[2].substring(0, splitMessage[2].indexOf("=")).equals("idDino"))
								&& (splitMessage[3].substring(0, splitMessage[3].indexOf("=")).equals("dest")))
						{
							if((splitMessage[3].contains("{")) && (splitMessage[4].contains("}")))
							{
								if((splitMessage[3].substring(0, splitMessage[3].indexOf("{")).equals("dest="))
										&& (splitMessage[4].length() > 1))
								{
									String validMessage = msg.substring(msg.indexOf(',')+1);
									String[] returnValues = validMessage.split(",");
									
									returnValues[0] = returnValues[0].substring(returnValues[0].indexOf('=')+1);
									returnValues[1] = returnValues[1].substring(returnValues[1].indexOf('=')+1);
									returnValues[2] = returnValues[2].substring(returnValues[2].indexOf('{')+1);
									returnValues[3] = returnValues[3].substring(0, returnValues[3].indexOf('}'));
									
									return returnValues;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Elimina il simbolo "=" dai parametri di un comando
	 * @param str
	 * @return array contente i valori dei parametri
	 */
	private static String[] deleteEqualSymbol(String[] str)
	{
		for (int i = 0; i<str.length; i++) 
		{
			str[i] = str[i].substring(str[i].indexOf('=')+1);
		}
		
		return str;
	}
	
	// End gestione messaggi in entrata
	
	// Creazione messaggi in uscita
	
	/**
	 * Crea il messaggio contenente solo ok
	 * @return Il messaggio da inviare al Client
	 */
	public static String createOkMessage()
	{
		return "@ok";
	}
	/**
	 * Crea il messaggio di errore da mandare al Client
	 * @param errorType stringa contenente il messaggio di errore
	 * @return messaggio da mandare al Client
	 */
	public static String createErroMessage(String errorType)
	{
		return "@no,@" + errorType;
	}
	
	/**
	 * Crea il messaggio di errore tokenNonValido
	 * @return Il messaggio da mandare al Client
	 */
	public static String createTokenNonValidoErrorMessage()
	{
		return "@no,@tokenNonValido";
	}
	
	/**
	 * Crea il messaggio standard con: chiocNomeComando,parametro1,parametro2...
	 * @param parameters contiene i parametri da concatenare nel messaggio
	 * @return messaggio da mandare al Client
	 */
	public static String createStandardMessage(ArrayList<String> parameters)
	{
		String returnMessage = new String("");
		boolean isFor = false;
		
		if(parameters.size() == 1)
		{
			return "@" + parameters.get(0);
		}
		if(parameters.size() > 1)
		{
			returnMessage = returnMessage + "@" + parameters.get(0);
			for(int i = 1; i<parameters.size(); i++)
			{
				returnMessage = returnMessage + "," + parameters.get(i);
				isFor = true;
			}
			
			if(!isFor)
				returnMessage += ",";
		}
		return returnMessage;
	}
	
	/**
	 * Crea il messaggio di ok con un parametro in coda: @ok,nomeParametro
	 * @param parameter : Parametro da mandare al Client
	 * @return Messaggio da mandare al Client
	 */
	public static String createOkMessageWithOneParameter(String parameter)
	{
		return "@ok," + parameter;
	}
	
	/**
	 * Crea il messaggio di ok con due parametro in coda: @ok,nomeParametro
	 * @param parameter : Parametro da mandare al Client
	 * @return Messaggio da mandare al Client
	 */
	public static String createOkMessageWithTwoParameter(String parameter1, String parameter2)
	{
		return "@ok," + "@" + parameter1 + "," + parameter2;
	}
	
	
	/**
	 * Crea il messaggio contenente la classifica generale della partita
	 * @param ranking contiene i parametri da concatenare nel messaggio
	 * @return messaggio da mandare al Client
	 */
	public static String createRankingList(ArrayList<String> ranking)
	{
		String returnMessage = new String("");
		returnMessage += "@classifica";
		
		if(ranking.size() > 0)
		{
			returnMessage += ",";
		}
		
		for(int i = 0; i<ranking.size(); i+=4)
		{
			returnMessage += "{";
			for(int j = 0; j<4; j++)
			{
				if(j == 3)
				{
					returnMessage += ranking.get(i+j);
				}
				else
				{
					returnMessage += ranking.get(i+j) + ",";
				}
			}
			returnMessage += "}";
		}
		
		return returnMessage;
	}

	/**
	 * Crea il messaggio contenente la mappa generale
	 * @param map contiene i parametri da concatenare nel messaggio
	 * @return messaggio da mandare al Client
	 */
	public static String createGeneraleMap(ArrayList<String> map)
	{
		String returnMap = new String("");
		returnMap += "@mappaGenerale,{" + map.get(0) + "," + map.get(1) + "},"; 
		
		int columns = Integer.parseInt(map.get(1));
		int columnCount = 0;
		
		for(int i = 2; i<map.size(); i++)
		{
			if(columnCount == columns)
			{
				returnMap += ";";
				columnCount = 0;
			}
			
			returnMap += "[" + map.get(i) + "]";
			columnCount++;
		}
		
		return returnMap;
	}
	
	/**
	 * Crea il messaggio contenente la vista locale di un dinosauro
	 * @param zoom vista locale del dinosauro
	 * @return messaggio da mandare al Client
	 */
	public static String createDinoZoom(ArrayList<String> zoom)
	{
		String dinoZoom = new String("");
		dinoZoom += "@vistaLocale,{" + zoom.get(0) + "," + zoom.get(1) + "},{" + zoom.get(2) + "," + zoom.get(3) + "},";
		
		int columns = Integer.parseInt(zoom.get(3));
		int columnCount = 0;
		
		for(int i = 4; i<zoom.size(); i++)
		{
			if(columnCount == columns)
			{
				dinoZoom += ";";
				columnCount = 0;
			}
			
			dinoZoom += "[" + zoom.get(i) + "]";
			columnCount++;
		}
		
		return dinoZoom;
	}

	/**
	 * Crea il messaggio contenente lo stato di un dinosauro
	 * @param state
	 * @return messaggio da mandare al Client
	 */
	public static String createDinoState(ArrayList<String> state)
	{
		if(state.size() > 0)
		{
			String dinoState = new String("");
			dinoState += "@statoDinosauro," + state.get(0) + "," + state.get(1) + "," + state.get(2) + ",";
			dinoState += "{" + state.get(3) + "," + state.get(4) + "}";
			
			for(int i = 5; i<state.size(); i++)
			{
				dinoState += "," + state.get(i);
			}
			
			return dinoState;
		}
		else
		{
			return "@no";
		}
	}
	
	/**
	 * Crea il messaggio contenente l'esito positivo di un movimento
	 * @param battleResult : esito della battaglia(v | p)
	 * @return Messaggio da mandare al Client
	 */
	public static String createDinoMovementWithBattle(String battleResult)
	{
		return "@ok,@combattimento," + battleResult;
	}
	
	/**
	 * Crea il messaggio di cambio del turno da parte del ServerLogic(notifica in partita)
	 * @param username : username del giocatore che è abilitato a fare le proprie mosse
	 * @return Messaggio da mandare ai Client
	 */
	public static String createServerRoundSwitch(String username)
	{
		return "@cambioTurno," + username; 
	}
	
	
	// End creazione messaggi in uscita
}
