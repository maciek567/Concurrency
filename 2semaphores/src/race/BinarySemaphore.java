package race;

public class BinarySemaphore {

    private boolean state;  // false -  locked, true - unlocked

    public BinarySemaphore() {
        this.state = true;
    }

    public synchronized void raise() {
        state = true;
        notifyAll();
    }

    public synchronized void lower() {
        while(!state) {
            try {
                wait();
            } catch (InterruptedException e) { e.printStackTrace();  }
        }
        state = false;
    }

}
