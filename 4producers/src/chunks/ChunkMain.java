package chunks;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChunkMain {

    public static void task2() {

        //chooseMonitor(true);
        chooseMonitor(false);

    }

    private static void chooseMonitor(boolean monitor1) {
        PrintWriter printWriterP = null;
        PrintWriter printWriterC = null;
        try {
            FileWriter fileWriterP = new FileWriter("producersAccessTime");
            FileWriter fileWriterC = new FileWriter("consumersAccessTime");
            printWriterP = new PrintWriter(fileWriterP);
            printWriterC= new PrintWriter(fileWriterC);
            printWriterP.print("M,N,chunksToPut,accessTime\n");
            printWriterC.print("M,N,chunksToGet,accessTime\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] sizeOfM = {1000, 10000, 100000};
        int[] producersAndConsumersNumber = {10, 100, 1000};

        for(int M : sizeOfM){
            for(int N : producersAndConsumersNumber) {
                Monitor monitor;
                if(monitor1) monitor = new Monitor1(2*M, N, N);
                else monitor = new Monitor2(2*M, N, N);

                ExecutorService producers = Executors.newFixedThreadPool(N);
                ExecutorService consumers = Executors.newFixedThreadPool(N);

                for(int producerId=1; producerId<=N; producerId++) {
                    Producer producer = new Producer(monitor, M, N, producerId, printWriterP);
                    producers.submit(producer);
                }
                for(int consumerId=1; consumerId<=N; consumerId++) {
                    Consumer consumer = new Consumer(monitor, M, N, consumerId, printWriterC);
                    producers.submit(consumer);
                }

                try { // block main thread until all producers and consumers have finished their work or time exceeded
                    producers.awaitTermination(100, TimeUnit.MILLISECONDS);
                    System.out.println();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                producers.shutdown();
                consumers.shutdown();
            }
        }
        printWriterP.close();
        printWriterC.close();
    }

}
