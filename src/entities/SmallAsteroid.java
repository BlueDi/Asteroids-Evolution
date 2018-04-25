package entities;

import managers.Settings;

public class SmallAsteroid extends Asteroid {
	public SmallAsteroid() {
		super(Asteroid.SMALL);
		numPoints = 8;
		width = height = 12;
		init();
	}

	public SmallAsteroid(float x, float y) {
		super(x, y, Asteroid.SMALL);
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
