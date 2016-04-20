package server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import rmiutils.RMITask;
import rmiutils.RemoteMethodServer;
import utils.Protocol;

public class ServerApplication {
	
	private static OperationServer server;
	
	private static boolean bMultiThread = true;
	private static boolean bUseRMI = true;

	public static void main(String[] args) {
				
		System.out.println("Server started...");
		
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
		
		if (bUseRMI){
			initRMI();
		}
		
		server.start();
		
		

	}
	
	private static void initRMI(){
		/*if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }*/
        try {
            String name = Protocol.RMI_NAME;
            RemoteMethodServer engine = new RemoteMethodEngine();
            RemoteMethodServer stub =
                (RemoteMethodServer) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.createRegistry(Protocol.RMI_PORT);
            registry.rebind(name, stub);
            System.out.println("RMIServerEngine bound on port "+Protocol.RMI_PORT);
        } catch (Exception e) {
            System.err.println("RMI Server exception:");
            e.printStackTrace();
        }
	}
	
	private static void printHelpText(){
		System.out.println("Unknown argument. Exiting..");
		System.out.println("Try: ./server.jar multi");
		System.out.println("Or: ./server.jar single");
		System.out.println("No argument means multi threaded.");
	}


	


}
