package com.search;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class InputDir {

	private JFrame frame;
	private JTextField textDirectoryPath;
	private JTextField textSearchKey;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InputDir window = new InputDir();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public InputDir() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame(Constants.INPUT_PAGE);
		frame.setBounds(100, 100, 570, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton(Constants.SEARCH_BUTTON);
		textDirectoryPath = new JTextField();
		textDirectoryPath.setBounds(150, 36, 236, 38);
		frame.getContentPane().add(textDirectoryPath);
		textDirectoryPath.setColumns(10);
		textSearchKey = new JTextField();
		textSearchKey.setBounds(150, 102, 236, 28);
		frame.getContentPane().add(textSearchKey);
		textSearchKey.setColumns(10);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					SearchEngineOne sr=new SearchEngineOne(textDirectoryPath,textSearchKey);
					sr.setVisible(true);
				}catch(Exception ex){
					
				}
			}
		});
		btnNewButton.setBounds(151, 163, 122, 23);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Directory Path");
		lblNewLabel.setBounds(46, 34, 100, 43);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Search keyword");
		lblNewLabel_1.setBounds(32, 102, 100, 23);
		frame.getContentPane().add(lblNewLabel_1);
		
	}
}