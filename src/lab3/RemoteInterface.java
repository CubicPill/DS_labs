package lab3;


interface RemoteInterface extends Remote {
    boolean registerAccount(String username, String password) throws RemoteException;

    boolean login(String username, String password) throws RemoteException;
}
