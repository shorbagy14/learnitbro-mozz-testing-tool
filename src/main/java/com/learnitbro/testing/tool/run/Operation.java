package com.learnitbro.testing.tool.run;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.learnitbro.testing.tool.reporting.Report;
import com.learnitbro.testing.tool.stream.StreamHandler;
import com.learnitbro.testing.tool.App;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Operation {

	private WebDriver driver;
	private String driverType;
	private Report report;

	public Operation(String driverType) {
		this.driverType = driverType;
		if (report == null) {
			report = new Report();
		}
	}

	@SuppressWarnings("unused")
	private Operation() {
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
		StreamHandler stream = new StreamHandler();
		String browserInfo = stream.inputStreamTextBuilder(getClass().getResourceAsStream("/info.txt"));
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
		if (p.contains("Mac")) {
			os = "Mac OS X";
		} else if (p.contains("Win")) {
			os = "Windows";
		} else if (p.contains("Android") || p.contains("Linux a")) {
			os = "Android";
		} else if (p.contains("Linux")) {
			os = "Linux";
		} else if (p.contains("iPhone") || p.contains("iPad")) {
			os = "iPhone";
		}
		return os;
	}

	public WebDriver setupDriver(boolean headless) {
		System.out.println("Driver type: " + driverType);
//		DesiredCapabilities caps = new DesiredCapabilities();
//		LoggingPreferences logPrefs = new LoggingPreferences();
//		logPrefs.enable(LogType.BROWSER, Level.ALL);
//		logPrefs.enable(LogType.DRIVER, Level.ALL);
//		logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
//		caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
		switch (driverType.toLowerCase()) {
		case "chrome":
//			WebDriverManager.chromedriver().setup();
			WebDriverManager.chromedriver().version("73").setup();

			ChromeOptions chromeOptions = new ChromeOptions();
//			chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
			chromeOptions.addArguments("window-size=1200x600");
			chromeOptions.addArguments("--disable-dev-shm-usage");
			if (headless) {
				chromeOptions.setHeadless(true);
				chromeOptions.addArguments("--headless");
				// chromeOptions.addArguments("--no-sandbox");
				driver = new ChromeDriver(chromeOptions);
			} else {
				driver = new ChromeDriver(chromeOptions);
			}
			break;

		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			if (headless) {
				firefoxOptions.setHeadless(true);
				firefoxOptions.addArguments("--headless");
				driver = new FirefoxDriver(firefoxOptions);
			} else {
				driver = new FirefoxDriver(firefoxOptions);
			}
			break;

		default:
			throw new WebDriverException("Driver type is not defined");
		}
		// driver.manage().window().fullscreen();
		// driver.manage().window().maximize();
		
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		
		App app = new App();
		report.info(app.getTitle());
		report.info(app.getVersion());
		report.pass("Launching browser");
		report.info("Browser Name: " + driverType);
		report.info("Browser Version: " + getBrowserVersion(driver));
		report.info("Browser Platform: " + getBrowserPlatform(driver));
		report.info("Machine OS: " + System.getProperty("os.name"));
		report.info("Headless: " + headless);
		return driver;
	}

	public void setupWebsite(String website) {

		driver.get(website);

		System.out.println("Url: " + driver.getCurrentUrl());
		System.out.println("Title: " + driver.getTitle());
		report.pass("Launching website");
		report.info("Website: " + website);
	}

	public void setupTest() {
		Coordinator coordinator = new Coordinator(driver, getReport());
		coordinator.runTests();
	}

	public Report getReport() {
		return report;
	}
}