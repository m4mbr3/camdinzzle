/**
 * @author Forme
 * created 			28/04/2011
 * last modified	28/04/2011 
 */

package Client;

import java.util.ArrayList;
/**
 * Interfaccia che definisce i metodi di interazione con l'utente.
 */
public interface Visual 
{
	
	/**
	 *  Disegna la mappa generale del player con terra, acqua, vegetazione oppure buio.
	 */
	public void drawMap(ArrayList<String> mapList);
	
	/**
	 * Disegna la mappa intorno ad un dinosauro identificato dal suo dinoId. La mappa conterrà, oltre 
	 * agli elementi contenuti nella mappa generale, l'energia della vegetazione o delle carogne e i dinosauri
	 * nella area di visibilità con il proprio dinoId.
	 */
	public void drawDinoZoom(String dinoId, ArrayList<String> mapList);
	
	/**
	 * Disegna la lista dei dinosauri del player identificati dal dinoId e con indicata l'energia,
	 * l'età e la dimensione del dinosauro.
	 */
	public void drawDinoList(Object[] msgDinoList);
	
	/**
	 * Disegna l'area relativa al tempo a disposizione del player. Gestisce sia il timeout di scelta di
	 * gioco, sia il timeout di fine turno.
	 */
	public void drawTime(int timeInt);
	
	/**
	 * Metodo che disegna lo stato della connessione fra client e server
	 */
	public void drawConnectionState(String msg);
	
	/**
	 * Disegna la classifica attuale della partita.
	 */
	public void drawRanking(ArrayList<String> rankingList);
	
	/**
	 * Disegna lo stato del dinosauro selezionato.
	 */
	public void drawDinoState(String dinoId, String[] msgDinoState);
	
	/**
	 * Disegna la lista dei giocatori.
	 */
	public void drawPlayerList(String[] msgPlayerList);
}
