package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import org.json.simple.JSONObject;

import rmiutils.DeepThoughtTask;
import rmiutils.EchoTask;
import rmiutils.PurposeTask;
import rmiutils.RemoteMethodServer;
import rmiutils.RemoteTask;
import rmiutils.ReverseEchoTask;
import utils.Protocol;
import utils.Protocol.Operation;

public class OperationClient {

	private Socket clientSocket = null;
	private String clientName = "client";
	
	private RemoteMethodServer remoteServer;
	
	public OperationClient(){
	}
	
	
	/**
	 * Called to connect to the server. 
	 * PORT and URL are specified in Protocol class.
	 */
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
		} 
	}
	
	public void setRemoteServer(RemoteMethodServer rmServer){
		this.remoteServer = rmServer;
	}
	
	/**
	 * Called to disconnect from the server. Closes the socket.
	 */
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
	
	public Socket getConnectedSocket(){
		return clientSocket;
	}
	
	
	/**
	 * Called when successfully connected to a server.
	 * @param socket The connected socket reference
	 */
	private void connectedToServer(Socket socket){
		System.out.println("Client successfully connected to server!");
		if (clientAuthenticate(socket)){
			System.out.println("SUCCESSFULLY AUTHENTICATED!");
		}else{
			System.out.println("AUTHENTICATION ERROR! Disconnecting..");
			disconnect();
		}
		
		/*clientRequestAddition(socket, 1,2);
		clientRequestSubtraction(socket, 1, 2);
		clientRequestMultiplication(socket, 1, 2);*/

	}
	
	/**
	 * Requests the authentication operation from server
	 * @param socket The connected socket reference
	 * @return Whether the authentication was successful.
	 */
	private boolean clientAuthenticate(Socket socket){
		Protocol.request(socket, Protocol.Operation.AUTHENTICATE, clientName, new Integer[]{});
		String replyMessage;
		while((replyMessage = readResult(socket))== null);
		JSONObject responseObj = Protocol.makeJsonObjectByString(replyMessage);
		String resultType = (String) responseObj.get("type");
		return resultType.equals("OK");
	}
	
	/**
	 * Requests the Addition operation to the server
	 * @param socket The connected socket reference
	 * @param arg0 The first Argument of addition
	 * @param arg1 The second Argument of addition
	 * @return Whether the request and computation was successful.
	 */
	public boolean clientRequestAddition(Socket socket, Integer arg0, Integer arg1){
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
	
	/**
	 * Requests the Subtraction operation to the server
	 * @param socket The connected socket reference
	 * @param arg0 The first Argument of subtraction
	 * @param arg1 The second Argument of subtraction
	 * @return Whether the request and computation was successful.
	 */
	public boolean clientRequestSubtraction(Socket socket, Integer arg0, Integer arg1){
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
	
	/**
	 * Requests the Multiplication operation to the server
	 * @param socket The connected socket reference
	 * @param arg0 The first Argument of multiplication
	 * @param arg1 The second Argument of multiplication
	 * @return Whether the request and computation was successful.
	 */
	public boolean clientRequestMultiplication(Socket socket, Integer arg0, Integer arg1){
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
	
	
	/**
	 * Requests arg0 lucas numbers from the server.
	 * @param socket The connected socket reference
	 * @param arg0 The number of lucas numbers to request
	 * @return whether the request was successful.
	 */
	public boolean clientRequestLucasNumbers(Socket socket, Integer arg0){
		Protocol.request(socket, Operation.LUCAS, clientName, arg0);
		String replyMessage;
		while((replyMessage = readResult(socket))== null);
		JSONObject responseObj = Protocol.makeJsonObjectByString(replyMessage);
		String resultType = (String) responseObj.get("type");
		if (!resultType.equals("OK")){
			return false;
		}else{
			Integer result = Integer.parseInt((String)responseObj.get("result"));
			System.out.println("Lucas Number result of "+arg0+": "+result);
			return true;
		}
	}
	
	/**
	 * Waits for result by server and returns the string.
	 * @param socket The connected Socket reference
	 * @return The reply message of the server
	 */
	
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
	

	
	
	public void rmiEcho(String msg){
		
		EchoTask echoTask = new EchoTask(msg);
		System.out.println("Sending RMI echo with msg "+msg);
		try {
			String result = remoteServer.executeTask(echoTask);
			System.out.println("Result of RMI Echo Task: "+result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rmiReverse(String msg){

		ReverseEchoTask revEchoTask = new ReverseEchoTask(msg);
		try {
			String result = remoteServer.executeTask(revEchoTask);
			System.out.println("Result of RMI Reverse Echo Task: "+result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rmiQuestion(String question){
		DeepThoughtTask questionTask = new DeepThoughtTask("Question of life, the universe and everything?");
		try {
			Boolean result = remoteServer.executeTask(questionTask);
			System.out.println("Result of RMI DeepThought Task: "+result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rmiPurpose(){
		PurposeTask task = new PurposeTask();
		try {
			String result = remoteServer.executeTask(task);
			System.out.println("Result of RMI Purpose Task: "+result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
