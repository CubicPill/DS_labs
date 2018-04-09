package lab2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RORInvocationHandler implements InvocationHandler {
    Remote remoteObj;

    public RORInvocationHandler(Remote o) {
        remoteObj = o;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // talk to remote server here
        System.out.println("Method:" + method);

        return null;
    }
}
