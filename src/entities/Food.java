package entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import managers.Settings;

public class Food extends SpaceObject {
    public Food(float x, float y) {
        this.x = x;
        this.y = y;
        this.lifeTime = Settings.FOOD_LIFETIME;

        width = Settings.FOOD_SIZE;
        height = Settings.FOOD_SIZE;
    }

    public void update(float dt) {
        isAlive(dt);
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(0, 1, 0, 1);
        sr.begin(Settings.FOOD_SHAPE);
        sr.circle(x - width / 2, y - height / 2, width / 2);
        sr.end();
    }
}
