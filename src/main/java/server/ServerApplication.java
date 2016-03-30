package server;

import java.io.IOException;

public class ServerApplication {
	
	private static OperationServer server;
	
	private static boolean bMultiThread = true;

	public static void main(String[] args) {
				
		System.out.println("Server started..");
		
		if (args.length > 0){
			switch(args[0]){
			case "single": 
				bMultiThread = false;
				break;
			case "multi": bMultiThread = true; break;
			default: 
					printHelpText();
					System.exit(0);
			break;
			}
		}
		
		if (bMultiThread){
			System.out.println("Server running in Multi Thread Mode.");
		}else{
			System.out.println("Server running in Single Thread Mode.");
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
	
	private static void printHelpText(){
		System.out.println("Unknown argument. Exiting..");
		System.out.println("Try: ./server.jar multi");
		System.out.println("Or: ./server.jar single");
		System.out.println("No argument means multi threaded.");
	}
	


}
