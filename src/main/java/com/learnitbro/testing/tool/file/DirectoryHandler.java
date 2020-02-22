package com.learnitbro.testing.tool.file;

import java.io.File;

public class DirectoryHandler {
	
	public File create(String path) {
		File folder = new File(path);
		if (!folder.exists()) {
			File parent = folder.getParentFile();
			if (!parent.exists() && !parent.mkdirs()) {
				throw new IllegalStateException("Couldn't create dir: " + parent);
			}
			if (folder.mkdir()) {
				System.out.println("Directory: " + path + " is created!");
			} else {
				System.out.println("Failed to create directory: " + path);
			}
		} else {
			System.out.println("Directory already exists: " + path);
		}
		return folder;
	}
	
	public boolean delete(File dir) {
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) {
				boolean success = delete(children[i]);
				if (!success) {
					return false;
				}
			}
		} // either file or an empty directory

		System.out.println("removing file or directory : " + dir.getName());
		return dir.delete();
	}
	
	public boolean delete(String dir) {
		return delete(new File(dir));
	}
}