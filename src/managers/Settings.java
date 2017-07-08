package managers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public final class Settings {
    public static boolean DEBUG = true;
    public static int WINDOW_WIDTH = 1600;
    public static int WINDOW_HEIGHT = 800;
    public static int TIME_MULTIPLIER = 1;
    public static int NUMBER_OF_SHIPS = 100;
    public static int NUMBER_OF_ASTEROIDS = 10;
    public static int NUMBER_OF_FOOD = 30;
    public static int MUTATION_PROBABILITY = 50;
    public static float MUTATION_VARIATION = 0.05f;

    //PlayState
    public static int DISTANCE_SHIP_FOOD = 200;
    public static int DISTANCE_SHIP_ASTEROID = 50;

    //Ship
    public static int SHIP_LIFETIME = 5;
    public static int SHIP_MAX_BULLETS = 4;
    public static float SHIP_MAX_SPEED = 500;
    public static float SHIP_MAX_ACCELERATION = 500;
    public static float SHIP_MAX_DECELERATION = 250;
    public static float SHIP_ROTATION = (float) Math.PI;
    public static float SHIP_STARTING_ORIENTATION = (float) Math.PI / 2;
    public static double SHIP_SATISFIABLE_ANGLE = (float) Math.PI / 100;
    public static double SHIP_DODGE_ANGLE = (float) Math.PI;
    public static int SHIP_DISTANCE_DODGE = 200;

    //Bullet
    public static int BULLET_LIFETIME = 1;
    public static float BULLET_SPEED = 35;
    public static ShapeType BULLET_SHAPE = ShapeType.Circle;

    //Asteroid
    public static int ASTEROID_LIFETIME = 10;
    public static int ASTEROID_ROTATION = 6;
    public static int ASTEROID_SPEED_SMALL = 100;
    public static int ASTEROID_SPEED_MEDIUM = 60;
    public static int ASTEROID_SPEED_LARGE = 30;

    //Food
    public static int FOOD_SIZE = 2;
    public static ShapeType FOOD_SHAPE = ShapeType.Circle;
    public static int FOOD_LIFETIME = 10;

    private Settings() {
    }
}
