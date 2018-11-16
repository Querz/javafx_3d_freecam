package net.querz.ddd;

public class Vector3f implements Cloneable {
	float x, y, z;

	public Vector3f(double x, double y, double z) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public float lengthSquared() {
		return x * x + y * y + z * z;
	}

	public float max() {
		return Math.max(x, Math.max(y, z));
	}

	public float dot(Vector3f v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public Vector3f cross(Vector3f v) {
		return new Vector3f(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
	}

	public Vector3f normalized() {
		float length = length();
		return new Vector3f(x / length, y / length, z / length);
	}

	public Vector3f rotate(Vector3f axis, float angle) {
		float sin = (float) Math.sin(-angle);
		float cos = (float) Math.cos(-angle);
		return cross(axis.mul(sin)).add((mul(cos)).add(axis.mul(dot(axis.mul(1 - cos)))));
	}

	public Vector3f add(Vector3f v) {
		return new Vector3f(x + v.x, y + v.y, z + v.z);
	}

	public Vector3f add(float f) {
		return new Vector3f(x + f, y + f, z + f);
	}

	public Vector3f sub(Vector3f v) {
		return new Vector3f(x - v.x, y - v.y, z - v.z);
	}

	public Vector3f sub(float f) {
		return new Vector3f(x - f, y - f, z - f);
	}

	public Vector3f mul(Vector3f v) {
		return new Vector3f(x * v.x, y * v.y, z * v.z);
	}

	public Vector3f mul(float f) {
		return new Vector3f(x * f, y * f, z * f);
	}

	public Vector3f div(Vector3f v) {
		return new Vector3f(x / v.x, y / v.y, z / v.z);
	}

	public Vector3f div(float f) {
		return new Vector3f(x / f, y / f, z / f);
	}

	public Vector3f Abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	public boolean equals(Vector3f v) {
		return x == v.x && y == v.y && z == v.z;
	}

	@Override
	public Vector3f clone() {
		try {
			return (Vector3f) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}
}
