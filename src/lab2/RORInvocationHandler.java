package lab2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RORInvocationHandler implements InvocationHandler {
    Remote remoteObj = null;

    public RORInvocationHandler(Remote o) {
        remoteObj = o;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
