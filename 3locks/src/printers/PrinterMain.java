package printers;

import production.Consumer;
import production.Producer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrinterMain {
    public static void task2() {
        int N = 20;  // number of writers
        int M = 5;  // number of printers

        PrintersMonitor printersMonitor = new PrintersMonitor(M);
        ExecutorService writers = Executors.newFixedThreadPool(N);

        for(int i=0; i<N; i++) {
            Writer writer = new Writer(printersMonitor, i);
            writers.submit(writer);
        }

        writers.shutdown();
    }
}
