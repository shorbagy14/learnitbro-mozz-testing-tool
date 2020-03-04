package com.learnitbro.testing.tool.run;

import java.io.File;

import org.json.JSONObject;

import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.file.JSONHandler;

public class Control {
	
	public static String browser;
//	public static String version = "80";
	private boolean headless;
	
	public void start() {
		
		String content = null;
		try {
			content = JSONHandler.read(new File(FileHandler.getUserDir() + "/temp/tree.json"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject main = new JSONObject(content);
		JSONObject obj = main.getJSONArray("children").getJSONObject(0);
		browser = obj.getJSONArray("browserName").getString(0);
		headless = Boolean.valueOf(obj.getJSONArray("headless").getString(0));
		
		Operation operation = new Operation(browser);
		operation.setupReport();
		operation.setupDriver(headless);
		operation.setupTest(obj);
	}
}
