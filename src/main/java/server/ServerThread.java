package server;

import java.net.Socket;

public class ServerThread implements Runnable {

	Socket clientSocket;
	OperationServer serverApp;
	
	public ServerThread(Socket socket, OperationServer server){
		this.clientSocket = socket;
		this.serverApp = server;
	}
	
	@Override
	public void run() {
		this.serverApp.clientConnected(this.clientSocket);

	}

}
