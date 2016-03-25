package server;

import java.io.IOException;

public class ServerApplication {
	
	private static OperationServer server;

	public static void main(String[] args) {
				
		System.out.println("Server started..");
		
		try {
			server = new OperationServer();
		} catch (IOException e) {
			System.out.println("Could not create server socket.");
			e.printStackTrace();
			System.exit(0);
		}
		
		server.start();

	}
	


}
