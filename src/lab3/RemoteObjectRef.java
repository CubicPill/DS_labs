package lab3;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class RemoteObjectRef {
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
            // get RemoteInterfaceImpl
            Object o = c.newInstance();

            InvocationHandler handler = new RORInvocationHandler(this);
            // create InvocationHandler, return proxy object
            return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), handler);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}