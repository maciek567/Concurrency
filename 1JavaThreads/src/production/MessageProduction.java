package production;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageProduction {

    public static void task2() {
        int producersNumber = 40;
        int consumersNumber = 89;
        int messagesNumber = 210;

        Buffer buffer = new Buffer(messagesNumber);
        Producer producer = new Producer(buffer, messagesNumber);
        Consumer consumer = new Consumer(buffer, messagesNumber);

        ExecutorService producers = Executors.newFixedThreadPool(producersNumber);
        ExecutorService consumers = Executors.newFixedThreadPool(consumersNumber);
        for(int i=0; i<producersNumber; i++) producers.submit(producer);
        for(int i=0; i<consumersNumber; i++) consumers.submit(consumer);

        producers.shutdown();
        consumers.shutdown();
    }
}
