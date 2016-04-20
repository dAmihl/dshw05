package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PushbackInputStream;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Protocol {
	
	/**
	 * Enum for the client to send requests of operations
	 * The server interpretes the messages using these enums.
	 * @author dAmihl
	 *
	 */
	public static enum Operation{
		AUTHENTICATE,
		ADDITION,
		SUBTRACTION,
		MULTIPLICATION,
		LUCAS	
	}
	
	/**
	 * Enum for the server to send replys to clients.
	 * The client uses this for error handling.
	 * @author dAmihl
	 *
	 */
	public static enum REPLY_TYPE{
		OK,
		ERROR
	}

	/**
	 * PORT and URL of the server
	 */
	public static final int PORT = 1337;
	public static final String URL = "localhost";
	public static final String RMI_NAME = "RemoteComputation";
	public static final Integer RMI_PORT = 1099;
	
	 
	/**
	 * The request method the client can call to send a request to the server
	 * @param clientSocket the socket on client which is connected to a server
	 * @param op The requested operation (addition, subtraction,multiplication, lucas)
	 * @param args The arguments (array of integers with arbitrary size)
	 */
	public static void request(Socket clientSocket, Protocol.Operation op, String clientName, Integer...args){
		sendMessage(clientSocket, makeClientJSONMessage(clientName, op, args));
	}
	
	
	/**
	 * The reply method the server can call to reply to the client
	 * @param serverSocket
	 */
	public static void reply(Socket serverSocket, REPLY_TYPE type, String message){
		sendMessage(serverSocket, makeServerJSONMessage(type, message));
	}
	
	
	/**
	 * Writes a message to the output stream of the given socket.
	 * @param socket The given socket to write to.
	 * @param message The message to write.
	 */
	private static void sendMessage(Socket socket, String message){
		OutputStream out = null;
		try {
			out = socket.getOutputStream();
			PrintStream printStream = new PrintStream(out, true);
			printStream.println(message);
			printStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Constructs a JSON message the server can send to clients.
	 * @param type The reply type: OK or ERROR
	 * @param message The message (or computation result if OK)
	 * @return the JSON message
	 */
	public static String makeServerJSONMessage(REPLY_TYPE type, String message){
		JSONObject json = new JSONObject();
		json.put("type", type.toString());
		json.put("result", message);
		return json.toJSONString();
	}
	
	/**
	 * Constructs a JSON message the client can send to a server
	 * @param clientName the clients name
	 * @param op The requested operation
	 * @param args The arguments
	 * @return The json message
	 */
	public static String makeClientJSONMessage(String clientName, Operation op, Integer... args){
		JSONObject json = new JSONObject();
		json.put("name", clientName);
		json.put("operation", op.toString());
		Integer i = 0;
		for(Integer argument: args){
			String key = "arg"+(i++);
			json.put(key, argument.toString());
		}
		return json.toJSONString();
	}
	
	
	/**
	 * Parses a JSON object from a given string. Used by receiving message
	 * from server or client
	 * @param jsonString the received json string.
	 * @return the parsed json object.
	 */
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
	
	
	/**
	 * Checks if input stream can be read.
	 */
	public static InputStream canReadInputStream(InputStream stream){
	    PushbackInputStream pushbackInputStream = new PushbackInputStream(stream);
	    int b;
		try {
			b = pushbackInputStream.read();
		} catch (IOException e) {
			System.out.println("Could not read from input stream.");
			e.printStackTrace();
			return null;
		}
	    if (b==-1){
	    	return null;
	    }else{
	    	try {
				pushbackInputStream.unread(b);
				return pushbackInputStream;
			} catch (IOException e) {
				System.out.println("Could not unread input stream. Data is now corrupted!");
				e.printStackTrace();
				return null;
			}
	    }
	}
	
}
