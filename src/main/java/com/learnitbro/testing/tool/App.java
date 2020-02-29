package com.learnitbro.testing.tool;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.FileUtils;

import com.learnitbro.testing.tool.exceptions.CopyFileException;
import com.learnitbro.testing.tool.exceptions.JSONFileNotValidException;
import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.file.JSONHandler;
import com.learnitbro.testing.tool.run.Control;
import com.learnitbro.testing.tool.window.UI;

public class App {

	private String version = "Version : " + getClass().getPackage().getImplementationVersion();
	private String title = "Learn It Bro Testing Tool";

	/**
	 * Launch the application.
	 * 
	 */
	public static void main(String[] args) throws Exception {
		App app = new App();
		app.printCreds();

		if (args.length == 0) {
			System.out.println("UI mode activated");
			app.ui();
		} else if (args.length == 1) {
			app.console(args[0]);
		} else {
			throw new RuntimeException("Incorrect number of arguments");
		}
	}

	/**
	 * Launches the UI to allow user to create the test cases
	 */
	private void ui() {
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

	/**
	 * Launches the test thru the console
	 * 
	 * @param file - JSON file location
	 * @throws Exception
	 */
	private void console(String file) throws Exception {
		System.out.println("Console mode activated");
		
		File source = new File(file);
		File dest = new File(FileHandler.getUserDir() + "/tree.json");

		if (source.exists()) {
			if (JSONHandler.isJSONValid(JSONHandler.read(source))) {
				try {
					FileUtils.copyFile(source, dest);
				} catch (Exception e) {
					throw new CopyFileException("Failed to copy file");
				}
				Control control = new Control();
				control.start();
			} else {
				throw new JSONFileNotValidException("JSON file is not valid");
			}
		} else {
			throw new FileNotFoundException(String.format("File : '%s' is not found", file));
		}
	}

	/**
	 * Returns the title of the project
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the version of the project
	 * 
	 * @return version
	 */
	public String getVersion() {
		if (getClass().getPackage().getImplementationVersion() == null)
			version = "DEBUG MODE";
		return version;
	}

	/**
	 * Prints the credentials of the project
	 */
	private void printCreds() {
		System.out.print("Copyright Learn It Bro (2020) All Rights Reserved\nMohamed Elshorbagy\nLearn It Bro\n");
		System.out.println(getTitle());
		System.out.println(getVersion());
	}
}