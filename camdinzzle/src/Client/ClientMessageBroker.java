/**
 * @author Forme
 * created 			05/05/2011
 * last modified	06/05/2011
 */

package Client;

import java.util.ArrayList;

/**
 * classe utilizzata per la gestione delle stringhe dei messaggi proveniente dal server. Utilizzato in tutte e 
 * due i tipi di visualizzazione. I metodi che iniziano con "manage" gestiscono le stringhe dei messaggi 
 * proveniente dal Server; i metodi che iniziano con "create" creano le stringhe dei messaggi da mandare al Server
 */
public class ClientMessageBroker 
{
	// Gestione messaggi in entrata
	
	/**
	 * Controlla se il messaggio è valido oppure no
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
		return true;
	}
	
	/**
	 * Gestisce il messaggio della mappa generale
	 * @param msg Messaggio del server
	 * @return ArrayList contenente(in ordine di posizione): x di partenza, y di partenza, numero di righe della
	 * vista, numero di colonne della vista, elemento della prima colonna della prima riga, elemento della seconda
	 * colonna della prima riga e così via
	 */
	public static ArrayList<String> manageGeneralMap(String msg)
	{
		ArrayList<String> generalMap = new ArrayList<String>();
		String validMessage = msg.substring(msg.indexOf(',')+1);
		String[] dotAndCommaSeparator = validMessage.split(";");
		
		/**
		 * gestione prima parte del messaggio contenente la dimenzione della mappa generale
		 */
		String[] commaSeparator = dotAndCommaSeparator[0].split(",");
		generalMap.add(commaSeparator[0].substring(1, 2));
		generalMap.add(commaSeparator[1].substring(0, 1));
		/**
		 * mette la prima riga della vista al primo posto dell'array contenente le altre righe della vista così da
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
	public static String[] manageDinoList(String msg)
	{
		String validMessage = msg.substring(msg.indexOf(',')+1);
		String[] dinoList = validMessage.split(",");
		
		return dinoList;
	}
	
	/**
	 * Gestisce il messaggio dello zoom su uno specifico dinosauro
	 * @param msg
	 * @return ArrayList contenente le posizioni di partenza (x e y in posizioni diverse) o 
	 * la dimensione della  vista o il contenuto di una cella della vista
	 */
	public static ArrayList<String> manageDinoZoom(String msg)
	{
		ArrayList<String> dinoZoomList = new ArrayList<String>();
		String validMessage = msg.substring(msg.indexOf(',')+1);
		String[] dotAndCommaSeparator = validMessage.split(";");
		
		/**
		 * gestione prima parte del messaggio contenente la posizione di partenza, la dimenzione della vista e
		 * la prima riga della vista
		 */
		String[] commaSeparator = dotAndCommaSeparator[0].split(",");
		dinoZoomList.add(commaSeparator[0].substring(1, 2));
		dinoZoomList.add(commaSeparator[1].substring(0, 1));
		dinoZoomList.add(commaSeparator[2].substring(1, 2));
		dinoZoomList.add(commaSeparator[3].substring(0, 1));
		/**
		 * mette la prima riga della vista al primo posto dell'array contenente le altre righe della vista così da
		 * poter iniziare il ciclo di gestione delle singole righe
		 */
		dotAndCommaSeparator[0] = commaSeparator[4];
		String[] bracketSquareSeparator;
		
		for (String row : dotAndCommaSeparator) 
		{
			bracketSquareSeparator = row.trim().split("\\[");
			
			// Ciclo che parte da uno perchè il primo carattere dell'array è uno spazio 
			for (int i = 1; i<bracketSquareSeparator.length; i++) 
			{
				dinoZoomList.add(bracketSquareSeparator[i].substring(0, bracketSquareSeparator[i].indexOf(']')));
			}
		}
		
		return dinoZoomList;
	}
	
	/**
	 * Gestisce il messaggio dello stato di un singolo dinosauro
	 * @param msg
	 * @return array contenente i parametri del messaggio
	 */
	public static String[] manageDinoState(String msg)
	{
		String validMessage = msg.substring(msg.indexOf(',')+1);
		String[] dinoState = validMessage.split(",");
		
		//Eliminazione delle parentesi nelle coordinate di inizio
		 
		dinoState[3] = dinoState[3].substring(1, 2);
		dinoState[4] = dinoState[4].substring(0, 1);
		
		return dinoState;
	}

	/**
	 * Gestisce il messaggio del movimento di un dinosauro
	 * @param msg
	 * @return stringa contenente l'esito del combattimento se c'è stato, altrimenti ritorna null
	 */
	public static String manageDinoMove(String msg)
	{
		String validMessage = msg.substring(msg.indexOf(',')+1);
		
		if(validMessage.indexOf(',') != -1)
		{
			String[] splittedMessage = validMessage.split(",");
			 
			return splittedMessage[1];
		}
		return null;
	}
	
	/**
	 * Gestisce il messaggio della deposizione di un uovo di un dinosauro
	 * @param msg
	 * @return array contenente i parametri del messaggio
	 */
	public static String[] manageNewEgg(String msg)
	{
		return splitMessage(msg);
	}
	
	/**
	 * Gestisce il messaggio del cambio del turno
	 * @param msg
	 * @return array contenente i parametri del messaggio
	 */
	public static String[] roundSwitch(String msg)
	{
		return splitMessage(msg);
	}
	
	/**
	 * Gestisce il messaggio del login di un giocatore
	 * @param msg
	 * @return array contenente i parametri del messaggio
	 */
	public static String[] manageLogin(String msg)
	{
		return splitMessage(msg);
	}
	
	/**
	 * Gestisce il messaggio della lista dei giocatori di una partita
	 * @param msg
	 * @return array contenente i parametri del messaggio
	 */
	public static String[] managePlayerList(String msg)
	{
		return splitMessage(msg);
	}
	
	/**
	 * Gestisce il messaggio della classifica dei giocatori di una partita
	 * @param msg
	 * @return ArrayList contenente in ordine i giocatori con il loro stato; 4 celle per ogni giocatore 
	 * contenti(in ordine): username, nome specie, punteggio, se è in partita
	 */
	public static ArrayList<String> manageRanking(String msg)
	{
		ArrayList<String> rankingList = new ArrayList<String>();
		String validMessage = msg.substring(msg.indexOf(',')+1);
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
		
		return rankingList;
	}
	
	/**
	 * Creazione utente, per creare un nuovo utente
	 * @param username
	 * @param password
	 * @return messaggio completo
	 */
	public static String createUser(String username, String password)
	{
		return "@creaUtente,user=" + username + ",pass=" + password;
	}
	
	// End gestione messaggi in entrata
	
	
	// Creazione messaggi in uscita
	
	/**
	 * Login, per effettuare il login, identificandosi e autenticandosi
	 * @param username
	 * @param password
	 * @return messaggio completo
	 */
	public static String createLogin(String username, String password)
	{
		return "@login,user=" + username + ",pass=" + password;
	}
	
	/**
	 * Creazione razza dinosauri, per chiedere la creazione di una nuova razza di dinosauri
	 * @param token
	 * @param nome
	 * @param tipo
	 * @return messaggio completo
	 */
	public static String createRace(String token, String nome, String tipo)
	{
		return "@creaRazza,token=" + token + ",nome=" + nome + ",tipo=" + tipo;
	}
	
	/**
	 * Accesso partita, per entrare nella partita
	 * @param token
	 * @return messaggio completo
	 */
	public static String createGameAccess(String token)
	{
		return "@accessoPartita,token=" + token;
	}
	
	/**
	 * Uscita partita, per abbandonare la partita
	 * @param token
	 * @return messaggio completo
	 */
	public static String createGameExit(String token)
	{
		return "@uscitaPartita,token=" + token;
	}
	
	/**
	 * Lista giocatori, per ottenere la lista degli utenti attualmente in partita
	 * @param token
	 * @return messaggio completo
	 */
	public static String createPlayerList(String token)
	{
		return "@listaGiocatori,token=" + token;
	}
	
	/**
	 * Classifica, per ottenere  la  classifica generale
	 * @param token
	 * @return messaggio completo
	 */
	public static String createRanking(String token)
	{
		return "@classifica,token=" + token;
	}
	
	/**
	 * Logout, per effettuare il logout
	 * @param token
	 * @return messaggio completo
	 */
	public static String createLogout(String token)
	{
		return "@logout,token=" + token;
	}
	
	/**
	 * Mappa generale, per chiedere la mappa generale aggiornata
	 * @param token
	 * @return messaggio completo
	 */
	public static String createGeneralMap(String token)
	{
		return "@mappaGenerale,token=" + token;
	}
	
	/**
	 * Lista dinosauri, per ottenere la lista degli identificatori dei propri dinosauri
	 * @param token
	 * @return messaggio completo
	 */
	public static String createDinoList(String token)
	{
		return "@listaDinosauri,token=" + token;
	}
	
	/**
	 * Vista locale, per ottenere la vista locale dettagliata di uno dei propri dinosauri
	 * @param token
	 * @param dinoId
	 * @return messaggio completo
	 */
	public static String createDinoZoom(String token, String dinoId)
	{
		return "@vistaLocale,token=" + token + ",idDino=" + dinoId;
	}
	
	/**
	 * Stato dinosauro, per ottenere lo stato di un proprio dinosauro oppure di un dinosauro altrui
	 * @param token
	 * @param dinoId
	 * @return messaggio completo
	 */
	public static String createDinoState(String token, String dinoId)
	{
		return "@statoDinosauro,token=" + token + ",idDino=" + dinoId;
	}
	
	/**
	 * Movimento dinosauro, per spostare un proprio dinosauro in una nuova posizione sulla mappa
	 * @param token
	 * @param idDino
	 * @param destX
	 * @param destY
	 * @return messaggio completo
	 */
	public static String createDinoMove(String token, String idDino, String destX, String destY)
	{
		return "@muoviDinosauro,token="+ token + ",idDino=" + idDino + ",dest={"+ destX + ","+ destY + "}";
	}
	
	/**
	 * Crescita dinosauro, per  far crescere un proprio dinosauro
	 * @param token
	 * @param dinoId
	 * @return messaggio completo
	 */
	public static String createDinoGrowUp(String token, String dinoId)
	{
		return "@cresciDinosauro,token=" +  token + ",idDino=" + dinoId;
	}
	
	/**
	 * Deposizione uovo, per creare un nuovo dinosauro nei pressi di un proprio dinosauro
	 * @param token
	 * @param dinoId
	 * @return messaggio completo
	 */
	public static String createNewEgg(String token, String dinoId)
	{
		return "@deponiUovo,token=" + token + ",idDino=" + dinoId;
	}
	
	/**
	 * Conferma turno, per confermare la volontà di utilizzare il turno (entro 30 secondi dalla notifica 
	 * che è il proprio turno)
	 * @param token
	 * @return messaggio completo
	 */
	public static String createRoundConfirmation(String token)
	{
		return "@confermaTurno,token=" + token;
	}
	
	/**
	 * Passa turno, per passare il turno prima della scadenza del tempo a disposizione (2 minuti)
	 * @param token
	 * @return messaggio completo
	 */
	public static String createPassOffRound(String token)
	{
		return "@passaTurno,token=" + token;
	}

	// Edn creazione messaggi in uscita
}

