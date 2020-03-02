package com.learnitbro.testing.tool.activity;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.learnitbro.testing.tool.reporting.Report;

public class ActionBuilder {

	private WebDriver driver;
	private Report report;
	private Actions actions;
	private JavascriptExecutor js;

	@SuppressWarnings("unused")
	private ActionBuilder() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public ActionBuilder(WebDriver driver, Report report) {
		this.driver = driver;
		this.report = report;
		if (actions == null)
			actions = new Actions(driver);
		if (js == null)
			js = ((JavascriptExecutor) driver);

	}

	/////////////////////// WebDriver & WebElement Class ///////////////////////

	/**
	 * navigate to url
	 * 
	 * @param url
	 */
	public void link(String url) {
		driver.get(url);
		report.info("Going to " + url);
	}

	/**
	 * navigate back
	 */
	public void back() {
		driver.navigate().back();
		report.info("Navigating back");
	}
	
	/**
	 * navigate forward
	 */
	public void forward() {
		driver.navigate().forward();
		report.info("Navigating forward");
	}
	
	/**
	 * refresh
	 */
	public void refresh() {
		driver.navigate().refresh();
		report.info("Page refresh");
	}

	/**
	 * close current tab
	 */
	public void close() {
		driver.close();
		report.info("Close current tab");
	}

//	/**
//	 * Close browser
//	 */
//	public void quit() {
//		driver.quit();
//		report.info("Close browser");
//	}

	/**
	 * clicks the element
	 * 
	 * @param locator : xpath
	 */
	public void click(By locator) {
		driver.findElement(locator).click();
		report.info("Clicking on " + locator);
	}

	/**
	 * clears the element
	 * 
	 * @param locator : xpath
	 */
	public void clear(By locator) {
		driver.findElement(locator).clear();
		report.info("Clearing field on " + locator);
	}

	/**
	 * submit the element
	 * 
	 * @param locator : xpath
	 */
	public void submit(By locator) {
		driver.findElement(locator).submit();
		report.info("Submitting field on " + locator);
	}

	/**
	 * input text to the element
	 * 
	 * @param locator : xpath
	 */
	public void inputText(By locator, String value) {
		driver.findElement(locator).sendKeys(value);
		report.info("Input text of " + locator + " on " + value);
	}

	/**
	 * upload to the element
	 * 
	 * @param locator : xpath
	 */
	public void upload(By locator, String value) {
		driver.findElement(locator).sendKeys(value);
		report.info("Uploading " + locator + " on " + value);
	}
	
	/////////////////////// Actions Class ///////////////////////

	/**
	 * Click and hold the element
	 * 
	 * @param locator
	 *
	 */
	public void clickAndHold(By locator) {
		actions.clickAndHold(driver.findElement(locator)).build().perform();
		report.info("Click and hold on " + locator);
	}
	
	/**
	 * Releasing mouse click and hold on element
	 * 
	 * @param locator
	 *
	 */
	public void release() {
		actions.release().build().perform();
		report.info("Releasing mouse click and hold");
	}

	/**
	 * Drag and drop on element
	 * 
	 * @param source
	 * @param target
	 */
	public void dragAndDrop(By source, By target) {
		actions.dragAndDrop(driver.findElement(source), driver.findElement(target)).build().perform();
		report.info("Drag and drop on " + source + " to " + target);
	}

	/**
	 * Hover on element
	 * 
	 * @param locator
	 * 
	 */
	public void hover(By locator) {
		actions.moveToElement(driver.findElement(locator)).build().perform();
		report.info("Hover on " + locator);
	}

	/**
	 * Context click the element
	 * 
	 * @param locaor
	 * 
	 */
	public void contextClick(By locator) {
		actions.contextClick(driver.findElement(locator)).build().perform();
		report.info("Context click on " + locator);
	}

	/**
	 * Double click on element
	 * 
	 * @param locator
	 */
	public void doubleClick(By locator) {
		actions.doubleClick(driver.findElement(locator)).build().perform();
		report.info("Double click on " + locator);
	}

	/////////////////// Select Class ////////////////////////
	
	/**
	 * Select element in dropdown using visible text
	 * 
	 * @param locator
	 * @param text    (String)
	 */
	public void selectDropdownByVisibleText(By locator, String text) {
		Select select = new Select(driver.findElement(locator));
		select.selectByVisibleText(text);
		report.info("Selecting  " + text + "  from drop down");
	}

	/**
	 * Select element in drop down using value
	 * 
	 * @param locator
	 * @param value   (String)
	 */
	public void selectDropdownByValue(By locator, String value) {
		Select select = new Select(driver.findElement(locator));
		select.selectByValue(value);
		report.info("Selecting  " + value + "  from drop down");
	}

	/**
	 * Select element in drop down using index
	 * 
	 * @param locator
	 * @param index   (Integer)
	 */
	public void selectDropdownByIndex(By locator, int index) {
		Select select = new Select(driver.findElement(locator));
		select.selectByIndex(index);
		report.info("Selecting element at index  " + index + "  from drop down");
	}
}
