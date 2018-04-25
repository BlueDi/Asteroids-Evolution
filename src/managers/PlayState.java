package managers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import entities.Asteroid;
import entities.Food;
import entities.MediumAsteroid;
import entities.Ship;
import entities.SmallAsteroid;

class PlayState {
	private ShapeRenderer sr;

	private List<Ship> ships;
	private List<Ship> storedShips;
	private List<Asteroid> asteroids;
	private List<Food> food;

	private int GENERATION_COUNTER;
	private int ELITISM;

	private StatisticsTracker stats;

	PlayState() {
		sr = new ShapeRenderer();
		ships = new ArrayList<>();
		storedShips = new ArrayList<>();
		asteroids = new ArrayList<>();
		food = new ArrayList<>();
		stats = new StatisticsTracker();

		init();
	}

	private void setElitism(int elitism) {
		if (elitism > Settings.NUMBER_OF_SHIPS)
			ELITISM = Settings.NUMBER_OF_SHIPS;
		else if (elitism < 0)
			ELITISM = 0;
		else
			ELITISM = elitism;
	}

	private void init() {
		setElitism(Settings.ELITISM);

		spawnShips();

		if (Settings.DEBUG) {
			stats.printGenerationHeader(GENERATION_COUNTER);
			stats.printPopulation(ships);
		}
	}

	private void spawnSingleShip() {
		ships.add(new Ship());
	}

	private void spawnShips() {
		while (ships.size() < Settings.NUMBER_OF_SHIPS)
			spawnSingleShip();
	}

	private void removeShip(int i) {
		storedShips.add(new Ship(ships.get(i)));
		ships.remove(i);
	}

	private Ship cloneShip(Ship s) {
		Ship clone = new Ship(s);
		ships.add(clone);
		return clone;
	}

	private void cleanShips() {
		for (int i = 0; i < ships.size(); i++) {
			if (ships.get(i).shouldRemove()) {
				removeShip(i);
				i--;
			}
		}
	}

	/**
	 * Creates a mutant based on a Ship.
	 *
	 * @param oldShip
	 *            Mutant will be based on this ship
	 */
	private void createMutation(Ship oldShip) {
		Ship clone = cloneShip(oldShip);
		clone.mutate();
	}

	/**
	 * Fitness function: Ship.lifeTimer^FIT_EXPONENTIAL.
	 *
	 * @return List with acumulatives percentages of the Ships
	 */
	private List<Double> fitnessFunction() {
		List<Double> lifeTimeList = new ArrayList<>();
		double sumLifes = 0;
		for (Ship s : storedShips) {
			double fitness = Math.pow(s.getLifeTime(), Settings.FIT_EXPONENTIAL);
			lifeTimeList.add(fitness);
			sumLifes += fitness;
		}

		lifeTimeList.set(0, lifeTimeList.get(0) / sumLifes);
		for (int i = 1; i < lifeTimeList.size(); i++) {
			lifeTimeList.set(i, lifeTimeList.get(i - 1) + (lifeTimeList.get(i) / sumLifes));
		}

		return lifeTimeList;
	}

	/**
	 * The Elite Ships, the best from the previous generation, get to clone
	 * themselves into the new generation.
	 */
	private void elitism() {
		for (int i = (storedShips.size() - 1); i >= (storedShips.size() - ELITISM); i--) {
			ships.add(new Ship(storedShips.get(i)));
		}
	}

	private void evolution() {
		ships = new ArrayList<>();
		int[] childOfWho = new int[Settings.NUMBER_OF_SHIPS];

		elitism();

		List<Double> lifeTimeList = fitnessFunction();
		for (int i = 0; i < (Settings.NUMBER_OF_SHIPS - ELITISM); i++) {
			double whoWillReproduce = Math.random();
			int j = 0;
			while (whoWillReproduce > lifeTimeList.get(j))
				j++;
			childOfWho[j]++;
			Ship shipToReproduce = storedShips.get(j);
			createMutation(shipToReproduce);
		}

		if (Settings.DEBUG) {
			stats.printMostChild(childOfWho);
			stats.printGenerationHeader(GENERATION_COUNTER);
			stats.printPopulation(ships);
		}

		storedShips = new ArrayList<>();
	}

	private void updateShips(float dt) {
		for (Ship s : ships) {
			s.closest(food, asteroids);
			s.update(dt);
		}

		cleanShips();

		if (ships.isEmpty()) {
			if (Settings.DEBUG)
				stats.printBestShips(ELITISM, storedShips, Settings.NUMBER_OF_SHIPS);
			evolution();
		}
	}

	/**
	 * Creates a new Asteroid.
	 */
	private void spawnSingleAsteroid() {
		asteroids.add(new Asteroid(Asteroid.LARGE));
	}

	private void spawnAsteroids() {
		while (asteroids.size() < Settings.NUMBER_OF_ASTEROIDS)
			spawnSingleAsteroid();
	}

	private void splitAsteroid(Asteroid a) {
		if (a.getType() == Asteroid.LARGE) {
			asteroids.add(new MediumAsteroid(a.getX(), a.getY()));
			asteroids.add(new MediumAsteroid(a.getX(), a.getY()));
		} else if (a.getType() == Asteroid.MEDIUM) {
			asteroids.add(new SmallAsteroid(a.getX(), a.getY()));
			asteroids.add(new SmallAsteroid(a.getX(), a.getY()));
		}
	}

	private void spawnSingleFood() {
		food.add(new Food());
	}

	private void spawnFood() {
		while (food.size() < Settings.NUMBER_OF_FOOD)
			spawnSingleFood();
	}

	void update(float dt) {
		handleInput();

		updateShips(dt);

		for (int i = 0; i < asteroids.size(); i++) {
			Asteroid a = asteroids.get(i);
			a.update(dt);
			if (a.shouldRemove()) {
				asteroids.remove(i);
				i--;
			}
		}

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
	 */
	private void checkShipsAsteroidsCollisions() {
		for (int i = 0; i < ships.size(); i++) {
			Ship s = ships.get(i);
			for (int j = 0; j < asteroids.size(); j++) {
				Asteroid a = asteroids.get(j);
				if (a.intersects(s)) {
					removeShip(i);
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
					s.setLifeTimer(-2);
					s.setLifeTime(2);
					food.remove(j);
					break;
				}
			}
	}

	/**
	 * Checks all the Ships for collisions with Food or Asteroids.
	 */
	private void checkCollisions() {
		checkShipsFoodCollisions();
		checkShipsAsteroidsCollisions();
	}

	private void drawShips() {
		for (Ship ship : ships)
			ship.draw(sr);
	}

	private void drawAsteroids() {
		for (Asteroid asteroid : asteroids)
			asteroid.draw(sr);
	}

	private void drawFood() {
		for (Food f : food)
			f.draw(sr);
	}

	private void drawStats() {
		stats.draw(ships);
	}

	/**
	 * Draws everything to the screen.
	 */
	void draw() {
		drawShips();
		drawAsteroids();
		drawFood();
		drawStats();
	}

	/**
	 * On click creates an Asteroid at mouse position.
	 */
	private void handleInput() {
		if (Gdx.input.justTouched()) {
			spawnSingleAsteroid();
			Asteroid a = asteroids.get(asteroids.size() - 1);
			a.setX(Gdx.input.getX());
			a.setY(Gdx.input.getY() * -1 + Game.HEIGHT);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			Settings.DEBUG = !Settings.DEBUG;
		}
	}
}
