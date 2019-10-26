package chunks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Monitor1 implements Monitor {
    private final Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();
    private int buffer; // current number of elements in buffer
    private int bufferSize;
    private int producersNumber;
    private int consumersNumber;

    Monitor1(int bufferSize, int producersNumber, int consumersNumber) {
        this.buffer = 0;
        this.bufferSize = bufferSize;
        this.producersNumber = producersNumber;
        this.consumersNumber = consumersNumber;
    }


    public void put(int producerId, int chunksToPut){
        lock.lock();
        while(this.buffer + chunksToPut > this.bufferSize) {
            try {
                this.notFull.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.buffer += chunksToPut;
        System.out.println("Producer " + producerId + " has put " + chunksToPut + " chunks into buffer [in buffer: " + buffer + "]");
        notEmpty.signal();
        lock.unlock();
    }

    public void get(int consumerId, int chunksToGet) {
        lock.lock();
        while(this.buffer - chunksToGet < 0) {
            try {
                notEmpty.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.buffer -= chunksToGet;
        System.out.println("Consumer " + consumerId + " has got " + chunksToGet + " chunks from buffer [in buffer: " + buffer + "]");
        notFull.signal();
        lock.unlock();
    }
}
