package lab2;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

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
                Socket socket = serverSoc.accept();

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());


                String interfaceName = in.readUTF();
                String methodName = in.readUTF();
                Class<?>[] argTypes = (Class<?>[]) in.readObject();
                Object[] args = (Object[]) in.readObject();
                Object result;
                try {
                    result = this.obj.getClass().getDeclaredMethod(methodName, argTypes).invoke(this.obj, args);
                } catch (InvocationTargetException e) {
                    result = e.getCause();
                }
                out.writeObject(result);

            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();

        }
    }
}

