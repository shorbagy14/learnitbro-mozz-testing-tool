package com.learnitbro.testing.tool.run;

import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.learnitbro.testing.tool.reporting.Report;
import com.learnitbro.testing.tool.stream.StreamHandler;
import com.learnitbro.testing.tool.App;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Operation {

	private WebDriver driver;
	private String driverType;
	private Report report;
//	private WebEventListener eventListener;

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
		if (p.contains("Mac")) {
			os = "Mac OS X";
		} else if (p.contains("Win")) {
			os = "Windows";
		} else if (p.contains("Linux")) {
			os = "Linux";
		}
		return os;
	}

	public WebDriver setupDriver(boolean headless) {
		System.out.println("Driver type: " + driverType);

//		eventListener = new WebEventListener(report);

		switch (driverType.toLowerCase()) {
		case "chrome":
			WebDriverManager.chromedriver().setup();
//			WebDriverManager.chromedriver().version("79").setup();

			ChromeOptions chromeOptions = new ChromeOptions();
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
				driver = new FirefoxDriver();
			}
			break;

		case "ie":
			WebDriverManager.iedriver().setup();
			InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
			if (headless) {
				internetExplorerOptions.setCapability("headless", true);
				driver = new InternetExplorerDriver(internetExplorerOptions);
			} else {
				driver = new InternetExplorerDriver();
			}
			break;

		case "edge":
			WebDriverManager.edgedriver().setup();
			EdgeOptions edgeOptions = new EdgeOptions();
			if (headless) {
				edgeOptions.setCapability("headless", true);
				driver = new EdgeDriver(edgeOptions);
			} else {
				driver = new EdgeDriver();
			}
			break;

		case "opera":
			WebDriverManager.operadriver().setup();
			OperaOptions operaOptions = new OperaOptions();
			if (headless) {
				operaOptions.addArguments("--headless");
				driver = new OperaDriver(operaOptions);
			} else {
				driver = new OperaDriver();
			}
			break;

		case "safari":
			SafariOptions safariOptions = new SafariOptions();
			if (headless) {
				safariOptions.setCapability("headless", true);
				driver = new SafariDriver(safariOptions);
			} else {
				driver = new SafariDriver();
			}
			break;

		default:
			throw new WebDriverException("Driver type is not defined");
		}
		// driver.manage().window().fullscreen();
		// driver.manage().window().maximize();

		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

//		EventFiringWebDriver driver = new EventFiringWebDriver(driver);
//		driver.register(eventListener);

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

	public void setupTest(JSONObject obj) {
		Coordinator coordinator = new Coordinator(driver, getReport());
		coordinator.runTests(obj);
	}

	public Report getReport() {
		return report;
	}
}