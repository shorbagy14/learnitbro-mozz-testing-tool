package com.learnitbro.testing.tool.activity;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.learnitbro.testing.tool.reporting.Report;

public class ActionBuilder {

	private WebDriver driver;
	private Report report;
	private Actions actions;

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

	}

	/////////////////////// WebDriver & WebElement Class ///////////////////////

	/**
	 * navigate to url
	 * 
	 * @param url
	 */
	public void link(String url) {
		report.info("Going to " + url);
		driver.get(url);
	}

	/**
	 * navigate back
	 */
	public void back() {
		report.info("Navigating back");
		driver.navigate().back();
	}

	/**
	 * navigate forward
	 */
	public void forward() {
		report.info("Navigating forward");
		driver.navigate().forward();
	}

	/**
	 * refresh
	 */
	public void refresh() {
		report.info("Page refresh");
		driver.navigate().refresh();
	}

	/**
	 * close current tab
	 */
	public void close() {
		report.info("Close current tab");
		driver.close();
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
		report.info("Clicking on " + locator);
		driver.findElement(locator).click();
	}

	/**
	 * clears the element
	 * 
	 * @param locator : xpath
	 */
	public void clear(By locator) {
		report.info("Clearing field on " + locator);
		driver.findElement(locator).clear();
	}

	/**
	 * submit the element
	 * 
	 * @param locator : xpath
	 */
	public void submit(By locator) {
		report.info("Submitting field on " + locator);
		driver.findElement(locator).submit();
	}

	/**
	 * input text to the element
	 * 
	 * @param locator : xpath
	 */
	public void inputText(By locator, String value) {
		report.info("Input text of " + locator + " on " + value);
		driver.findElement(locator).sendKeys(value);
	}

	/**
	 * upload to the element
	 * 
	 * @param locator : xpath
	 */
	public void upload(By locator, String value) {
		report.info("Uploading " + locator + " on " + value);
		driver.findElement(locator).sendKeys(value);
	}

	/////////////////////// Actions Class ///////////////////////

	/**
	 * Click and hold the element
	 * 
	 * @param locator
	 *
	 */
	public void clickAndHold(By locator) {
		report.info("Click and hold on " + locator);
		actions.clickAndHold(driver.findElement(locator)).build().perform();
	}

	/**
	 * Releasing mouse click and hold on element
	 * 
	 * @param locator
	 *
	 */
	public void release() {
		report.info("Releasing mouse click and hold");
		actions.release().build().perform();
	}

	/**
	 * Drag and drop on element
	 * 
	 * @param source
	 * @param target
	 */
	public void dragAndDrop(By source, By target) {
		report.info("Drag and drop on " + source + " to " + target);
		actions.dragAndDrop(driver.findElement(source), driver.findElement(target)).build().perform();
	}

	/**
	 * Hover on element
	 * 
	 * @param locator
	 * 
	 */
	public void hover(By locator) {
		report.info("Hover on " + locator);
		actions.moveToElement(driver.findElement(locator)).build().perform();
	}

	/**
	 * Context click the element
	 * 
	 * @param locaor
	 * 
	 */
	public void contextClick(By locator) {
		report.info("Context click on " + locator);
		actions.contextClick(driver.findElement(locator)).build().perform();
	}

	/**
	 * Double click on element
	 * 
	 * @param locator
	 */
	public void doubleClick(By locator) {
		report.info("Double click on " + locator);
		actions.doubleClick(driver.findElement(locator)).build().perform();
	}

	/////////////////// Select Class ////////////////////////

//	/**
//	 * Select element in dropdown using visible text
//	 * 
//	 * @param locator
//	 * @param text    (String)
//	 */
//	public void selectDropdownByVisibleText(By locator, String text) {
//		report.info("Selecting " + text + " from drop down");
//		Select select = new Select(driver.findElement(locator));
//		select.selectByVisibleText(text);
//	}
//
//	/**
//	 * Select element in drop down using value
//	 * 
//	 * @param locator
//	 * @param value   (String)
//	 */
//	public void selectDropdownByValue(By locator, String value) {
//		report.info("Selecting " + value + " from drop down");
//		Select select = new Select(driver.findElement(locator));
//		select.selectByValue(value);
//	}
//
//	/**
//	 * Select element in drop down using index
//	 * 
//	 * @param locator
//	 * @param index   (Integer)
//	 */
//	public void selectDropdownByIndex(By locator, int index) {
//		report.info("Selecting element at index " + index + " from drop down");
//		Select select = new Select(driver.findElement(locator));
//		select.selectByIndex(index);
//	}

	/////////////////// Window Class ////////////////////////

	/**
	 * 
	 */
	public void maximize() {
		report.info("Browser window maximize");
		driver.manage().window().maximize();
	}

	/**
	 * 
	 */
	public void fullscreen() {
		report.info("Browser window fullscreen");
		driver.manage().window().fullscreen();
	}

	/**
	 * 
	 */
	public void minimize() {
		report.info("Browser window minimize");
		driver.manage().window().setPosition(new Point(0, -2000));
	}

	/**
	 * 
	 * @param index
	 */
	public void switchToWindow(int index) {
		report.info("Switching to window of index : " + index);
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(index));
	}

	/////////////////// Frame Class ////////////////////////

	/**
	 * 
	 * @param locator
	 */
	public void switchToFrame(By locator) {
		report.info("Switching to frame of locator : " + locator);
		driver.switchTo().frame(driver.findElement(locator));
	}
	
	/**
	 * 
	 * @param index
	 */
	public void switchToFrame(int index) {
		report.info("Switching to frame of index : " + index);
		driver.switchTo().frame(index);
	}

	/**
	 * 
	 */
	public void switchToParentFrame() {
		report.info("Switching to parent frame");
		driver.switchTo().parentFrame();
	}

	/**
	 * 
	 */
	public void switchToDefaultFrame() {
		report.info("Switching to default frame");
		driver.switchTo().defaultContent();
	}
}
