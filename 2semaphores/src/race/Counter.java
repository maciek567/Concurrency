package race;

public class Counter {

    private int counter;

    public Counter() { this.counter = 0; }

    public int getCounter() { return this.counter; }

    public void increment() { this.counter++; }
    public void decrement() { this.counter--; }
}
