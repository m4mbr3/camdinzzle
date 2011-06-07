/**
 * 
 */
package Server;

/**
 * @author Andrea
 *
 */
public class ClientManagerLocal implements ClientManager{

	private ServerLogic serverLogic;
	/**
	 * 
	 */
	public ClientManagerLocal(ServerLogic serverLogic) {
		// TODO Auto-generated constructor stub
		this.serverLogic = serverLogic;
	}

	@Override
	public boolean sendChangeRound(String msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getIsInGame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIsInGame(boolean isInGame) {
		// TODO Auto-generated method stub
		
	}

}
