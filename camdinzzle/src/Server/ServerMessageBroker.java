/**
 * @author Forme
 * created 			06/05/2011
 * last modified	09/05/2011
 */ 

package Server;

import java.sql.Array;
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
			return msg.substring(1);
	}
	
	/**
	 * Gestisce la divisione del messaggio in parametri
	 * @param msg
	 * @return array contenente il messaggio diviso per parametri tranne il nome del comando
	 */
	public static String[] manageReceiveMessageSplit(String msg)
	{
		String validMessage = msg.substring(msg.indexOf(',')+1);
		String[] returnValues = validMessage.split(",");
		
		return deleteEqualSymbol(returnValues);
	}
	
	/**
	 * Gestisce il messaggio di movimento di un dinosauro
	 * @param msg
	 * @return array contenente il messaggio diviso per parametri. Le coordinate sono in ultima e penultima 
	 * posizione
	 */
	public static String[] manageDinoMovement(String msg)
	{
		String validMessage = msg.substring(msg.indexOf(',')+1);
		String[] returnValues = validMessage.split(",");
		
		returnValues[0] = returnValues[0].substring(returnValues[0].indexOf('=')+1);
		returnValues[1] = returnValues[1].substring(returnValues[1].indexOf('=')+1);
		returnValues[2] = returnValues[2].substring(returnValues[2].indexOf('{')+1);
		returnValues[3] = returnValues[3].substring(0, returnValues[3].indexOf('}'));
		
		return returnValues;
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
		
		if(parameters.size() > 0)
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
		return "@ok," + parameter1 + parameter2;
	}
	
	/**
	 * Crea il messaggio contenente la classifica generale della partita
	 * @param ranking contiene i parametri da concatenare nel messaggio
	 * @return messaggio da mandare al Client
	 */
	public static String createRankingList(ArrayList<String> ranking)
	{
		String returnMessage = new String("");
		returnMessage += "@classifica,";
		
		for(int i = 0; i<ranking.size(); i+=4)
		{
			returnMessage += "{";
			for(int j = 0; j<4; j++)
			{
				returnMessage += ranking.get(i+j) + ",";
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
		String dinoState = new String("");
		dinoState += "@statoDinosauro," + state.get(0) + "," + state.get(1) + "," + state.get(2) + ",";
		dinoState += "{" + state.get(3) + "," + state.get(4) + "}";
		
		for(int i = 5; i<state.size(); i++)
		{
			dinoState += "," + state.get(i);
		}
		
		return dinoState;
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
