package minesweeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * 
 * @author laurencousin
 *
 */
public class GameTimer {
	private String time = "00";
	private Timer timer;
	public JLabel lblTimer;
	
	private long startTime;
	
	public GameTimer() {
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getTime();
			};
		});
	}
	
	public JLabel createLblTimer() {
		// TODO: Margins, font
		lblTimer = new JLabel("");
		lblTimer.setOpaque(true);
		lblTimer.setIcon(Board.TIMER_ICON);
		lblTimer.setText(time);
		lblTimer.setHorizontalAlignment(SwingConstants.CENTER);
		return lblTimer;
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
		timer.setInitialDelay(1000);
		timer.start();
	}
	
	public void stop() {
		timer.stop();
	}
	
	public void getTime() {
		long currentTime = System.currentTimeMillis();
		long elapsedTime = (currentTime - startTime) / 1000;

		if(elapsedTime < 10) {
			lblTimer.setText("0" + String.valueOf(elapsedTime));
		}
		else {
			lblTimer.setText(String.valueOf(elapsedTime));
		}
	}

}
