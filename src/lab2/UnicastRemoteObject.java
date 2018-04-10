package lab2;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class UnicastRemoteObject {
    public static RemoteObjectRef exportObject(Object s, int port) throws RemoteException {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return exportObject(s, addr.getHostAddress(), port);
        } catch (UnknownHostException e) {
            throw new RemoteException();
        }
    }

    public static RemoteObjectRef exportObject(Object obj, String ip, int port) throws RemoteException {

        RemoteObjectRef ror = new RemoteObjectRef(ip, port, 114514L, obj.getClass().getInterfaces()[0].getSimpleName());
        Skeleton skeleton = new Skeleton(obj, ror);
        skeleton.setDaemon(false);
        skeleton.start();

        return ror;
    }
}
