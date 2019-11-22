package minesweeper;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import javax.swing.JToolBar;

public class Board extends JFrame {

	private JPanel contentPane;
	private Cell[][] cells;
	private int numMines;
	private Status status;
	private Set<Cell> clickedCells;
	private static Random rand = new Random();
	
	private final int CELL_WIDTH = 42;
	private final int CELL_HEIGHT = 40;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Use parameterized constructor to experiment with
					// different board sizes and mine amounts
					Board frame = new Board();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Creates the board with the default parameters (9 cells by 9 cells, with 10 mines).
	 */
	public Board() {
		this(9, 9, 10);
	}

	/**
	 * Create the frame.
	 */
	public Board(int width, int height, int numMines) {
		if(width < 3 || height < 3) {
			throw new IllegalArgumentException("Width and height of board must "
					+ "be at least 3");
		}
		if(numMines < width * height / 20) {
			throw new IllegalArgumentException("Too few mines for the selected "
					+ "board size (should have at least " + width * height / 20
					+ " for " + width + "*" + height + " board)");
		}
		if(numMines > width * height / 2) {
			throw new IllegalArgumentException("Too many mines for the selected "
					+ "board size (should have at most " + width * height / 2
					+ " for " + width + "*" + height + " board)");
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, CELL_WIDTH * width + 30, CELL_HEIGHT * height + 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = createButtonGrid(width, height);
		contentPane.add(panel, BorderLayout.CENTER);
		
		JPanel bottomPanel = createBottomPanel();
		
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		this.numMines = numMines;
		placeMines(numMines);
		calculateNumAdjacentMines();
	}

	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: Start timer, remove button etc
				status = Status.INPROGRESS;
			}
		});
		bottomPanel.add(btnStart);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: Quit game
			}
		});
		bottomPanel.add(btnQuit);
		
		return bottomPanel;
	}

	private JPanel createButtonGrid(int width, int height) {
		cells = new Cell[width][height];
		
		JPanel cellGrid = new JPanel();
		cellGrid.setPreferredSize(new Dimension(CELL_WIDTH * width, CELL_HEIGHT * height));
		cellGrid.setLayout(new GridLayout(cells.length, cells[0].length, 0, 0));
		
		for(int x = 0; x < cells.length; x++) {
			for(int y = 0; y < cells.length; y++) {
				cells[x][y] = new Cell();
				
				// The action listener requires its local variables to be final
				final int thisX = x;
				final int thisY = y;
				
				cells[x][y].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(status == Status.INPROGRESS) {
							cells[thisX][thisY].reveal();
							
							if(cells[thisX][thisY].getNumAdjacentMines() == 0) {
								// Auto-reveal cells around a "0"
								revealAround(thisX, thisY);
							}
							
							// TODO: Check whether game is won or lost
						}
					}
				});
				cellGrid.add(cells[x][y]);
			}
		}
		
		/* The content pane uses BorderLayout, but we don't want the grid to
		 * expand to fill the whole center area, so we place the grid in a new
		 * JPanel (which uses FlowLayout and won't resize its contents) 
		 */
		JPanel gridContainer = new JPanel();
		gridContainer.add(cellGrid);
		
		return gridContainer;
	}
	
	private void revealAround(int x, int y) {
		for(int xOff = -1; xOff <= 1; xOff++) {
			for(int yOff = -1; yOff <= 1; yOff++) {
				if((x + xOff < 0) || (x + xOff >= cells.length) ||
						(y + yOff < 0) || (y + yOff >= cells[0].length))
					continue;
				
				Cell thisCell = cells[x + xOff][y + yOff];
				if(!thisCell.isRevealed()) {
					thisCell.reveal();
					// If we newly uncovered a "0", reveal around that as well
					if(thisCell.getNumAdjacentMines() == 0) {
						revealAround(x + xOff, y + yOff);
					}
				}
			}
		}
	}
	
	/**
	 * Tells each cell in the board how many mines are adjacent to it.
	 */
	private void calculateNumAdjacentMines() {
		// For each non-mine cell in the board,
		for(int x = 0; x < cells.length; x++) {
			for(int y = 0; y < cells.length; y++) {
				if(cells[x][y].hasMine()) {
					cells[x][y].setNumAdjacentMines(-1);
					continue;
				}
				
				int count = 0;
				// look at the neighborhood of surrounding cells,
				for(int xOff = -1; xOff <= 1; xOff++) {
					for(int yOff = -1; yOff <= 1; yOff++) {
						// (ignoring cells outside of the board)
						if((x + xOff < 0) || (x + xOff >= cells.length) ||
								(y + yOff < 0) || (y + yOff >= cells[0].length))
							continue;
						
						// and increment count for each one that has a mine.
						if(cells[x + xOff][y + yOff].hasMine())
							count++;
					}
				}
				
				cells[x][y].setNumAdjacentMines(count);
			}
		}
	}

	/**
	 * Places mines in the grid, ensuring that the same cell isn't chosen twice
	 * (resulting in too few mines).
	 * 
	 * @param minesToPlace the number of cells which will have a mine added
	 */
	private void placeMines(int minesToPlace) {
		int numPlaced = 0;
		while(numPlaced < minesToPlace) {
			int x = rand.nextInt(cells.length);
			int y = rand.nextInt(cells[0].length);
			if(!cells[x][y].hasMine()) {
				cells[x][y].setMine();
				numPlaced++;
			}
		}
	}

}
