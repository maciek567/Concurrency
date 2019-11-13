package test;

import java.util.concurrent.Callable;

public class Task implements Callable<Long> {

    private int factor;

    public Task(int factor) {
        this.factor = factor;
    }

    @Override
    public Long call() throws Exception {
        long d=0;
        for(int i= 0; i<10000000; i++) {
            d += factor;
        }
        return d;
    }
}
