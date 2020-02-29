package com.learnitbro.testing.tool.activity;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

	//////////// ACTIONS ////////////

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
	 * navigate to url
	 * 
	 * @param url
	 */
	public void navigateToUrl(String url) {
		driver.navigate().to(url);
		report.info("Navigating to " + url);
	}

	/**
	 * navigate back
	 */
	public void back() {
		driver.navigate().back();
		report.info("Navigating back");
	}

	/**
	 * Close current tab
	 */
	public void close() {
		driver.close();
		report.info("Close current tab");
	}

	/**
	 * Close browser
	 */
	public void quit() {
		driver.quit();
		report.info("Close browser");
	}

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

	/**
	 * double click on element
	 * 
	 * @param locator
	 */
	public void doubleClick(By locator) {
		WebElement target = driver.findElement(locator);
		actions.doubleClick(target).build().perform();
		report.info("Double clicking on " + locator);
	}

	public void sleep(long Time) {
		try {
			Thread.sleep(Time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//////////// RETURNS ////////////

	/**
	 * Returns the text of element
	 * 
	 * @param locator
	 * @return text (String)
	 */
	public String getText(By locator) {
		String text = driver.findElement(locator).getText();
		report.info("Getting text of " + locator + " to be " + text);
		return text;
	}

	/**
	 * Returns Attribute value
	 * 
	 * @param locator
	 * @param attribute(String)
	 * @return text (String)
	 */

	public String getAttributeValue(By locator, String attribute) {
		String text = driver.findElement(locator).getAttribute(attribute);
		report.info("Getting value of Attribute" + attribute + " to be " + text);
		return text;
	}

	/**
	 * Returns the Page source
	 * 
	 * 
	 * @return text (String)
	 */
	public String getPageSource() {
		String source = driver.getPageSource();
		report.info("Page source is " + source);
		return source;
	}

	//////////// CHECKS ////////////

	/**
	 * Returns if element text contains certain value
	 * 
	 * @param locator
	 * @param value   (String)
	 * @return true or false (boolean)
	 */
	public boolean textContains(By locator, String value) {
		boolean isTrue = getText(locator).contains(value);
		report.info("Text of " + locator + " contains " + value + " : " + isTrue);
		return isTrue;
	}

	/**
	 * Returns if element text is equal to certain value
	 * 
	 * @param locator
	 * @param value   (String)
	 * @return true or false (boolean)
	 */
	public boolean textEqual(By locator, String value) {
		boolean isTrue = getText(locator).equals(value);
		report.info("Text of " + locator + " is same as " + value + " : " + isTrue);
		return isTrue;
	}

	/**
	 * Get list of elements
	 * 
	 * @param locator
	 * @return element(List)
	 */
	public List<WebElement> getWebElements(By locator) {

		List<WebElement> list = driver.findElements(locator);
		report.info("Web Elements list is  " + list);
		return list;
	}

	/////////////////// Select from Drop Down////////////////////////
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
	 * @param index   (String)
	 */
	public void selectDropdownByindex(By locator, String index) {
		Select select = new Select(driver.findElement(locator));
		select.selectByIndex(Integer.parseInt(index));
		report.info("Selecting  element at index  " + index + "  from drop down");
	}

	/////////////////////// Action class operations///////////////////

	/**
	 * Click and hold the element
	 * 
	 * @param locator
	 *
	 */
	public void clickAndHold(By locator) {
		actions.clickAndHold(driver.findElement(locator)).build().perform();
		;
		report.info("Click and hold the " + locator);
	}

	/**
	 * Drag and drop the element
	 * 
	 * @param source
	 * @param target
	 */
	public void dragAndDrop(By source, By target) {
		WebElement s = driver.findElement(source);
		WebElement t = driver.findElement(target);
		actions.dragAndDrop(s, t).build().perform();
		report.info("Drag and drop the element " + source + " to " + target);
	}

	/**
	 * Move mouse to the element
	 * 
	 * @param locator
	 * 
	 */
	public void moveToElement(By locator) {

		WebElement webelement = driver.findElement(locator);
		actions.moveToElement(webelement).build().perform();
		report.info("Move the mouse to the element " + locator);
	}

	/**
	 * Context click the element
	 * 
	 * @param locaor
	 * 
	 */
	public void contextClick(By locator) {

		actions.contextClick(driver.findElement(locator)).build().perform();
		report.info("Context click on the element " + locator);
	}
}
