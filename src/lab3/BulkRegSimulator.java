package lab3;

public class BulkRegSimulator {
    public static void main(String args[]) throws InterruptedException {

        RegisterThread[] pool = new RegisterThread[Integer.parseInt(args[0])];
        for (int i = 0; i < pool.length; ++i) {
            pool[i] = new RegisterThread();
        }
        for (RegisterThread t : pool) {
            t.start();
        }
        int repeats = 10;
        System.out.println("All threads are up.");
        while (repeats > 0) {
            --repeats;
            Thread.sleep(1000);
        }
        for (RegisterThread t : pool) {
            t.stopThread();
        }
        for (RegisterThread t : pool) {
            while (t.isAlive()) {
                Thread.sleep(1);
                //wait until stopped
            }
        }
        int totalSum = 0;
        int totalSuccess = 0;
        for (RegisterThread t : pool) {
            totalSum += t.getTotalCount();
            totalSuccess += t.getTotalSuccess();
        }
        System.out.printf("%d/%d\n", totalSuccess, totalSum);


    }
}
