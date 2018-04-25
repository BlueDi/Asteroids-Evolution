package entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

import managers.Settings;

public class Asteroid extends SpaceObject {
	private int type;
	public static final int SMALL = 0;
	public static final int MEDIUM = 1;
	public static final int LARGE = 2;

	protected int numPoints;
	private float[] dists;

	public Asteroid(int type) {
		this.type = type;
		this.lifeTime = Settings.ASTEROID_LIFETIME * Settings.TIME_MULTIPLIER;
		this.rotationSpeed = MathUtils.random(-Settings.ASTEROID_ROTATION, Settings.ASTEROID_ROTATION);

		setRandomPosition();
		speed =

				orientation = MathUtils.random(2 * (float) Math.PI);
		dx = MathUtils.cos(orientation) * speed;
		dy = MathUtils.sin(orientation) * speed;

		shapex = new float[numPoints];
		shapey = new float[numPoints];
		dists = new float[numPoints];

		int radius = width / 2;
		for (int i = 0; i < numPoints; i++)
			dists[i] = MathUtils.random(radius / 2, radius);

		setShape();
	}

	public Asteroid(float x, float y, int type) {
		this(type);
		this.x = x;
		this.y = y;
	}

	public int getType() {
		return type;
	}

	private void setShape() {
		float angle = 0;
		for (int i = 0; i < numPoints; i++) {
			shapex[i] = x + MathUtils.cos(angle + orientation) * dists[i];
			shapey[i] = y + MathUtils.sin(angle + orientation) * dists[i];
			angle += 2 * 3.1415f / numPoints;
		}
	}

	public float getSpeed() {
		return speed;
	}

	public void update(float dt) {
		isAlive(dt);

		updatePosition(dt);

		orientation += rotationSpeed * dt;
		setShape();

		wrap();
	}

	public void draw(ShapeRenderer sr) {
		sr.setColor(1, 0, 0, 1);
		sr.begin(ShapeType.Line);
		for (int i = 0, j = shapex.length - 1; i < shapex.length; j = i++)
			sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
		sr.end();
	}
}
