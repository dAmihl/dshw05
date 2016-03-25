package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONObject;

import utils.Protocol;

public class OperationServer {

	private boolean isRunning = true;
	private ServerSocket serverSocket = null;
	
	public OperationServer() throws IOException{
	
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
				case "AUTHENTICATE": Protocol.reply(socket, "I will auth."); break; 
				case "ADDITION": Protocol.reply(socket, "I will add."); break;
				case "SUBTRACTION": Protocol.reply(socket, "I will sub.");break;
				case "MULTIPLICATION": Protocol.reply(socket, "I will mult."); break;
				case "LUCAS": Protocol.reply(socket, "I will lucas");break;
				default:Protocol.reply(socket, "Unknown command!"); break;
			}
		}
		
	}
	
	
}
