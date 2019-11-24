package minesweeper;

import java.io.Serializable;
import java.util.Set;

/**
 * 
 * @author cameronlentz
 *
 */
@SuppressWarnings("serial")
public class GameState implements Serializable {

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
	
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @param mineLocations the mineLocations to set
	 */
	public void setMineLocations(Set<int[]> mineLocations) {
		this.mineLocations = mineLocations;
	}

	/**
	 * @param flagLocations the flagLocations to set
	 */
	public void setFlagLocations(Set<int[]> flagLocations) {
		this.flagLocations = flagLocations;
	}

	/**
	 * @param clickedCells the clickedCells to set
	 */
	public void setClickedCells(Set<Cell> clickedCells) {
		this.clickedCells = clickedCells;
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
	public Set<Cell> getClickedCells() {
		return clickedCells;
	}

}
