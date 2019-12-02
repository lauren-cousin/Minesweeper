package minesweeper;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JFileChooser;

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
	 * Saves the game to a file.
	 * 
	 * @param fileName
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

				System.out.println("output: " + output);
				System.out.println("game state: " + gameState);

				output.writeObject(gameState);
				output.flush();
				output.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Loads the game from a file.
	 * 
	 * @param file
	 */
	public void load(GameState gameState) {
		GameState savedGameState;

		JFileChooser loadFromFile = new JFileChooser();
		int response = loadFromFile.showOpenDialog(null);

		if (response == JFileChooser.APPROVE_OPTION) {
			try {
				String path = loadFromFile.getSelectedFile().getAbsolutePath();

				ObjectInputStream input = new ObjectInputStream(new FileInputStream(path));
				savedGameState = (GameState) input.readObject();
				System.out.println(savedGameState.toString());

				gameState.setWidth(savedGameState.getWidth());
				gameState.setHeight(savedGameState.getHeight());
				gameState.setMineLocations(savedGameState.getMineLocations());
				gameState.setFlagLocations(savedGameState.getFlagLocations());
				gameState.setClickedCells(savedGameState.getClickedCells());
				gameState.setCurrentTime(savedGameState.getCurrentTime());

				input.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Terminates the currently running Minesweeper application.
	 */
	// TODO: Nice to have feature, possibly add:
	// 'Are you sure?' messaging before exiting. Timer pauses but game does not exit
	// yet.
	public static void quit() {
		System.exit(0);
	}

}
