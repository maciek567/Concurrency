package shop;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Shop {
    public static void counterSemaphoreShopping() {
        int noBasket = 15;
        int noCustomers = 25;

        CounterSemaphore counterSemaphore = new CounterSemaphore(noBasket);

        Runnable runnableCustomer = () -> {
            Random rand = new Random();
            int shoppingTime = rand.nextInt(1000);
            counterSemaphore.lower(Thread.currentThread().getName());
            try {
                TimeUnit.MILLISECONDS.sleep(shoppingTime);  // time spent on shopping
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counterSemaphore.raise(Thread.currentThread().getName(), shoppingTime);
        };

        ExecutorService customers = Executors.newFixedThreadPool(noCustomers);
        for(int customer=0; customer<noCustomers; customer++) customers.submit(runnableCustomer);
        customers.shutdown();
    }
}
