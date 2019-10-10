package production;

import java.util.concurrent.TimeUnit;

public class Producer implements Runnable {
    private Buffer buffer;
    private final int MESSAGES_NUMBER;

    public Producer(Buffer buffer, int number) {
        this.buffer = buffer;
        this.MESSAGES_NUMBER = number;
    }

    public void run() {
        while(buffer.getMessageNumber() <= MESSAGES_NUMBER) {
            synchronized (this) {
                if(buffer.getMessageNumber() <= MESSAGES_NUMBER) // condition in while loop could fulfil several threads simultaneously
                    buffer.put("message " + buffer.getMessageNumber(), Thread.currentThread().getName());
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);  // because of that another thread can put next message
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyAll(); // thanks to this program will always terminate
    }
}
