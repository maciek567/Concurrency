package counter;

public class Counter {

    private int counter;

    public Counter() { this.counter = 0; }

    public int getCounter() { return this.counter; }

    synchronized public void increment() { this.counter++; }
    synchronized public void decrement() { this.counter--; }
}
