package com.learnitbro.testing.tool.activity;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.learnitbro.testing.tool.reporting.Report;

public class JSBuilder {

	private WebDriver driver;
	private Report report;
	private JavascriptExecutor js;

	@SuppressWarnings("unused")
	private JSBuilder() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public JSBuilder(WebDriver driver, Report report) {
		this.driver = driver;
		this.report = report;
		if (js == null)
			js = ((JavascriptExecutor) driver);

	}

	/**
	 * 
	 * @param command
	 */
	public void execute(String command) {
		report.info("Executing this command: " + command);
		js.executeScript(command);
	}

	/**
	 * 
	 * @param locator
	 * @param command
	 */
	public void execute(By locator, String command) {
		report.info("Executing this command: '" + command + "' using this locator : " + locator);
		js.executeScript(command, driver.findElement(locator));
	}

//	/**
//	 * 
//	 * @param command
//	 * @return
//	 */
//	private String getExecutedResults(String command) {
//		return js.executeScript(command).toString();
//	}
//	
//	/**
//	 * 
//	 * @param locator
//	 * @param command
//	 * @return
//	 */
//	private String getExecutedResults(By locator, String command) {
//		return js.executeScript(command, driver.findElement(locator)).toString();
//	}
	
	// Scroll

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
	
	/**
	 * 
	 */
	public void scrollAllTheWayUp () {
		report.info("Scroll all the way up");
        js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
	}
	
	/**
	 * 
	 */
	public void scrollAllTheWayDown () {
		report.info("Scroll all the way down");
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}
	
	/**
	 * 
	 * @param url
	 */
	public void openNewTab(String url) {
		report.info("Open new tab");
		js.executeScript(String.format("window.open('%s','_blank')", url));
	}
}
