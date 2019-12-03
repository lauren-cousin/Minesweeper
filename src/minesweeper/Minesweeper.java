package minesweeper;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author laurencousin
 * @author cameronlentz
 *
 */
public class Minesweeper implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Launches the application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Board frame = new Board();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initializes a new game with the specified width, height, and number of mines
	 *
	 * @param width    the width of the board in cells
	 * @param height   the height of the board in cells
	 * @param numMines the number of mines in the board
	 */
	public void newGame(int width, int height, int numMines) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Board frame = new Board(width, height, numMines);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Saves the game to a file.
	 *
	 * @param gameState the game state to save
	 */
	public void save(GameState gameState) {
		JFileChooser saveToFile = new JFileChooser();
		int response = saveToFile.showSaveDialog(null);

		if (response == JFileChooser.APPROVE_OPTION) {
			try {
				String path = saveToFile.getSelectedFile().getAbsolutePath();
				String fileName = path + ".ser";

				FileOutputStream saveFile = new FileOutputStream(fileName);
				ObjectOutputStream output = new ObjectOutputStream(saveFile);

				output.writeObject(gameState);
				output.flush();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (response == JFileChooser.CANCEL_OPTION) {
			 JOptionPane.showMessageDialog(null, "Please select a location and file name to save.");
		}
	}

	/**
	 * Returns the game state loaded from a file.
	 *
	 * @return the game state
	 */
	public GameState load() {
		GameState savedGameState;

		JFileChooser loadFromFile = new JFileChooser();
		int response = loadFromFile.showOpenDialog(null);

		if (response == JFileChooser.APPROVE_OPTION) {
			try {
				String path = loadFromFile.getSelectedFile().getAbsolutePath();
	
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(path));
				savedGameState = (GameState) input.readObject();
				input.close();
				return savedGameState;

			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		else if (response == JFileChooser.CANCEL_OPTION) { 
			JOptionPane.showMessageDialog(null, "Please select a file to load.");
		}
		return null;
	}

	/**
	 * Terminates the currently running Minesweeper application.
	 */
	public static void quit() {
		System.exit(0);
	}

}
