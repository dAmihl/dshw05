package client;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import rmiutils.EchoTask;
import rmiutils.RemoteMethodServer;
import utils.Protocol;

public class ClientApplication {

	private static Scanner inputReader;
	private static OperationClient client;
	
	private static boolean bExitProgram = false;
	private static boolean bUseRMI = true;
	private static RemoteMethodServer remoteRMIServer;
	
	public static void main(String[] args) {
		System.out.println("Client started..");
		System.out.println("Trying to connect to: "+Protocol.URL+":"+Protocol.PORT);

		
		
		
		
		client = new OperationClient();
		client.connect();
		
		if (bUseRMI){
			initRMI();
			testEchoRMI();
		}
		
		if (args.length > 1){
			readCommandLineArguments(args);
		}else{
			while (!bExitProgram){
				startUserInput();
			}
		}
	}
	
	
	private static void initRMI(){
		/*if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }*/
        try {
            String name = Protocol.RMI_NAME;
            Registry registry = LocateRegistry.getRegistry(Protocol.RMI_PORT);
            remoteRMIServer = (RemoteMethodServer) registry.lookup(name);
            System.out.println("RMI Initialized. Ready to use.");
            client.setRemoteServer(remoteRMIServer);
            //Pi task = new Pi(Integer.parseInt(args[1]));
            //BigDecimal pi = comp.executeTask(task);
            //System.out.println(pi);
        } catch (Exception e) {
            System.err.println("Compute RMI exception:");
            e.printStackTrace();
        }
	}
	
	private static void testEchoRMI(){
		EchoTask t = new EchoTask("Hello World ECHO!");
		t.setTaskName("EchoTask");
		try {
			if (remoteRMIServer != null){
			String result = (String) remoteRMIServer.executeTask(t);
			System.out.println("RMI Echo result: "+result);
			} else{
				System.out.println("Remote server is null!");
			}
		} catch (RemoteException e) {
			System.out.println("RMI Echo exception!");
			e.printStackTrace();
		}
	}
	
	/** 
	 * COMMAND LINE ARGUMENTS METHODS
	 */
	
	private static void readCommandLineArguments(String[] args){
		switch (args[0]){
		case "+": readAdditionCommandLineArguments(args);  break;
		case "-": readSubtractionCommandLineArguments(args); break;
		case "*": readMultiplicationCommandLineArguments(args); break;
		case "lucas": readLucasNumbersCommandLineArguments(args);break;
		default: printHelpText(); break;
		}
	}
	
	private static void readAdditionCommandLineArguments(String[] args){
		if (args.length < 3){
			System.out.println("Not enough arguemnents found!");
			printHelpText();
		}
		Integer arg0 = Integer.parseInt(args[1]);
		Integer arg1 = Integer.parseInt(args[2]);
		client.clientRequestAddition(client.getConnectedSocket(), arg0, arg1);
	}
	
	private static void readSubtractionCommandLineArguments(String[] args){
		if (args.length < 3){
			System.out.println("Not enough arguemnents found!");
			printHelpText();
		}
		Integer arg0 = Integer.parseInt(args[1]);
		Integer arg1 = Integer.parseInt(args[2]);
		client.clientRequestSubtraction(client.getConnectedSocket(), arg0, arg1);
	}
	
	private static void readMultiplicationCommandLineArguments(String[] args){
		if (args.length < 3){
			System.out.println("Not enough arguemnents found!");
			printHelpText();
		}
		Integer arg0 = Integer.parseInt(args[1]);
		Integer arg1 = Integer.parseInt(args[2]);
		client.clientRequestMultiplication(client.getConnectedSocket(), arg0, arg1);
	}
	
	private static void readLucasNumbersCommandLineArguments(String[] args){
		if (args.length < 2){
			System.out.println("Not enough arguemnents found!");
			printHelpText();
		}
		Integer arg0 = Integer.parseInt(args[1]);
		client.clientRequestLucasNumbers(client.getConnectedSocket(), arg0);
	}
	
	private static void printHelpText(){
		System.out.println("HELP TEXT");
		System.out.println("Example ./client.jar * 1 2");
		System.out.println("Example ./client.jar + 2 5");
		System.out.println("Example ./client.jar lucas 5");
		System.out.println("Example ./client.jar - 5 3");
	}
	
	
	
	
	
	/**
	 * USER INPUT METHODS
	 */
	
	private static void startUserInput(){
		inputReader = new Scanner(System.in);  // Reading from System.in
		startOperationSelect();

	}
	
	private static void startOperationSelect(){
		System.out.println("Select an operation:");
		System.out.println("1. Addition");
		System.out.println("2. Subtraction");
		System.out.println("3. Multiplication");
		System.out.println("4. Lucas Numbers");
		System.out.println("5. RMI - Echo");
		System.out.println("6. RMI - Reverse");
		System.out.println("7. RMI - Question DeepThought");
		System.out.println("8. RMI - What is my purpose?");
		System.out.println("0. Exit");
		
		System.out.println("Enter a number: ");

		int operationSelection = inputReader.nextInt();
		inputReader.nextLine();
		
		
		switch(operationSelection){
		case 1: startAdditionUserInput(); break;
		case 2: startSubtractionUserInput();break;
		case 3: startMultiplicationUserInput();break;
		case 4: startLucasNumbersUserInput();break;
		case 0: endProgram(); break;
		case 5: startRMIEchoUserInput(); break;
		case 6: startRMIReverseUserInput(); break;
		case 7: startRMIDeepThoughtUserInput(); break;
		case 8: startRMIPurposeUserInput(); break;
		default: System.out.println("Unknown input. Please try again."); break;
		}
	}
	
	private static void startRMIEchoUserInput(){
		System.out.println("Enter a String: ");
		String inputString = inputReader.nextLine();
		client.rmiEcho(inputString);
	}
	
	private static void startRMIReverseUserInput(){
		System.out.println("Enter a String: ");
		String inputString = inputReader.nextLine();
		client.rmiReverse(inputString);
	}
	
	private static void startRMIDeepThoughtUserInput(){
		System.out.println("Question of life, the universe and everything.. ");
		client.rmiQuestion();
	}
	
	private static void startRMIPurposeUserInput(){
		System.out.println("What is my purpose?");
		client.rmiPurpose();
	}
	
	
	
	
	private static void startAdditionUserInput(){
		System.out.println("You selected the Addition operation.");
		System.out.println("Please enter two (integer) arguments:");
		System.out.println("First argument:");
		Integer arg0 = inputReader.nextInt();
		System.out.println("Second argument:");
		Integer arg1 = inputReader.nextInt();
		client.clientRequestAddition(client.getConnectedSocket(), arg0, arg1);

	}
	
	private static void  startSubtractionUserInput(){
		System.out.println("You selected the Subtraction operation.");
		System.out.println("Please enter two (integer) arguments:");
		System.out.println("First argument:");
		Integer arg0 = inputReader.nextInt();
		System.out.println("Second argument:");
		Integer arg1 = inputReader.nextInt();
		client.clientRequestSubtraction(client.getConnectedSocket(), arg0, arg1);
		}
	
	private static void startMultiplicationUserInput(){
		System.out.println("You selected the Multiplication operation.");
		System.out.println("Please enter two (integer) arguments:");
		System.out.println("First argument:");
		Integer arg0 = inputReader.nextInt();
		System.out.println("Second argument:");
		Integer arg1 = inputReader.nextInt();
		client.clientRequestMultiplication(client.getConnectedSocket(), arg0, arg1);
	}
	
	private static void startLucasNumbersUserInput(){
		System.out.println("You selected the Lucas Numbers operation.");
		System.out.println("Please enter one (integer) arguments:");
		System.out.println("First argument:");
		Integer arg0 = inputReader.nextInt();
	
		client.clientRequestLucasNumbers(client.getConnectedSocket(), arg0);
	}
	
	private static void endProgram(){
		if (!client.getConnectedSocket().isClosed()){
			client.disconnect();
		}
		bExitProgram = true;
	}

}
