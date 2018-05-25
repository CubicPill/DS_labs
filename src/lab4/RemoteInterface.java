package lab4;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface RemoteInterface extends Remote {
    boolean registerAccount(String username, String password) throws RemoteException;

    boolean[] subscribeToTopics(String[] topics);

    boolean login(String username, String password) throws RemoteException;

    String[] getTopicFeeds(String topic);

    boolean publishOnTopic(String topic, String content);
}
