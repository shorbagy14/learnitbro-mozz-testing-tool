package com.learnitbro.testing.tool.activity;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.learnitbro.testing.tool.reporting.Report;

public class AssertBuilder {

	private WebDriver driver;
	private Report report;
	private Actions actions;
	private JavascriptExecutor js;

	@SuppressWarnings("unused")
	private AssertBuilder() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public AssertBuilder(WebDriver driver, Report report) {
		this.driver = driver;
		this.report = report;
		if (actions == null)
			actions = new Actions(driver);
		if (js == null)
			js = ((JavascriptExecutor) driver);

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
	 * Returns if element is displayed
	 * 
	 * @param locator
	 * @return true or false (boolean)
	 */
	public boolean isDisplayed(By locator) {
		boolean isTrue = driver.findElement(locator).isDisplayed();
		report.info("Element of " + locator + " is displayed : " + isTrue);
		return isTrue;
	}
	
	/**
	 * Returns if element is enabled
	 * 
	 * @param locator
	 * @return true or false (boolean)
	 */
	public boolean isEnabled(By locator) {
		boolean isTrue = driver.findElement(locator).isEnabled();
		report.info("Element of " + locator + " is enabled : " + isTrue);
		return isTrue;
	}
	
	/**
	 * Returns if element is selected
	 * 
	 * @param locator
	 * @return true or false (boolean)
	 */
	public boolean isSelected(By locator) {
		boolean isTrue = driver.findElement(locator).isSelected();
		report.info("Element of " + locator + " is selected : " + isTrue);
		return isTrue;
	}

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

}
