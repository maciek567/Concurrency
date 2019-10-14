package production;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageMain {

    public static void task1() {
        int producersNumber = 5;
        int consumersNumber = 10;
        int messagesNumber = 20;
        int bufferSize = 10;

        BoundedBuffer buffer = new BoundedBuffer(bufferSize, messagesNumber);
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
