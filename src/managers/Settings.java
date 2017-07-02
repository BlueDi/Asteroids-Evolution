package managers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public final class Settings {
    public static int WINDOW_WIDTH = 1800;
    public static int WINDOW_HEIGHT = 800;
    static int TIME_MULTIPLIER = 1;
    public static int NUMBER_OF_SHIPS = 3;

    //PlayState
    public static int DISTANCE_SHIP_FOOD = 100;
    public static int INITIAL_NUMBER_OF_ASTEROIDS = 4;

    //Ship
    public static int SHIP_MAX_BULLETS = 4;
    public static float SHIP_MAX_SPEED = 300;
    public static float SHIP_ACCELERATION = 200;
    public static float SHIP_DECELERATION = 10;
    public static float SHIP_ROTATION = 3;

    //Bullet
    public static int BULLET_LIFETIME = 1;
    public static float BULLET_SPEED = 350;
    public static ShapeType BULLET_SHAPE = ShapeType.Circle;

    //Asteroid
    public static int ASTEROID_LIFETIME = 10;


    private Settings() {
    }
}
