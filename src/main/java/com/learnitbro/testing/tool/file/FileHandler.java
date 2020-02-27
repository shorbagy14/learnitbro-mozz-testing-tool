package com.learnitbro.testing.tool.file;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

public class FileHandler {
	
	public File gettResourceAsFile(String fileName) throws IOException {
		InputStream initialStream = getClass().getResourceAsStream(fileName);
		String dir = System.getProperty("user.dir");
		File outputfile = new File(String.format("%s%s", dir, fileName));
		Files.copy(initialStream, outputfile.toPath());
		return outputfile;
	}

	public String getDownloadedFileAsString(String FILE_NAME, String FILE_URL) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
		String dir = System.getProperty("user.dir");
		DirectoryHandler d = new DirectoryHandler();
		d.create(String.format("%s/assets/", dir));
		String outputfile = String.format("%s/assets%s", dir, FILE_NAME);
		
		FileOutputStream fileOutputStream = new FileOutputStream(outputfile);
		byte dataBuffer[] = new byte[1024];
		int bytesRead;
		while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
			fileOutputStream.write(dataBuffer, 0, bytesRead);
		}
		fileOutputStream.close();
		return outputfile;

	}

	public File create(String path) {
		File file = new File(path);
		System.out.println(path);
		if (!file.exists()) {
			try {
				File parent = file.getParentFile();
				if (!parent.exists() && !parent.mkdirs()) {
					throw new IllegalStateException("Couldn't create dir: " + parent);
				}
				if (file.createNewFile()) {
					System.out.println("File: " + path + " is created!");
				} else {
					System.out.println("Failed to create file: " + path);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("File already exists: " + path);
		}
		return file;
	}
	
	public static String getUserDir() {
		return System.getProperty("user.dir");
	}

	public static String getUserHome() {
		return System.getProperty("user.home");
	}

	public static String getUserName() {
		return System.getProperty("user.name");
	}

	public static boolean isValidFile(File file) {
		return file.isFile() && !file.isHidden() && !file.getName().startsWith(".");
	}

	public static boolean isValidImage(File file) {
		return file.getName().toLowerCase().contains(".jpg")
				|| file.getName().toLowerCase().contains(".png") && isValidFile(file);
	}
}