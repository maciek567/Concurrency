package executor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import javax.swing.JFrame;

public class Mandelbrot extends JFrame{

    private BufferedImage I;

    private Mandelbrot() {
        super("executor.Mandelbrot Set");
        int[] noThreads = {1, 4, 8};
        int[] taskToThreadsRatioArray = {1, 10, 50000};

        PrintWriter printWriter = null;
        try {
            FileWriter fileWriter = new FileWriter("resources/computingTime.csv");
            printWriter = new PrintWriter(fileWriter);
            printWriter.print("threads,taskToThreadsRatio,time\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        for(int threads : noThreads) {
            for(int taskToThreadsRatio : taskToThreadsRatioArray) {
                int tasks = threads * taskToThreadsRatio;
                double width = Math.pow(tasks, 0.5); // 1 task computes 1 pixel
                double height = Math.pow(tasks, 0.5);
                setBounds(100, 100, (int)width, (int)height);

                for(int i=0; i<10; i++) {  // 10 repetitions of every measurement
                    I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

                    ExecutorService executorService = Executors.newFixedThreadPool(threads);
                    List<Callable<BufferedImage>> parts = new ArrayList<>(threads);
                    List<Future<BufferedImage>> results = new ArrayList<>(threads);
                    int yBeginCoordinate = 0;

                    long initialTime = System.nanoTime();

                    for(int thread=0; thread<threads; thread++) {
                        parts.add(new MandelbrotPart(I, (int)width, (int) height,
                                yBeginCoordinate, yBeginCoordinate+ (int)height /threads));
                        results.add(executorService.submit(parts.get(thread)));
                        yBeginCoordinate = yBeginCoordinate + (int)height /threads;
                    }
                    for(int thread=0; thread<threads; thread++) {
                        try {
                            this.I = results.get(thread).get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    long finishTime = System.nanoTime();
                    long timeInUs = (finishTime - initialTime) / 1000;
                    assert printWriter != null;
                    printWriter.printf("%d,%d,%d\n", threads, taskToThreadsRatio, timeInUs);
                    System.out.printf("threads: %d  ratio: %d  size: %d time: %d\n",
                            threads, taskToThreadsRatio, (int)width, timeInUs);
                }
            }

        }


        printWriter.close();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }

    public static void main(String[] args) {
        new Mandelbrot().setVisible(true);
    }


}