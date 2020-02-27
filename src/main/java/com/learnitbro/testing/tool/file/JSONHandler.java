package com.learnitbro.testing.tool.file;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class JSONHandler {
	
	public static void write(File name, String body) {
        try (FileWriter file = new FileWriter(name)) {
 
            file.write(body);
            file.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static String read(File file) throws IOException  {
	    String content = FileUtils.readFileToString(file, "utf-8");
	    return content;
	}

}