package minesweeper;

import java.awt.EventQueue;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


/**
 * 
 * @author laurencousin
 * @author cameronlentz
 *
 */
public class Minesweeper {

	/**
	 * Launches the application.
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
	 * @param fileName
	 */
	public void save(String fileName) {
		// TODO: update this with the actual gamestate
		GameState gameState = new GameState(0, 0, null, null, null, 0);
		try {
			OutputStream saveFile = new FileOutputStream(fileName + ".bin");
			OutputStream buffer = new BufferedOutputStream(saveFile);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(gameState);
			output.flush();
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Loads the game from a file.
	 * @param file
	 */
	public void load(File file) {
		GameState gameState;
		
		InputStream saveFile;
		
		try {
			saveFile = new FileInputStream(file);
			InputStream buffer = new BufferedInputStream(saveFile);
			ObjectInput input = new ObjectInputStream(buffer);
			gameState = (GameState) input.readObject();
			
//			this.width = gameState.width;
//			this.height = gameState.height;
//			this.mineLocations = gameState.mineLocations;
//			this.flagLocations = gameState.flagLocations;
//			this.clickedCells = gameState.clickedCells;
			
			input.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Terminates the currently running Minesweeper application.
	 */
	// TODO: Nice to have feature, possibly add:
	// 'Are you sure?' messaging before exiting. Timer pauses but game does not exit yet.
	public static void quit() {
		System.exit(0);
	}

}
