package com.learnitbro.testing.tool.run;

import java.io.File;

import org.json.JSONObject;

import com.learnitbro.testing.tool.events.WebEventListener;
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
		
		JSONObject obj = new JSONObject(content);
		browser = obj.getString("browserName");
		headless = Boolean.valueOf(obj.getString("headless"));
		
		Operation operation = new Operation(browser);
		operation.setupReport();
		operation.setupDriver(headless);
		operation.setupTest(obj);
	}

}
