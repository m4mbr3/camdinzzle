

import java.util.ArrayList;

public class Game {

	/**
	 * @uml.property  name="player"
	 * @uml.associationEnd  multiplicity="(1 -1)" ordering="true" inverse="game:Player"
	 * @uml.association  name="player in game"
	 */
	private ArrayList<Player> player;

	/**
	 * Getter of the property <tt>player</tt>
	 * @return  Returns the player.
	 * @uml.property  name="player"
	 */
	public ArrayList<Player> getPlayer() {
		return player;
	}

	/**
	 * Setter of the property <tt>player</tt>
	 * @param player  The player to set.
	 * @uml.property  name="player"
	 */
	public void setPlayer(ArrayList<Player> player) {
		this.player = player;
	}

	/**
	 * @uml.property  name="gameId"
	 */
	private String gameId;

	/**
	 * Getter of the property <tt>gameId</tt>
	 * @return  Returns the gameId.
	 * @uml.property  name="gameId"
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * Setter of the property <tt>gameId</tt>
	 * @param gameId  The gameId to set.
	 * @uml.property  name="gameId"
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

		
		/**
		 */
		public Game(){
		}

			
			/**
			 */
			public void newUser(){
			}

				
				/**
				 */
				public void addPlayer(){
				}

				/**
				 * @uml.property  name="map" multiplicity="(0 -1)" dimension="2"
				 */
				private Object maps;

				/**
				 * Getter of the property <tt>map</tt>
				 * @return  Returns the maps.
				 * @uml.property  name="map"
				 */
				public Object getMap() {
					return maps;
				}

				/**
				 * Setter of the property <tt>map</tt>
				 * @param map  The maps to set.
				 * @uml.property  name="map"
				 */
				public void setMap(Object map) {
					maps = map;
				}

}
