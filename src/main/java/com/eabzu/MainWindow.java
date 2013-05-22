package com.eabzu;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class MainWindow {

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		_frame = new JFrame("Command Tester");
		_frame.setLocationByPlatform(true);
		_frame.setBounds(100, 100, 450, 300);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		_frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
	}

	public boolean isVisible() {
		return (_frame != null) 
					? false 
					: _frame.isVisible();
	}
	public void setVisible(boolean b) {
		if (_frame != null)
			_frame.setVisible(true);
	}

	private JFrame _frame;
}
