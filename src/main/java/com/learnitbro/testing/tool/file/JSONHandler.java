package com.learnitbro.testing.tool.file;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHandler {
	
	/**
	 * Writes the JSON string into a file
	 * 
	 * @param name - location of the file to be written to
	 * @param body - JSON string
	 */
	public static void write(File name, String body) {
        try (FileWriter file = new FileWriter(name)) {
 
            file.write(body);
            file.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * Returns the JSON as a string
	 * 
	 * @param file
	 * @return string
	 * @throws Exception
	 */
	public static String read(File file) throws Exception  {
	    String content = FileUtils.readFileToString(file, "utf-8");
	    return content;
	}
	
	/**
	 * Returns true if the file is a valid JSON
	 * 
	 * @param file
	 * @return boolean
	 */
	public static boolean isJSONValid(String file) {
	    try {
	        new JSONObject(file);
	    } catch (JSONException ex) {
	        try {
	            new JSONArray(file);
	        } catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}
}