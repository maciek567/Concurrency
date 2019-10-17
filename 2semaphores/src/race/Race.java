package race;

public class Race {

    public static void binarySemaphoreRace() throws InterruptedException {
        BinarySemaphore binarySemaphore = new BinarySemaphore();
        Counter counter = new Counter();

        Runnable runnableIncrement = () -> {
            for(int i=0; i<100000; i++) {
                binarySemaphore.lower();
                counter.increment();
                binarySemaphore.raise();
            }
            System.out.println("incrementing thread finish: " + counter.getCounter());
        };

        Runnable runnableDecrement = () -> {
            for(int i=0; i<100000; i++) {
                binarySemaphore.lower();
                counter.decrement();
                binarySemaphore.raise();
            }
            System.out.println("decrementing thread finish: " + counter.getCounter());
        };

        Thread myThreadIncrement = new Thread(runnableIncrement);
        Thread myThreadDecrement = new Thread(runnableDecrement);

        myThreadIncrement.start();
        myThreadDecrement.start();

        myThreadIncrement.join();
        myThreadDecrement.join();

        System.out.println("Sum is: " + counter.getCounter());
    }
}
