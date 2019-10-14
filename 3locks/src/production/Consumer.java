package production;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer implements Runnable {
    private BoundedBuffer buffer;
    private final int MESSAGES_NUMBER;
    private final Lock lock = new ReentrantLock();


    public Consumer(BoundedBuffer buffer, int number) {
        this.buffer = buffer;
        this.MESSAGES_NUMBER = number;
    }

    public void run(){
        //for(int i=1; i<=this.MESSAGES_NUMBER; i++) {
        while(buffer.getMessageCount() <= this.MESSAGES_NUMBER) {
            try {
                lock.lock();
                String message = buffer.take(Thread.currentThread().getName());
                lock.unlock();
                TimeUnit.MILLISECONDS.sleep(10);  // because of that another thread can take next message
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
