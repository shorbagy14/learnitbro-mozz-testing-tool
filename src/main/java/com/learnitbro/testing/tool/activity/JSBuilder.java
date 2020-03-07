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

	public void execute(String command) {
		js.executeScript(command);
	}

	public void execute(By locator, String command) {
		js.executeScript(command, driver.findElement(locator));
	}

	public String getExecutedResults(String command) {
		return js.executeScript(command).toString();
	}
	
	public String getExecutedResults(By locator, String command) {
		return js.executeScript(command, driver.findElement(locator)).toString();
	}
	
	// Scroll

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void scrollBy(int x, int y) {
		js.executeScript(String.format("window.scrollBy(%s,%s)", x, y));
	}

	/**
	 * 
	 * @param locator
	 */
	public void scrollIntoView(By locator) {
		js.executeScript("arguments[0].scrollIntoView()", driver.findElement(locator));
	}
	
	/**
	 * 
	 */
	public void scrollAllTheWayUp () {
        js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
	}
	
	/**
	 * 
	 */
	public void scrollAllTheWayDown () {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}
	
	/**
	 * 
	 * @param url
	 */
	public void openNewTab(String url) {
		js.executeScript(String.format("window.open('%s','_blank')", url));
	}
}
