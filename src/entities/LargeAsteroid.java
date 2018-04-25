package entities;

import managers.Settings;

public class LargeAsteroid extends Asteroid {
	public LargeAsteroid() {
		super(Asteroid.LARGE);
		numPoints = 8;
		width = height = 12;
	}

	public LargeAsteroid(float x, float y) {
		super(x, y, Asteroid.LARGE);
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
		return Settings.ASTEROID_SPEED_SMALL * Settings.TIME_MULTIPLIER;
	}

}
