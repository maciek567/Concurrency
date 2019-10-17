package shop;

public class CounterSemaphore {

    private int state;  // state==0 ->  locked,  state>=1 -> unlocked

    public CounterSemaphore(int initialState) {
        this.state = initialState;
    }

    public int getState() {
        return state;
    }

    public synchronized void raise(String threadName, int shoppingTime) {
        state += 1;
        notifyAll();
        System.out.println("Customer: " + threadName +
                " has finished shopping after: " + shoppingTime + "ms");
    }

    public synchronized void lower(String threadName) {
        while(state == 0) {
            try {
                System.out.println("Customer " + threadName + " is waiting for available basket");
                wait();
            } catch (InterruptedException e) { e.printStackTrace();  }
        }
        state -= 1;
        System.out.println("Customer " + threadName + " has started shopping");
    }

}
