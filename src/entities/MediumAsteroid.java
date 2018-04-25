package entities;

import managers.Settings;

public class MediumAsteroid extends Asteroid {
	private int numPoints = 10;
	private int width = height = 20;

	public MediumAsteroid() {
		super(Asteroid.MEDIUM);
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

	public double getSpeed() {
		return Settings.ASTEROID_SPEED_MEDIUM * Settings.TIME_MULTIPLIER;
	}

}
