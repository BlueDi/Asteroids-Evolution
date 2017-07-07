package entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import managers.Game;
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

    private float maxSpeed = Settings.SHIP_MAX_SPEED * Settings.TIME_MULTIPLIER;
    private float acceleration = Settings.SHIP_ACCELERATION * Settings.TIME_MULTIPLIER;
    private float deceleration = Settings.SHIP_DECELERATION * Settings.TIME_MULTIPLIER;
    private float acceleratingTimer;

    private double min_distance_to_food;
    private double min_distance_to_asteroid;

    private float PI = (float) Math.PI;

    public Ship(List<Bullet> bullets) {
        this.bullets = bullets;
        this.lifeTime = Settings.SHIP_LIFETIME * Settings.TIME_MULTIPLIER;

        setRandomPosition();

        shapex = new float[4];
        shapey = new float[4];
        flamex = new float[3];
        flamey = new float[3];

        orientation = Settings.SHIP_STARTING_ORIENTATION;
        rotationSpeed = Settings.SHIP_ROTATION;
    }

    public Ship(Ship s) {
        this(s.getBullets());
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

    public void setTimer(int t) {
        lifeTimer = t;
        lifeTime += t;
    }

    public void resetLifeTime() {
        lifeTime = Settings.SHIP_LIFETIME;
    }

    public void shoot() {
        if (bullets.size() == MAX_BULLETS)
            return;
        bullets.add(new Bullet(x, y, orientation));
    }

    private void transform2pi(float n) {
        while (n > 2 * Math.PI)
            n -= 2 * Math.PI;

        while (n < 2 * Math.PI)
            n += 2 * Math.PI;
    }

    private float calculateDesiredOrientation(float closestX, float closestY) {
        float desired_angle_radians = (float) Math.atan2(closestY - y, closestX - x);
        transform2pi(desired_angle_radians);
        return desired_angle_radians;
    }

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
    private void nearestFood(List<Food> food) {
        min_distance_to_food = 9999;
        Food closestFood = null;
        for (Food f : food) {
            double distance = Math.sqrt(Math.pow(f.getX() - x, 2) + Math.pow(f.getY() - y, 2));
            if (distance < min_distance_to_food) {
                min_distance_to_food = distance;
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
    private void nearestAsteroid(List<Asteroid> asteroids) {
        min_distance_to_asteroid = 9999;
        Asteroid closestAsteroid = null;
        for (Asteroid a : asteroids) {
            double distance = Math.sqrt(Math.pow(a.getX() - x, 2) + Math.pow(a.getY() - y, 2));
            if (distance < min_distance_to_asteroid) {
                min_distance_to_asteroid = distance;
                closestAsteroid = a;
            }
        }

        if (min_distance_to_asteroid < Settings.SHIP_DISTANCE_STOP_DODGE) {
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

    public void nearest(List<Food> food, List<Asteroid> asteroids) {
        nearestFood(food);
        nearestAsteroid(asteroids);
    }

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

    private void accelerate(float dt) {
        if (up) {
            dx += MathUtils.cos(orientation) * acceleration * dt;
            dy += MathUtils.sin(orientation) * acceleration * dt;
            acceleratingTimer += dt;
            if (acceleratingTimer > 0.1f)
                acceleratingTimer = 0;
        } else {
            acceleratingTimer = 0;
        }
    }

    private void decelerate(float dt) {
        float vec = (float) Math.sqrt(dx * dx + dy * dy);
        if (vec > 0) {
            dx -= (dx / vec) * deceleration * dt;
            dy -= (dy / vec) * deceleration * dt;
        }
        if (vec > maxSpeed) {
            dx = (dx / vec) * maxSpeed;
            dy = (dy / vec) * maxSpeed;
        }
    }

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

    public void draw(ShapeRenderer sr) {
        if (Settings.DEBUG) {
            sr.begin(ShapeType.Circle);
            sr.setColor(1, 0.5f, 0.5f, 1);
            sr.circle(x, y, (float) Settings.SHIP_DISTANCE_STOP_DODGE);
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
