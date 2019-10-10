package counter;

public class CounterModification {

    public static void task1() throws InterruptedException {
        Counter counter = new Counter();

        Runnable runnableIncrement = () -> {
            for(int i=0; i<100000; i++) {
                counter.increment();
            }
            System.out.println("incrementing thread: " + counter.getCounter());
        };

        Runnable runnableDecrement = () -> {
            for(int i=0; i<100000; i++) {
                counter.decrement();
            }
            System.out.println("decrementing thread: " + counter.getCounter());
        };

        Thread myThreadIncrement = new Thread(runnableIncrement);
        Thread myThreadDecrement = new Thread(runnableDecrement);

        myThreadIncrement.start();
        myThreadDecrement.start();

        myThreadIncrement.join();
        myThreadDecrement.join();

        System.out.println("Main application thread: " + counter.getCounter());
    }
}
