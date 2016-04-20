package rmiutils;

public interface IRemoteClientCallback extends java.rmi.Remote{ 
	
	 public String callback(String result) 
		      throws java.rmi.RemoteException;

}
