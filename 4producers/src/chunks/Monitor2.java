package chunks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Monitor2 implements Monitor {
    private final Lock lock = new ReentrantLock();
    private Condition firstProducer = lock.newCondition();
    private Condition otherProducers = lock.newCondition();
    private Condition firstConsumer = lock.newCondition();
    private Condition otherConsumers = lock.newCondition();
    private Boolean waitingProducer = false;
    private Boolean waitingConsumer = false;
    private int buffer; // current number of elements in buffer
    private int bufferSize;
    private int producersNumber;
    private int consumersNumber;

    Monitor2(int bufferSize, int producersNumber, int consumersNumber) {
        this.buffer = 0;
        this.bufferSize = bufferSize;
        this.producersNumber = producersNumber;
        this.consumersNumber = consumersNumber;
    }


    public void put(int producerId, int chunksToPut){
        lock.lock();
        if(waitingProducer) {
            try {
                otherProducers.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(this.buffer + chunksToPut > this.bufferSize) {
            try {
				waitingProducer = true;
                this.firstProducer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.buffer += chunksToPut;
        waitingProducer = false;
        System.out.println("Producer " + producerId + " has put " + chunksToPut + " chunks into buffer [in buffer: " + buffer + "]");

        otherProducers.signal();
        firstConsumer.signal();
        lock.unlock();
    }

    public void get(int consumerId, int chunksToGet) {
        lock.lock();
        if(waitingConsumer) {
            try {
                otherConsumers.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(this.buffer - chunksToGet < 0) {
            try {
				waitingConsumer = true;
                firstConsumer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.buffer -= chunksToGet;
        waitingConsumer= false;
        System.out.println("Consumer " + consumerId + " has got " + chunksToGet + " chunks from buffer [in buffer: " + buffer + "]");

        otherConsumers.signal();
        firstProducer.signal();
        lock.unlock();
    }
}
