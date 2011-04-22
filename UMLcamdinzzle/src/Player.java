

public class Player {

	/** 
	 * @uml.property name="species"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="player:Species"
	 * @uml.association name="manage"
	 */
	private Species species = null;

	/** 
	 * Getter of the property <tt>specie</tt>
	 * @return  Returns the specie.
	 * @uml.property  name="species"
	 */
	public Species getSpecies() {
		return species;
	}

	/** 
	 * Setter of the property <tt>specie</tt>
	 * @param specie  The specie to set.
	 * @uml.property  name="species"
	 */
	public void setSpecies(Species species) {
		this.species = species;
	}

	/**
	 * @uml.property  name="userName"
	 */
	private String userName;

	/**
	 * Getter of the property <tt>userName</tt>
	 * @return  Returns the userName.
	 * @uml.property  name="userName"
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Setter of the property <tt>userName</tt>
	 * @param userName  The userName to set.
	 * @uml.property  name="userName"
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @uml.property  name="password"
	 */
	private String password;

	/**
	 * Getter of the property <tt>password</tt>
	 * @return  Returns the password.
	 * @uml.property  name="password"
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter of the property <tt>password</tt>
	 * @param password  The password to set.
	 * @uml.property  name="password"
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @uml.property  name="game"
	 * @uml.associationEnd  inverse="player:Game"
	 * @uml.association  name="player in game"
	 */
	private Game game;

	/**
	 * Getter of the property <tt>game</tt>
	 * @return  Returns the game.
	 * @uml.property  name="game"
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Setter of the property <tt>game</tt>
	 * @param game  The game to set.
	 * @uml.property  name="game"
	 */
	public void setGame(Game game) {
		this.game = game;
	}

		
		/**
		 */
		public Player(){
		}

}
