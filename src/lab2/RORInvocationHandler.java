package lab2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

class RORInvocationHandler implements InvocationHandler {
    private final Remote remote;

    public RORInvocationHandler(final Remote remote) {
        super();
        this.remote = remote;

    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


        Socket socket = new Socket();
        socket.setSoLinger(true, 10);
        socket.connect(new InetSocketAddress("127.0.0.1", 1100));

        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());


        RemoteObjectRef ror = RORtbl.findROR(this.remote);


        if (ror == null) {
            throw new RemoteException();
        }
        output.writeUTF(ror.interfaceName);
        output.writeUTF(method.getName());
        output.writeObject(method.getParameterTypes());
        output.writeObject(args);


        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        Object result = input.readObject();
        if (result instanceof Throwable) {
            throw (Throwable) result;
        }
        return result;


    }
}