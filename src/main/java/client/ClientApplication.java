package client;


import utils.Protocol;

public class ClientApplication {

	public static void main(String[] args) {
		System.out.println("Client started..");
		System.out.println("Trying to connect to: "+Protocol.URL+":"+Protocol.PORT);

		OperationClient client = new OperationClient();
		client.connect();
		
	}

}
