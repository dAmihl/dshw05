package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
	}
	
	
}
