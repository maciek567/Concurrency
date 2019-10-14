package waiter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Pair {
    boolean firstInPair;  // if true first person in pair requested for table
    boolean secondInPair;  // if true second person in pair requested for table

    Pair(boolean firstInPair, boolean secondInPair) {
        this.firstInPair = firstInPair;
        this.secondInPair = secondInPair;
    }
}

public class WaiterMonitor {

    private boolean emptyTable;
    private List<Pair> customerList;
    private final Lock lock = new ReentrantLock();
    private List<Condition> waitForAnotherInPair;
    private final Condition freeTable = lock.newCondition();

    WaiterMonitor(int noCustomersPairs) {
        this.emptyTable = true;
        customerList = new ArrayList<>(noCustomersPairs);
        waitForAnotherInPair = new ArrayList<>(noCustomersPairs);
        Pair pair = new Pair(false, false);
        for(int customerPair=0; customerPair<noCustomersPairs; customerPair++) {
            customerList.add(pair);  // initially no customers have requested for table
            waitForAnotherInPair.add(customerPair, lock.newCondition());
        }
    }


    void reserveTable(int customerPair, boolean firstInPair) throws InterruptedException {  // one person in a pair is ready to eat
        lock.lock();
        if (firstInPair) {
            Pair pair = new Pair(true, this.customerList.get(customerPair).secondInPair);  // update value in array
            this.customerList.set(customerPair, pair);

            if(this.customerList.get(customerPair).secondInPair) {  // both in a pair are ready to take a table
                takeTable(customerPair);
                waitForAnotherInPair.get(customerPair).signal();
            } else {  // another in a pair is not ready so requesting must wait
                System.out.println("First person in pair: " + customerPair + " is waiting for partner");
                waitForAnotherInPair.get(customerPair).await();
            }

        } else {
            Pair pair = new Pair(this.customerList.get(customerPair).firstInPair, true);  // update value in array
            this.customerList.set(customerPair, pair);

            if(this.customerList.get(customerPair).firstInPair) {  // both in a pair are ready to take a table
                waitForAnotherInPair.get(customerPair).signal();
                takeTable(customerPair);
            }
            else {  // another in a pair is not ready so requesting must wait
                System.out.println("Second person in pair: " + customerPair + " is waiting for partner");
                waitForAnotherInPair.get(customerPair).await();
            }
        }
        lock.unlock();
    }

    void finishEating(int customerPair, boolean firstInPair) throws InterruptedException {  // one person in a pair has finished eating
        lock.lock();
        if (firstInPair) {
            Pair pair = new Pair(false, this.customerList.get(customerPair).secondInPair);  // update value in array
            this.customerList.set(customerPair, pair);
            if(!this.customerList.get(customerPair).secondInPair) {  // both in a pair has finished eating
                releaseTable(customerPair);
            }
        } else {
            Pair pair = new Pair(this.customerList.get(customerPair).firstInPair, false);  // update value in array
            this.customerList.set(customerPair, pair);
            if(!this.customerList.get(customerPair).firstInPair) {  // both in a pair has finished eating
                releaseTable(customerPair);
            }
        }
        lock.unlock();
    }

    private void takeTable(int jPair) throws InterruptedException {
        lock.lock();
        if(!this.emptyTable) System.out.println("Pair: " + jPair + " is ready and waiting for available table");
        while (!this.emptyTable) {
            freeTable.await();
        }
        this.emptyTable = false;
        System.out.println("Pair " + jPair + " has occupied table");
        lock.unlock();
    }

    private void releaseTable(int jPair) throws InterruptedException {
        lock.lock();
        this.emptyTable = true;
        System.out.println("Pair " + jPair + " has released table");
        freeTable.signalAll();
        lock.unlock();
    }

}
