package rmiutils;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientCallback extends UnicastRemoteObject implements IRemoteClientCallback{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RMIClientCallback() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String callback(String result) throws RemoteException {
		String returnMessage = "Callback received. Result is: "+result;
		System.out.println(returnMessage);
		return returnMessage;
	}

}
