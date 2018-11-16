package net.querz.ddd;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import java.awt.*;

public class CollidingCamera extends PerspectiveCamera implements GameObject {

	private PassiveInput input;
	private SubScene scene3D;
	private Rotate cYaw = new Rotate(0, Rotate.Y_AXIS);
	private Rotate cPit = new Rotate(0, Rotate.X_AXIS);
	private Translate cTrans = new Translate(0, 0, 0);
	private int centerX, centerY;
	private static final double MOUSE_SPEED = 0.05;

	private AABB hitbox;

	//this camera is "upside-down" to flip the coordinate system.
	//the positive y-axis should expand upwards, not downwards.
	public CollidingCamera(SubScene scene3D, boolean fixedEyeAtCameraZoom) {
		super(fixedEyeAtCameraZoom);
		this.scene3D = scene3D;
		centerX = (int) scene3D.getWidth() / 2;
		centerY = (int) scene3D.getHeight() / 2;
		getTransforms().addAll(cTrans, cYaw, cPit);
		input = new PassiveInput(scene3D.getScene());
		scene3D.setOnMouseMoved(this::onMouseMoved);
		scene3D.setOnMouseExited(this::onMouseExited);
		hitbox = new AABB(
				new Vector3f(cTrans.getX() - 5, cTrans.getY() - 5, cTrans.getZ() - 5),
				new Vector3f(cTrans.getX() + 5, cTrans.getY() + 5, cTrans.getZ() + 5));
	}

	@Override
	public void update(GameLoop gameLoop) {
		if (input.isMouseButtonPressed(MouseButton.PRIMARY)) {
			scene3D.setCursor(Cursor.NONE);
		}

		Vector3f velocity = new Vector3f(0, 0, 0);

		if (input.isKeyPressed(KeyCode.W)) {
			double dx = Math.sin(Math.toRadians(cYaw.getAngle()));
			double dz = Math.cos(Math.toRadians(cYaw.getAngle()));
			velocity = velocity.add(new Vector3f(-dx, 0, -dz));
		}
		if (input.isKeyPressed(KeyCode.S)) {
			double dx = Math.sin(Math.toRadians(cYaw.getAngle()));
			double dz = Math.cos(Math.toRadians(cYaw.getAngle()));
			velocity = velocity.add(new Vector3f(dx, 0, dz));
		}
		if (input.isKeyPressed(KeyCode.A)) {
			double dx = Math.sin(Math.toRadians(cYaw.getAngle() + 90));
			double dz = Math.cos(Math.toRadians(cYaw.getAngle() + 90));
			velocity = velocity.add(new Vector3f(-dx, 0, -dz));
		}
		if (input.isKeyPressed(KeyCode.D)) {
			double dx = Math.sin(Math.toRadians(cYaw.getAngle() + 90));
			double dz = Math.cos(Math.toRadians(cYaw.getAngle() + 90));
			velocity = velocity.add(new Vector3f(dx, 0, dz));
		}
		if (input.isKeyPressed(KeyCode.ESCAPE)) {
			scene3D.setCursor(Cursor.DEFAULT);
		}
		if (input.isKeyPressed(KeyCode.SPACE)) {
			velocity = velocity.add(new Vector3f(0, 1, 0));
		}
		if (input.isKeyPressed(KeyCode.SHIFT)) {
			velocity = velocity.add(new Vector3f(0, -1, 0));
		}
		move(velocity);
	}

	//this also calculates collision
	public void move(Vector3f velocity) {
		if (velocity.x == 0 && velocity.y == 0 && velocity.z == 0) {
			return;
		}

		velocity = World.collision.check(hitbox, velocity);
		setLocation(hitbox.min.x + velocity.x, hitbox.min.y + velocity.y, hitbox.min.z + velocity.z);
	}

	//x,y,z is the minimum corner of the hitbox
	public void setLocation(double x, double y, double z) {
		Vector3f hSize = hitbox.size();
		cTrans.setX(x + hSize.x / 2);
		cTrans.setY(y + hSize.y / 2);
		cTrans.setZ(z + hSize.z / 2);
		hitbox.move(new Vector3f(x, y, z));
	}

	public void setYaw(double angle) {
		cYaw.setAngle(normalizeAngle(angle));
	}

	public void setPitch(double angle) {
		cPit.setAngle(normalizeAngle(angle + 180));
	}

	public double getPitch() {
		return cPit.getAngle() - 180;
	}

	public void teleport(double x, double y, double z, double pitch, double yaw) {
		setLocation(x, y, z);
		setPitch(pitch);
		setYaw(yaw);
	}

	private void onMouseMoved(MouseEvent event) {
		if (scene3D.getCursor() == Cursor.NONE) {
			Bounds b = scene3D.localToScreen(scene3D.getBoundsInLocal());

			double dx = (event.getSceneX() - centerX) * MOUSE_SPEED;
			double dy = (event.getSceneY() - centerY) * MOUSE_SPEED;

			setYaw(cYaw.getAngle() - dx);
			setPitch(getPitch() - dy);

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

	private double normalizeAngle(double angle) {
		return angle > 0 ? angle % 360 : (360 + angle % 360) % 360;
	}
}
