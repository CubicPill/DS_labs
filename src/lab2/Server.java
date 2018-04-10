package lab2;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Server implements RemoteInterface {
    private Database db;
    private MessageDigest md;

    public Server() {
        db = new Database("jdbc:mysql://data-cn-0.vedbs.link:3306/vedbs_1374", "vedbs_1374", "LNwYp4wpuy");
        System.out.println("DB connection is up");
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Seems there's no MD5 in the environment...");
            System.exit(1);
        }
    }

    private String getMD5(String ori) {
        // get MD5 hash of string
        return new String(md.digest(ori.getBytes()));
    }

    public boolean registerAccount(String username, String password) throws RemoteException {
        // register account
        System.out.printf(">>> Register: %s\n", username);

        if (username.length() > 20) {
            System.out.println("<<< Username is too long (>20 chars)");
            return false;
        }
        String hashed = getMD5(password);

        if (db.saveAccountCredentials(username, hashed)) { // check if already registered
            System.out.printf("<<< Registration of %s success!\n", username);
            return true;
        }
        System.out.printf("<<< Registration of %s failed\n", username);
        return false;
    }

    public boolean login(String username, String password) throws RemoteException {
        // handle login request
        System.out.printf(">>> Login: %s\n", username);
        if (username.length() > 20) {
            System.out.println("<<< Username is too long (>20 chars)");
            return false;
        }
        String hashed = getMD5(password);
        if (hashed.equals(db.readAccountHashedPassword(username))) { // check if correct
            System.out.printf("<<< Login to %s success\n", username);
            return true;
        }
        System.out.printf("<<< Login to %s failed\n", username);
        return false;
    }

    public static void main(String args[]) {
        try {
            Server s = new Server();
            RemoteObjectRef stub = UnicastRemoteObject.exportObject(s, "127.0.0.1", 1100);
            Registry r = LocateRegistry.getRegistry("localhost", 23333);
            r.rebind("Server", stub);
            System.out.println("Server is up");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}