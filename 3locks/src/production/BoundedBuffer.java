package production;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BoundedBuffer {
    private final Lock lock = new ReentrantLock();
    private final Condition notFull  = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private final String[] items;
    private final int bufferSize;
    private final int maxMessages;
    private int putptr, takeptr, count, messageCount;

    public BoundedBuffer(int bufferSize, int maxMessages) {
        this.bufferSize = bufferSize;
        this.maxMessages = maxMessages;
        this.items = new String[bufferSize];
        putptr = 0; takeptr = 0; count = 0;
        messageCount = 1;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void put(String message, String threadName) throws InterruptedException {
        if(messageCount > maxMessages) return;
        lock.lock();
        try {
            while (count == items.length) {
                System.out.println("Thread: " + threadName + " Buffer is full - cannot add more messages");
                notFull.await();
            }
            items[putptr] = message;
            System.out.println("Thread " + threadName + " has put: " + message +
                    " to buffer on position: " + putptr);
            if (++putptr == items.length) putptr = 0;
            ++count;
            notEmpty.signal();
        } finally {

            lock.unlock();
        }
    }

    public String take(String threadName) throws InterruptedException {
        if(messageCount > maxMessages) return "";
        lock.lock();
        String message = "";
        try {
            while (count == 0) {
                System.out.println("Thread:"  + threadName +  " Buffer is empty - cannot take more messages");
                notEmpty.await();
            }
            message = items[takeptr];
            this.messageCount++;
            System.out.println("Thread " + threadName + " has taken: " + message +
                    " from buffer from position: " + takeptr);
            if (++takeptr == items.length) takeptr = 0;
            --count;
            notFull.signal();
            return message;
        } finally {
            lock.unlock();
        }
    }
}