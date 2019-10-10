package production;

import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {
    private Buffer buffer;
    private final int MESSAGES_NUMBER;


    public Consumer(Buffer buffer, int number) {
        this.buffer = buffer;
        this.MESSAGES_NUMBER = number;
}

    public void run(){
        while(buffer.getMessageNumber() <= MESSAGES_NUMBER) {
            synchronized (this) {
                String message = buffer.take(Thread.currentThread().getName());
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);  // because of that another thread can take next message
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyAll(); // thanks to this program will always terminate
    }
}
