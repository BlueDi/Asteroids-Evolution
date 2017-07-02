package gamestates;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import entities.Asteroid;
import entities.Bullet;
import entities.Ship;
import managers.Game;
import managers.GameStateManager;
import managers.Settings;

import java.util.ArrayList;

public class PlayState extends gamestates.GameState {
    private ShapeRenderer sr;

    private ArrayList<Ship> ships;
    private ArrayList<ArrayList<Bullet>> bullets;
    private ArrayList<Asteroid> asteroids;

    private int numToSpawn = Settings.INITIAL_NUMBER_OF_ASTEROIDS;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        sr = new ShapeRenderer();

        ships = new ArrayList<>();
        bullets = new ArrayList<>();
        asteroids = new ArrayList<>();

        for (int i = 0; i < Settings.NUMBER_OF_SHIPS; i++) {
            bullets.add(new ArrayList<>());
            ships.add(new Ship(bullets.get(i)));
        }

        asteroids.add(new Asteroid(100, 100, Asteroid.LARGE));
        asteroids.add(new Asteroid(200, 100, Asteroid.MEDIUM));
        asteroids.add(new Asteroid(300, 100, Asteroid.SMALL));

        spawnAsteroids();
    }

    private void spawnSingleAsteroid() {
        float x = MathUtils.random(Game.WIDTH);
        float y = MathUtils.random(Game.HEIGHT);

        float dx = x - ships.get(0).getx();
        float dy = y - ships.get(0).gety();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        while (dist < Settings.DISTANCE_SHIP_FOOD) {
            x = MathUtils.random(Game.WIDTH);
            y = MathUtils.random(Game.HEIGHT);
            dx = x - ships.get(0).getx();
            dy = y - ships.get(0).gety();
            dist = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        }

        asteroids.add(new Asteroid(x, y, Asteroid.LARGE));
    }

    private void spawnAsteroids() {
        asteroids.clear();

        for (int i = 0; i < numToSpawn; i++)
            spawnSingleAsteroid();
    }

    private void splitAsteroid(Asteroid a) {
        if (a.getType() == Asteroid.LARGE) {
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
        } else if (a.getType() == Asteroid.MEDIUM)
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
    }

    public void update(float dt) {
        // get user input
        handleInput();

        // update ship
        for (Ship ship : ships) {
            ship.nearestFood(asteroids);
            ship.update(dt);
        }

        // update ship bullets
        for (ArrayList<Bullet> bullets_of_ship : bullets)
            for (int i = 0; i < bullets_of_ship.size(); i++) {
                bullets_of_ship.get(i).update(dt);
                if (bullets_of_ship.get(i).shouldRemove()) {
                    bullets_of_ship.remove(i);
                    i--;
                }
            }

        // update asteroids
        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).update(dt);
            if (asteroids.get(i).shouldRemove()) {
                asteroids.remove(i);
                i--;
            }
        }

        checkCollisions();

        while (asteroids.size() < numToSpawn)
            spawnSingleAsteroid();
    }

    /**
     * Checks if any ship colided with a bullet or asteroid.
     */
    private void checkShipsAsteroidsCollisions() {
        for (int i = 0; i < ships.size(); i++) {
            Ship s = ships.get(i);
            for (int j = 0; j < asteroids.size(); j++) {
                Asteroid a = asteroids.get(j);
                if (a.intersects(s)) {
                    ships.remove(i);
                    bullets.remove(i);
                    i--;
                    asteroids.remove(j);
                    splitAsteroid(a);
                    break;
                }
            }
        }
    }

    private void checkShipsBulletsCollisions() {
        for (int i = 0; i < ships.size(); i++) {
            Ship s = ships.get(i);
            for (int j = 0; j < bullets.size(); j++)
                if (i != j) {
                    ArrayList<Bullet> enemy_bullets = bullets.get(j);
                    for (int k = 0; k < enemy_bullets.size(); k++) {
                        Bullet b = enemy_bullets.get(k);
                        if (s.contains(b.getx(), b.gety())) {
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

    private void checkBulletsAsteroidsCollisions() {
        for (ArrayList<Bullet> bullets_flying : bullets)
            for (int i = 0; i < bullets_flying.size(); i++) {
                Bullet b = bullets_flying.get(i);
                for (int j = 0; j < asteroids.size(); j++) {
                    Asteroid a = asteroids.get(j);
                    if (a.contains(b.getx(), b.gety())) {
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
        checkShipsAsteroidsCollisions();
        checkBulletsAsteroidsCollisions();
    }

    private void drawShips() {
        for (Ship ship : ships)
            ship.draw(sr);
    }

    private void drawBullets() {
        for (ArrayList<Bullet> bullets_flying : bullets)
            for (Bullet bullet : bullets_flying)
                bullet.draw(sr);
    }

    private void drawAsteroids() {
        for (Asteroid asteroid : asteroids)
            asteroid.draw(sr);
    }

    public void draw() {
        drawShips();
        drawBullets();
        drawAsteroids();
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









