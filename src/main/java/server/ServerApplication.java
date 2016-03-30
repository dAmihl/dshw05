package server;

import java.io.IOException;

public class ServerApplication {
	
	private static OperationServer server;
	
	private static boolean bMultiThread = false;

	public static void main(String[] args) {
				
		System.out.println("Server started..");
		
		if (args.length > 0){
			switch(args[0]){
			case "single": bMultiThread = false; break;
			case "multi": bMultiThread = true; break;
			default: 
					System.out.println("Unknown argument. Exiting.."); 
					System.exit(0);
			break;
			}
		}
		
		try {
			server = new OperationServer(bMultiThread);
		} catch (IOException e) {
			System.out.println("Could not create server socket.");
			e.printStackTrace();
			System.exit(0);
		}
		
		server.start();

	}
	


}
