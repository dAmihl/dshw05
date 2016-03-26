package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;

import utils.Protocol;
import utils.Protocol.Operation;

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
		if (clientAuthenticate(socket)){
			System.out.println("SUCCESSFULLY AUTHENTICATED!");
		}else{
			System.out.println("AUTHENTICATION ERROR! Disconnecting..");
			disconnect();
		}
		clientRequestAddition(socket, 1,2);
		clientRequestSubtraction(socket, 1, 2);
		clientRequestMultiplication(socket, 1, 2);

	}
	
	private boolean clientAuthenticate(Socket socket){
		Protocol.request(socket, Protocol.Operation.AUTHENTICATE, clientName, new Integer[]{});
		String replyMessage;
		while((replyMessage = readResult(socket))== null);
		JSONObject responseObj = Protocol.makeJsonObjectByString(replyMessage);
		String resultType = (String) responseObj.get("type");
		return resultType.equals("OK");
	}
	
	private boolean clientRequestAddition(Socket socket, Integer arg0, Integer arg1){
		Protocol.request(socket, Operation.ADDITION, clientName, new Integer[]{arg0, arg1});
		String replyMessage;
		while((replyMessage = readResult(socket))== null);
		JSONObject responseObj = Protocol.makeJsonObjectByString(replyMessage);
		String resultType = (String) responseObj.get("type");
		if (!resultType.equals("OK")){
			return false;
		}else{
			Integer result = Integer.parseInt((String)responseObj.get("result"));
			System.out.println("Addition result of "+arg0+" and "+arg1+": "+result);
			return true;
		}
	}
	
	private boolean clientRequestSubtraction(Socket socket, Integer arg0, Integer arg1){
		Protocol.request(socket, Operation.SUBTRACTION, clientName, new Integer[]{arg0, arg1});
		String replyMessage;
		while((replyMessage = readResult(socket))== null);
		JSONObject responseObj = Protocol.makeJsonObjectByString(replyMessage);
		String resultType = (String) responseObj.get("type");
		if (!resultType.equals("OK")){
			return false;
		}else{
			Integer result = Integer.parseInt((String)responseObj.get("result"));
			System.out.println("Subtraction result of "+arg0+" and "+arg1+": "+result);
			return true;
		}
	}
	
	private boolean clientRequestMultiplication(Socket socket, Integer arg0, Integer arg1){
		Protocol.request(socket, Operation.MULTIPLICATION, clientName, new Integer[]{arg0, arg1});
		String replyMessage;
		while((replyMessage = readResult(socket))== null);
		JSONObject responseObj = Protocol.makeJsonObjectByString(replyMessage);
		String resultType = (String) responseObj.get("type");
		if (!resultType.equals("OK")){
			return false;
		}else{
			Integer result = Integer.parseInt((String)responseObj.get("result"));
			System.out.println("Multiplication result of "+arg0+" and "+arg1+": "+result);
			return true;
		}
	}
	
	
	private String readResult(Socket socket){
		try {
			InputStream in = socket.getInputStream();
		
		
			BufferedReader buff = new BufferedReader(new InputStreamReader(in));
		
			String serverMessage = null;
			while(buff.ready()){
				serverMessage = buff.readLine();
				System.out.println("Received message by Server:");
				System.out.println(serverMessage);
				return serverMessage;
			}
			
		} catch (IOException e) {
			System.out.println("Failed to get Input Stream by socket.");
			e.printStackTrace();
			return "Error on Input Stream";
		} 
		return null;
	}
	
	
	
	
	
	
}
