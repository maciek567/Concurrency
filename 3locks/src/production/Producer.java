package production;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Producer implements Runnable {
    private BoundedBuffer buffer;
    private final int MESSAGES_NUMBER;
    private final Lock lock = new ReentrantLock();

    public Producer(BoundedBuffer buffer, int number) {
        this.buffer = buffer;
        this.MESSAGES_NUMBER = number;
    }

    public void run() {
        //for(int i=1; i<=this.MESSAGES_NUMBER; i++) {
        while(buffer.getMessageCount() <= this.MESSAGES_NUMBER) {
            try {
                lock.lock();
                buffer.put("message_" + buffer.getMessageCount(), Thread.currentThread().getName());
                lock.unlock();
                TimeUnit.MILLISECONDS.sleep(10);  // because of that another thread can put next message
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
