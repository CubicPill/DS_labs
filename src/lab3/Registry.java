package lab3;

import java.io.*;
import java.net.*;

public class Registry {
    private final String host;
    private final int port;

    public Registry(String ipAddr, int port) {
        host = ipAddr;
        this.port = port;
    }

    public Remote lookup(String serviceName) throws IOException {
        // open socket.
        // it assumes registry is already located by locate registry.
        // you should usually do try-catch here (and later).
        Socket soc = new Socket(host, port);


        // get TCP streams and wrap them.
        BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

        out.println("lookup");
        // it is locate request, with a service name.
        out.println(serviceName);


        // branch according to the answer.
        String res = in.readLine();
        RemoteObjectRef ror;

        if (res.equals("found")) {


            // receive ROR data, witout check.
            String rorIP = in.readLine();


            int rorPort = Integer.parseInt(in.readLine());


            int rorObjKey = Integer.parseInt(in.readLine());


            String rorInterfaceName = in.readLine();


            // make ROR.
            ror = new RemoteObjectRef(rorIP, rorPort, rorObjKey, rorInterfaceName);

        } else {

            ror = null;
        }

        // close the socket.
        soc.close();

        // return ROR.

        if (ror != null) {
            return (Remote) ror.localize();
        }
        return null;
    }


    public void rebind(String serviceName, RemoteObjectRef ror) throws IOException {
        // open socket. same as before.
        Socket soc = new Socket(host, port);

        // get TCP streams and wrap them.
        BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

        // it is a rebind request, with a service name and ROR.
        out.println("rebind");
        out.println(serviceName);
        out.println(ror.ip);
        out.println(ror.port);
        out.println(ror.objectKey);
        out.println(ror.interfaceName);

        // it also gets an ack, but this is not used.
        // Just ignore this
        in.readLine();

        // close the socket.
        soc.close();
    }
}
