package pipe;

import java.util.List;

public class Worker implements Runnable {

    private ProcessType processType;
    private Monitor monitor;
    private List<Integer> buffer;
    private int bufferSize;
    private int workerId;
    private int currentlyProcessedElement;

    Worker(ProcessType processType, Monitor monitor, List<Integer> buffer, int bufferSize, int workerId) {
        this.processType = processType;
        this.monitor = monitor;
        this.buffer = buffer;
        this.bufferSize = bufferSize;
        this.workerId = workerId;
        this.currentlyProcessedElement = 0;
    }

    @Override
    public void run() {
        while(true) {
            try {
                this.monitor.take(this.workerId, this.currentlyProcessedElement);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            process();
            this.monitor.give(this.workerId, this.currentlyProcessedElement);
            this.currentlyProcessedElement = (this.currentlyProcessedElement + 1) % this.bufferSize;

        }
    }

    private void process() {
        if(this.processType.equals(ProcessType.PRODUCER))
            this.buffer.set(this.currentlyProcessedElement, 0);
        else if(this.processType.equals(ProcessType.PROCESSOR))
            this.buffer.set(this.currentlyProcessedElement, this.buffer.get(this.currentlyProcessedElement) + 1);
        else if(this.processType.equals(ProcessType.CONSUMER))
            this.buffer.set(this.currentlyProcessedElement, -1);
    }
}
