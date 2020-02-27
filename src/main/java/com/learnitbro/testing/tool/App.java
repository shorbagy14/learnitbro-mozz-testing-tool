package com.learnitbro.testing.tool;

import java.awt.EventQueue;

import com.learnitbro.testing.tool.window.UI;

public class App {
	
	private String version = "Version : " + getClass().getPackage().getImplementationVersion();
	private String title = "Learn It Bro Testing Tool";
	
	public static String emailList = "shorbagy14@gmail.com";
	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		App app = new App();
		app.printCreds();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI window = new UI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public String getTitle() {
		return title;
	}

	public String getVersion() {
		if (getClass().getPackage().getImplementationVersion() == null)
			version = "DEBUG MODE";
		return version;
	}

	private void printCreds() {
		System.out.print("Copyright Learn It Bro (2020) All Rights Reserved\nMohamed Elshorbagy\nLearn It Bro\n");
		System.out.println(getTitle());
		System.out.println(getVersion());
	}
}