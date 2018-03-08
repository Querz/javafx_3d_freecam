package net.querz.ddd;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Window extends Application {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	@Override
	public void start(Stage primaryStage) {
		Box c = new Box(4, 4, 4);
		c.setMaterial(new PhongMaterial(Color.RED));
		c.setDrawMode(DrawMode.FILL);
		c.getTransforms().addAll(new Translate(0, -2, 0), new Rotate(20, Rotate.Y_AXIS));

		Plane plane = new Plane(1000, 1000);
		plane.setMaterial(new PhongMaterial(Color.WHITE));
		plane.setDrawMode(DrawMode.FILL);

		Group root = new Group();
		root.getChildren().add(plane);
		root.getChildren().add(c);

		SubScene scene3D = new SubScene(root, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
		scene3D.setFill(Color.BLACK);

		Group group = new Group();
		group.getChildren().add(scene3D);

		primaryStage.setResizable(false);
		Scene scene = new Scene(group);

		FreeCamera camera = new FreeCamera(scene3D, true);
		camera.setFarClip(1000);
		camera.teleport(0, -15, -30, -25, 0);
		scene3D.setCamera(camera);

		primaryStage.setOnCloseRequest(e -> System.exit(0));
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
