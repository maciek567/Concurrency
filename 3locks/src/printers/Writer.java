package printers;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Writer implements Runnable{

    private PrintersMonitor printersMonitor;
    private int writerId;
    private final Lock lock = new ReentrantLock();

    Writer(PrintersMonitor printersMonitor, int writerId) {
        this.printersMonitor = printersMonitor;
        this.writerId = writerId;
    }

    @Override
    public void run() {
        Random random = new Random();
      //  while(true) {
            try {
                int create_task = random.nextInt(100);
                TimeUnit.MILLISECONDS.sleep(create_task);

                int printer_id = printersMonitor.reserve();

                print(printer_id);

                printersMonitor.release(printer_id);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
       // }
    }

    private void print(int printer_id) throws InterruptedException {
        Random random = new Random();
        int printing_time = random.nextInt(100);
        TimeUnit.MILLISECONDS.sleep(printing_time);
        lock.lock();
        System.out.println("Printer: " + printer_id + " is printing materials from writer: " +
                this.writerId);
        lock.unlock();
    }
}
