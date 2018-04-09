package lab2;

import java.net.*;
import java.io.*;

public class Registry {
    // registry holds its port and host, and connects to it each time.
    String host;
    int port;

    // ultra simple constructor.
    public Registry(String IPAdr, int PortNum) {
        host = IPAdr;
        port = PortNum;
    }

    // returns the stub (if ror found and localization successful) or null (if else)
    public Remote lookup(String serviceName) throws IOException {
        // open socket.
        // it assumes registry is already located by locate registry.
        // you should usually do try-catch here (and later).
        Socket soc = new Socket(host, port);

        System.out.println("socket made.");

        // get TCP streams and wrap them.
        BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

        System.out.println("stream made.");

        // it is locate request, with a service name.
        out.println("lookup");
        out.println(serviceName);

        System.out.println("command and service name sent.");

        // branch according to the answer.
        String res = in.readLine();
        RemoteObjectRef ror;

        if (res.equals("found")) {

            System.out.println("it is found!.");

            // receive ROR data, witout check.
            String ro_IPAdr = in.readLine();

            System.out.println(ro_IPAdr);

            int ro_PortNum = Integer.parseInt(in.readLine());

            System.out.println(ro_PortNum);

            int ro_ObjKey = Integer.parseInt(in.readLine());

            System.out.println(ro_ObjKey);

            String ro_InterfaceName = in.readLine();

            System.out.println(ro_InterfaceName);

            // make ROR.
            ror = new RemoteObjectRef(ro_IPAdr, ro_PortNum, ro_ObjKey, ro_InterfaceName);

        } else {
            System.out.println("it is not found!.");

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

    // rebind a ROR. ROR can be null. again no check, on this or whatever.
    // I hate this but have no time.
    public void rebind(String serviceName, RemoteObjectRef ror) throws IOException {
        // open socket. same as before.
        Socket soc = new Socket(host, port);

        // get TCP streams and wrap them.
        BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

        // it is a rebind request, with a service name and ROR.
        out.println("rebind");
        out.println(serviceName);
        out.println(ror.IP_adr);
        out.println(ror.Port);
        out.println(ror.Obj_Key);
        out.println(ror.Remote_Interface_Name);

        // it also gets an ack, but this is not used.
        String ack = in.readLine();

        // close the socket.
        soc.close();
    }
}
