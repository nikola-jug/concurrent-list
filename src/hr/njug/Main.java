package hr.njug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings({"squid:S106", "squid:S2142", "squid:S1148"})
public class Main {

	private List<Integer> completed = Collections.synchronizedList(new ArrayList<>());

	public static void main(String[] args) {
		Main r = new Main();
		ExecutorService exe = Executors.newFixedThreadPool(30);
		int tasks = 100;
		CountDownLatch latch = new CountDownLatch(tasks);
		for (int i = 0; i < tasks; i++) {
			exe.submit(r.new Task(i, latch));
		}
		try {
			latch.await();
			System.out.println("Summary:");
			System.out.println("Number of tasks completed: " + r.completed.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		exe.shutdown();

		r.completed.forEach(integer ->
				System.out.print(integer + ",")
		);
	}

	class Task implements Runnable {

		private int id;
		private CountDownLatch latch;

		Task(int id, CountDownLatch latch) {
			this.id = id;
			this.latch = latch;
		}

		public void run() {
			Random r = new Random();
			try {
				Thread.sleep(r.nextInt(5000)); //Actual work of the task
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			completed.add(id);
			latch.countDown();
		}
	}
}