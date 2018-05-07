package lab3;

public class RemoteInterfaceImpl implements RemoteInterface {

    @Override
    public boolean registerAccount(String username, String password) throws RemoteException {
        return false;
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        return false;
    }
}
