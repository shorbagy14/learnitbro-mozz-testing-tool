package com.learnitbro.testing.tool.activity;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Element {
	
	private WebDriver driver;

	@SuppressWarnings("unused")
	private Element() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public Element(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebElement getDisplayedElement(By locator) {
		List<WebElement> elements = driver.findElements(locator);
		
		for(int x=0; x < elements.size(); x++) {
			if(isElementDisplayed(elements.get(x))) {
				return elements.get(x);
			}
		}
		return null;
	}
	
	public boolean isElementDisplayed(By locator) {
		try {
			return driver.findElement(locator).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isElementDisplayed(WebElement element) {
		try {
			return element.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
}