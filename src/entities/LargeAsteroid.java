package entities;

import managers.Settings;

public class LargeAsteroid extends Asteroid {
	private int numPoints = 8;
	private int width = height = 12;

	public LargeAsteroid() {
		super(Asteroid.LARGE);
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

	public double getSpeed() {
		return Settings.ASTEROID_SPEED_SMALL * Settings.TIME_MULTIPLIER;
	}

}
