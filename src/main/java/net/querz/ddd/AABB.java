package net.querz.ddd;

import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class AABB {

	Vector3f min, max;
	private Box visualLines;
	private Box visualBox;
	private Translate visualPos;

	public AABB(Vector3f min, Vector3f max) {
		this.min = min;
		this.max = max;
	}

	public AABB(SubScene scene3D, Vector3f min, Vector3f max) {
		this(min, max);
		visualLines = new Box(size().x, size().y, size().z);
		visualPos = new Translate();
		visualLines.getTransforms().add(visualPos);
		visualLines.setMaterial(new PhongMaterial(Color.BLACK));
		visualLines.setDrawMode(DrawMode.LINE);
		visualLines.setCullFace(CullFace.NONE);

		visualBox = new Box(size().x, size().y, size().z);
		visualBox.getTransforms().add(visualPos);
		visualBox.getTransforms().add(new Scale(0.99999, 0.99999, 0.99999));
		visualBox.setMaterial(new PhongMaterial(Color.LIGHTGREEN));
		move(min);
		((Group) scene3D.getRoot()).getChildren().addAll(visualLines, visualBox);
	}

	public double resolveCollisionX(AABB other, double moveAmtX) {
		double newAmtX;
		if (moveAmtX == 0.0) {
			return moveAmtX;
		}
		if (moveAmtX > 0) {
			newAmtX = other.min.x - max.x;
		} else {
			newAmtX = other.max.x - min.x;
		}

		if (Math.abs(newAmtX) < Math.abs(moveAmtX)) {
			moveAmtX = newAmtX;
		}
		return moveAmtX;
	}

	public double resolveCollisionY(AABB other, double moveAmtY) {
		double newAmtY;
		if (moveAmtY == 0.0) {
			return moveAmtY;
		}
		if (moveAmtY > 0) {
			newAmtY = other.min.y - max.y;
		} else {
			newAmtY = other.max.y - min.y;
		}

		if (Math.abs(newAmtY) < Math.abs(moveAmtY)) {
			moveAmtY = newAmtY;
		}
		return moveAmtY;
	}

	public double resolveCollisionZ(AABB other, double moveAmtZ) {
		double newAmtZ;
		if (moveAmtZ == 0.0) {
			return moveAmtZ;
		}
		if (moveAmtZ > 0) {
			newAmtZ = other.min.z - max.z;
		} else {
			newAmtZ = other.max.z - min.z;
		}

		if (Math.abs(newAmtZ) < Math.abs(moveAmtZ)) {
			moveAmtZ = newAmtZ;
		}
		return moveAmtZ;
	}

	public Vector3f resolveCollision(AABB other, Vector3f moveAmt) {
		//this = collider

		if (this == other) {
			return moveAmt;
		}

		AABB collisionRange = stretch(moveAmt);

		if (other.intersects(collisionRange)) {
			moveAmt = new Vector3f(
					resolveCollisionX(other, moveAmt.x),
					resolveCollisionY(other, moveAmt.y),
					resolveCollisionZ(other, moveAmt.z)
			);
		}
		return moveAmt;

	}

	public Vector3f size() {
		return max.sub(min);
	}

	public void move(Vector3f to) {
		max = to.add(max.sub(min));
		min = to;
		if (visualPos != null) {
			visualPos.setX(min.x + size().x / 2.0);
			visualPos.setY(min.y + size().y / 2.0);
			visualPos.setZ(min.z + size().z / 2.0);
		}
	}

	public boolean contains(AABB other) {
		return min.x <= other.min.x && other.max.x <= max.x
				&& min.y <= other.min.y && other.max.y <= max.y
				&& min.z <= other.min.z && other.max.z <= max.z;
	}

	public boolean intersects(AABB other) {
		return min.x < other.max.x && max.x > other.min.x
				&& min.y < other.max.y && max.y > other.min.y
				&& min.z < other.max.z && max.z > other.min.z;
	}

	public AABB combine(AABB other) {
		return new AABB(
				new Vector3f(
						Math.min(min.x, other.min.x),
						Math.min(min.y, other.min.y),
						Math.min(min.z, other.min.z)),
				new Vector3f(
						Math.max(max.x, other.max.x),
						Math.max(max.y, other.max.y),
						Math.max(max.z, other.max.z))
		);
	}
	
	public AABB stretch(Vector3f amt) {
		float minX, maxX, minY, maxY, minZ, maxZ;
		if (amt.x < 0) {
			minX = min.x + amt.x;
			maxX = max.x;
		} else {
			minX = min.x;
			maxX = max.x + amt.x;
		}
		if (amt.y < 0) {
			minY = min.y + amt.y;
			maxY = max.y;
		} else {
			minY = min.y;
			maxY = max.y + amt.y;
		}

		if (amt.z < 0) {
			minZ = min.z + amt.z;
			maxZ = max.z;
		} else {
			minZ = min.z;
			maxZ = max.z + amt.z;
		}
		return new AABB(new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ));
	}
}
