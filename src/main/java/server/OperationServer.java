package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONObject;

import utils.Protocol;
import utils.Protocol.REPLY_TYPE;

public class OperationServer {

	private boolean isRunning = true;
	private ServerSocket serverSocket = null;
	private boolean bMultiThread = false;
	private ExecutorService executorSrvc;
	private static int numThreads = 10;
	
	private static final ArrayList<String> knownClientNames = new ArrayList<>();
	
	public OperationServer(boolean multiThread) throws IOException{
			knownClientNames.add("client");
			bMultiThread = multiThread;
			serverSocket = new ServerSocket(Protocol.PORT);
			
			if (bMultiThread){
				executorSrvc = Executors.newFixedThreadPool(numThreads);
			}
		
	}
	
	/**
	 * Starts the server and listening.
	 * Runs as long as isRunning is true.
	 */
	public void start(){
		while (isRunning){
			Socket client = null;
			System.out.println("Waiting for client...");
			try {
				client = serverSocket.accept();
				
				if (bMultiThread){
					executorSrvc.execute(new ServerThread(client, this));
				}else{
					clientConnected(client);
				}
			} catch (IOException e) {
				System.out.println("Could not accept client connection!");
				e.printStackTrace();
			} 
		}
		if (bMultiThread){
			executorSrvc.shutdown();
		}
	}
	
	
	/**
	 * Called when a client successfully connected to the server.
	 * @param clientSocket The connected socket to client.
	 */
	public void clientConnected(Socket clientSocket){
		System.out.println("Client successfully connected!");
		System.out.println("Server now listening to client..");
		InputStream effectiveInputStream = null;
		// Waits for the client to send a message..
		try {
			while((effectiveInputStream = Protocol.canReadInputStream(clientSocket.getInputStream()))!= null){
				while(!readClientMessage(clientSocket, effectiveInputStream));
			}
			System.out.println("Connection lost. Disconnecting..");
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Could not get input stream.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads a message by the client and interprets it.
	 * @param clientSocket The socket to wait for a message
	 * @return Whether a message was read yet or not.
	 */
	private boolean readClientMessage(Socket clientSocket, InputStream inputStream){
		try {
			InputStream in = inputStream;
		
		
			BufferedReader buff = new BufferedReader(new InputStreamReader(in));
			
			String clientMessage = null;
			while(buff.ready()){
				clientMessage = buff.readLine();
				System.out.println("Received message by Client:");
				System.out.println(clientMessage);
				interpretMessageByClient(clientSocket, clientMessage);
				return true;
			}
		
		} catch (IOException e) {
			System.out.println("Failed to get Input Stream by socket.");
			e.printStackTrace();
			return true;
		}
		return false;
	}
	
	
	/**
	 * Interpretes a sent message by the client.
	 * @param socket The connected socket of the client.
	 * @param message The received message. Has to be in JSON format.
	 */
	private void interpretMessageByClient(Socket socket,String message){
		
		JSONObject jsonObj = Protocol.makeJsonObjectByString(message);
		
		// Decide what operation should be done for the client.
		if (jsonObj != null){
			String requestedOperation = (String) jsonObj.get("operation");
			switch (requestedOperation){
				case "AUTHENTICATE": doAuthenticate(socket, jsonObj); break; 
				case "ADDITION": doAddition(socket, jsonObj); break;
				case "SUBTRACTION": doSubtraction(socket, jsonObj);break;
				case "MULTIPLICATION": doMultiplication(socket, jsonObj); break;
				case "LUCAS": doLucasNumbers(socket, jsonObj);break;
				default:
					Protocol.reply(socket, Protocol.REPLY_TYPE.ERROR, "Unknown command!"); break;
			}
		}
		
	}
	
	/**
	 * Trys to authenticate the user by the clients name.
	 * @param socket The connected socket to the client.
	 * @param clientMessage The JSONobject the client sent. This contains the clients name.
	 */
	private void doAuthenticate(Socket socket, JSONObject clientMessage){
		String clientName = (String) clientMessage.get("name");
		if (knownClientNames.contains(clientName)){
			Protocol.reply(socket, REPLY_TYPE.OK, "Successfully authenticated!");
		}else{
			Protocol.reply(socket, REPLY_TYPE.ERROR, "Unknown client name.");
		}
		
	}
	
	/**
	 * Performs the addition operation. Reads 2 arguments of the message.
	 * @param socket The connected socket of client.
	 * @param clientMessage The JSONObject the client sent.
	 */
	private void doAddition(Socket socket, JSONObject clientMessage){
		Integer arg0 = Integer.parseInt(((String) clientMessage.get("arg0")));
		Integer arg1 = Integer.parseInt(((String) clientMessage.get("arg1")));
		if (arg0 == null || arg1 == null){
			Protocol.reply(socket, REPLY_TYPE.ERROR, "Wrong arguments!");
		}else{
			Integer result = arg0 + arg1;
			Protocol.reply(socket, REPLY_TYPE.OK, result.toString());
		}
	}
	
	/**
	 * Performs the subtraction operation. Reads 2 arguments of the message.
	 * @param socket The connected socket of client.
	 * @param clientMessage The JSONObject the client sent.
	 */
	private void doSubtraction(Socket socket, JSONObject clientMessage){
		Integer arg0 = Integer.parseInt(((String) clientMessage.get("arg0")));
		Integer arg1 = Integer.parseInt(((String) clientMessage.get("arg1")));
		if (arg0 == null || arg1 == null){
			Protocol.reply(socket, REPLY_TYPE.ERROR, "Wrong arguments!");
		}else{
			Integer result = arg0 - arg1;
			Protocol.reply(socket, REPLY_TYPE.OK, result.toString());
		}
	}
	
	/**
	 * Performs the multiplication operation. Reads 2 arguments of the message.
	 * @param socket The connected socket of client.
	 * @param clientMessage The JSONObject the client sent.
	 */
	private void doMultiplication(Socket socket, JSONObject clientMessage){
		Integer arg0 = Integer.parseInt(((String) clientMessage.get("arg0")));
		Integer arg1 = Integer.parseInt(((String) clientMessage.get("arg1")));
		if (arg0 == null || arg1 == null){
			Protocol.reply(socket, REPLY_TYPE.ERROR, "Wrong arguments!");
		}else{
			Integer result = arg0 * arg1;
			Protocol.reply(socket, REPLY_TYPE.OK, result.toString());
		}
	}
	
	/**
	 * Performs the lucas numbers operation. Reads 1 argument 
	 * (the number of lucas numbers to compute)
	 *  of the message.
	 * @param socket The connected socket of client.
	 * @param clientMessage The JSONObject the client sent.
	 */
	private void doLucasNumbers(Socket socket, JSONObject clientMessage){
		Integer arg0 = Integer.parseInt((String) clientMessage.get("arg0"));
		
		Integer result = lucasNumber(arg0);
		Protocol.reply(socket, REPLY_TYPE.OK, result.toString());
	}
	
	/**
	 * Naive way to compute lucas numbers. 
	 * @param N The number to compute
	 * @return lucas number for number N
	 */
	private Integer lucasNumber(int N){
		if( N == 0 ) return 2;
	    if( N == 1 ) return 1;
	    return lucasNumber(N-1) + lucasNumber(N-2);
	}
	
	
	
	
	
	
}
