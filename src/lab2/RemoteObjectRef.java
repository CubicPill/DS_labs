package lab2;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class RemoteObjectRef implements Serializable, Remote {
    final String ip;
    final int port;
    final long objectKey;
    final String interfaceName;

    public RemoteObjectRef(String ip, int port, long objectKey, String interfaceName) {
        this.ip = ip;
        this.port = port;
        this.objectKey = objectKey;
        this.interfaceName = interfaceName;

    }


    public Object localize() {
        //creates stub
        Class c;

        try {
            c = Class.forName(this.getClass().getPackage().getName() + "." + interfaceName + "Impl");

            Object o = c.newInstance();
            RORtbl.table.put(this, o);

            InvocationHandler handler = new RORInvocationHandler((Remote) o);
            return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), handler);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}