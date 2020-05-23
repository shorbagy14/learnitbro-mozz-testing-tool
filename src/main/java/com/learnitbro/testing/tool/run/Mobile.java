package com.learnitbro.testing.tool.run;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.learnitbro.testing.tool.App;
import com.learnitbro.testing.tool.os.OS;
import com.learnitbro.testing.tool.reporting.Report;
import com.learnitbro.testing.tool.stream.StreamHandler;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Mobile {

	private final String ADDRESS = "0.0.0.0";
	private final String PORT = "4723";
	private final String BP = "4724";

	private AppiumDriverLocalService service;
	private AppiumServiceBuilder builder;
	private DesiredCapabilities cap;

	private WebDriver driver;
	private String driverType;
	private Report report;

	private String appType;
	private String appPlatform;
	private String appActivity;

	public Mobile(String driverType) {
		this.driverType = driverType;
		if (report == null) {
			report = new Report();
		}
	}

	public Mobile(String appType, String appPlatform, String appActivity) {
		this.appType = appType;
		this.appPlatform = appPlatform;
		this.appActivity = appActivity;
		if (report == null) {
			report = new Report();
		}
	}

	@SuppressWarnings("unused")
	private Mobile() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException(
				"You forgot to pass the 'driverType' to this class: " + this.getClass().getName());
	}

	public void setupReport() {
		report.setExtentReports(driverType);
		report.createTest("Information");
	}

	public String getBrowserInfo(WebDriver myDriver) {
		JavascriptExecutor js = (JavascriptExecutor) myDriver;
		String browserInfo = StreamHandler.inputStreamTextBuilder(getClass().getResourceAsStream("/info.txt"));
		Object info = js.executeScript("return " + browserInfo).toString();
		browserInfo = String.valueOf(info);
		return browserInfo;
	}

	public String getBrowserName(WebDriver myDriver) {
		String name = getBrowserInfo(myDriver).replaceAll("[0-9 ]+", "");
		return name;

	}

	public String getBrowserVersion(WebDriver myDriver) {
		String version = getBrowserInfo(myDriver).replaceAll("[^0-9]+", "");
		return version;
	}

	public String getBrowserPlatform(WebDriver myDriver) {
		JavascriptExecutor js = (JavascriptExecutor) myDriver;
		Object platform = js.executeScript("return navigator.platform;").toString();
		String p = String.valueOf(platform);
		String os = null;
		if (p.contains("Android") || p.contains("Linux")) {
			os = "Android";
		} else if (p.contains("iPhone") || p.contains("iPad")) {
			os = "iOS";
		}
		return os;
	}

	public void startServer() {

		OS.OSType ostype = OS.getOperatingSystemType();

		switch (ostype) {

		case Windows:

			// Set Capabilities
			cap = new DesiredCapabilities();
			cap.setCapability("noReset", "false");

			// Build the Appium service
			builder = new AppiumServiceBuilder();
			builder.withIPAddress("127.0.0.1");
			builder.usingPort(4723);
			builder.withCapabilities(cap);
			builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
			builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");

			// Start the server with the builder
			service = AppiumDriverLocalService.buildService(builder);
			service.start();
			break;

		case Linux:

			// Set Capabilities
			cap = new DesiredCapabilities();
			cap.setCapability("noReset", "false");

			// Build the Appium service
			builder = new AppiumServiceBuilder();
			builder.withIPAddress("127.0.0.1");
			builder.usingPort(4723);
			builder.withCapabilities(cap);
			builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
			builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");

			// Start the server with the builder
			service = AppiumDriverLocalService.buildService(builder);
			service.start();
			break;

		case MacOS:

			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("killall node");

				CommandLine command = new CommandLine("/usr/local/bin/node");
				command.addArgument("/usr/local/bin/appium", false);

				command.addArgument("--address", false);
				command.addArgument(ADDRESS);
				command.addArgument("--port", false);
				command.addArgument(PORT);
				command.addArgument("-bp", false);
				command.addArgument(BP);
				// command.addArgument("--full-reset", true);
				// command.addArgument("--session-override", true);
				// command.addArgument("--no-reset", true);
				DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
				DefaultExecutor executor = new DefaultExecutor();
				executor.setExitValue(1);
				executor.execute(command, resultHandler);
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		default:
			throw new RuntimeException("Platform is not supported. Must be Windows, Linux or Mac");
		}
	}

	public void stopServer() {

		OS.OSType ostype = OS.getOperatingSystemType();

		switch (ostype) {

		case MacOS:
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("killall node");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case Windows:
			service.stop();
			break;
		case Linux:
			service.stop();
			break;
		default:
			throw new RuntimeException("Platform is not supported. Must be Windows, Linux or Mac");
		}
	}

	public boolean checkIfServerIsRunnning(int port) {

		boolean isServerRunning = false;
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.close();
		} catch (IOException e) {
			// If control comes here, then it means that the port is in use
			isServerRunning = true;
		} finally {
			serverSocket = null;
		}
		return isServerRunning;
	}

	public WebDriver setupApp() {
		System.out.println("App type: " + appPlatform);

		switch (appPlatform.toLowerCase()) {
		case "android":
			DesiredCapabilities capabilities_andriod_app = new DesiredCapabilities();
			capabilities_andriod_app.setCapability("appPackage", appType);
			capabilities_andriod_app.setCapability("appActivity", appActivity);
			capabilities_andriod_app.setCapability("platformName", "android");
			capabilities_andriod_app.setCapability("deviceName", "mozz");

			try {
				WebDriverManager.chromedriver().setup();
				driver = new AndroidDriver<MobileElement>(new URL(String.format("http://%s:%s/wd/hub", ADDRESS, PORT)),
						capabilities_andriod_app);
			} catch (MalformedURLException e) {
				System.out.println(e.getMessage());
			}

			break;
		case "ios":
			DesiredCapabilities capabilities_ios_app = new DesiredCapabilities();
			capabilities_ios_app.setCapability("appPackage", appType);
			capabilities_ios_app.setCapability("appActivity", appActivity);
			capabilities_ios_app.setCapability("platformName", "ios");
			capabilities_ios_app.setCapability("deviceName", "mozz");

			try {
				driver = new AndroidDriver<MobileElement>(new URL(String.format("http://%s:%s/wd/hub", ADDRESS, PORT)),
						capabilities_ios_app);
			} catch (MalformedURLException e) {
				System.out.println(e.getMessage());
			}

			break;

		default:
			throw new WebDriverException("Driver type is not defined");
		}

		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		App app = new App();
		report.info(app.getTitle());
		report.info(app.getVersion());
		report.pass("Launching app");
		report.info("App Name: " + appType);
		report.info("App Platform: " + appPlatform);
		report.info("Machine OS: " + System.getProperty("os.name"));
		report.info("Headless: false");
		return driver;
	}

//	private void launchAndroidApp() {
//		DesiredCapabilities caps = new DesiredCapabilities();
//		caps.setCapability("deviceName", "My Phone");
//		caps.setCapability("udid", "ABCD");
//		caps.setCapability("platformName", "Android");
//		caps.setCapability("platformVersion", "6.0");
//		caps.setCapability("appPackage", "com.android.learnitbro");
//		caps.setCapability("appActivity", "com.google.android.finsky.activities.MainActivity");
//		caps.setCapability("noReset", "true");
//
//		// Instantiate Appium Driver
//		try {
//			driver = new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:4723/wd/hub"), caps);
//		} catch (MalformedURLException e) {
//			System.out.println(e.getMessage());
//		}
//	}

	public void setupTest(JSONObject obj) {
		Coordinator coordinator = new Coordinator(driver, getReport());
		coordinator.runTests(obj);
	}

	public Report getReport() {
		return report;
	}

	public WebDriver setupDriver() {
		System.out.println("Driver type: " + driverType);

		switch (driverType.toLowerCase()) {
		case "chrome":
			DesiredCapabilities capabilities_andriod_mobile_web = new DesiredCapabilities();
			capabilities_andriod_mobile_web.setCapability("browserName", "chrome");
			capabilities_andriod_mobile_web.setCapability("platformName", "android");
			capabilities_andriod_mobile_web.setCapability("deviceName", "mozz");

			try {
				WebDriverManager.chromedriver().setup();
				driver = new AndroidDriver<MobileElement>(new URL(String.format("http://%s:%s/wd/hub", ADDRESS, PORT)),
						capabilities_andriod_mobile_web);
			} catch (MalformedURLException e) {
				System.out.println(e.getMessage());
			}

			break;
		case "safari":
			DesiredCapabilities capabilities_ios_mobile_web = new DesiredCapabilities();
			capabilities_ios_mobile_web.setCapability("browserName", "safari");
			capabilities_ios_mobile_web.setCapability("platformName", "ios");
			capabilities_ios_mobile_web.setCapability("deviceName", "mozz");

			try {
				driver = new AndroidDriver<MobileElement>(new URL(String.format("http://%s:%s/wd/hub", ADDRESS, PORT)),
						capabilities_ios_mobile_web);
			} catch (MalformedURLException e) {
				System.out.println(e.getMessage());
			}

			break;

		default:
			throw new WebDriverException("Driver type is not defined");
		}

		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		App app = new App();
		report.info(app.getTitle());
		report.info(app.getVersion());
		report.pass("Launching browser");
		report.info("Browser Name: " + driverType);
		report.info("Browser Version: " + getBrowserVersion(driver));
		report.info("Browser Platform: " + getBrowserPlatform(driver));
		report.info("Machine OS: " + System.getProperty("os.name"));
		report.info("Headless: false");
		return driver;
	}
}