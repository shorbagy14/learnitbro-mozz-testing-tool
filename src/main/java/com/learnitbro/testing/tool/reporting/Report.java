package com.learnitbro.testing.tool.reporting;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentLoggerReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.learnitbro.testing.tool.file.DirectoryHandler;
import com.learnitbro.testing.tool.file.FileHandler;

public class Report {

	private ExtentTest test;
	private ExtentReports extent;
	private String location;
	private String timeStamp;
	private String details;

//	@SuppressWarnings("unused")
//	private Report() {
//		// Leave Empty -- Always keep this constructor private
//		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
//	}

	public ExtentTest getTest() {
		return test;
	}

	public void setExtentReports(String details) {
		this.details = details;
		
		setTime();
		location = DirectoryHandler.create(String.format("%s/reports/report-%s-%s", FileHandler.getUserDir(), details, timeStamp)).getPath();

		ExtentLoggerReporter logger = new ExtentLoggerReporter(location);
		ExtentHtmlReporter html = new ExtentHtmlReporter(location + "/report.html");
		logger.config().setTheme(Theme.DARK);
		html.config().setTheme(Theme.DARK);
		extent = new ExtentReports();
		extent.attachReporter(logger);
		extent.attachReporter(html);
	}

	private void setTime() {
		timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime());
	}
	
	public String getDetails() {
		return details;
	}
	
	public String getTime() {
		return timeStamp;
	}
	
	public String[] getAllReports() {
		String report = location + "/report.html";
//		String index = location + "/index.html";
//		String dashboard = location + "/dashboard.html";
//		return new String[] {report, index, dashboard};
		return new String[] {report};
	}

	public String getPathFile() {
		return location + "/report.html";
	}

	public File getFile() {
		return new File(location + "/report.html");
	}

	public ExtentReports getExtentReports() {
		return extent;
	}

	public void flush() {
		extent.flush();
	}

	public void createTest(String details) {
		test = extent.createTest(details);
		System.out.println(details);
	}

	public void info(String details) {
		test.info(details);
		// test.log(Status.INFO, "INFO");
		System.out.println(details);
	}

	public void debug(String details) {
		test.debug(details);
		// test.log(Status.DEBUG, "DEBUG");
		System.out.println(details);
	}

	public void warning(String details) {
		test.warning(details);
		// test.log(Status.WARNING, "WARNING");
		System.out.println(details);
	}

	public void skip(String details) {
		test.skip(details);
		// test.log(Status.SKIP, "SKIP");
		System.out.println(details);
	}

	public void pass(String details) {
		test.pass(details);
		// test.log(Status.PASS, "PASS");
		System.out.println(details);
	}

	public void fail(String details, Throwable exception) {
		test.fail(details);
		warning(exception.toString());
		// test.log(Status.FAIL, "FAIL");
		System.out.println(details);
		exception.printStackTrace();
		// takeScreenshot();
	}

	public void error(String details, Throwable exception) {
		test.error(details);
		warning(exception.toString());
		// test.log(Status.ERROR, "ERROR");
		System.out.println(details);
		exception.printStackTrace();
		// takeScreenshot();
	}

	public void fatal(String details, Throwable exception) {
		test.fatal(details);
		warning(exception.toString());
		// test.log(Status.FATAL, "FATAL");
		System.out.println(details);
		exception.printStackTrace();
		// takeScreenshot();
	}

	private void addScreenCaptureFromPath(File path) throws IOException {
		test.addScreenCaptureFromPath(path.getPath());
	}

	public void takeScreenshot(WebDriver driver) {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String location = "/output/screenshot";
		String path = String.format("%s/%s.png", DirectoryHandler.create(location), System.currentTimeMillis());
		String dir = System.getProperty("user.dir");
		File file = new File(dir + path);
		try {
			FileUtils.copyFile(scrFile, file);
			addScreenCaptureFromPath(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}