package chunks;

import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer implements Runnable {

    private final Lock lock = new ReentrantLock();
    private Monitor monitor;
    private int M; // maximum number of produced elements put int buffer at once
    private int consumersNumber;
    private int consumerId;
    private PrintWriter printWriter;

    Consumer(Monitor monitor, int M, int consumersNumber, int consumerId, PrintWriter printWriter) {
        this.monitor = monitor;
        this.M = M;
        this.consumersNumber = consumersNumber;
        this.consumerId = consumerId;
        this.printWriter =  printWriter;
    }

    @Override
    public void run() {
        Random random = new Random();
        int chunksToGet = random.nextInt(this.M) + 1;
        long initialTime = System.nanoTime();
        this.monitor.get(this.consumerId, chunksToGet);
        long timeAfterAccessToBuffer = System.nanoTime();
        long timeDifference = (timeAfterAccessToBuffer - initialTime) / 1000;
        System.out.println("Consumer " + this.consumerId + " has accessed buffer after " + timeDifference + " us"
                + " getting: " + chunksToGet + " chunks");

        lock.lock();
        // (M - 1/2 of buffer size, number of producers, chunksToGet time difference)
        printWriter.printf("%d,%d,%d,%d\n", this.M, this.consumersNumber, chunksToGet, timeDifference);
        lock.unlock();
    }


}
