package test;

import java.util.concurrent.*;

public class TestMain {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Callable<Long> task1 = new Task(1);
        Callable<Long> task2 = new Task(2);
        Callable<Long> task3 = new Task(3);
        Callable<Long> task4 = new Task(4);

        Future<Long> result1 = executorService.submit(task1);
        Future<Long> result2 = executorService.submit(task2);
        Future<Long> result3 = executorService.submit(task3);
        Future<Long> result4 = executorService.submit(task4);

        long sumOfResults = 0;
        try {
            sumOfResults += result1.get();
            sumOfResults += result2.get();
            sumOfResults += result3.get();
            sumOfResults += result4.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(sumOfResults);
        executorService.shutdown();
    }
}
