package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import utils.Protocol;
import utils.Protocol.REPLY_TYPE;

public class OperationServer {

	private boolean isRunning = true;
	private ServerSocket serverSocket = null;
	
	private static final ArrayList<String> knownClientNames = new ArrayList<>();
	
	public OperationServer() throws IOException{
			knownClientNames.add("client");
			serverSocket = new ServerSocket(Protocol.PORT);
		
	}
	
	public void start(){
		while (isRunning){
			Socket client = null;
			
			try {
				client = serverSocket.accept();
				clientConnected(client);
			} catch (IOException e) {
				System.out.println("Could not accept client connection!");
				e.printStackTrace();
			} finally{
				if (client != null){
					try {
						client.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
		}
	}
	
	private void clientConnected(Socket clientSocket){
		System.out.println("Client successfully connected!");
		System.out.println("Server now listening to client..");
		while(readClientMessage(clientSocket));
	}
	
	private boolean readClientMessage(Socket clientSocket){
		try {
			InputStream in = clientSocket.getInputStream();
		
		
			BufferedReader buff = new BufferedReader(new InputStreamReader(in));
			
			String clientMessage = null;
			while(buff.ready()){
				clientMessage = buff.readLine();
				System.out.println("Received message by Client:");
				System.out.println(clientMessage);
				interpretMessageByClient(clientSocket, clientMessage);
			}
		
		} catch (IOException e) {
			System.out.println("Failed to get Input Stream by socket.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void interpretMessageByClient(Socket socket,String message){
		
		JSONObject jsonObj = Protocol.makeJsonObjectByString(message);
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
	
	private void doAuthenticate(Socket socket, JSONObject clientMessage){
		String clientName = (String) clientMessage.get("name");
		if (knownClientNames.contains(clientName)){
			Protocol.reply(socket, REPLY_TYPE.OK, "Successfully authenticated!");
		}else{
			Protocol.reply(socket, REPLY_TYPE.ERROR, "Unknown client name.");
		}
		
	}
	
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
	
	private void doLucasNumbers(Socket socket, JSONObject clientMessage){
		
	}
	
	
	
	
}
