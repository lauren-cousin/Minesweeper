package minesweeper;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Represents a single cell in a Minesweeper game board.
 * <p>
 * The cell may contain a hidden mine, and the user may mark it with a flag or
 * click it to reveal whether it has a mine. If it has a mine, revealing it
 * causes the cell to turn red; otherwise, it displays the number of adjacent
 * cells which have mines.
 * 
 * @author cameronlentz
 *
 */
@SuppressWarnings("serial")
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
	
	/**
	 * Creates a cell with parameters, to be used when restoring a saved game.
	 * 
	 * @param mine whether this cell has a mine
	 * @param flag whether this cell is marked with a flag
	 * @param revealed whether this cell has been revealed
	 */
	public Cell(boolean mine, boolean flag, boolean revealed) {
		this.mine = mine;
		this.flag = flag;
		this.revealed = revealed;
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
	 * Places a mine in this cell. This method is used when creating
	 * a game board.
	 */
	public void setMine() {
		mine = true;
	}

	/**
	 * Tells this cell how many mines are adjacent to it in the board. If this
	 * cell has a mine, the number does not matter so long as it is not 0.
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
	 * Returns true if this cell has been revealed already.
	 * 
	 * @return whether the cell is revealed
	 */
	public boolean isRevealed() {
		return revealed;
	}

	/**
	 * Toggles whether there is a flag on this cell, unless the cell is already
	 * revealed.
	 */
	public void toggleFlag() {
		if(!revealed) {
			flag = !flag;
			setIcon(flag? Board.FLAG_ICON : null);
		}
	}
	
	/**
	 * Returns true if and only if this cell is marked with a flag.
	 * 
	 * @return whether there is a flag on this cell
	 */
	public boolean hasFlag() {
		return flag;
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
		} else {
			setBackground(Color.WHITE);
			if(numAdjacentMines != 0) {
				setIcon(new ImageIcon(Cell.class.getResource(
						"/resources/" + numAdjacentMines + ".png")));
				//setText(Integer.toString(numAdjacentMines));
			}
		}
		
	}
}
