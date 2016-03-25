package utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Protocol {
	
	
	public static enum Operation{
		AUTHENTICATE,
		ADDITION,
		SUBTRACTION,
		MULTIPLICATION,
		LUCAS	
	}

	public static final int PORT = 1337;
	public static final String URL = "localhost";
	
	 
	/**
	 * The request method the client can call to send a request to the server
	 * @param clientSocket the socket on client which is connected to a server
	 * @param op The requested operation (addition, subtraction,multiplication, lucas)
	 * @param args The arguments (array of integers with arbitrary size)
	 */
	public static void request(Socket clientSocket, Protocol.Operation op, String clientName, Integer...args){
		sendMessage(clientSocket, makeJSONMessage(clientName, op, args));
	}
	
	
	/**
	 * The reply method the server can call to reply to the client
	 * @param serverSocket
	 */
	public static void reply(Socket serverSocket, String message){
		sendMessage(serverSocket, message);
	}
	
	
	private static void sendMessage(Socket socket, String message){
		OutputStream out = null;
		try {
			out = socket.getOutputStream();
			PrintStream printStream = new PrintStream(out, true);
			printStream.println(message);
			printStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			/*try {
				if (out != null){
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}
	
	public static String makeJSONMessage(String clientName, Operation op, Integer... args){
		JSONObject json = new JSONObject();
		json.put("name", clientName);
		json.put("operation", op.toString());
		Integer i = 0;
		for(Integer argument: args){
			String key = "arg"+(i++);
			json.put(key, argument);
		}
		return json.toJSONString();
	}
	
	public static JSONObject makeJsonObjectByString(String jsonString){
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e) {
			System.out.println("Error parsing JSON String");
			e.printStackTrace();
		}
		return obj;
	}
	
}
