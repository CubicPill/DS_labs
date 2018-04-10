package lab2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class RORInvocationHandler implements InvocationHandler {
    private Remote remote;

    public RORInvocationHandler(final Remote remote) {
        super();
        this.remote = remote;
        System.out.println(remote.getClass().getName());
        System.out.println(remote.getClass().getCanonicalName());
        System.out.println(remote.getClass().getSimpleName());

    }

    /**
     * proxy ： com.sun.proxy.$Proxy0 代理类 method: 接口中定义的方法对象 args： 接口方法入参 Object：
     * 方法调用返回结果
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        doBefore();

        System.out.println("proxy:" + proxy.getClass().getCanonicalName());
        System.out.println("method:" + method.getName() + ",returnType:" + method.getReturnType().getCanonicalName());
        /* result为方法调用返回结果 */
        // Object result = method.invoke(remote, args);
        // open socket. same as before.
        // Socket soc = new Socket("127.0.0.1", 1100);
        //
        // // get TCP streams and wrap them.
        // BufferedReader in = new BufferedReader(new
        // InputStreamReader(soc.getInputStream()));
        // PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
        //
        // // 将class\mothod\args传送到服务器
        // out.println("stub_invocation");
        //
        //
        // out.println(proxy.getClass().getInterfaces().toString());
        // out.println(proxy.getClass().getName());
        // out.println(method.getName());
        // int n = args.length;
        // out.println(n);
        // for (Object o: args){
        // out.println(o);
        // }
        // System.out.println("stub_invocation");
        // System.out.println(proxy.getClass().getInterfaces().toString());
        // System.out.println(proxy.getClass().getName());
        // System.out.println(method.getName());
        // System.out.println(n);
        // for (Object o: args){
        // System.out.println(o);
        // }
        //
        // // it also gets an ack, but this is not used.
        // String ack = in.readLine();
        // Boolean result = Boolean.getBoolean(ack);
        // close the socket.
        //
        //
        // soc.close();
        //
        // System.out.println("result's type :" +
        // result.getClass().getCanonicalName());
        // doAfter();

        Socket socket = new Socket();
        // socket.close()执行时 若数据没有发送完成则阻塞 10秒
        socket.setSoLinger(true, 10);
        socket.connect(new InetSocketAddress("127.0.0.1", 1100));
        try {
            // 往socket写数据
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            try {
                Iterator<Map.Entry<RemoteObjectRef, Object>> it = RORtbl.table.entrySet().iterator();
                Entry<RemoteObjectRef, Object> entry;
                RemoteObjectRef ror = null;
                while (it.hasNext()) {
                    entry = it.next();
                    if (entry.getValue() == this.remote) {
                        ror = entry.getKey();
                        break;
                    }
                }
                System.out.println(ror.className);
                output.writeUTF(ror.className);
                output.writeUTF(method.getName());
                output.writeObject(method.getParameterTypes());
                output.writeObject(args);

                // 读出数据
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                try {
                    Object result = input.readObject();
                    if (result instanceof Throwable) {
                        throw (Throwable) result;
                    }
                    return result;
                } finally {
                    input.close();
                }
            } finally {
                if (output != null) {
                    output.close();
                }
            }
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    protected void doBefore() {
        System.out.println("before:");
    }


}
