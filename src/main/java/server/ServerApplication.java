package server;

import org.json.simple.JSONObject;

public class ServerApplication {

	public static void main(String[] args) {
System.out.println("I am the server!");
JSONObject obj = new JSONObject();

obj.put("name", "foo");
obj.put("num", new Integer(100));
obj.put("balance", new Double(1000.21));
obj.put("is_vip", new Boolean(true));

System.out.print(obj);
	}

}
