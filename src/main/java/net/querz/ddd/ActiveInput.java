package net.querz.ddd;

import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ActiveInput {
	private static ExecutorService threadpool;
	private SubScene scene;
	private Map<KeyCode, KeyCode> pressedKeys = new ConcurrentHashMap<>();
	private Map<KeyCode, Consumer<SubScene>> keyActions = new ConcurrentHashMap<>();
	private boolean running = true;

	public ActiveInput(SubScene subScene) {
		this.scene = subScene;
		this.scene.getScene().setOnKeyPressed(this::onKeyPressed);
		this.scene.getScene().setOnKeyReleased(this::onKeyReleased);
		if (threadpool == null) {
			threadpool = Executors.newCachedThreadPool();
			Runtime.getRuntime().addShutdownHook(new Thread(() -> threadpool.shutdown()));
		}
		Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
		threadpool.execute(this::run);
	}

	private void run() {
		while (running) {
			try {
				runKeyActions();
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void shutdown() {
		running = false;
	}

	public void registerKeyAction(KeyCode k, Consumer<SubScene> r) {
		keyActions.put(k, r);
	}

	public void unregisterKeyAction(KeyCode k) {
		keyActions.remove(k);
	}

	private void onKeyPressed(KeyEvent event) {
		if (keyActions.containsKey(event.getCode())) {
			pressedKeys.put(event.getCode(), event.getCode());
		}
	}

	private void onKeyReleased(KeyEvent event) {
		pressedKeys.remove(event.getCode());
	}

	private void runKeyActions() {
		pressedKeys.keySet().forEach(k -> keyActions.get(k).accept(scene));
	}
}
