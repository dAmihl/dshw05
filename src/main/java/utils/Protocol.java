package utils;

import java.net.Socket;

public class Protocol {
	
	public enum Operation{
		ADDITION,
		SUBTRACTION,
		MULTIPLICATION,
		LUCAS
		
	}

	public static final int PORT = 1337;
	public static final String URL = "localhost";
	
	 
	/**
	 * The request method the client can call to send a request to the server
	 * @param clientSocket the socket on client which is connected to a server
	 * @param op The requested operation (addition, subtraction,multiplication, lucas)
	 * @param args The arguments (array of integers with arbitrary size)
	 */
	public static void request(Socket clientSocket, Operation op, Integer...args){
		
	}
	
	
	/**
	 * The reply method the server can call to reply to the client
	 * @param serverSocket
	 */
	public static void reply(Socket serverSocket){
		
	}
	
}
