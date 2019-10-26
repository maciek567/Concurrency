package pipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PipeMain {

    public static void task1() {

        int bufferSize = 10;
        int processorsNumber = 3;
        List<Integer> buffer = new ArrayList<>(bufferSize);
        for(int bufferElement=0; bufferElement<bufferSize; bufferElement++) {
            buffer.add(bufferElement, -1);
        }
        Monitor monitor = new Monitor(buffer, bufferSize, processorsNumber+2);

        ExecutorService workers = Executors.newFixedThreadPool(processorsNumber + 2);
        Worker producer = new Worker(ProcessType.PRODUCER, monitor, buffer, bufferSize, 0);

        workers.submit(producer);
        for(int i=1; i<=processorsNumber; i++) {
            Worker processor = new Worker(ProcessType.PROCESSOR, monitor, buffer, bufferSize, i);
            workers.submit(processor);
        }

        Worker consumer = new Worker(ProcessType.CONSUMER, monitor, buffer, bufferSize, processorsNumber+1);
        workers.submit(consumer);

        workers.shutdown();
    }
}
