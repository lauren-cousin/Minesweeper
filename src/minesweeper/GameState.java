package minesweeper;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the state of a game of Minesweeper, which can be read from an
 * active game or from a file. The game state can then be used to write a save
 * file or resume a game.
 *
 * @author cameronlentz
 *
 */

public class GameState implements Serializable {

	private static final long serialVersionUID = 1L;
	private int width;
	private int height;

	/*
	 * Each int[] in these sets is an array of two numbers, an x-coordinate and a
	 * y-coordinate. Thus, the sets track all the coordinate pairs where the cell
	 * has the respective property.
	 */
	private Set<int[]> mineLocations;
	private Set<int[]> flagLocations;
	private Set<int[]> clickedCells;

	private long currentTime;

	/**
	 * Generates a game state from a grid of cells and a timer.
	 *
	 * @param cells the cells in the board
	 * @param timer the game's timer
	 */
	public GameState(Cell[][] cells, GameTimer timer) {
		height = cells.length;
		width = cells[0].length;

		mineLocations = new HashSet<>();
		flagLocations = new HashSet<>();
		clickedCells = new HashSet<>();

		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[0].length; y++) {
				if (cells[x][y].hasMine()) {
					mineLocations.add(new int[] { x, y });
				}
				if (cells[x][y].hasFlag()) {
					flagLocations.add(new int[] { x, y });
				}
				if (cells[x][y].isRevealed()) {
					clickedCells.add(new int[] { x, y });
				}
			}
		}

		currentTime = timer.getTime();
	}

	/**
	 * Constructs a GameState from the width, height, mine and flag locations,
	 * clicked cells, and current time. This can be used to initialize a GameState
	 * after reading these values from a file.
	 *
	 * @param width         the board width
	 * @param height        the board height
	 * @param mineLocations the coordinate pairs (arrays of two integers) where
	 *                      mines are located
	 * @param flagLocations the coordinate pairs (arrays of two integers) where
	 *                      flags have been placed
	 * @param clickedCells  the coordinate pairs (arrays of two integers) where the
	 *                      user has clicked
	 * @param currentTime   the current game time in milliseconds
	 */
	public GameState(int width, int height, Set<int[]> mineLocations, Set<int[]> flagLocations, Set<int[]> clickedCells,
			long currentTime) {
		this.width = width;
		this.height = height;
		this.mineLocations = mineLocations;
		this.flagLocations = flagLocations;
		this.clickedCells = clickedCells;
		this.currentTime = currentTime;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the mineLocations
	 */
	public Set<int[]> getMineLocations() {
		return mineLocations;
	}

	/**
	 * @return the flagLocations
	 */
	public Set<int[]> getFlagLocations() {
		return flagLocations;
	}

	/**
	 * @return the clickedCells
	 */
	public Set<int[]> getClickedCells() {
		return clickedCells;
	}

	/**
	 * @return the currentTime
	 */
	public long getCurrentTime() {
		return currentTime;
	}

}
