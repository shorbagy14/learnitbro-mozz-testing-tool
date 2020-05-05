package com.learnitbro.testing.tool.web;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ElementHandler {
	
	private WebDriver driver;

	@SuppressWarnings("unused")
	private ElementHandler() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public ElementHandler(WebDriver driver) {
		this.driver = driver;
	}
	
	/**
	 * 
	 * @param locator
	 * @return
	 */
	public WebElement getDisplayedElement(By locator) {
		List<WebElement> elements = driver.findElements(locator);
		
		for(int x=0; x < elements.size(); x++) {
			if(isElementDisplayed(elements.get(x))) {
				return elements.get(x);
			}
		}
		return null;
	}
	
	/**
	 * Get list of elements
	 * 
	 * @param locator
	 * @return element(List)
	 */
	public List<WebElement> getWebElements(By locator) {
		List<WebElement> list = driver.findElements(locator);
		return list;
	}

	/**
	 * 
	 * @param locator
	 * @return
	 */
	public boolean isElementDisplayed(By locator) {
		try {
			return driver.findElement(locator).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 */
	public boolean isElementDisplayed(WebElement element) {
		try {
			return element.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Returns the locator based on type in JSON
	 * 
	 * @param type - locator type
	 * @param text - locator value
	 * @return locator
	 */
	public static By getLocator(String type, String text) {
		if(type.equals("xpath"))
			return By.xpath(text);
		else if(type.equals("text"))
			return By.linkText(text);
		else if(type.equals("partial text"))
			return By.partialLinkText(text);
		else if(type.equals("class name"))
			return By.className(text);
		else if(type.equals("css selector"))
			return By.cssSelector(text);
		else if(type.equals("name"))
			return By.name(text);
		else if(type.equals("id"))
			return By.id(text);
		return null;
	}
}