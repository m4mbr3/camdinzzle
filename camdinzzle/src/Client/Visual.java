/**
 * @author Forme
 * created 			28/04/2011
 * last modified	28/04/2011 
 */

package Client;

public interface Visual 
{
	
	/**
	 *  Metodo che disegna la mappa generale del player con terra, acqua, vegetazione oppure buio
	 */
	public void drawMap(String msg);
	
	/**
	 * Metodo che disegna la mappa intorno ad un dinosauro identificato dal suo dinoId. La mappa conterrà, oltre 
	 * agli elementi contenuti nella mappa generale, l'energia della vegetazione o delle carogne e i dinosauri
	 * nella area di visibilità con il proprio dinoId
	 */
	public void drawDinoZoom(String dinoId, String msg);
	
	/**
	 * Metodo che disegna la lista dei dinosauri del player identificati dal dinoId e con indicata l'energia,
	 * l'età e la dimensione del dinosauro
	 */
	public void drawDinoList(String msg);
	
	/**
	 * Metodo che disegna l'area relativa al tempo a disposizione del player. Gestisce sia il timeout di scelta di
	 * gioco, sia il timeout di fine turno
	 */
	public void drawTime(String msg);
	
	/**
	 * Metodo che disegna lo stato della connessione fra client e server
	 */
	public void drawConnectionState(String msg);
	
	/**
	 * Metodo che disegna la classifica attuale della partitas
	 */
	public void showRanking(String msg);
}
