package com.learnitbro.testing.tool.activity;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.learnitbro.testing.tool.reporting.Report;

public class AssertBuilder {

	private WebDriver driver;
	private Report report;

	@SuppressWarnings("unused")
	private AssertBuilder() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public AssertBuilder(WebDriver driver, Report report) {
		this.driver = driver;
		this.report = report;

	}

	//////////// RETURNS ////////////

	/**
	 * Returns the text of element
	 * 
	 * @param locator
	 * @return text (String)
	 */
	private String getText(By locator) {
		String text = driver.findElement(locator).getText();
		report.info("Getting text of " + locator + " to be " + text);
		return text;
	}

	/**
	 * 
	 * @return
	 */
	private String getTitle() {
		String title = driver.getTitle();
		report.info("Title is " + title);
		return title;
	}
	
	/**
	 * 
	 * @return
	 */
	private String getUrl() {
		String url = driver.getCurrentUrl();
		report.info("URL is " + url);
		return url;
	}

	/**
	 * Returns Attribute value
	 * 
	 * @param locator
	 * @param attribute(String)
	 * @return text (String)
	 */

	private String getAttributeValue(By locator, String attribute) {
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
	private String getPageSource() {
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
		report.info("Checking locator is displayed");
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
		report.info("Checking locator is enabled");
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
		report.info("Checking locator is selected");
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
		report.info("Checking text contains a value");
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
	public boolean textEquals(By locator, String value) {
		report.info("Checking text is equal to a value");
		boolean isTrue = getText(locator).equals(value);
		report.info("Text of " + locator + " is same as " + value + " : " + isTrue);
		return isTrue;
	}
	
	/**
	 * Returns if element text starts with certain value
	 * 
	 * @param locator
	 * @param value   (String)
	 * @return true or false (boolean)
	 */
	public boolean textStartsWith(By locator, String value) {
		report.info("Checking text starts with a value");
		boolean isTrue = getText(locator).startsWith(value);
		report.info("Text of " + locator + " starts with " + value + " : " + isTrue);
		return isTrue;
	}
	
	/**
	 * Returns if element text ends with certain value
	 * 
	 * @param locator
	 * @param value   (String)
	 * @return true or false (boolean)
	 */
	public boolean textEndsWith(By locator, String value) {
		report.info("Checking text ends with a value");
		boolean isTrue = getText(locator).endsWith(value);
		report.info("Text of " + locator + " ends with " + value + " : " + isTrue);
		return isTrue;
	}
	
	
	//////////////////////////////////////////////// NOT ADDED YET
	
	/**
	 * Returns if title contains certain value
	 * 
	 * @param locator
	 * @param value   (String)
	 * @return true or false (boolean)
	 */
	public boolean titleContains(String value) {
		report.info("Checking title contains a value");
		boolean isTrue = getTitle().contains(value);
		report.info("Title contains " + value + " : " + isTrue);
		return isTrue;
	}

	/**
	 * Returns if title is equal to certain value
	 * 
	 * @param locator
	 * @param value   (String)
	 * @return true or false (boolean)
	 */
	public boolean titleEquals(String value) {
		report.info("Checking title is equal to a value");
		boolean isTrue = getTitle().equals(value);
		report.info("Title is same as " + value + " : " + isTrue);
		return isTrue;
	}
	
	/**
	 * Returns if title is ends with to certain value
	 * 
	 * @param locator
	 * @param value   (String)
	 * @return true or false (boolean)
	 */
	public boolean titleEndsWith(String value) {
		report.info("Checking title is ends with a value");
		boolean isTrue = getTitle().endsWith(value);
		report.info("Title ends with " + value + " : " + isTrue);
		return isTrue;
	}
	
	/**
	 * Returns if title is starts with to certain value
	 * 
	 * @param locator
	 * @param value   (String)
	 * @return true or false (boolean)
	 */
	public boolean titleStartsWith(String value) {
		report.info("Checking title is starts with a value");
		boolean isTrue = getTitle().startsWith(value);
		report.info("Title starts with " + value + " : " + isTrue);
		return isTrue;
	}
	
	/**
	 * Returns if url contains certain value
	 * 
	 * @param locator
	 * @param value   (String)
	 * @return true or false (boolean)
	 */
	public boolean urlContains(String value) {
		report.info("Checking url contains a value");
		boolean isTrue = getUrl().contains(value);
		report.info("Url of contains " + value + " : " + isTrue);
		return isTrue;
	}

	/**
	 * Returns if url is equal to certain value
	 * 
	 * @param locator
	 * @param value   (String)
	 * @return true or false (boolean)
	 */
	public boolean urlEquals(String value) {
		report.info("Checking url is equal to a value");
		boolean isTrue = getUrl().equals(value);
		report.info("Url is same as " + value + " : " + isTrue);
		return isTrue;
	}
	
	/**
	 * Returns if url is ends with to certain value
	 * 
	 * @param locator
	 * @param value   (String)
	 * @return true or false (boolean)
	 */
	public boolean urlEndsWith(String value) {
		report.info("Checking url is ends with a value");
		boolean isTrue = getUrl().endsWith(value);
		report.info("Url ends with " + value + " : " + isTrue);
		return isTrue;
	}
	
	/**
	 * Returns if url is starts with to certain value
	 * 
	 * @param locator
	 * @param value   (String)
	 * @return true or false (boolean)
	 */
	public boolean urlStartsWith(String value) {
		report.info("Checking url is starts with a value");
		boolean isTrue = getUrl().startsWith(value);
		report.info("Url starts with " + value + " : " + isTrue);
		return isTrue;
	}
	
	
	///////
	
	/**
	 * Returns if page source contains certain value
	 * 
	 * @param locator
	 * @param value   (String)
	 * @return true or false (boolean)
	 */
	public boolean pageSourceContains(String value) {
		report.info("Checking page source contains a value");
		boolean isTrue = getPageSource().contains(value);
		report.info("Page source contains " + value + " : " + isTrue);
		return isTrue;
	}
	
	/**
	 * 
	 * @param locator
	 * @param attribute
	 * @param value
	 * @return
	 */
	public boolean attributeEquals(By locator, String attribute, String value) {
		report.info("Checking attribute of " + attribute + " is equal to a value");
		boolean isTrue = getAttributeValue(locator, attribute).equals(value);
		report.info("Attribute of " + locator + " is same as " + value + " : " + isTrue);
		return isTrue;
	}
	
	/**
	 * 
	 * @param locator
	 * @param attribute
	 * @param value
	 * @return
	 */
	public boolean attributeContains(By locator, String attribute, String value) {
		report.info("Checking attribute of " + attribute + " contains a value");
		boolean isTrue = getAttributeValue(locator, attribute).contains(value);
		report.info("Attribute of " + locator + " contains " + value + " : " + isTrue);
		return isTrue;
	}
	
	/**
	 * 
	 * @param locator
	 * @param attribute
	 * @param value
	 * @return
	 */
	public boolean attributeStartsWith(By locator, String attribute, String value) {
		report.info("Checking attribute of " + attribute + " starts with a value");
		boolean isTrue = getAttributeValue(locator, attribute).startsWith(value);
		report.info("Attribute of " + locator + " starts with " + value + " : " + isTrue);
		return isTrue;
	}
	
	/**
	 * 
	 * @param locator
	 * @param attribute
	 * @param value
	 * @return
	 */
	public boolean attributeEndsWith(By locator, String attribute, String value) {
		report.info("Checking attribute of " + attribute + " ends with a value");
		boolean isTrue = getAttributeValue(locator, attribute).endsWith(value);
		report.info("Attribute of " + locator + " ends with " + value + " : " + isTrue);
		return isTrue;
	}

}
