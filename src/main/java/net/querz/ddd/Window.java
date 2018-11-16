package net.querz.ddd;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Window extends Application {
	public static final int WIDTH = 300;
	public static final int HEIGHT = 300;

	@Override
	public void start(Stage primaryStage) {
		Plane plane = new Plane(1000, 1000);
		plane.setMaterial(new PhongMaterial(Color.GRAY));
		plane.setDrawMode(DrawMode.FILL);

		Group root = new Group();
		root.getChildren().add(plane);
//		root.getChildren().add(c);
		drawCoordinateSystem(root);
		root.getChildren().add(new AmbientLight(Color.WHITE));

		SubScene scene3D = new SubScene(root, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
		scene3D.setFill(Color.WHITE);

		Group group = new Group();
		group.getChildren().add(scene3D);

		primaryStage.setResizable(false);
		Scene scene = new Scene(group);

		CollidingCamera camera = new CollidingCamera(scene3D, true);
		camera.setFarClip(1000);
		camera.teleport(80, 80, 80, -35, 90);
		scene3D.setCamera(camera);

		World.collision.add(new AABB(scene3D, new Vector3f(0, 0, 0), new Vector3f(40, 40, 40)));
		World.collision.add(new AABB(scene3D, new Vector3f(45, 0, -50), new Vector3f(51, 50, 50)));

		GameLoop gameLoop = new GameLoop();
		gameLoop.addGameObject(camera);
		gameLoop.start();

		primaryStage.setOnCloseRequest(e -> System.exit(0));
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void drawCoordinateSystem(Group root) {
		Box x = new Box(1000, 0.1, 0.1);
		x.getTransforms().add(new Translate(500, 0, 0));
		x.setMaterial(new PhongMaterial(Color.RED));
		Box y = new Box(0.1, 1000, 0.1);
		y.getTransforms().add(new Translate(0, 500, 0));
		y.setMaterial(new PhongMaterial(Color.BLUE));
		Box z = new Box(0.1, 0.1, 1000);
		z.getTransforms().add(new Translate(0, 0, 500));
		z.setMaterial(new PhongMaterial(Color.GREEN));
		root.getChildren().addAll(x, y, z);
	}
}
