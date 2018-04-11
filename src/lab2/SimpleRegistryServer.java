package lab2;

import java.util.*;
import java.net.*;
import java.io.*;

// This is a simple registry server.
// The system does not do any error checking or bound checking.
// It uses the ROR as specified in the coursework sheet.

// protocol: 
//   (1) lookup  --> returns ROR.
//   (2) rebind --> binds ROR.
//   (3) whoareyou --> I am simple registry etc.
// it is used through SimpleRegistry and LocateSimpleRegistry.

class SimpleRegistryServer {

    public static void main(String args[]) throws IOException {
        // I do no checking. A user supplies one argument,
        // which is a port name for the registry
        // at the host in which it is running.
        int port;
        if (args.length > 1) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 23333;
        }
        // create a socket.
        ServerSocket serverSoc = new ServerSocket(port);

        // create a table of keys (service names) and ROR.
        Hashtable<String, RemoteObjectRef> serviceNameTable = new Hashtable<>();
        System.out.println("SimpleRegistryServer is up");

        while (true) {
            Socket socket = serverSoc.accept();


            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);


            String command = in.readLine();
            switch (command) {
                case "lookup": {

                    String serviceName = in.readLine();
                    System.out.println("lookup " + serviceName);


                    if (serviceNameTable.containsKey(serviceName)) {

                        RemoteObjectRef ror = serviceNameTable.get(serviceName);


                        out.println("found");
                        out.println(ror.ip);
                        out.println(Integer.toString(ror.port));
                        out.println(Long.toString(ror.objectKey));
                        out.println(ror.interfaceName);

                    } else {

                        out.println("not found");
                    }
                    break;
                }
                case "rebind": {

                    // again no error check.
                    String serviceName = in.readLine();

                    System.out.println("rebind " + serviceName);
                    // get ROR data.
                    // I do not serialise.
                    // Go elementary, that is my slogan.


                    String rorIPAddr = in.readLine();
                    int rorPort = Integer.parseInt(in.readLine());
                    int rorObjKey = Integer.parseInt(in.readLine());
                    String rorInterfaceName = in.readLine();


                    // make ROR.
                    RemoteObjectRef ror = new RemoteObjectRef(rorIPAddr, rorPort, rorObjKey, rorInterfaceName);

                    // put it in the table.
                    serviceNameTable.remove(serviceName);
                    serviceNameTable.put(serviceName, ror);


                    // ack.
                    out.println("bound");
                    break;
                }
                case "who are you?":
                    out.println("I am a simple registry.");
                    break;
                default:
                    System.err.println("Unrecognized command " + command);
                    break;
            }

            socket.close();
        }
    }
}
