/**
 * @author Forme
 * created 			05/05/2011
 * last modified	06/05/2011
 */

package Server;

import java.util.ArrayList;

/**
 * classe utilizzata per la gestione delle stringhe dei messaggi proveniente dal server. Utilizzato in tutte e 
 * due i tipi di visualizzazione. I metodi che iniziano con "convert" gestiscono le stringhe dei messaggi 
 * proveniente dal ServerLogic; i metodi che iniziano con "create" creano le stringhe dei messaggi da mandare al ServerLogic
 */
public class RMIMessageBroker 
{
	// Gestione messaggi in entrata
	
	/**
	 * Controlla se il messaggio � valido oppure no
	 * @param msg messaggio del server
	 * @return true se messaggio valido, false altrimenti
	 */
	public static boolean checkMessage(String msg)
	{
		String[] splittedMessage;
		
		if(msg.contains(","))
		{
			splittedMessage = msg.split(",");
			
			if(splittedMessage[0].equals("@no"))
				return false;
			return true;
		}
		else if(msg.equals("@ok"))
			return true;
		else
			return false;
	}
	
	/**
	 * Gestisce il comando di creazione utente
	 * @param msg : messaggio di risposta del Server
	 * @return Array contenente in prima posizione l'esito del comando e in seconda posizione l'errore se c'� stato
	 */
	public static String[] convertCreateUser(String msg)
	{
		String[] ret = new String[2];
		
		if(msg.equals("@ok"))
		{
			ret[0] = "ok";
			
			return ret;
		}
		else if(msg.equals("@no,@usernameOccupato"))
		{
			ret[0] = "no";
			ret[1] = msg.substring(msg.indexOf(",") + 2);
			
			return ret;
		}
		else if(msg.equals("@no"))
		{
			return new String[]{"no", "COMANDO NON ESEGUITO"};
		}
		return null;
	}
	
	/**
	 * Gestisce il comando di creazione della specie
	 * @param msg : messaggio di ripsota del Server
	 * @return Array contenente in prima posizione l'esito del messaggio e se � no contiene in seconda posizione l'errore
	 */
	public static String[] convertCreateSpecies(String msg)
	{
		String[] ret = new String[2];
		ret[0] = null;
		ret[1] = null;
		
		if(msg.equals("@ok"))
		{
			ret[0] = "ok";
			
			return ret;
		}
		else if((msg.equals("@no,@nomeRazzaOccupato")) || (msg.equals("@no")))
		{
			ret[0] = "no";
			if(msg.contains(","))
				ret[1] = msg.substring(msg.indexOf(",") + 2);
			return ret;
		}
		else if(msg.equals("@no,@tokenNonValido"))
		{
			ret[0] = "no";
			ret[1] = "COMANDO NON ESEGUITO";
		}
		return null;
	}
	
	/**
	 * Gestisce il comando di accesso partita
	 * @param msg
	 * @return
	 */
	public static String[] convertGameAccess(String msg)
	{
		String[] ret = new String[2];
		
		if(msg.equals("@ok"))
		{
			ret[0] = "ok";
			
			return ret;
		}
		else if((msg.equals("@no,@troppiGiocatori")) || (msg.equals("@no,@tokenNonValido")))
		{
			ret[0] = "no";
			ret[1] = msg.substring(msg.indexOf(",") + 2);
			
			return ret;
		}
		else if(msg.equals("@no"))
		{
			ret[0] = "no";
			ret[1] = "COMANDO NON ESEGUITO";
		}
		return null;
	}
	
	/**
	 * Gestisce il comando di uscita partita
	 * @param msg
	 * @return
	 */
	public static String[] convertGameExit(String msg)
	{
		String[] ret = new String[2];
		
		if(msg.equals("@ok"))
		{
			ret[0] = "ok";
			
			return ret;
		}
		else if(msg.equals("@no,@tokenNonValido"))
		{
			ret[0] = "no";
			ret[1] = msg.substring(msg.indexOf(",") + 2);
			
			return ret;
		}
		else if(msg.equals("@no"))
		{
			ret[0] = "no";
			ret[1] = "COMANDO NON ESEGUITO";
		}
		return null;
	}
	
	/**
	 * Gestisce il comando di logout
	 * @param msg
	 * @return
	 */
	public static String[] convertLogout(String msg)
	{
		if(msg.equals("@ok"))
		{
			return new String[]{"ok"};
		}
		else if(msg.equals("@no,@tokenNonValido"))
		{
			return new String[]{"no", "tokenNonValido"};
		}
		else if(msg.equals("@no"))
		{
			return new String[]{"no", "COMANDO NON ESEGUITO"};
		}
		return null;
	}
	
	/**
	 * Gestisce il nome del comando ricevuto
	 * @param msg
	 * @return nome del comando ricevuto
	 */
	public static String convertMessageType(String msg)
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
	 * Gestisce il messaggio della mappa generale
	 * @param msg Messaggio del server
	 * @return ArrayList contenente(in ordine di posizione): x di partenza, y di partenza, numero di righe della
	 * vista, numero di colonne della vista, elemento della prima colonna della prima riga, elemento della seconda
	 * colonna della prima riga e cos� via
	 */
	public static ArrayList<String> convertGeneralMap(String msg)
	{
		if(msg.contains("@mappaGenerale,"))
		{
			ArrayList<String> generalMap = new ArrayList<String>();
			String validMessage = msg.substring(msg.indexOf(',')+1);
			
			if(msg.contains(";"))
			{
				String[] dotAndCommaSeparator = validMessage.split(";");
				
				/**
				 * gestione prima parte del messaggio contenente la dimenzione della mappa generale
				 */
				String[] commaSeparator = dotAndCommaSeparator[0].split(",");
				generalMap.add(commaSeparator[0].substring(1, 2));
				generalMap.add(commaSeparator[1].substring(0, 1));
				/**
				 * mette la prima riga della vista al primo posto dell'array contenente le altre righe della vista cos� da
				 * poter iniziare il ciclo di gestione delle singole righe
				 */
				dotAndCommaSeparator[0] = commaSeparator[2];
				String[] bracketSquareSeparator;
				
				for (String row : dotAndCommaSeparator) 
				{
					bracketSquareSeparator = row.split("\\[");
					
					for (int i = 1; i<bracketSquareSeparator.length; i++) 
					{
						generalMap.add(bracketSquareSeparator[i].substring(0, bracketSquareSeparator[i].indexOf(']')));
					}
				}
				
				return generalMap;
			}
		}
		else if((msg.equals("@no,@tokenNonValido")) || (msg.equals("@no,@nonInPartita")))
		{
			ArrayList<String> ret = new ArrayList<String>();
			ret.add("no");
			ret.add(msg.substring(msg.indexOf(",")) + 2);
			
			return ret;
		}
		else if(msg.equals("@no"))
		{
			ArrayList<String> ret = new ArrayList<String>();
			ret.add("no");
			ret.add("COMANDO NON ESEGUITO");
			return ret;
		}
		return null;
	}
	
	/**
	 * Gestisce la divisione del messaggio in parametri. Se il messaggio non contiene parametri viene tornato null
	 * @param msg
	 * @return array contenente il messaggio diviso per parametri
	 */
	public static String[] splitMessage(String msg)
	{
		if(msg.contains(","))
		{
			String validMessage = msg.substring(msg.indexOf(',')+1);
			String[] splittedMessage = validMessage.split(",");
			
			return splittedMessage;
		}
		
		return null;
	}
	
	/**
	 * Gestisce il messaggio della lista dei dinosauri
	 * @param msg
	 * @return array contenente i dinoId dei dinosauri
	 */
	public static String[] convertDinoList(String msg)
	{
		if(msg.equals("@listaDinosauri"))
		{
			return new String[]{"null"};
		}
		else if(msg.contains("@listaDinosauri,"))
		{
			String validMessage = msg.substring(msg.indexOf(',')+1);
			
			if(validMessage.contains(","))
			{
				String[] dinoList = validMessage.split(",");
				
				return dinoList;
			}
			else
			{
				return new String[]{validMessage};
			}
		}
		else if((msg.equals("@no,@tokenNonValido")) || (msg.equals("@no,@nonInPartita")))
		{
			return new String[]{"no", msg.substring(msg.indexOf(",") + 2)};
		}
		else if(msg.equals("@no"))
		{
			return new String[]{"no", "COMANDO NON ESEGUITO"};
		}
		return null;
	}
	
	/**
	 * Gestisce il messaggio dello zoom su uno specifico dinosauro
	 * @param msg
	 * @return ArrayList contenente le posizioni di partenza (x e y in posizioni diverse) o 
	 * la dimensione della  vista o il contenuto di una cella della vista
	 */
	public static ArrayList<String> convertDinoZoom(String msg)
	{
		if(msg.contains("@vistaLocale,"))
		{
			ArrayList<String> dinoZoomList = new ArrayList<String>();
			String validMessage = msg.substring(msg.indexOf(',')+1);
			String[] dotAndCommaSeparator = validMessage.split(";");
			
			/**
			 * gestione prima parte del messaggio contenente la posizione di partenza(basso a sx), la dimenzione della vista e
			 * la prima riga della vista
			 */
			String[] commaSeparator = dotAndCommaSeparator[0].split(",");
			dinoZoomList.add(commaSeparator[0].substring(1));
			dinoZoomList.add(commaSeparator[1].substring(0, commaSeparator[1].length() - 1));
			dinoZoomList.add(commaSeparator[2].substring(1));
			dinoZoomList.add(commaSeparator[3].substring(0, commaSeparator[3].length() - 1));
			/**
			 * mette la prima riga della vista al primo posto dell'array contenente le altre righe della vista cos� da
			 * poter iniziare il ciclo di gestione delle singole righe
			 */
			dotAndCommaSeparator[0] = dotAndCommaSeparator[0].substring(dotAndCommaSeparator[0].lastIndexOf("\\}") + 2);
			String[] bracketSquareSeparator;
			
			for (String row : dotAndCommaSeparator) 
			{
				bracketSquareSeparator = row.trim().split("\\[");
				
				// Ciclo che parte da uno perch� il primo carattere dell'array � uno spazio 
				for (int i = 1; i<bracketSquareSeparator.length; i++) 
				{
					dinoZoomList.add(bracketSquareSeparator[i].substring(0, bracketSquareSeparator[i].indexOf(']')));
				}
			}
			
			return dinoZoomList;
		}
		else if((msg.equals("@ok,@tokenNonValido")) ||(msg.equals("@ok,@idNonValido")) || (msg.equals("@ok,@nonInPartita")))
		{
			ArrayList<String> ret = new ArrayList<String>();
			
			ret.add("no");
			ret.add(msg.substring(msg.indexOf(",") + 2));
			
			return ret;
		}
		else if(msg.equals("@no"))
		{
			ArrayList<String> ret = new ArrayList<String>();
			ret.add("no");
			ret.add("COMANDO NON ESEGUITO");
			return ret;
		}
		return null;
	}
	
	/**
	 * Gestisce il messaggio dello stato di un singolo dinosauro
	 * @param msg
	 * @return array contenente i parametri del messaggio
	 */
	public static String[] convertDinoState(String msg)
	{
		if(msg.equals("@no"))
		{
			return new String[]{"null"};
		}
		if(msg.contains("@statoDinosauro,"))
		{
			String validMessage = msg.substring(msg.indexOf(',')+1);
			String[] dinoState = validMessage.split(",");
			
			//Eliminazione delle parentesi nelle coordinate di inizio
			dinoState[3] = dinoState[3].substring(1);
			dinoState[4] = dinoState[4].substring(0, dinoState[4].length() - 1);
			
			return dinoState;
		}
		else if((msg.equals("@no,@tokenNonValido")) || (msg.equals("@no,@nonInPartita")) || (msg.equals("@no,@idNonValido")))
		{
			return new String[]{"no", msg.substring(msg.indexOf(",") + 2)};
		}
		else if(msg.equals("@no"))
		{
			return new String[]{"no", "COMANDO NON ESEGUITO"};
		}
		return null;
	}

	/**
	 * Gestisce il messaggio del movimento di un dinosauro
	 * @param msg
	 * @return stringa contenente l'esito del combattimento se c'� stato, altrimenti ritorna ok
	 */
	public static String[] convertDinoMove(String msg)
	{
		if(msg.equals("@ok"))
		{
			return new String[]{"ok"};
		}
		else if(msg.equals("@ok,@combattimento,v"))
		{
			return new String[]{"ok", "combattimento", "v"};
		}
		else if(msg.equals("@ok,@combattimento,p"))
		{
			return new String[]{"ok", "combattimento", "p"};
		}
		else if((msg.equals("@no,@tokenNonValido")) || (msg.equals("@no,@idNonValido"))
				|| (msg.equals("@no,@destinazioneNonValida")) || (msg.equals("@no,@raggiuntoLimiteMosseDinosauro"))
				|| (msg.equals("@no,@mortePerInedia")) || (msg.equals("@no,@nonIlTuoTurno"))
				|| (msg.equals("@no,@nonInPartita")))
		{
			return new String[]{"no", msg.substring(msg.indexOf(",") + 2)};
		}
		else if(msg.equals("@no"))
		{
			return new String[]{"no", "COMANDO NON ESEGUITO"};
		}
		return null;
	}
	
	public static String[] convertDinoGrowUp(String msg)
	{
		if(msg.equals("@ok"))
		{
			return new String[]{"ok"};
		}
		else if((msg.equals("@no,@tokenNonValido")) || (msg.equals("@no,@idNonValido"))
				|| (msg.equals("@no,@raggiuntoLimiteMosseDinosauro"))
				|| (msg.equals("@no,@mortePerInedia")) || (msg.equals("@no,@nonIlTuoTurno"))
				|| (msg.equals("@no,@nonInPartita")))
		{
			return new String[]{"no", msg.substring(msg.indexOf(",") + 2)};
		}
		else if(msg.equals("@no"))
		{
			return new String[]{"no", "COMANDO NON ESEGUITO"};
		}
		return null;
	}
	
	/**
	 * Gestisce il messaggio della deposizione di un uovo di un dinosauro
	 * @param msg
	 * @return array contenente i parametri del messaggio
	 */
	public static String[] convertNewEgg(String msg)
	{
		if(msg.equals("@ok"))
		{
			return new String[]{"ok"};
		}
		else if((msg.equals("@no,@tokenNonValido")) || (msg.equals("@no,@idNonValido"))
				|| (msg.equals("@no,@raggiuntoLimiteMosseDinosauro"))
				|| (msg.equals("@no,@raggiuntoNumeroMaxDinosauri"))
				|| (msg.equals("@no,@mortePerInedia")) || (msg.equals("@no,@nonIlTuoTurno"))
				|| (msg.equals("@no,@nonInPartita")))
		{
			return new String[]{"no", msg.substring(msg.indexOf(",") + 2)};
		}
		else if(msg.equals("@no"))
		{
			return new String[]{"no", "COMANDO NON ESEGUITO"};
		}
		return null;
	}
	
	/**
	 * Gestisce il messaggio del cambio del turno
	 * @param msg
	 * @return array contenente i parametri del messaggio
	 */
	public static String[] roundSwitch(String msg)
	{
		if(msg.equals("@ok"))
		{
			return new String[]{"ok"};
		}
		else if((msg.equals("@no,@tokenNonValido")) || (msg.equals("@no,@nonIlTuoTurno"))
				|| (msg.equals("@no,@nonInPartita")))
		{
			return new String[]{"no", msg.substring(msg.indexOf(",") + 2)};
		}
		else if(msg.equals("@no"))
		{
			return new String[]{"no", "COMANDO NON ESEGUITO"};
		}
		return null;
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public static String[] convertRoundConfirm(String msg)
	{
		if(msg.equals("@ok"))
		{
			return new String[]{"ok"};
		}
		else if((msg.equals("@no,@tokenNonValido")) || (msg.equals("@no,@nonIlTuoTurno"))
				|| (msg.equals("@no,@nonInPartita")))
		{
			return new String[]{"no", msg.substring(msg.indexOf(",") + 2)};
		}
		else if(msg.equals("@no"))
		{
			return new String[]{"no", "COMANDO NON ESEGUITO"};
		}
		return null;
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public static String[] convertPlayerChangeRound(String msg)
	{
		if(msg.equals("@ok"))
		{
			return new String[]{"ok"};
		}
		else if((msg.equals("@no,@tokenNonValido")) || (msg.equals("@no,@nonIlTuoTurno"))
				|| (msg.equals("@no,@nonInPartita")))
		{
			return new String[]{"no", msg.substring(msg.indexOf(",") + 2)};
		}
		else if(msg.equals("@no"))
		{
			return new String[]{"no", "COMANDO NON ESEGUITO"};
		}
		return null;
	}
	
	/**
	 * Gestisce il messaggio del login di un giocatore
	 * @param msg
	 * @return array contenente i parametri del messaggio
	 */
	public static String[] convertLogin(String msg)
	{
		String[] ret = new String[2];
		
		if(msg.contains("@ok,"))
		{
			ret[0] = "ok";
			ret[1] = msg.substring(msg.indexOf(",") + 1);
			
			return ret;
		}
		else if(msg.equals("@no,@autenticazioneFallita"))
		{
			ret[0] = "no";
			ret[1] = msg.substring(msg.indexOf(",") + 2);
			
			return ret;
		}
		else if(msg.equals("@no"))
		{
			return new String[]{"no", "COMANDO NON ESEGUITO"};
		}
		return null;
	}
	
	/**
	 * Gestisce il messaggio della lista dei giocatori di una partita
	 * @param msg
	 * @return array contenente i parametri del messaggio
	 */
	public static String[] convertPlayerList(String msg)
	{
		if(msg.contains("@listaGiocatori"))
		{			
			String validMessage = msg.substring(1);
			
			if(validMessage.contains(","))
			{
				return validMessage.split(",");
			}
			else
			{				
				return new String[]{"null"};
			}
		}
		else if(msg.equals("@no,@tokenNonValido"))
		{
			return new String[]{"no", "tokenNonValido"};
		}
		else if(msg.equals("@no"))
		{
			return new String[]{"no", "COMANDO NON ESEGUITO"};
		}
		return null;
	}
	
	/**
	 * Gestisce il messaggio della classifica dei giocatori di una partita
	 * @param msg
	 * @return ArrayList contenente in ordine i giocatori con il loro stato; 4 celle per ogni giocatore 
	 * contenti(in ordine): username, nome specie, punteggio, se � in partita
	 */
	public static ArrayList<String> convertRanking(String msg)
	{
		if(msg.equals("@classifica"))
		{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("null");
			
			return arr;
		}
		if(msg.contains("@classifica,"))
		{
			ArrayList<String> rankingList = new ArrayList<String>();
			rankingList.add(msg.substring(1, msg.indexOf(",")));
			
			String validMessage = msg.substring(msg.indexOf(',')+1);
			
			if(validMessage.contains("{"))
			{
				String[] bracketBraceSeparator = validMessage.split("\\{");
				
				for (int j = 1; j<bracketBraceSeparator.length;j++) 
				{
					String[] commaSeparator = bracketBraceSeparator[j].split(",");
					for(int i = 0; i<=2; i++)
					{
						rankingList.add(commaSeparator[i]);
					}
					rankingList.add(commaSeparator[3].substring(0, 1));
				}
			}
			return rankingList;
		}
		else if(msg.equals("@no,@tokenNonValido"))
		{
			ArrayList<String> ret = new ArrayList<String>();
			
			ret.add("no");
			ret.add("tokenNonValido");
			
			return ret;
		}
		else if(msg.equals("@no"))
		{
			ArrayList<String> ret = new ArrayList<String>();
			ret.add("no");
			ret.add("COMANDO NON ESEGUITO");
			return ret;
		}
		return null;
	}
	
	/**
	 * Gestisce il messaggio del cambio del turno(notifica in partita)
	 * @param msg
	 * @return Username del giocatore abilitato a fare le proprie mosse, altrimenti null
	 */
	public static String convertChangeRound(String msg)
	{
		if(msg.contains(","))
		{
			return msg.substring(msg.indexOf(",") + 1);
		}
		return null;
	}
	
}

