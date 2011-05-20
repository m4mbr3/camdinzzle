package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerLogic serverLogic = new ServerLogic();
		
		ServerSocket deal_newuser;
		ServerSocket deal_login;
		
		System.out.println("<<SERVER>>--INIT");
		// Definition of default port login
		int port_login = 4567;
		// Definition of default port NewUser
		int port_newuser = 4566;
		
		// Definition of new Server login for passing it to startLogin and
		// launch login daemon
		System.out.println("<<SERVER>>--STARTING LOGIN DAEMON ");
		try {
			deal_login = new ServerSocket(port_login);
			startLoginDaemon(deal_login, serverLogic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("<<SERVER>>--LOGIN DAEMON STARTED");

		// Definition of new Server NewUser for passing it to startNewUser and
		// launch newuser daemon
		System.out.println("<<SERVER>>--STARTING NEWUSER DAEMON ");
		try {
			deal_newuser = new ServerSocket(port_newuser);
			startNewUserDaemon(deal_newuser, serverLogic);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("<<SERVER>>--NEWUSER DAEMON STARTED");
		
		System.out.println("<<SERVER>>--DEFINITION OF ENVIROMENT VARIABLES");
		
		
		// Definition of new PlayerList empty
	}

	
	/**
	 * Method for starting 
	 */
	public static void startLoginDaemon(ServerSocket server_socket, ServerLogic sl) {

		/* The connection for login must be at this port : 4567 */
		Login login = new Login(server_socket, sl);
		(new Thread(login)).start();

	}

	public static void startClientConnectionManagerDaemon(Socket socket_with_client,
			String token, String username, ServerLogic sl) {
		ClientManagerSocket new_manager = new ClientManagerSocket(
				socket_with_client, sl, username, token);
		// TODO Da rivedere
		/*
		loggedPlayers.put(token, players.get(username));
		(new Thread(new_manager)).start();
		loggedClientManager.put(token, new_manager);
		new_manager = null;
		*/
	}

	public static void startNewUserDaemon(ServerSocket server_socket, ServerLogic sl) {
		NewUser newuser = new NewUser(server_socket, sl);
		(new Thread(newuser)).start();
	}
}
