package C5Interrupts;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class I3InterruptBlocked {

  // Ok, we check interrupted flag.
  // But what if the thread is blocked.
  // For example, waiting response from database.

  static class LongTask implements Runnable {

    int blackHole = 0;

    @Override
    public void run() {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      while (!Thread.currentThread().isInterrupted()) {
        blackHole += random.nextInt();
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException e) {
          System.out.println(Thread.currentThread().getName() + " got " + e);
        }
      }
    }
  }

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    LongTask longTask = new LongTask();

    Thread thread = new Thread(longTask, "long task");
    thread.start();

    Thread.sleep(10L);

    thread.interrupt();
    thread.join();

    System.out.println("Thread stopped, " + longTask.blackHole);

  }
}
