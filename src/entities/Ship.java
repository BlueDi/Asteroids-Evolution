package entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import managers.Game;
import managers.Settings;

import java.util.ArrayList;

public class Ship extends SpaceObject {
    private final int MAX_BULLETS = Settings.SHIP_MAX_BULLETS;
    private ArrayList<Bullet> bullets;

    private float[] flamex;
    private float[] flamey;

    private boolean left;
    private boolean right;
    private boolean up;

    private float maxSpeed = Settings.SHIP_MAX_SPEED;
    private float acceleration = Settings.SHIP_ACCELERATION;
    private float deceleration = Settings.SHIP_DECELERATION;
    private float acceleratingTimer;

    private float PI = (float) Math.PI;

    public Ship(ArrayList<Bullet> bullets) {
        this.bullets = bullets;

        x = (float) Math.random() * Game.WIDTH / 2 + Game.WIDTH / 4;
        y = (float) Math.random() * Game.HEIGHT / 2 + Game.HEIGHT / 4;

        shapex = new float[4];
        shapey = new float[4];
        flamex = new float[3];
        flamey = new float[3];

        radians = PI / 2;
        rotationSpeed = Settings.SHIP_ROTATION;
    }

    private void setShape() {
        shapex[0] = x + MathUtils.cos(radians) * 8;
        shapey[0] = y + MathUtils.sin(radians) * 8;

        shapex[1] = x + MathUtils.cos(radians - 4 * PI / 5) * 8;
        shapey[1] = y + MathUtils.sin(radians - 4 * PI / 5) * 8;

        shapex[2] = x + MathUtils.cos(radians + PI) * 5;
        shapey[2] = y + MathUtils.sin(radians + PI) * 5;

        shapex[3] = x + MathUtils.cos(radians + 4 * PI / 5) * 8;
        shapey[3] = y + MathUtils.sin(radians + 4 * PI / 5) * 8;
    }

    private void setFlame() {
        flamex[0] = x + MathUtils.cos(radians - 5 * PI / 6) * 5;
        flamey[0] = y + MathUtils.sin(radians - 5 * PI / 6) * 5;

        flamex[1] = x + MathUtils.cos(radians - PI) * (6 + acceleratingTimer * 50);
        flamey[1] = y + MathUtils.sin(radians - PI) * (6 + acceleratingTimer * 50);

        flamex[2] = x + MathUtils.cos(radians + 5 * PI / 6) * 5;
        flamey[2] = y + MathUtils.sin(radians + 5 * PI / 6) * 5;
    }

    public void setLeft(boolean b) {
        left = b;
    }

    public void setRight(boolean b) {
        right = b;
    }

    public void setUp(boolean b) {
        up = b;
    }

    public void shoot() {
        if (bullets.size() == MAX_BULLETS)
            return;
        bullets.add(new Bullet(x, y, radians));
    }

    public void update(float dt) {
        // turning
        if (left) {
            radians += rotationSpeed * dt;
        } else if (right) {
            radians -= rotationSpeed * dt;
        }

        // accelerating
        if (up) {
            dx += MathUtils.cos(radians) * acceleration * dt;
            dy += MathUtils.sin(radians) * acceleration * dt;
            acceleratingTimer += dt;
            if (acceleratingTimer > 0.1f) {
                acceleratingTimer = 0;
            }
        } else {
            acceleratingTimer = 0;
        }

        // deceleration
        float vec = (float) Math.sqrt(dx * dx + dy * dy);
        if (vec > 0) {
            dx -= (dx / vec) * deceleration * dt;
            dy -= (dy / vec) * deceleration * dt;
        }
        if (vec > maxSpeed) {
            dx = (dx / vec) * maxSpeed;
            dy = (dy / vec) * maxSpeed;
        }

        // set position
        x += dx * dt;
        y += dy * dt;

        // set shape
        setShape();

        // set flame
        if (up)
            setFlame();

        // screen wrap
        wrap();
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


















