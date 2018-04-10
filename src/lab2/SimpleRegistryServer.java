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
        System.out.println("server socket created.\n");

        // create a table of keys (service names) and ROR.
        Hashtable<String, RemoteObjectRef> table = new Hashtable<>();

        // loop: accept, receive request, reply, close.
        // again no error checking: this is not robust at all.
        // it does not reuse connection.
        // moreover there is no concurrency: this is a bad
        // server programming.
        // in any way.

        while (true) {
            // create new connections.
            Socket newsoc = serverSoc.accept();

            System.out.println("accepted the request.");

            // input/output streams (remember, TCP is bidirectional).
            BufferedReader in = new BufferedReader(new InputStreamReader(newsoc.getInputStream()));
            PrintWriter out = new PrintWriter(newsoc.getOutputStream(), true);

            // get a line. this should be a command:
            // (1) lookup servicename --> ["found", ROR data] or ["not found"]
            // (2) rebound servicename ROR --> ["bound"]
            // (3) who are you? --> I am a simple registry.

            String command = in.readLine();
            // branch: commands are either lookup or rebind.
            switch (command) {
                case "lookup": {
                    System.out.println("it is lookup request.");

                    String serviceName = in.readLine();

                    System.out.println("The service name is " + serviceName + ".");

                    // tests if it is in the table.
                    // if it is gets it.
                    if (table.containsKey(serviceName)) {
                        System.out.println("the service found.");

                        RemoteObjectRef ror = table.get(serviceName);

                        System.out.println("ROR is " + ror.ip + "," + ror.port + "," + ror.objectKey + ","
                                + ror.interfaceName + ".");

                        out.println("found");
                        out.println(ror.ip);
                        out.println(Integer.toString(ror.port));
                        out.println(Long.toString(ror.objectKey));
                        out.println(ror.interfaceName);

                        System.out.println("ROR was sent.\n");
                    } else {
                        System.out.println("the service not found.\n");

                        out.println("not found");
                    }
                    break;
                }
                case "rebind": {
                    System.out.println("it is rebind request.");

                    // again no error check.
                    String serviceName = in.readLine();

                    System.out.println("the service name is " + serviceName + ".");

                    // get ROR data.
                    // I do not serialise.
                    // Go elementary, that is my slogan.

                    System.out.println("I got the following ror:");

                    String IP_adr = in.readLine();
                    int Port = Integer.parseInt(in.readLine());
                    int Obj_Key = Integer.parseInt(in.readLine());
                    String Remote_Interface_Name = in.readLine();

                    System.out.println("IP address: " + IP_adr);
                    System.out.println("port num:" + Port);
                    System.out.println("object key:" + Obj_Key);
                    System.out.println("Interface Name:" + Remote_Interface_Name);

                    // make ROR.
                    RemoteObjectRef ror = new RemoteObjectRef(IP_adr, Port, Obj_Key, Remote_Interface_Name);

                    // put it in the table.
                    table.remove(serviceName);
                    Object res = table.put(serviceName, ror);

                    System.out.println("ROR is put in the table.\n");

                    // ack.
                    out.println("bound");
                    break;
                }
                case "who are you?":
                    out.println("I am a simple registry.");
                    System.out.println("I was asked who I am, so I answered.\n");
                    break;
                default:
                    System.out.println("I got an imcomprehensive message.\n");
                    break;
            }

            // close the socket.
            newsoc.close();
        }
    }
}
