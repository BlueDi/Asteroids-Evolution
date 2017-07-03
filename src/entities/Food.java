package entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import managers.Settings;

public class Food extends SpaceObject {
    private float lifeTime = Settings.FOOD_LIFETIME;
    private float lifeTimer = 0;
    private boolean remove = false;

    public Food(float x, float y) {
        this.x = x;
        this.y = y;

        width = Settings.FOOD_SIZE;
        height = Settings.FOOD_SIZE;
    }

    public boolean shouldRemove() {
        return remove;
    }

    public void update(float dt) {
        lifeTimer += dt;
        if (lifeTimer > lifeTime) {
            remove = true;
        }
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(0, 1, 0, 1);
        sr.begin(Settings.FOOD_SHAPE);
        sr.circle(x - width / 2, y - height / 2, width / 2);
        sr.end();
    }
}
