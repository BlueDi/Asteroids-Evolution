package entities;

import managers.Settings;

public class MediumAsteroid extends Asteroid {
	public MediumAsteroid() {
		super(Asteroid.MEDIUM);
		numPoints = 10;
		width = height = 20;
		init();
	}

	public MediumAsteroid(float x, float y) {
		super(x, y, Asteroid.MEDIUM);
	}

	public int getPoints() {
		return numPoints;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float getSpeed() {
		return Settings.ASTEROID_SPEED_MEDIUM * Settings.TIME_MULTIPLIER;
	}

}
