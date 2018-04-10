package lab2;

import java.net.*;
import java.io.*;

class LocateRegistry {


    public static Registry getRegistry(String host, int port) {
        // open socket.
        try {
            Socket soc = new Socket(host, port);

            // get TCP streams and wrap them.
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

            // ask.
            out.println("who are you?");

            // gets answer.
            if ((in.readLine()).equals("I am a simple registry.")) {
                return new Registry(host, port);
            } else {
                System.out.println("somebody is there but not a  registry!");
                return null;
            }
        } catch (Exception e) {
            System.out.println("nobody is there!" + e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
