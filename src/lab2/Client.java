package lab2;

import java.util.Scanner;

public class Client {
    private static RemoteInterface stub = null;
    private static Scanner s = new Scanner(System.in);

    public Client() {

        try {
            Registry reg = LocateRegistry.getRegistry("localhost",23333);
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
        try {
            result = stub.registerAccount(usn, pwd);
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
        }

    }

    private boolean register(String username, String password) {
        // call remote procedure to register
        boolean result = false;
        try {
            result = stub.registerAccount(username, password);
        } catch (Exception e) {
            System.out.println("Remote method exception thrown: " + e.getMessage());
        }
        return result;
    }

    private boolean login(String username, String password) {
        // call remote login function
        boolean result = false;

        try {
            result = stub.login(username, password);
        } catch (Exception e) {
            System.out.println("Remote method exception thrown: " + e.getMessage());
        }
        return result;
    }
}
