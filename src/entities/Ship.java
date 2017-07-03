package entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import managers.Game;
import managers.Settings;

import java.util.List;

public class Ship extends SpaceObject {
    private final int MAX_BULLETS = Settings.SHIP_MAX_BULLETS;
    private List<Bullet> bullets;

    private float[] flamex;
    private float[] flamey;

    private boolean left;
    private boolean right;
    private boolean up;

    private float maxSpeed = Settings.SHIP_MAX_SPEED;
    private float acceleration = Settings.SHIP_ACCELERATION;
    private float deceleration = Settings.SHIP_DECELERATION;
    private float acceleratingTimer;

    private double min_distance_to_food;
    private double min_distance_to_asteroid;

    private float PI = (float) Math.PI;

    public Ship(List<Bullet> bullets) {
        this.bullets = bullets;
        this.lifeTime = Settings.SHIP_LIFETIME;

        x = (float) Math.random() * Game.WIDTH / 2 + Game.WIDTH / 4;
        y = (float) Math.random() * Game.HEIGHT / 2 + Game.HEIGHT / 4;

        shapex = new float[4];
        shapey = new float[4];
        flamex = new float[3];
        flamey = new float[3];

        orientation = Settings.SHIP_STARTING_ORIENTATION;
        rotationSpeed = Settings.SHIP_ROTATION;
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
        double desired_angle_radians = Math.atan2(closestY - y, closestX - x);
        if (desired_angle_radians < 0)
            desired_angle_radians += 2 * Math.PI;
        return (float) desired_angle_radians;
    }

    private void rotateAndFly(float desired) {
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
     * Searches the closest asteroid and flies in its direction.
     * TODO: a nave ainda não vira diretamente para o asteroide.
     * TODO: converter de array de Asteroids para array de SpaceObjects
     *
     * @param food Asteroids to search
     */
    public void nearestFood(List<Food> food) {
        min_distance_to_food = 9999;
        Food closestFood = null;
        for (Food f : food) {
            double distance = Math.abs(f.getx() - x + f.gety() - y);
            if (distance < min_distance_to_food) {
                min_distance_to_food = distance;
                closestFood = f;
            }
        }

        float desired = 0;
        if (closestFood != null)
            desired = calculateDesiredOrientation(closestFood.getx(), closestFood.gety());
        rotateAndFly(desired);
    }

    public void nearestAsteroid(List<Asteroid> asteroids) {
        min_distance_to_asteroid = 9999;
        Asteroid closestAsteroid = null;
        for (Asteroid a : asteroids) {
            double distance = Math.abs(a.getx() - x + a.gety() - y);
            if (distance < min_distance_to_asteroid) {
                min_distance_to_asteroid = distance;
                closestAsteroid = a;
            }
        }

        if (min_distance_to_asteroid < min_distance_to_food) {
            float desired = 0;
            if (closestAsteroid != null)
                desired = calculateDesiredOrientation(closestAsteroid.getx(), closestAsteroid.gety());
            desired += Math.PI;
            rotateAndFly(desired);
        }
    }

    private void rotate(float dt) {
        if (left) {
            orientation += rotationSpeed * dt;
        } else if (right) {
            orientation -= rotationSpeed * dt;
        }
    }

    private void accelerate(float dt) {
        if (up) {
            dx += MathUtils.cos(orientation) * acceleration * dt;
            dy += MathUtils.sin(orientation) * acceleration * dt;
            acceleratingTimer += dt;
            if (acceleratingTimer > 0.1f) {
                acceleratingTimer = 0;
            }
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

        // screen wrap
        wrap();

        isAlive(dt);
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(1, 1, 1, 1);
        sr.begin(ShapeType.Line);
        for (int i = 0, j = shapex.length - 1; i < shapex.length; j = i++)
            sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);

        if (up)
            for (int i = 0, j = flamex.length - 1; i < flamex.length; j = i++)
                sr.line(flamex[i], flamey[i], flamex[j], flamey[j]);

        sr.end();
    }
}
