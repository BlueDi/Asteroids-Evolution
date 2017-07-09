package entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import managers.Settings;

import java.util.List;

/**
 * TODO: talvez por as naves a terem que aprender que asteroides sao maus, e comida é bom
 */
public class Ship extends SpaceObject {
    private final int MAX_BULLETS = Settings.SHIP_MAX_BULLETS;
    private List<Bullet> bullets;

    private float[] flamex;
    private float[] flamey;

    private boolean left;
    private boolean right;
    private boolean up;

    private float desired = 0f;

    private float maxSpeed;
    private float acceleration;
    private float deceleration;
    private float acceleratingTimer;

    private double distanceToDodge;

    private float PI = (float) Math.PI;

    /**
     * Creates a new Ship with random stats.
     *
     * @param bullets Ship's Bullets list
     */
    public Ship(List<Bullet> bullets) {
        this.bullets = bullets;
        this.lifeTime = Settings.SHIP_LIFETIME / Settings.TIME_MULTIPLIER;

        setRandomLimitedPosition();

        shapex = new float[4];
        shapey = new float[4];
        flamex = new float[3];
        flamey = new float[3];

        orientation = Settings.SHIP_STARTING_ORIENTATION;
        maxSpeed = MathUtils.random(10, Settings.SHIP_MAX_SPEED) * Settings.TIME_MULTIPLIER;
        acceleration = MathUtils.random(10, Settings.SHIP_MAX_ACCELERATION) * Settings.TIME_MULTIPLIER;
        deceleration = MathUtils.random(10, Settings.SHIP_MAX_DECELERATION) * Settings.TIME_MULTIPLIER;
        rotationSpeed = MathUtils.random(2, Settings.SHIP_ROTATION);
        distanceToDodge = MathUtils.random(1, Settings.SHIP_DISTANCE_DODGE);
    }

    /**
     * Clones the Ship received.
     *
     * @param s Ship to clone
     */
    public Ship(Ship s) {
        this(s.getBullets());
        this.maxSpeed = s.getMaxSpeed();
        this.acceleration = s.getAcceleration();
        this.deceleration = s.getDeceleration();
        this.rotationSpeed = s.rotationSpeed;
        this.distanceToDodge = s.getDistanceToDodge();
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getDeceleration() {
        return deceleration;
    }

    public void setDeceleration(float deceleration) {
        this.deceleration = deceleration;
    }

    private List<Bullet> getBullets() {
        return this.bullets;
    }

    private void setShape() {
        shapex[0] = x + MathUtils.cos(orientation) * 8;
        shapey[0] = y + MathUtils.sin(orientation) * 8;

        shapex[1] = x + MathUtils.cos(orientation - 4 * PI / 5) * 8;
        shapey[1] = y + MathUtils.sin(orientation - 4 * PI / 5) * 8;

        shapex[2] = x + MathUtils.cos(orientation + PI) * 5;
        shapey[2] = y + MathUtils.sin(orientation + PI) * 5;

        shapex[3] = x + MathUtils.cos(orientation + 4 * PI / 5) * 8;
        shapey[3] = y + MathUtils.sin(orientation + 4 * PI / 5) * 8;
    }

    private void setFlame() {
        flamex[0] = x + MathUtils.cos(orientation - 5 * PI / 6) * 5;
        flamey[0] = y + MathUtils.sin(orientation - 5 * PI / 6) * 5;

        flamex[1] = x + MathUtils.cos(orientation - PI) * (6 + acceleratingTimer * 50);
        flamey[1] = y + MathUtils.sin(orientation - PI) * (6 + acceleratingTimer * 50);

        flamex[2] = x + MathUtils.cos(orientation + 5 * PI / 6) * 5;
        flamey[2] = y + MathUtils.sin(orientation + 5 * PI / 6) * 5;
    }

    private void setLeft() {
        left = true;
        right = false;
    }

    private void setRight() {
        right = true;
        left = false;
    }

    private void setStraight() {
        left = false;
        right = false;
    }

    public void setUp(boolean b) {
        up = b;
    }


    public double getDistanceToDodge() {
        return distanceToDodge;
    }

    public void setDistanceToDodge(double distance_to_dodge) {
        this.distanceToDodge = distance_to_dodge;
    }

    /**
     * Shoots a bullet.
     */
    public void shoot() {
        if (bullets.size() < MAX_BULLETS)
            bullets.add(new Bullet(x, y, orientation));
    }

    /**
     * Calculates the desired angle (radian) the ship should take.
     *
     * @param closestX X final coord
     * @param closestY Y final coord
     * @return Angle (radian) the ship should rotate to
     */
    private float calculateDesiredOrientation(float closestX, float closestY) {
        float desired_angle_radians = (float) Math.atan2(closestY - y, closestX - x);
        return transform2pi(desired_angle_radians);
    }

    /**
     * Calculates if the Ship should rotate or fly.
     */
    private void rotateAndFly() {
        float max_angle = (float) (desired + Settings.SHIP_SATISFIABLE_ANGLE);
        float min_angle = (float) (desired - Settings.SHIP_SATISFIABLE_ANGLE);
        transform2pi(max_angle);
        transform2pi(min_angle);
        transform2pi(orientation);

        if (orientation < min_angle) {
            setLeft();
            setUp(false);
        } else if (orientation > max_angle) {
            setRight();
            setUp(false);
        } else {
            setStraight();
            setUp(true);
        }
    }

    /**
     * Searches the closest Food and flies in its direction.
     * TODO: converter de array de Food para array de SpaceObjects
     *
     * @param food Food to search
     */
    private void closestFood(List<Food> food) {
        double minDistanceToFood = 9999;
        Food closestFood = null;
        for (Food f : food) {
            double distance = Math.sqrt(Math.pow(f.getX() - x, 2) + Math.pow(f.getY() - y, 2));
            if (distance < minDistanceToFood) {
                minDistanceToFood = distance;
                closestFood = f;
            }
        }

        desired = 0f;
        if (closestFood != null)
            desired = calculateDesiredOrientation(closestFood.getX(), closestFood.getY());
        rotateAndFly();
    }

    /**
     * Searches the closest Asteroid and, if the difference between it and the closest Food is less than
     * Setting.SIP_DISNTACE_STOP_DODGE, the ship is rotated to fly in the opposite direction.
     * TODO: converter de array de Asteroids para array de SpaceObjects
     *
     * @param asteroids Asteroids to search
     */
    private void closestAsteroid(List<Asteroid> asteroids) {
        double minDistanceToAsteroid = 9999;
        Asteroid closestAsteroid = null;
        for (Asteroid a : asteroids) {
            double distance = Math.sqrt(Math.pow(a.getX() - x, 2) + Math.pow(a.getY() - y, 2));
            if (distance < minDistanceToAsteroid) {
                minDistanceToAsteroid = distance;
                closestAsteroid = a;
            }
        }

        if (minDistanceToAsteroid < distanceToDodge) {
            float dodge_desired = 0f;
            if (closestAsteroid != null)
                dodge_desired = calculateDesiredOrientation(closestAsteroid.getX(), closestAsteroid.getY());
            dodge_desired += Settings.SHIP_DODGE_ANGLE;
            transform2pi(dodge_desired);
            if (dodge_desired > desired)
                desired -= (desired - dodge_desired);
            else
                desired += (desired - dodge_desired);
            transform2pi(desired);
            rotateAndFly();
        }
    }

    /**
     * Calculates the closest SpaceObject to the Ship.
     *
     * @param food      All the food
     * @param asteroids All the asteroids
     */
    public void closest(List<Food> food, List<Asteroid> asteroids) {
        closestFood(food);
        closestAsteroid(asteroids);
    }

    /**
     * Rotates the Ship into the desired orientation.
     * The Ship rotation isn't instantaneous, so it must receive the time elapsed.
     *
     * @param dt Time elapsed
     */
    private void rotate(float dt) {
        transform2pi(desired);
        transform2pi(orientation);
        if (left) {
            orientation += rotationSpeed * dt;
            if (orientation > desired + Settings.SHIP_SATISFIABLE_ANGLE)
                orientation = (float) (desired + Settings.SHIP_SATISFIABLE_ANGLE);
        } else if (right) {
            orientation -= rotationSpeed * dt;
            if (orientation < desired - Settings.SHIP_SATISFIABLE_ANGLE)
                orientation = (float) (desired + Settings.SHIP_SATISFIABLE_ANGLE);
        }
        transform2pi(orientation);
    }

    /**
     * Acceleration.
     * If the Ship velocity is less than its max speed, increase speed.
     *
     * @param dt Time elapsed
     */
    private void accelerate(float dt) {
        if (up) {
            float initialVelocity = (float) Math.sqrt(dx * dx + dy * dy);
            if (initialVelocity < maxSpeed) {
                dx += MathUtils.cos(orientation) * acceleration * dt;
                dy += MathUtils.sin(orientation) * acceleration * dt;
            }
            acceleratingTimer += dt;
            if (acceleratingTimer > 0.1f)
                acceleratingTimer = 0;
        } else {
            acceleratingTimer = 0;
        }
    }

    /**
     * Friction.
     * Maybe a Space Ship shouldn't have friction...
     *
     * @param dt Time elapsed
     */
    private void decelerate(float dt) {
        float vec = (float) Math.sqrt(dx * dx + dy * dy);
        if (vec > 0) {
            dx -= (dx / vec) * deceleration * dt;
            dy -= (dy / vec) * deceleration * dt;
        }
    }

    /**
     * Update the stats of the ship.
     *
     * @param dt Time elapsed
     */
    public void update(float dt) {
        rotate(dt);
        accelerate(dt);
        decelerate(dt);
        updatePosition(dt);

        setShape();

        if (up)
            setFlame();

        isAlive(dt);
    }

    /**
     * Representation of the ship has a String.
     * <p>[maxSpeed; acceleration; deceleration; distanceToDodge; rotationSpeed; lifeTime]
     *
     * @return String with Ship stats
     */
    public String toString() {
        return "[" + String.format("%4.0f", maxSpeed) + "; " + String.format("%4.0f", acceleration) + "; " + String.format("%4.0f", deceleration) + "; " + String.format("%3.0f", distanceToDodge) + "; " + String.format("%.2f", rotationSpeed) + "; " + String.format("%3.0f", lifeTime) + "]";
    }

    /**
     * Draws the ship into the ShapeRenderer.
     *
     * @param sr ShapeRenderer where the Ship will be drawn.
     */
    public void draw(ShapeRenderer sr) {
        if (Settings.DEBUG) {
            sr.begin(ShapeType.Circle);
            sr.setColor(1, 0.5f, 0.5f, 1);
            sr.circle(x, y, (float) distanceToDodge);
            sr.end();
        }

        float blender = lifeTimer / lifeTime;
        sr.setColor(blender, 1 - blender, 0, 1);
        sr.begin(ShapeType.Line);
        for (int i = 0, j = shapex.length - 1; i < shapex.length; j = i++)
            sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
        if (up)
            for (int i = 0, j = flamex.length - 1; i < flamex.length; j = i++)
                sr.line(flamex[i], flamey[i], flamex[j], flamey[j]);
        sr.end();
    }
}
