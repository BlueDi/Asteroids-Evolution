package managers;

import java.util.List;

import entities.Asteroid;
import entities.SpaceObject;

public class SpriteSpawner {

	private List<SpaceObject> spaceObjects;

	public void spawnSingleAsteroid() {
		spaceObjects.add(new Asteroid(Asteroid.LARGE));
	}

	public void spawnManyAsteroid(int num) {
		for (int i = 0; i < num; i++)
			spawnSingleAsteroid();
	}
}
