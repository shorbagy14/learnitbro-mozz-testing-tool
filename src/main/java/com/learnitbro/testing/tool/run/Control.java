package com.learnitbro.testing.tool.run;

public class Control {
	
	public static String browser = "chrome";
	private boolean headless = false;
	
	public void start() {
		Operation operation = new Operation(browser);
		operation.setupReport();
		operation.setupDriver(headless);
//		operation.setupWebsite(website);
		operation.setupTest();
	}

}
