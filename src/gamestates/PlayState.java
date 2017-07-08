package gamestates;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import entities.Asteroid;
import entities.Bullet;
import entities.Food;
import entities.Ship;
import managers.Game;
import managers.GameStateManager;
import managers.Settings;

import java.util.ArrayList;
import java.util.List;

public class PlayState extends gamestates.GameState {
    private ShapeRenderer sr;

    private List<Ship> ships;
    private List<Ship> storedShips;
    private List<List<Bullet>> bullets;
    private List<Asteroid> asteroids;
    private List<Food> food;

    private int numShips;
    private int numAsteroids;
    private int numFood;

    private int GENERATION_COUNTER;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        sr = new ShapeRenderer();

        ships = new ArrayList<>();
        storedShips = new ArrayList<>();
        bullets = new ArrayList<>();
        asteroids = new ArrayList<>();
        food = new ArrayList<>();

        numShips = Settings.NUMBER_OF_SHIPS;

        spawnShips();

        if (Settings.DEBUG)
            print();

        numAsteroids = Settings.NUMBER_OF_ASTEROIDS;
        numFood = Settings.NUMBER_OF_FOOD;
    }

    private float[] generatePositionFarFromShip(int min_distance) {
        double dist;
        float[] position = new float[2];

        do {
            position[0] = MathUtils.random(Game.WIDTH - 200) + 100;
            position[1] = MathUtils.random(Game.HEIGHT - 200) + 100;
            float dx = position[0] - ships.get(0).getX();
            float dy = position[1] - ships.get(0).getY();
            dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        } while (dist < min_distance);

        return position;
    }

    private void spawnSingleShip() {
        bullets.add(new ArrayList<>());
        ships.add(new Ship(bullets.get(bullets.size() - 1)));
    }

    private void spawnShips() {
        while (ships.size() < numShips)
            spawnSingleShip();
    }

    private void storeAndCleanShips() {
        for (int i = 0; i < ships.size(); i++) {
            Ship s = ships.get(i);
            if (s.shouldRemove()) {
                storedShips.add(new Ship(s, s.getDistanceToDodge(), s.getRotationSpeed()));
                ships.remove(i);
                bullets.remove(i);
                i--;
            }
        }
    }

    /**
     * Mutates the last element of ships.
     *
     * @param distance_to_dodge Old distance for the ship to start dodge asteroids
     * @param rotation_speed Old rotation speed of the ship
     */
    private void mutate(double distance_to_dodge, float rotation_speed) {
        double m = MathUtils.random(0, 100);
        if (m > Settings.MUTATION_PROBABILITY && m < (Settings.MUTATION_PROBABILITY + (100 - Settings.MUTATION_PROBABILITY / 2))) {
            ships.get(ships.size() - 1).setDistanceToDodge(distance_to_dodge + distance_to_dodge * Settings.MUTATION_VARIATION);
            ships.get(ships.size() - 1).setRotationSpeed(rotation_speed + rotation_speed * Settings.MUTATION_VARIATION);
        } else if (m > (Settings.MUTATION_PROBABILITY + (100 - Settings.MUTATION_PROBABILITY / 2))) {
            ships.get(ships.size() - 1).setDistanceToDodge(distance_to_dodge - distance_to_dodge * Settings.MUTATION_VARIATION);
            ships.get(ships.size() - 1).setRotationSpeed(rotation_speed - rotation_speed * Settings.MUTATION_VARIATION);
        }
        if(ships.get(ships.size() - 1).getDistanceToDodge() < 1)
            ships.get(ships.size() - 1).setDistanceToDodge(1);
    }

    private void evolution() {
        List<Float> listadasvidas = new ArrayList<>();
        float sumLifes = 0f;
        for (Ship s : storedShips) {
            listadasvidas.add(s.getLifeTime());
            sumLifes += s.getLifeTime();
        }

        for (int i = 0; i < listadasvidas.size(); i++) {
            if (i != 0)
                listadasvidas.set(i, listadasvidas.get(i - 1) + (listadasvidas.get(i) / sumLifes));
            else
                listadasvidas.set(i, listadasvidas.get(i) / sumLifes);
        }

        ships = new ArrayList<>();
        bullets = new ArrayList<>();
        for (int i = 0; i < Settings.NUMBER_OF_SHIPS; i++) {
            double f = Math.random();
            int j = 0;
            while (f > listadasvidas.get(j))
                j++;
            Ship oldShip = storedShips.get(j);
            spawnSingleShip();
            mutate(oldShip.getDistanceToDodge(), oldShip.getRotationSpeed());
        }

        if (Settings.DEBUG)
            print();

        storedShips = new ArrayList<>();
    }

    private void updateShips(float dt) {
        for (Ship s : ships) {
            s.nearest(food, asteroids);
            s.update(dt);
        }

        if (Settings.DEBUG && ships.size() == 1 && ships.get(0).shouldRemove())
            printBestShip();

        storeAndCleanShips();

        if (ships.isEmpty())
            evolution();
    }

    /**
     * Creates a new Asteroid.
     * TODO: decidir se mantenho a verificação de proximidade da posição inicial do asteroide à nave.
     */
    private void spawnSingleAsteroid() {
        float[] position = generatePositionFarFromShip(Settings.DISTANCE_SHIP_ASTEROID);
        asteroids.add(new Asteroid(position[0], position[1], Asteroid.LARGE));
    }

    private void spawnAsteroids() {
        while (asteroids.size() < numAsteroids)
            spawnSingleAsteroid();
    }

    private void splitAsteroid(Asteroid a) {
        if (a.getType() == Asteroid.LARGE) {
            asteroids.add(new Asteroid(a.getX(), a.getY(), Asteroid.MEDIUM));
            asteroids.add(new Asteroid(a.getX(), a.getY(), Asteroid.MEDIUM));
        } else if (a.getType() == Asteroid.MEDIUM) {
            asteroids.add(new Asteroid(a.getX(), a.getY(), Asteroid.SMALL));
            asteroids.add(new Asteroid(a.getX(), a.getY(), Asteroid.SMALL));
        }
    }

    private void spawnSingleFood() {
        float[] position = generatePositionFarFromShip(Settings.DISTANCE_SHIP_FOOD);
        food.add(new Food(position[0], position[1]));
    }

    private void spawnFood() {
        while (food.size() < numFood)
            spawnSingleFood();
    }

    public void update(float dt) {
        // get user input
        handleInput();

        updateShips(dt);

        // update ship bullets
        for (List<Bullet> bullets_of_ship : bullets)
            for (int i = 0; i < bullets_of_ship.size(); i++) {
                bullets_of_ship.get(i).update(dt);
                if (bullets_of_ship.get(i).shouldRemove()) {
                    bullets_of_ship.remove(i);
                    i--;
                }
            }

        // update asteroids
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid a = asteroids.get(i);
            a.update(dt);
            if (a.shouldRemove()) {
                asteroids.remove(i);
                i--;
            }
        }

        // update food
        for (int i = 0; i < food.size(); i++) {
            Food f = food.get(i);
            f.update(dt);
            if (f.shouldRemove()) {
                food.remove(i);
                i--;
            }
        }

        checkCollisions();
        spawnAsteroids();
        spawnFood();
    }

    /**
     * Checks if any Ship collided with a Asteroid.
     * TODO: Se eliminar as bullets depois tenho que as voltar a por na evolution.
     */
    private void checkShipsAsteroidsCollisions() {
        for (int i = 0; i < ships.size(); i++) {
            Ship s = ships.get(i);
            for (int j = 0; j < asteroids.size(); j++) {
                Asteroid a = asteroids.get(j);
                if (a.intersects(s)) {
                    ships.remove(i);
                    //bullets.remove(i);
                    i--;
                    asteroids.remove(j);
                    splitAsteroid(a);
                    break;
                }
            }
        }
    }

    /**
     * Check if any Ship collided with a Food.
     */
    private void checkShipsFoodCollisions() {
        for (Ship s : ships)
            for (int j = 0; j < food.size(); j++) {
                Food f = food.get(j);
                if (s.contains(f.getX(), f.getY())) {
                    s.setTimer(0);
                    food.remove(j);
                    break;
                }
            }
    }

    /**
     * Check if any Ship collided with a Bullet.
     * TODO: Se eliminar as bullets depois tenho que as voltar a por na evolution.
     */
    private void checkShipsBulletsCollisions() {
        for (int i = 0; i < ships.size(); i++) {
            Ship s = ships.get(i);
            for (int j = 0; j < bullets.size(); j++)
                if (i != j) {
                    List<Bullet> enemy_bullets = bullets.get(j);
                    for (int k = 0; k < enemy_bullets.size(); k++) {
                        Bullet b = enemy_bullets.get(k);
                        if (s.contains(b.getX(), b.getY())) {
                            enemy_bullets.remove(b);
                            ships.remove(i);
                            bullets.remove(i);
                            i--;
                            j--;
                            break;
                        }
                    }
                }
        }
    }

    /**
     * Check if any Bullet collided with an Asteroid.
     */
    private void checkBulletsAsteroidsCollisions() {
        for (List<Bullet> bullets_flying : bullets)
            for (int i = 0; i < bullets_flying.size(); i++) {
                Bullet b = bullets_flying.get(i);
                for (int j = 0; j < asteroids.size(); j++) {
                    Asteroid a = asteroids.get(j);
                    if (a.contains(b.getX(), b.getY())) {
                        bullets_flying.remove(b);
                        i--;
                        asteroids.remove(a);
                        splitAsteroid(a);
                        break;
                    }
                }
            }
    }

    private void checkCollisions() {
        checkShipsBulletsCollisions();
        checkShipsFoodCollisions();
        checkShipsAsteroidsCollisions();
        checkBulletsAsteroidsCollisions();
    }

    private void printBestShip() {
        System.out.println("Generation " + GENERATION_COUNTER + " Best Ship:\n" + ships.get(0));
    }

    private void print() {
        System.out.println("Generation " + GENERATION_COUNTER++ + ":");
        for (Ship ship : ships)
            System.out.print(ship);
        System.out.print("\n\n");
    }

    private void drawShips() {
        for (Ship ship : ships)
            ship.draw(sr);
    }

    private void drawBullets() {
        for (List<Bullet> bullets_flying : bullets)
            for (Bullet bullet : bullets_flying)
                bullet.draw(sr);
    }

    private void drawAsteroids() {
        for (Asteroid asteroid : asteroids)
            asteroid.draw(sr);
    }

    private void drawFood() {
        for (Food f : food)
            f.draw(sr);
    }

    public void draw() {
        drawShips();
        drawBullets();
        drawAsteroids();
        drawFood();
    }

    public void handleInput() {
        /*
        ships.get(0).setLeft(GameKeys.isDown(GameKeys.LEFT));
        ships.get(0).setRight(GameKeys.isDown(GameKeys.RIGHT));
        ships.get(0).setUp(GameKeys.isDown(GameKeys.UP));
        if (GameKeys.isPressed(GameKeys.SPACE)) {
            ships.get(0).shoot();
        }
        */
    }

    public void dispose() {
    }
}
