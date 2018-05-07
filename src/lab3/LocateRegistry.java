package lab3;

import java.io.*;
import java.net.*;

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
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
