package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

/**
 * Represents a board for the game Minesweeper, displaying the title of the
 * game, a timer, a button for marking cells with flags, and a grid of
 * cells.<br/>
 * A start button and a quit button are displayed below the grid.<br/>
 * There are also buttons to load a saved game, or save your current game.
 *
 * @author cameronlentz
 * @author laurencousin
 *
 */

public class Board extends JFrame implements ActionListener, Serializable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Cell[][] cells;
	private int width, height;
	private int numMines;

	private Status status;
	private boolean flagging = false;
	private boolean tempFlagging = false;

	private static Random rand = new Random();

	private static final int CELL_WIDTH = 35;
	private static final int CELL_HEIGHT = 35;
	static final Icon FLAG_ICON = new ImageIcon(Cell.class.getResource("/resources/flag.png"));
	static final Icon TIMER_ICON = new ImageIcon(GameTimer.class.getResource("/resources/hourglass.png"));

	private GameTimer gameTimer = new GameTimer();
	private JButton btnStart;
	private JPanel grid;

	private JMenuItem save;
	private JMenuItem load;
	private JMenuItem updateBoardDifficulty;
	private JMenuItem howToPlay;

	/**
	 * Creates the board with the default parameters (9 cells by 9 cells, with 10
	 * mines).
	 */
	public Board() {
		this(9, 9, 10);
	}

	/**
	 * Creates the frame.
	 */
	public Board(int width, int height, int numMines) {
		if (width < 3 || height < 3) {
			throw new IllegalArgumentException("Width and height of board must " + "be at least 3");
		}
		if (numMines < width * height / 20 + 1) {
			throw new IllegalArgumentException("Too few mines for the selected " + "board size (should have at least "
					+ width * height / 20 + 1 + " for " + width + "*" + height + " board)");
		}
		if (numMines > width * height / 2) {
			throw new IllegalArgumentException("Too many mines for the selected " + "board size (should have at most "
					+ width * height / 2 + " for " + width + "*" + height + " board)");
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, CELL_WIDTH * width + 30, CELL_HEIGHT * height + 150);
		setTitle("Minesweeper");
		setJMenuBar(createMenuBar());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel topPanel = createTopPanel();
		contentPane.add(topPanel, BorderLayout.NORTH);

		/*
		 * Technically this grid is just for show, since the "Start"/"Play again" button
		 * replaces the current grid with a new one.
		 */
		grid = createButtonGrid(width, height);
		contentPane.add(grid, BorderLayout.CENTER);

		JPanel bottomPanel = createBottomPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);

		pack();

		this.width = width;
		this.height = height;
		this.numMines = numMines; // Mines are placed when user clicks Start
	}

	/**
	 * Creates the top panel, containing the game title, timer, and flag button.
	 *
	 * @return the top panel
	 */
	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();

		JButton btnFlag = new JButton();
		btnFlag.setBackground(Color.WHITE);
		btnFlag.setOpaque(true);
		btnFlag.setIcon(FLAG_ICON);
		btnFlag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (status == Status.INPROGRESS) {
					flagging = !flagging;
					btnFlag.setBackground(flagging ? Color.GRAY : Color.WHITE);
				}
			}
		});

		topPanel.add(gameTimer.createLblTimer());
		topPanel.add(createTitle());
		topPanel.add(btnFlag);

		return topPanel;
	}

	/**
	 * Creates the bottom panel, containing the Start button and Quit button.
	 *
	 * @return the bottom panel
	 */
	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel();

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status = Status.INPROGRESS;
				btnStart.setVisible(false);

				// Remove the existing game grid and replace it with a new one
				contentPane.remove(grid);
				grid = createButtonGrid(width, height);
				contentPane.add(grid, BorderLayout.CENTER);
				placeMines(numMines);
				calculateNumAdjacentMines();

				// Start timer
				gameTimer.start();
			}
		});
		bottomPanel.add(btnStart);

		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameTimer.stop();
				Minesweeper.quit();
			}
		});
		bottomPanel.add(btnQuit);

		return bottomPanel;
	}

	/**
	 * Creates the button grid, containing a grid of clickable cells.
	 *
	 * @param width  the number of columns in the grid
	 * @param height the number of rows in the grid
	 * @return the button grid
	 */
	private JPanel createButtonGrid(int width, int height) {
		return createButtonGrid(width, height, new HashSet<int[]>(), new HashSet<int[]>(), new HashSet<int[]>());
	}

	/**
	 * Creates the button grid with cells whose attributes are defined by the
	 * parameters.
	 *
	 * @param width         the number of columns in the grid
	 * @param height        the number of rows in the grid
	 * @param mineLocations a set of arrays representing coordinate pairs where
	 *                      mines are hidden
	 * @param flagLocations a set of arrays representing coordinate pairs where
	 *                      flags have been placed
	 * @param clickedCells  a set of arrays representing coordinate pairs where the
	 *                      cell has been revealed
	 * @return the button grid
	 */
	private JPanel createButtonGrid(int width, int height, Set<int[]> mineLocations, Set<int[]> flagLocations,
			Set<int[]> clickedCells) {
		/*
		 * Each element in cells is an array representing a horizontal row, so the first
		 * number here is the height (number of rows).
		 */
		cells = new Cell[height][width];

		JPanel cellGrid = new JPanel();
		cellGrid.setPreferredSize(new Dimension(CELL_WIDTH * width, CELL_HEIGHT * height));
		cellGrid.setLayout(new GridLayout(cells.length, cells[0].length, 0, 0));

		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[0].length; y++) {
				// Construct cell based on whether these coordinates are in the sets.
				cells[x][y] = new Cell(setHasCoords(mineLocations, x, y), setHasCoords(flagLocations, x, y),
						setHasCoords(clickedCells, x, y));

				// The action listener requires its local variables to be final
				final int thisX = x;
				final int thisY = y;

				cells[x][y].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (status == Status.INPROGRESS) {
							if (flagging || tempFlagging) {
								cells[thisX][thisY].toggleFlag();
								tempFlagging = false;
							} else if (!cells[thisX][thisY].hasFlag()) {
								if (cells[thisX][thisY].isRevealed()) {
									/*
									 * If a cell is revealed and displaying a number, and you've already placed that
									 * many flags next to it, clicking the number will "quick-reveal" the rest of
									 * the adjacent cells.
									 */
									tryQuickReveal(thisX, thisY);
								} else {
									cells[thisX][thisY].reveal();
								}

								if (cells[thisX][thisY].getNumAdjacentMines() == 0) {
									// Auto-reveal cells around a "0"
									revealAround(thisX, thisY);
								}

								checkGameState();
							}
						}
					}
				});

				/*
				 * Right-click detection. JButton's ActionListener is only triggered by a left
				 * click, so we use a MouseListener to detect right clicks.
				 */
				cells[x][y].addMouseListener(new MouseListener() {
					/*
					 * mouseClicked won't detect a click if the mouse is moved at all while the
					 * button is held, which feels clunky and unresponsive. Instead I use the other
					 * methods and a tempFlagging boolean for a nicer-feeling result.
					 */
					@Override
					public void mouseClicked(MouseEvent e) {
					}

					@Override
					public void mousePressed(MouseEvent e) {
						if (status == Status.INPROGRESS && SwingUtilities.isRightMouseButton(e))
							tempFlagging = true;
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						if (status == Status.INPROGRESS && SwingUtilities.isRightMouseButton(e)) {
							if (tempFlagging)
								cells[thisX][thisY].toggleFlag();
							tempFlagging = false;
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// Dragging out of the cell cancels the flag attempt.
						if (status == Status.INPROGRESS)
							tempFlagging = false;
					}
				});

				cellGrid.add(cells[x][y]);
			}
		}

		/*
		 * The content pane uses BorderLayout, but we don't want the grid to expand to
		 * fill the whole center area, so we place the grid in a new JPanel (which uses
		 * FlowLayout and won't resize its contents).
		 */
		JPanel gridContainer = new JPanel();
		gridContainer.add(cellGrid);

		return gridContainer;
	}

	/**
	 * Returns true if the set of int arrays contains one representing the given
	 * coordinates. (Set.contains() doesn't work as expected for this.)
	 *
	 * @param set
	 * @param x
	 * @param y
	 * @return true if the coordinates are in the set
	 */
	private boolean setHasCoords(Set<int[]> set, int x, int y) {
		for (int[] intArr : set) {
			if (intArr.length == 2 && intArr[0] == x && intArr[1] == y)
				return true;
		}

		return false;
	}

	/**
	 * Creates the title of the game to be displayed.
	 *
	 * @return the title
	 */
	private JLabel createTitle() {
		JLabel title = new JLabel("Minesweeper");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBorder(new EmptyBorder(5, 40, 5, 40));
		title.setFont(new Font("Tahoma", Font.PLAIN, 18));
		return title;
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenu settingsMenu = new JMenu("Settings");
		JMenu helpMenu = new JMenu("Help");

		save = new JMenuItem("Save");
		load = new JMenuItem("Load");
		updateBoardDifficulty = new JMenuItem("Update Board Difficulty");
		howToPlay = new JMenuItem("How to Play");

		fileMenu.add(save);
		fileMenu.add(load);
		settingsMenu.add(updateBoardDifficulty);
		helpMenu.add(howToPlay);

		menuBar.add(fileMenu);
		menuBar.add(settingsMenu);
		menuBar.add(helpMenu);

		// add ActionListener to JMenuItems
		save.addActionListener(this);
		load.addActionListener(this);
		updateBoardDifficulty.addActionListener(this);
		howToPlay.addActionListener(this);

		return menuBar;
	}

	/**
	 * Reveals around the coordinates passed in as parameters.
	 *
	 * @param x
	 * @param y
	 */
	private void revealAround(int x, int y) {
		// This method can't use adjacentCells() because it needs to keep track
		// of each adjacent cell's coordinates for the recursive call.
		for (int xOff = -1; xOff <= 1; xOff++) {
			for (int yOff = -1; yOff <= 1; yOff++) {
				if ((x + xOff < 0) || (x + xOff >= cells.length) || (y + yOff < 0) || (y + yOff >= cells[0].length))
					continue;

				Cell thisCell = cells[x + xOff][y + yOff];
				if (!thisCell.isRevealed() && !thisCell.hasFlag()) {
					thisCell.reveal();
					// If we newly uncovered a "0", reveal around that as well
					if (thisCell.getNumAdjacentMines() == 0) {
						revealAround(x + xOff, y + yOff);
					}
				}
			}
		}
	}

	/**
	 * Attempts to "quick-reveal" around the cell at the given coordinates. If the
	 * cell at the coordinates is showing a number and already has that many flags
	 * around it, the remaining adjacent cells are automatically revealed.
	 *
	 * @param x
	 * @param y
	 */
	private void tryQuickReveal(int x, int y) {
		if (!cells[x][y].isRevealed())
			return;

		int flagCount = 0;
		for (Cell c : adjacentCells(x, y)) {
			if (c.hasFlag())
				flagCount++;
		}

		if (flagCount == cells[x][y].getNumAdjacentMines()) {
			revealAround(x, y);
		}
	}

	/**
	 * Returns a set of up to 8 cells adjacent to the given coordinates.
	 *
	 * @param x
	 * @param y
	 * @return a set of up to 8 cells
	 */
	private Set<Cell> adjacentCells(int x, int y) {
		Set<Cell> adjCells = new HashSet<>();
		for (int xOff = -1; xOff <= 1; xOff++) {
			for (int yOff = -1; yOff <= 1; yOff++) {
				// Ignore cells outside of the board
				if ((x + xOff < 0) || (x + xOff >= cells.length) || (y + yOff < 0) || (y + yOff >= cells[0].length))
					continue;

				adjCells.add(cells[x + xOff][y + yOff]);
			}
		}
		adjCells.remove(cells[x][y]);
		return adjCells;
	}

	/**
	 * Tells each cell in the board how many mines are adjacent to it.
	 */
	private void calculateNumAdjacentMines() {
		// For each non-mine cell in the board
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[0].length; y++) {
				if (cells[x][y].hasMine()) {
					// The value for cells with mines doesn't matter as long as
					// it's non-zero
					cells[x][y].setNumAdjacentMines(-1);
					continue;
				}

				int count = 0;
				for (Cell c : adjacentCells(x, y)) {
					if (c.hasMine())
						count++;
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
		while (numPlaced < minesToPlace) {
			int x = rand.nextInt(cells.length);
			int y = rand.nextInt(cells[0].length);
			if (!cells[x][y].hasMine()) {
				cells[x][y].setMine();
				numPlaced++;
			}
		}
	}

	/**
	 * Checks the state of the game.
	 */
	private void checkGameState() {
		boolean isWon = true;
		for (Cell[] row : cells) {
			for (Cell c : row) {
				if ((!c.hasMine()) && (!c.isRevealed())) {
					// Unrevealed cell with no mine; game is not won yet
					isWon = false;
					/*
					 * Continue rather than break because we still need to check remaining cells for
					 * revealed mines
					 */
					continue;
				}
				if (c.hasMine() && c.isRevealed()) {
					// Mine was revealed
					status = Status.LOSE;
					gameTimer.stop();
					// Reveal all mines & wrongly placed flags to the user
					for (int x = 0; x < cells.length; x++) {
						for (int y = 0; y < cells[0].length; y++) {
							if (cells[x][y].hasMine()) {
								cells[x][y].reveal();
							} else if (cells[x][y].hasFlag()) {
								// Safe cell with a flag
								cells[x][y].setIcon(new ImageIcon(Cell.class.getResource("/resources/missedflag.png")));
							}
						}
					}
					break;
				}
			}
		}
		if (isWon) {
			status = Status.WIN;
			gameTimer.stop();
			// Mark remaining mines and turn the board a pale green
			for (Cell[] row : cells) {
				for (Cell c : row) {
					if (c.isRevealed())
						c.setBackground(new Color(230, 255, 200));
					else if (c.hasMine())
						c.setIcon(FLAG_ICON);
				}
			}
		}

		if (status != Status.INPROGRESS) {
			btnStart.setText("Play again?");
			btnStart.setVisible(true);
		}
	}

	/**
	 * Returns the current state of the game as a GameState object.
	 *
	 * @return the current game state
	 */
	public GameState getGameState() {
		return new GameState(cells, gameTimer);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Minesweeper game = new Minesweeper();

		// Save game
		if (e.getSource() == save) {
			System.out.println("saving");
			game.save(getGameState()); // TODO: get user input here
		}
		// Load game
		else if (e.getSource() == load) {
			System.out.println("loading");
			contentPane.remove(grid);
			contentPane.invalidate();
			GameState gameState = game.load();
			grid = createButtonGrid(gameState.getWidth(), gameState.getHeight(), gameState.getMineLocations(),
					gameState.getFlagLocations(), gameState.getClickedCells());
			calculateNumAdjacentMines();
			contentPane.add(grid, BorderLayout.CENTER);
			contentPane.validate();
			contentPane.repaint();
		}
		// Change difficulty
		else if (e.getSource() == updateBoardDifficulty) {
			Minesweeper minesweeper = new Minesweeper();
			int newWidth;
			int newHeight;
			int newMines;

			Object[] options = { "Easy", "Medium", "Hard" };
			String s = (String) JOptionPane.showInputDialog(contentPane, "Select game difficulty:\n",
					"Select game difficulty", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

			if ((s != null) && (s.length() > 0)) {
				try {
					if (s.toString().toLowerCase().contentEquals("easy")) {
						newWidth = 9;
						newHeight = 9;
						newMines = 10;
						dispose();
						minesweeper.newGame(newWidth, newHeight, newMines);
					} else if (s.toString().toLowerCase().contentEquals("medium")) {
						System.out.println("medium");
						newWidth = 15;
						newHeight = 15;
						newMines = 111;
						dispose();
						minesweeper.newGame(newWidth, newHeight, newMines);
					} else if (s.toString().toLowerCase().contentEquals("hard")) {
						newWidth = 20;
						newHeight = 20;
						newMines = 150;
						dispose();
						minesweeper.newGame(newWidth, newHeight, newMines);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		// How to play
		else if (e.getSource() == howToPlay) {
			JOptionPane.showMessageDialog(contentPane, "The game contains a number of unmarked "
					+ "square buttons in a grid, and some of these squares contain hidden mines. \n\n"
					+ "Click squares to reveal them. \n\n"
					+ "The goal is to reveal all of the safe squares without clicking any mines. \n\n"
					+ "When a safe square is revealed, it is labeled with the number of mines in the 8 "
					+ "surrounding squares. \n\n" + "If you suspect that a square has a mine, mark it with a flag, "
					+ "which prevents it from being accidentally revealed. \n\n"
					+ "A timer keeps track of how long you've been playing. \n\n"
					+ "To mark a cell with a flag, click the flag button then the cells you want to " + "mark. \n\n",
					"How to play Minesweeper", JOptionPane.INFORMATION_MESSAGE);
		}

	}

}
