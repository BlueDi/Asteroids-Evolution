package entities;

import managers.Settings;

public class SmallAsteroid extends Asteroid {
	private int numPoints = 8;
	private int width = height = 12;

	public SmallAsteroid() {
		super(Asteroid.SMALL);
	}

	public SmallAsteroid(float x, float y) {
		super(x,y,Asteroid.SMALL);
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
