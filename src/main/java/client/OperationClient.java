package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import utils.Protocol;

public class OperationClient {

	private Socket clientSocket = null;
	
	public OperationClient(){
	}
	
	public void connect(){
		try {
			clientSocket = new Socket(Protocol.URL, Protocol.PORT);
			connectedToServer();
		} catch (UnknownHostException e) {
			System.out.println("Could not connect: UNKNOWN HOST");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error connecting to server!");
			e.printStackTrace();
		} finally {
			if (clientSocket != null){
				try {
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void connectedToServer(){
		System.out.println("Client successfully connected to server!");
	}
	
	
	
}
