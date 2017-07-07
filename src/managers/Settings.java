package managers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public final class Settings {
    public static boolean DEBUG = true;
    public static int WINDOW_WIDTH = 1600;
    public static int WINDOW_HEIGHT = 800;
    public static int TIME_MULTIPLIER = 1;
    public static int NUMBER_OF_SHIPS = 10;
    public static int NUMBER_OF_ASTEROIDS = 10;
    public static int NUMBER_OF_FOOD = 30;
    public static int MUTATION_PROBABILITY = 50;
    public static float MUTATION_VARIATION = 0.05f;

    //PlayState
    public static int DISTANCE_SHIP_FOOD = 200;
    public static int DISTANCE_SHIP_ASTEROID = 50;

    //Ship
    public static int SHIP_LIFETIME = 10;
    public static int SHIP_MAX_BULLETS = 4;
    public static float SHIP_MAX_SPEED = 60;
    public static float SHIP_ACCELERATION = 60;
    public static float SHIP_DECELERATION = 20;
    public static float SHIP_ROTATION = (float) Math.PI;
    public static float SHIP_STARTING_ORIENTATION = (float) Math.PI / 2;
    public static double SHIP_SATISFIABLE_ANGLE = (float) Math.PI / 20;
    public static double SHIP_DODGE_ANGLE = (float) Math.PI;
    public static int SHIP_DISTANCE_DODGE = 300;

    //Bullet
    public static int BULLET_LIFETIME = 1;
    public static float BULLET_SPEED = 35;
    public static ShapeType BULLET_SHAPE = ShapeType.Circle;

    //Asteroid
    public static int ASTEROID_LIFETIME = 10;
    public static int ASTEROID_ROTATION = MathUtils.random(-1, 1);
    public static int ASTEROID_SPEED_SMALL = MathUtils.random(7, 10);
    public static int ASTEROID_SPEED_MEDIUM = MathUtils.random(5, 6);
    public static int ASTEROID_SPEED_LARGE = MathUtils.random(2, 3);

    //Food
    public static int FOOD_SIZE = 2;
    public static ShapeType FOOD_SHAPE = ShapeType.Circle;
    public static int FOOD_LIFETIME = 10;

    private Settings() {
    }
}
