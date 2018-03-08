package net.querz.ddd;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.awt.*;

public class FreeCamera extends PerspectiveCamera {
	private SubScene scene3D;
	private Rotate cYaw = new Rotate(0, Rotate.Y_AXIS);
	private Rotate cPit = new Rotate(-20, Rotate.X_AXIS);
	private Translate cTrans = new Translate(0, 0, 0);
	private int centerX, centerY;
	private static final double MOUSE_SPEED = 0.1;

	public FreeCamera(SubScene scene3D, boolean fixedEyeAtCameraZoom) {
		super(fixedEyeAtCameraZoom);
		this.scene3D = scene3D;
		centerX = (int) scene3D.getWidth() / 2;
		centerY = (int) scene3D.getHeight() / 2;

		getTransforms().addAll(cTrans, cYaw, cPit);

		Input scene3DInput = new Input(scene3D);
		scene3DInput.registerKeyAction(KeyCode.W, s -> {
			double dx = Math.sin(Math.toRadians(cYaw.getAngle()));
			double dz = Math.cos(Math.toRadians(cYaw.getAngle()));
			cTrans.setX(cTrans.getX() + dx);
			cTrans.setZ(cTrans.getZ() + dz);
		});
		scene3DInput.registerKeyAction(KeyCode.S, s -> {
			double dx = Math.sin(Math.toRadians(cYaw.getAngle()));
			double dz = Math.cos(Math.toRadians(cYaw.getAngle()));
			cTrans.setX(cTrans.getX() - dx);
			cTrans.setZ(cTrans.getZ() - dz);
		});
		scene3DInput.registerKeyAction(KeyCode.A, s -> {
			double dx = Math.sin(Math.toRadians(cYaw.getAngle() + 90));
			double dz = Math.cos(Math.toRadians(cYaw.getAngle() + 90));
			cTrans.setX(cTrans.getX() - dx);
			cTrans.setZ(cTrans.getZ() - dz);
		});
		scene3DInput.registerKeyAction(KeyCode.D, s -> {
			double dx = Math.sin(Math.toRadians(cYaw.getAngle() + 90));
			double dz = Math.cos(Math.toRadians(cYaw.getAngle() + 90));
			cTrans.setX(cTrans.getX() + dx);
			cTrans.setZ(cTrans.getZ() + dz);
		});
		scene3DInput.registerKeyAction(KeyCode.ESCAPE, s -> scene3D.setCursor(Cursor.DEFAULT));
		scene3DInput.registerKeyAction(KeyCode.SPACE, s -> cTrans.setY(cTrans.getY() - 1));
		scene3DInput.registerKeyAction(KeyCode.SHIFT, s -> cTrans.setY(cTrans.getY() + 1));
		scene3D.setOnMouseMoved(this::onMouseMoved);
		scene3D.setOnMouseExited(this::onMouseExited);
		scene3D.setOnMousePressed(this::onMousePressed);
	}

	private void onMousePressed(MouseEvent event) {
		scene3D.setCursor(Cursor.NONE);
	}

	private void onMouseMoved(MouseEvent event) {
		if (scene3D.getCursor() == Cursor.NONE) {
			Bounds b = scene3D.localToScreen(scene3D.getBoundsInLocal());

			double dx = (event.getSceneX() - centerX) * MOUSE_SPEED;
			double dy = (event.getSceneY() - centerY) * MOUSE_SPEED;

			cYaw.setAngle(normalize(cYaw.getAngle() + dx));
			cPit.setAngle(normalize(cPit.getAngle() - dy));

			if (event.getSceneX() != centerX || event.getSceneY() != centerY) {
				resetCursor((int) b.getMinX() + centerX, (int) b.getMinY() + centerY);
			}
		}
	}

	private void onMouseExited(MouseEvent event) {
		onMouseMoved(event);
	}

	private void resetCursor(int screenX, int screenY) {
		Platform.runLater(() -> {
			try {
				Robot robot = new Robot();
				robot.mouseMove(screenX, screenY);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		});
	}

	private double normalize(double angle) {
		return angle > 0 ? angle % 360 : (360 + angle % 360) % 360;
	}
}