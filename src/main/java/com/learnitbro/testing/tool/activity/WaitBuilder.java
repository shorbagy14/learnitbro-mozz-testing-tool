package com.learnitbro.testing.tool.activity;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.JavascriptExecutor;

public class WaitBuilder {

	private WebDriver driver;

	@SuppressWarnings("unused")
	private WaitBuilder() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public WaitBuilder(WebDriver driver) {
		this.driver = driver;
	}

	public void pageLoad(int timeOut) {
		new WebDriverWait(driver, timeOut)
				.until(m -> ((JavascriptExecutor) m).executeScript("return document.readyState").equals("complete"));
	}

	public void presence(By locator, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.presenceOfElementLocated((locator)));
	}

	public void visibility(By locator, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.visibilityOfElementLocated((locator)));
	}
	
	public void visibility(WebElement element, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.visibilityOf((element)));
	}

	public void clickable(By locator, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public void invisibility(By locator, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.invisibilityOfElementLocated((locator)));
	}

	public void selected(By locator, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.elementToBeSelected((locator)));
	}

	public void titleContains(String title, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.titleContains(title));
	}

	public void titleIs(String title, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.titleIs(title));
	}

	public void urlContains(String url, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.urlContains(url));
	}

	public void urlToBe(String url, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.urlToBe(url));
	}

	public void alert(int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.alertIsPresent());
	}

	public void attributeToBe(By locator, String attribute, String value, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.attributeToBe(locator, attribute, value));
	}

	public void attributeContains(By locator, String attribute, String value, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.attributeContains(locator, attribute, value));
	}

}