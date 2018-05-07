package lab3;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

class Skeleton extends Thread {
    private final Object obj;
    private final RemoteObjectRef ror;

    public Skeleton(Object originalObject, RemoteObjectRef ror) {
        super();
        obj = originalObject;
        this.ror = ror;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSoc = new ServerSocket(ror.port);
            while (true) {
                try {
                    Socket socket = serverSoc.accept();

                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());


                    String interfaceName = in.readUTF();
                    //read interface name
                    String methodName = in.readUTF();
                    // method name
                    Class<?>[] argTypes = (Class<?>[]) in.readObject();
                    // argument types
                    Object[] args = (Object[]) in.readObject();
                    // args
                    Object result;
                    try {
                        Class c = this.obj.getClass();
                        assert Class.forName(interfaceName).isAssignableFrom(c);
                        // verify interface is the same
                        result = c.getDeclaredMethod(methodName, argTypes).invoke(this.obj, args);
                        // get result
                    } catch (InvocationTargetException e) {
                        result = e.getCause();
                        // if exception thrown, return the exception
                    }
                    out.writeObject(result);

                } catch (SocketException ignored) {

                }
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();

        }
    }
}

