package lab3;

public class BulkRegSimulator {
    public static void main(String args[]) {

        RegisterThread[] pool = new RegisterThread[Integer.parseInt(args[0])];
        for (int i = 0; i < pool.length; ++i) {
            pool[i] = new RegisterThread();
        }
        for (RegisterThread t : pool) {
            t.start();
        }
        System.out.println("All threads are up.");
        while (true) {
            int totalSum = 0;
            int totalSuccess = 0;
            for (RegisterThread t : pool) {
                totalSum += t.getTotalCount();
                totalSuccess += t.getTotalSuccess();
            }
            System.out.printf("%d/%d\n", totalSuccess, totalSum);
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {

            }
        }

    }
}
