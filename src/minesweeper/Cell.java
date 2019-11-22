package minesweeper;

import java.awt.Color;

import javax.swing.JButton;

public class Cell extends JButton {

	private boolean mine;
	private boolean flag;
	private boolean revealed;
	private int numAdjacentMines;
	
	/**
	 * Creates a cell with no mine and no flag.
	 */
	public Cell() {
		super();
		
		mine = false;
		flag = false;
		numAdjacentMines = 0;
	}
	
	public Cell(boolean mine, boolean flag) {
		this.mine = mine;
		this.flag = flag;
		numAdjacentMines = 0;
	}
	
	/**
	 * Returns true if and only if there is a mine in this cell.
	 * 
	 * @return whether the cell contains a mine
	 */
	public boolean hasMine() {
		return mine;
	}
	
	/**
	 * Places a mine in this cell. This method is intneded to be used when creating
	 * a game board.
	 */
	public void setMine() {
		mine = true;
	}

	/**
	 * Tells this cell how many mines are adjacent to it in the board. This is not
	 * necessary for cells that have mines.
	 * 
	 * @param numAdjacentMines the number of adjacent mines
	 */
	public void setNumAdjacentMines(int numAdjacentMines) {
		this.numAdjacentMines = numAdjacentMines;
	}

	/**
	 * Returns the number of mines adjacent to this one.
	 * 
	 * @return the number of adjacent mines
	 */
	public int getNumAdjacentMines() {
		return numAdjacentMines;
	}
	
	
	/**
	 * Reveals this cell, unless it is flagged. If the cell is a mine, you lose
	 * the game; otherwise, the cell turns a lighter color and is marked with
	 * the number of adjacent mines (if any).
	 */
	public void reveal() {
		if(flag) return;
		
		revealed = true;
		
		if(mine) {
			setBackground(Color.RED);
			// TODO: Finish lose condition
		} else {
			setBackground(Color.WHITE);
			if(numAdjacentMines == 0) {
				// TODO: Reveal adjacent cells
			} else {
				setText(Integer.toString(numAdjacentMines));
			}
		}
		
	}

	/**
	 * Returns true if this cell has been revealed already.
	 * 
	 * @return whether the cell is revealed
	 */
	public boolean isRevealed() {
		return revealed;
	}
}
