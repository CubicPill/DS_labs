package lab1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {
    boolean registerAccount(String username, String password) throws RemoteException;

    boolean login(String username, String password) throws RemoteException;
}
