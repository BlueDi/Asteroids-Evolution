package entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import managers.Settings;

public class Bullet extends SpaceObject {
    private float lifeTime = Settings.BULLET_LIFETIME;
    private float lifeTimer = 0;

    private boolean remove = false;

    Bullet(float x, float y, float radians) {
        this.x = x;
        this.y = y;
        this.orientation = radians;

        float speed = Settings.BULLET_SPEED;
        dx = MathUtils.cos(radians) * speed;
        dy = MathUtils.sin(radians) * speed;

        width = height = 2;
    }

    public boolean shouldRemove() {
        return remove;
    }

    public void update(float dt) {
        x += dx * dt;
        y += dy * dt;

        wrap();

        lifeTimer += dt;
        if (lifeTimer > lifeTime) {
            remove = true;
        }
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(1, 1, 1, 1);
        sr.begin(Settings.BULLET_SHAPE);
        sr.circle(x - width / 2, y - height / 2, width / 2);
        sr.end();
    }
}
