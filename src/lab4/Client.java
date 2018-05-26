package lab4;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Scanner;
import javax.jms.*;

class Client {
    private static RemoteInterface stub = null;
    private static final Scanner s = new Scanner(System.in);
    private static ConnFactory cf = new ConnFactory();

    private Client() {

        try {
            Registry reg = LocateRegistry.getRegistry("localhost");
            stub = (RemoteInterface) reg.lookup("Server");
        } catch (Exception e) {
            System.err.println("Client exception thrown: " + e.toString());
            e.printStackTrace();
        }
    }


    public static void main(String args[]) {
        System.out.println("Select your option:\n1.Login\n2.Register");
        int opt = s.nextInt();
        if (opt != 1 && opt != 2) {
            System.out.println("Invalid option");
            System.exit(1);
        }
        Client c = new Client();

        if (opt == 1) {
            c.loginProcedure();
        } else {
            c.registerProcedure();
        }


    }

    private void registerProcedure() {
        // handle registration
        s.nextLine();
        System.out.print("Please enter your username: ");
        String usn = s.nextLine();
        System.out.print("Enter your password: ");
        String pwd = s.nextLine();
        System.out.print("Enter your password again: ");
        if (!pwd.equals(s.nextLine())) {
            System.out.println("Password mismatch!");
            return;
        }
        boolean result = false;
        System.out.println("Input the topics you ant to subscribe, use comma to separate each topic");
        String[] topics = s.nextLine().split(",");


        try {
            result = stub.registerAccount(usn, pwd);
            stub.subscribeToTopic(usn, topics);

        } catch (Exception e) {
            System.out.println("Remote method exception thrown: " + e.getMessage());
        }
        if (!result) {
            System.out.println("Failed to register!");
        } else {
            System.out.println("Register success!");
        }


    }

    private void loginProcedure() {
        // handle login
        s.nextLine();
        System.out.print("Please enter your username: \n");
        String usn = s.nextLine();
        System.out.print("Enter your password: \n");
        String pwd = s.nextLine();
        boolean result = false;
        try {
            result = stub.login(usn, pwd);
        } catch (Exception e) {
            System.out.println("Remote method exception thrown: " + e.getMessage());
        }
        if (!result) {
            System.out.println("Failed to login!");
        } else {
            System.out.println("Login success!");
            afterLoginProcedure(usn);
        }

    }

    private void afterLoginProcedure(String username) {
        System.out.println("Choose your option:");
        System.out.println("1. View subscribed topics.");
        System.out.println("2. Publish on a topic.");
        int choice = s.nextInt();
        if (choice == 1) {
            viewFeedProcedure(username);
        } else if (choice == 2) {
            publishOnTopicProcedure(username);
        } else {
            System.exit(1);
        }
    }

    private void viewFeedProcedure(String username) {

        System.out.println("Start getting yur feed...");
        try {
            String[] topics = stub.getTopicList(username);
            if (topics == null) {
                System.out.println("Not subscribed to any topics");
                afterLoginProcedure(username);
            }
            for (String t : topics) {
                Consumer c = new Consumer(cf.createConnection(), t, username + "_" + t, username + "_" + t);
                c.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error!");
            System.exit(1);
        }
    }

    private void publishOnTopicProcedure(String username) {
        System.out.println("Input the topic name you want to publish on:");
        String topic = s.next();

        System.out.println("Input the content:");
        s.nextLine();
        String content = s.nextLine();
        try {
            Producer p = new Producer(cf.createConnection(), topic, username);
            p.start();
            p.send(content);
            System.out.println("Success!");

        } catch (JMSException e) {
            System.out.println("Error!");
            e.printStackTrace();
            System.exit(1);
        }
        afterLoginProcedure(username);

    }


}
