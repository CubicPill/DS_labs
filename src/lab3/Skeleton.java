package lab3;

import java.io.*;
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
                    SkeletonRequestHandler handler = new SkeletonRequestHandler(socket, this.obj);
                    handler.start();
                } catch (SocketException ignored) {
                    // ignore connection reset by other side
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

