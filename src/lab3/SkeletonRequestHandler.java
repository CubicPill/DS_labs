package lab3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class SkeletonRequestHandler extends Thread {
    Socket socket;
    Object obj;

    public SkeletonRequestHandler(Socket socket, Object originalObject) {
        this.socket = socket;
        this.obj = originalObject;
    }

    @Override
    public void run() {
        try {
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
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
