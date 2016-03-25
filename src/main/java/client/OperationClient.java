package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import utils.Protocol;

public class OperationClient {

	private Socket clientSocket = null;
	private String clientName = "client";
	
	public OperationClient(){
	}
	
	public void connect(){
		try {
			clientSocket = new Socket(Protocol.URL, Protocol.PORT);
			connectedToServer(clientSocket);
		} catch (UnknownHostException e) {
			System.out.println("Could not connect: UNKNOWN HOST");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error connecting to server!");
			e.printStackTrace();
		} finally {
			if (clientSocket != null){
				disconnect();
			}
		}
	}
	
	public void disconnect(){
		if (clientSocket != null){
			try {
				clientSocket.close();
				System.out.println("Socket closed.");
			} catch (IOException e) {
				System.out.println("Attempt to disconnect failed: Exception when closing socket.");
				e.printStackTrace();
			}
		}else{
			System.out.println("Attempt to disconnect failed: ClientSocket is null.");
		}
	}
	
	private void connectedToServer(Socket socket){
		System.out.println("Client successfully connected to server!");
		Integer[] args = {1,2};
		Protocol.request(socket, Protocol.Operation.AUTHENTICATE, clientName, args);
		while(readResult(socket));
	}
	
	private boolean readResult(Socket socket){
		try {
			InputStream in = socket.getInputStream();
		
		
			BufferedReader buff = new BufferedReader(new InputStreamReader(in));
		
			String serverMessage = null;
			while(buff.ready()){
				serverMessage = buff.readLine();
				System.out.println("Received message by Server:");
				System.out.println(serverMessage);	
			}
			
		} catch (IOException e) {
			System.out.println("Failed to get Input Stream by socket.");
			e.printStackTrace();
			return false;
		} 
		return true;
	}
	
	
	
	
	
	
}
