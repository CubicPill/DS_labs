package lab4;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface RemoteInterface extends Remote {
    boolean registerAccount(String username, String password) throws RemoteException;

    String[] getTopicList(String username) throws RemoteException;

    void subscribeToTopic(String username, String[] topics) throws RemoteException;

    boolean login(String username, String password) throws RemoteException;


}
