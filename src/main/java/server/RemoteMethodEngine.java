package server;

import java.rmi.RemoteException;

import rmiutils.RMITask;
import rmiutils.RemoteMethodServer;

public class RemoteMethodEngine implements RemoteMethodServer {

	@Override
	public <T> T executeTask(RMITask<T> t) throws RemoteException {
		return t.executeTask();
	}

}
