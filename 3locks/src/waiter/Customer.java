package waiter;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Customer implements Runnable {

    private WaiterMonitor waiter;
    private int noCustomerPair;
    private boolean firstInPair;

    public Customer(WaiterMonitor waiter, int customerPair, boolean firstInPair) {
        this.waiter = waiter;
        this.noCustomerPair = customerPair;
        this.firstInPair = firstInPair;
    }

    @Override
    public void run() {
        Random random = new Random();
       // while(true) {
            try {
                int personalMatters = random.nextInt(200);
                TimeUnit.MILLISECONDS.sleep(personalMatters);

                waiter.reserveTable(this.noCustomerPair, this.firstInPair);

                int eatingTime = random.nextInt(100);
                TimeUnit.MILLISECONDS.sleep(eatingTime);

                waiter.finishEating(this.noCustomerPair, this.firstInPair);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
       //}
    }
}
