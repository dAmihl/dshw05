package rmiutils;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteMethodServer extends Remote {
    <T> T executeTask(RMITask<T> t) throws RemoteException;
}
