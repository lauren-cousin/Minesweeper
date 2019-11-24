package minesweeper;

import java.awt.EventQueue;
import java.io.File;


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
	public void save(String fileName ) {
		
	}
	
	/**
	 * Loads the game from a file.
	 * @param file
	 */
	public void load(File file) {
		
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
