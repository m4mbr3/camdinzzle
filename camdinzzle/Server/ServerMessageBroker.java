/**
 * @author Forme
 * created 			06/05/2011
 * last modified	06/05/2011
 */ 

package Server;

/**
 * classe utilizzata per la gestione delle stringhe dei messaggi proveniente dal client. I metodi che iniziano con 
 * "manage" gestiscono le stringhe dei messaggi proveniente dal Client; i metodi che iniziano con "create" 
 * creano le stringhe dei messaggi da mandare al Client
 */
public class ServerMessageBroker 
{
	// Creazione messaggi in uscita
	
	/**
	 * Divide il messaggio in parametri
	 * @param msg
	 * @return array contenente il messaggio diviso per parametri
	 */
	public static String[] splitMessage(String msg)
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
		String[] splittedMessage = msg.split(",");
		
		return splittedMessage[0].substring(1);
	}
	
	/**
	 * Gestisce la divisione del messaggio in parametri
	 * @param msg
	 * @return array contenente il messaggio diviso per parametri
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
	
	// End creazione messaggi in uscita
}
