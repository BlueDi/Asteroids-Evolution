package managers;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Ship;

public class StatisticsTracker {
	private SpriteBatch spriteBatch;

	private BitmapFont font;

	private String bestShipEverStats;
	private float bestShipLifeTime;
	private String currentBestShipStats;

	public StatisticsTracker() {
		font = new BitmapFont();
		spriteBatch = new SpriteBatch();
	}

	public void draw(List<Ship> ships) {
		if (!ships.isEmpty()) {
			Ship currentBestShip = ships.get(ships.size() - 1);
			currentBestShipStats = currentBestShip.toString();
			if (currentBestShip.getLifeTime() > bestShipLifeTime) {
				bestShipLifeTime = currentBestShip.getLifeTime();
				bestShipEverStats = currentBestShip.toString();
			}
		}

		spriteBatch.begin();
		font.setColor(1, 1, 1, 1);
		font.draw(spriteBatch, "Current Best Ship: ", 25, 25 + font.getLineHeight());
		font.draw(spriteBatch, currentBestShipStats, 150, 25 + font.getLineHeight());
		font.draw(spriteBatch, "Best Ship Ever: ", 25, 25);
		font.draw(spriteBatch, bestShipEverStats, 150, 25);
		spriteBatch.end();
	}

	public void printBestShips(int ELITISM, List<Ship> storedShips, int numShips) {
		int i = 0;
		if (ELITISM <= 0) {
			System.out.println("\tBest Ships:");
			while (!storedShips.isEmpty() && i < 3 && i < numShips) {
				i++;
				System.out.println("\t\t" + i + ": " + storedShips.get(storedShips.size() - i) + "; ");
			}
		} else {
			System.out.println("\tElite Ships:");
			while (!storedShips.isEmpty() && i < ELITISM) {
				i++;
				System.out.println("\t\t" + i + ": " + storedShips.get(storedShips.size() - i) + "; ");
			}
		}
	}

	public void printMostChild(int[] numberOfChildren) {
		System.out.println("\tChildren:");
		System.out.print("\t\t");
		boolean found = false;
		for (int i = 0; i < numberOfChildren.length; i++)
			if (numberOfChildren[i] > 2) {
				found = true;
				System.out.print("[" + i + ": " + numberOfChildren[i] + "]; ");
			}
		if (!found)
			System.out.print("All have less than 2 children.");
		System.out.println();
	}

	public void printPopulation(List<Ship> ships) {
		System.out.println("\tPopulation:");
		System.out.print("\t\t");
		for (Ship ship : ships)
			System.out.print(ship + " ");
		System.out.println();
	}

	public void printGenerationHeader(int counter) {
		System.out.println("Generation " + (counter++) + ":");
	}
}
