package managers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public final class Settings {
    public static int WINDOW_WIDTH = 1800;
    public static int WINDOW_HEIGHT = 800;
    static int TIME_MULTIPLIER = 1;
    public static int NUMBER_OF_SHIPS = 10;
    public static int INITIAL_NUMBER_OF_ASTEROIDS = 10;
    public static int NUMBER_OF_FOOD = 30;

    //PlayState
    public static int DISTANCE_SHIP_FOOD = 200;
    public static int DISTANCE_SHIP_ASTEROID = 50;

    //Ship
    public static int SHIP_LIFETIME = 10;
    public static int SHIP_MAX_BULLETS = 4;
    public static float SHIP_MAX_SPEED = 300;
    public static float SHIP_ACCELERATION = 200;
    public static float SHIP_DECELERATION = 100;
    public static float SHIP_ROTATION = (float) Math.PI;
    public static float SHIP_STARTING_ORIENTATION = (float) Math.PI / 2;
    public static double SHIP_SATISFIABLE_ANGLE = (float) Math.PI / 20;

    //Bullet
    public static int BULLET_LIFETIME = 1;
    public static float BULLET_SPEED = 350;
    public static ShapeType BULLET_SHAPE = ShapeType.Circle;

    //Asteroid
    public static int ASTEROID_LIFETIME = 10;

    //Food
    public static int FOOD_SIZE = 2;
    public static ShapeType FOOD_SHAPE = ShapeType.Circle;
    public static int FOOD_LIFETIME = 10;

    private Settings() {
    }
}
