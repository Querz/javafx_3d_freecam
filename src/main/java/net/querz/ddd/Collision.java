package net.querz.ddd;

import java.util.HashSet;
import java.util.Set;

public class Collision {

	//TODO: implement AABB tree
	private Set<AABB> aabbs = new HashSet<>();

	public void add(AABB aabb) {
		aabbs.add(aabb);
	}

	public Vector3f check(AABB moving, Vector3f velocity) {
		for (AABB aabb : aabbs) {
			velocity = moving.resolveCollision(aabb, velocity);
		}

		return velocity;
	}

}
