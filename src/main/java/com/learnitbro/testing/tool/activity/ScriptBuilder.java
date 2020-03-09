package com.learnitbro.testing.tool.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.learnitbro.testing.tool.reporting.Report;

public class ScriptBuilder {

	private WebDriver driver;
	private Report report;
	private JavascriptExecutor js;

	@SuppressWarnings("unused")
	private ScriptBuilder() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public ScriptBuilder(WebDriver driver, Report report) {
		this.driver = driver;
		this.report = report;
		if (js == null)
			js = ((JavascriptExecutor) driver);

	}

	/**
	 * 
	 * @param command
	 */
	public void executeJS(String command) {
		report.info("Executing this command : " + command);
		js.executeScript(command);
	}

	/**
	 * 
	 * @param locator
	 * @param command
	 */
	public void executeJS(String command, By locator) {
		report.info("Executing this command : '" + command + "' using this locator : " + locator);
		js.executeScript(command, driver.findElement(locator));
	}

	/**
	 * 
	 * @param command
	 * @param text
	 * @return
	 */
	public boolean executedJSResultEquals(String command, String text) {
		report.info("Checking results of this command : " + command + " to be equal to " + text);
		boolean isTrue = getExecutedJSResult(command).equalsIgnoreCase(text);
		report.info("Command result : " + isTrue);
		return isTrue;
	}

	/**
	 * 
	 * @param command
	 * @return
	 */
	private String getExecutedJSResult(String command) {
		return js.executeScript(command).toString();
	}

	public void executeJavaFile(String path) {
		ProcessBuilder pb = null;
		pb = new ProcessBuilder("java", "-jar", path);

		pb.redirectErrorStream(true);
		Process p = null;
		try {
			p = pb.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		BufferedReader stdInput = null;
		try {
			stdInput = new BufferedReader(new InputStreamReader(p.getInputStream(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String s = null;

		// read the output from the command
		System.out.println("Here is the standard output of the command:\n");
		try {
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//////////// Scroll ////////////

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void scrollBy(int x, int y) {
		report.info("Scroll by " + x + " , " + y);
		js.executeScript(String.format("window.scrollBy(%s,%s)", x, y));
	}

	/**
	 * 
	 * @param locator
	 */
	public void scrollIntoView(By locator) {
		report.info("Scroll into view for locator : " + locator);
		js.executeScript("arguments[0].scrollIntoView()", driver.findElement(locator));
	}

//	/**
//	 * 
//	 */
//	public void scrollAllTheWayUp () {
//		report.info("Scroll all the way up");
//        js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
//	}
//	
//	/**
//	 * 
//	 */
//	public void scrollAllTheWayDown () {
//		report.info("Scroll all the way down");
//        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
//	}

	/**
	 * 
	 * @param url
	 */
	public void openNewTab(String url) {
		report.info("Open new tab");
		js.executeScript(String.format("window.open('%s','_blank')", url));
	}
}
