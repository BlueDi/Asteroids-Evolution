package entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import managers.Game;

abstract class SpaceObject {
    float x;
    float y;
    float dx;
    float dy;

    float orientation;
    float speed;
    float rotationSpeed;

    /**
     * Total time the SpaceObject has to live.
     */
    float lifeTime;
    /**
     * Time elapsed from birth.
     */
    float lifeTimer = 0;
    private boolean remove = false;

    int width;
    int height;

    float[] shapex;
    float[] shapey;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    /**
     * Places the SpaceObject in a position from 25% to 75% of the screen.
     */
    void setRandomPosition() {
        x = (float) Math.random() * Game.WIDTH;
        y = (float) Math.random() * Game.HEIGHT;
    }

    /**
     * Places the SpaceObject in a position from 25% to 75% of the screen.
     */
    void setRandomLimitedPosition() {
        x = (float) Math.random() * Game.WIDTH / 2 + Game.WIDTH / 4;
        y = (float) Math.random() * Game.HEIGHT / 2 + Game.HEIGHT / 4;
    }

    float getRotationSpeed() {
        return this.rotationSpeed;
    }

    public float getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int t) {
        lifeTime += t;
    }

    public void setLifeTimer(int t) {
        lifeTimer += t;
        if (lifeTimer < 0)
            lifeTimer = 0;
    }

    private float[] getShapeX() {
        return shapex;
    }

    private float[] getShapeY() {
        return shapey;
    }

    public boolean shouldRemove() {
        return remove;
    }

    /**
     * Transforms an radian angle to fit from [-2*PI; 2*PI].
     *
     * @param n Angle to fit in 2*PI
     * @return Angle in [-2*PI; 2*PI]
     */
    float transform2pi(float n) {
        while (n > 2 * Math.PI)
            n -= 2 * Math.PI;

        while (n < 2 * Math.PI)
            n += 2 * Math.PI;

        return n;
    }

    public abstract void update(float dt);

    public abstract void draw(ShapeRenderer sr);

    /**
     * If SpaceObject gets to the border of the screen mirrors its position to the other side.
     */
    void wrap() {
        if (x < 0)
            x = Game.WIDTH;
        if (x > Game.WIDTH)
            x = 0;
        if (y < 0)
            y = Game.HEIGHT;
        if (y > Game.HEIGHT)
            y = 0;
    }

    void updatePosition(float dt) {
        x += dx * dt;
        y += dy * dt;
    }

    /**
     * Updates life timer.
     *
     * @param dt Time elapsed
     */
    void isAlive(float dt) {
        lifeTimer += dt;
        if (lifeTimer >= lifeTime)
            remove = true;
    }

    /**
     * Verifica se dois SpaceObject colidem.
     *
     * @param other SpaceObject a verificar
     * @return true se colidem, false senao
     */
    public boolean intersects(SpaceObject other) {
        float[] sx = other.getShapeX();
        float[] sy = other.getShapeY();
        for (int i = 0; i < sx.length; i++) {
            if (contains(sx[i], sy[i]))
                return true;
        }
        return false;
    }

    public boolean contains(float x, float y) {
        boolean contain = false;
        for (int i = 0, j = shapex.length - 1; i < shapex.length; j = i++)
            if ((shapey[i] > y) != (shapey[j] > y) && (x < (shapex[j] - shapex[i]) * (y - shapey[i]) / (shapey[j] - shapey[i]) + shapex[i]))
                contain = !contain;
        return contain;
    }
}
