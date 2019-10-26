package chunks;

import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Producer implements Runnable {

    private final Lock lock = new ReentrantLock();
    private Monitor monitor;
    private int M; // maximum number of produced elements put int buffer at once
    private int producersNumber;
    private int producerId;
    private PrintWriter printWriter;

    Producer(Monitor monitor, int M, int producersNumber, int producerId, PrintWriter printWriter) {
        this.monitor = monitor;
        this.M = M;
        this.producersNumber = producersNumber;
        this.producerId = producerId;
        this.printWriter =  printWriter;
    }

    @Override
    public void run() {
        Random random = new Random();
        int chunksToPut = random.nextInt(this.M) + 1;
        long initialTime = System.nanoTime();
        this.monitor.put(this.producerId, chunksToPut);
        long timeAfterAccessToBuffer = System.nanoTime();
        long timeDifference = (timeAfterAccessToBuffer - initialTime) / 1000;
        System.out.println("Producer " + this.producerId + " has accessed buffer after " + timeDifference + " us"
            + " putting: " + chunksToPut + " chunks");

        lock.lock();
        // (M - 1/2 of buffer size, number of producers, chunksToPut, time difference)
        printWriter.printf("%d,%d,%d,%d\n", this.M, this.producersNumber, chunksToPut, timeDifference);
        lock.unlock();
    }


}
