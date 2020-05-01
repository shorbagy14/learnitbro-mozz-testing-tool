package com.learnitbro.testing.tool.run;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class Mobile {

	private AppiumDriverLocalService service;
	private AppiumServiceBuilder builder;
	private DesiredCapabilities cap;
	
	public void startServer() {
		//Set Capabilities
		cap = new DesiredCapabilities();
		cap.setCapability("noReset", "false");
		
		//Build the Appium service
		builder = new AppiumServiceBuilder();
		builder.withIPAddress("127.0.0.1");
		builder.usingPort(4723);
		builder.withCapabilities(cap);
		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
		builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");
		
		//Start the server with the builder
		service = AppiumDriverLocalService.buildService(builder);
		service.start();
	}
	
	public void stopServer() {
		service.stop();
	}
 
	public boolean checkIfServerIsRunnning(int port) {
		
		boolean isServerRunning = false;
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.close();
		} catch (IOException e) {
			//If control comes here, then it means that the port is in use
			isServerRunning = true;
		} finally {
			serverSocket = null;
		}
		return isServerRunning;
	}	
	
	private void launchAndroidApp() {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "My Phone");
		caps.setCapability("udid", "ABCD");
		caps.setCapability("platformName", "Android");
		caps.setCapability("platformVersion", "6.0");
		caps.setCapability("appPackage", "com.android.learnitbro");
		caps.setCapability("appActivity", "com.google.android.finsky.activities.MainActivity");
		caps.setCapability("noReset", "true");
		
		//Instantiate Appium Driver
		try {
				AppiumDriver<MobileElement> driver = new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:4723/wd/hub"), caps);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}
	}
 
//	public static void main(String[] args) {
//		Mobile appiumServer = new Mobile();
//		
//		int port = 4723;
//		if(!appiumServer.checkIfServerIsRunnning(port)) {
//			appiumServer.startServer();
//			appiumServer.stopServer();
//		} else {
//			System.out.println("Appium Server already running on Port - " + port);
//		}
//	}
}