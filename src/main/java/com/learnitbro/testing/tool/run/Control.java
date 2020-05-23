package com.learnitbro.testing.tool.run;

import java.io.File;

import org.json.JSONObject;

import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.file.JSONHandler;

public class Control {

	public static String browser;
	private String platform;
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
		platform = obj.getJSONArray("platform").getString(0);
		headless = Boolean.valueOf(obj.getJSONArray("headless").getString(0));

		if (platform.equalsIgnoreCase("web")) {
			Operation operation = new Operation(browser);
			operation.setupReport();
			operation.setupDriver(headless);
			operation.setupTest(obj);
		} else if (platform.equalsIgnoreCase("mobile web")) {
			Mobile mobile = new Mobile(browser);
			mobile.startServer();
			mobile.setupReport();
			mobile.setupDriver();
			mobile.setupTest(obj);
			mobile.stopServer();
		}
	}
}
