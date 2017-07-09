package managers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public final class Settings {
    public static boolean DEBUG = true;
    static int WINDOW_WIDTH = 1600;
    static int WINDOW_HEIGHT = 800;
    public static int TIME_MULTIPLIER = 1;
    static int NUMBER_OF_SHIPS = 100;
    static int NUMBER_OF_ASTEROIDS = 10;
    static int NUMBER_OF_FOOD = 30;
    static int MUTATION_PROBABILITY = 50;
    static float MUTATION_VARIATION = 0.05f;
    static int FIT_EXPONENTIAL = 5;
    static int ELITISM = 3;

    //Ship
    public static int SHIP_LIFETIME = 5;
    public static float SHIP_MAX_SPEED = 500;
    public static float SHIP_MAX_ACCELERATION = 500;
    public static float SHIP_MAX_DECELERATION = 250;
    public static float SHIP_ROTATION = (float) Math.PI;
    public static float SHIP_STARTING_ORIENTATION = (float) Math.PI / 2;
    public static double SHIP_SATISFIABLE_ANGLE = (float) Math.PI / 100;
    public static double SHIP_DODGE_ANGLE = (float) Math.PI;
    public static int SHIP_DISTANCE_DODGE = 200;

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
