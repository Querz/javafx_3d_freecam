package net.querz.ddd;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Plane extends MeshView {

	public Plane(float width, float depth) {
		TriangleMesh mesh = new TriangleMesh();
		float hw = width / 2;
		float hd = depth / 2;

		mesh.getPoints().addAll(
				-hw, 0, -hd,
				hw, 0, -hd,
				hw, 0, hd,
				-hw, 0, hd
		);
		mesh.getTexCoords().addAll(
				0, 0
		);
		mesh.getFaces().addAll(
				0, 0, 1, 0, 2, 0,
				0, 0, 2, 0, 3, 0
		);
		setMesh(mesh);
	}
}
