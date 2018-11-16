package net.querz.ddd;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameLoop {

	private static ExecutorService threadpool;
	private List<GameObject> gameObjects = new ArrayList<>();
	private boolean running = false;
	private long deltaTime = 0;
	private double fps = 0;

	public GameLoop() {
		if (threadpool == null) {
			threadpool = Executors.newCachedThreadPool();
			Runtime.getRuntime().addShutdownHook(new Thread(() -> threadpool.shutdown()));
		}
	}

	public void addGameObject(GameObject gameObject) {
		gameObjects.add(gameObject);
	}

	public void start() {
		if (!running) {
			running = true;
			threadpool.execute(this::run);
		}
	}

	public void stop() {
		running = false;
	}

	public double getDeltaTime() {
		return deltaTime;
	}

	private void run() {
		long startSec = System.nanoTime();
		int frames = 0;
		long start;

		while (running) {
			start = System.nanoTime();
			if (start - startSec > 1_000_000_000) {
				fps = frames;
				frames = 0;
				startSec = start;
//				System.out.println(fps);
			}
			frames++;

			for (GameObject gameObject : gameObjects) {
				gameObject.update(this);
			}

			//frame cap at 60 fps
//				deltaTime = System.nanoTime() - t;

			long delta = System.nanoTime() - start;
			if (delta < 1_000_000_000.0 / 60.0) {
				deltaTime = 1_000_000_000 / 60;
				try {
					Thread.sleep((deltaTime - delta) / 1_000_000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			} else {
				deltaTime = delta;
			}
		}
	}
}
