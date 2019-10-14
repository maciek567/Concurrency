package waiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaiterMain {
    public static void task3() {
        int N = 10;  // number of pairs in restaurant

        WaiterMonitor waiterMonitor = new WaiterMonitor(N);
        ExecutorService customers = Executors.newFixedThreadPool(2*N);

        for(int i=0; i<N; i++) {
            Customer customer1 = new Customer(waiterMonitor, i, true);
            Customer customer2 = new Customer(waiterMonitor, i, false);
            customers.submit(customer1);
            customers.submit(customer2);
        }

        customers.shutdown();
    }
}
