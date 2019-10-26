package pipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Monitor {
    private final Lock lock;
    private List<Integer> buffer;
    private int bufferSize;
    private int workersNumber;
    private List<Integer> howManyCanProcess; // how many elements in buffer can currently worker process
    private List<Condition> conditions;

    Monitor(List<Integer> buffer, int bufferSize, int workersNumber) {
        this.lock= new ReentrantLock();
        this.buffer = buffer;
        this.bufferSize = bufferSize;
        this.workersNumber = workersNumber;
        this.howManyCanProcess = new ArrayList<>(workersNumber);
        this.conditions = new ArrayList<>(workersNumber);

        howManyCanProcess.add(0, bufferSize); // producer can process all elements in buffer
        conditions.add(0, lock.newCondition());
        for(int workerId=1; workerId<workersNumber; workerId++) {
            // all others must wait until producer and previous workers process that element
            howManyCanProcess.add(workerId, 0);
            conditions.add(workerId, lock.newCondition());
        }


    }

    void take(int workerId, int currentlyProcessedElement) throws InterruptedException {
        lock.lock();
        if(howManyCanProcess.get(workerId) == 0) {
            conditions.get(workerId).await();
        }
        howManyCanProcess.set(workerId, howManyCanProcess.get(workerId)-1);
        System.out.println("Worker " + workerId + " has taken buffer element " + currentlyProcessedElement);
        lock.unlock();
    }

    void give(int workerId, int currentlyProcessedElement) {
        lock.lock();
        // inform next worker that can process currently released element
        howManyCanProcess.set((workerId + 1) % this.workersNumber,
                howManyCanProcess.get((workerId + 1) % this.workersNumber) + 1 );
        conditions.get((workerId + 1) % this.workersNumber).signal();
        System.out.println("Worker " + workerId + " has released buffer element " + currentlyProcessedElement);
        System.out.print("Buffer: ");
        for(int i=0; i<bufferSize; i++) {
            System.out.print(i + ":" + this.buffer.get(i) + " ");
        }
        System.out.println();
        lock.unlock();
    }
}
