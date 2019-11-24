package minesweeper;

import java.util.Set;

/**
 * 
 * @author cameronlentz
 *
 */
public class GameState {
	private int width;
	private int height;
	private Set<int[]> mineLocations;
	private Set<int[]> flagLocations;
	private Set<Cell> clickedCells;
	
	/**
	 * @param width
	 * @param height
	 * @param mineLocations
	 * @param flagLocations
	 * @param clickedCells
	 */
	public GameState(int width, int height, Set<int[]> mineLocations, Set<int[]> flagLocations,
			Set<Cell> clickedCells) {
		this.width = width;
		this.height = height;
		this.mineLocations = mineLocations;
		this.flagLocations = flagLocations;
		this.clickedCells = clickedCells;
	}

}
