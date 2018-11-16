package net.querz.ddd;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import java.util.HashSet;
import java.util.Set;

public class PassiveInput {
	private Set<KeyCode> pressedKeys = new HashSet<>();
	private Set<MouseButton> pressedMouseButtons = new HashSet<>();

	public PassiveInput(Scene scene) {
		scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
		scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
		scene.setOnMousePressed(e -> pressedMouseButtons.add(e.getButton()));
		scene.setOnMouseReleased(e -> pressedMouseButtons.remove(e.getButton()));
	}

	public boolean isKeyPressed(KeyCode... keyCode) {
		for (KeyCode k : keyCode) {
			if (!pressedKeys.contains(k)) {
				return false;
			}
		}
		return true;
	}

	public boolean isMouseButtonPressed(MouseButton... mouseButton) {
		for (MouseButton b : mouseButton) {
			if (!pressedMouseButtons.contains(b)) {
				return false;
			}
		}
		return true;
	}
}
