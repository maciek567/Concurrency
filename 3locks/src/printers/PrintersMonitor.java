package printers;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintersMonitor {

    private int noPrinters;  // number of printers
    private Hashtable<Integer, Boolean> printers;  // true if printer is available
    private final Lock lock = new ReentrantLock();
    private final Condition freePrinter = lock.newCondition();

    PrintersMonitor(int M) {
        this.noPrinters = M;
        this.printers = new Hashtable<>();
        for(int printer=0; printer<M; printer++){
          printers.put(printer, true);
        }

    }

    int reserve() throws InterruptedException {
        int emptyPrinterID = -1;
        lock.lock();
//        for (Map.Entry<Integer, Boolean> entry : printers.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }
//        System.out.println();
        while(emptyPrinterID == -1) {  // while there is no available printers
            for (Map.Entry<Integer, Boolean> printer : printers.entrySet()) {
                if (printer.getValue()) {  // if printer is available return its id
                    emptyPrinterID = printer.getKey();
                    printer.setValue(false);
                    break;
                }
            }
            if(emptyPrinterID == -1) {
                freePrinter.await();
            }
        }

        lock.unlock();
        return emptyPrinterID;
    }

    void release(int printer_id) {
        lock.lock();
        this.printers.put(printer_id, true);
        System.out.println("Printer: " + printer_id + " has been released");
        freePrinter.signal();
        lock.unlock();
    }
}
