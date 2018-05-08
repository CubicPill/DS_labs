package lab3;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Objects;
import java.util.Random;

public class RegisterThread extends Thread {
    private static RemoteInterface stub = null;
    private static final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private boolean stopSignal = false;
    private int totalCount;
    private int totalSuccess;

    public void stopThread() {
        this.stopSignal = true;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalSuccess() {
        return totalSuccess;
    }

    public RegisterThread() {
        try {
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", 23333);
            stub = (RemoteInterface) Objects.requireNonNull(reg).lookup("Server");

        } catch (Exception e) {
            System.err.println("Client exception thrown: " + e.toString());
            e.printStackTrace();
        }
    }

    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    @Override
    public void run() {
        while (!stopSignal) {
            try {
                stub.registerAccount(getRandomString(10), "testregister");
                totalSuccess += 1;
            } catch (UndeclaredThrowableException e) {
                if(e.getCause().getClass().getName().equals("java.net.ConnectException")){
                    System.err.println(String.format("%-12s get ConnectException", this.getName()));

                }else{
                    System.err.println(String.format("%-12s get UndeclaredThrowableException", this.getName()));

                }

            } catch (RemoteException e) {
                System.err.println(String.format("%-12s get RemoteException", this.getName()));

            } catch (Exception e) {
                System.err.println(String.format("%-12s get %s", this.getName(), e.getClass().getName()));
                e.printStackTrace();
                System.exit(1);
            } finally {
                totalCount += 1;

            }
        }

    }
}
