package com.learnitbro.testing.tool.activity;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.learnitbro.testing.tool.reporting.Report;

import org.openqa.selenium.JavascriptExecutor;

public class WaitBuilder {

	private WebDriver driver;
	private Report report;
	private Actions actions;
	private JavascriptExecutor js;

	@SuppressWarnings("unused")
	private WaitBuilder() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public WaitBuilder(WebDriver driver, Report report) {
		this.driver = driver;
		this.report = report;
		if (actions == null)
			actions = new Actions(driver);
		if (js == null)
			js = ((JavascriptExecutor) driver);

	}

	/**
	 * 
	 * @param timeOut
	 */
	public void pageToLoad(int timeOut) {
		new WebDriverWait(driver, timeOut)
				.until(m -> ((JavascriptExecutor) m).executeScript("return document.readyState").equals("complete"));
	}

	/**
	 * 
	 * @param locator
	 * @param timeOut
	 */
	public void presence(By locator, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.presenceOfElementLocated((locator)));
	}

	/**
	 * 
	 * @param locator
	 * @param timeOut
	 */
	public void visibility(By locator, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.visibilityOfElementLocated((locator)));
	}

	/**
	 * 
	 * @param locator
	 * @param timeOut
	 */
	public void clickable(By locator, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.elementToBeClickable(locator));
	}

	/**
	 * 
	 * @param locator
	 * @param timeOut
	 */
	public void invisibility(By locator, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.invisibilityOfElementLocated((locator)));
	}

	/**
	 * 
	 * @param locator
	 * @param timeOut
	 */
	public void selected(By locator, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.elementToBeSelected((locator)));
	}

	/**
	 * 
	 * @param title
	 * @param timeOut
	 */
	public void titleContains(String title, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.titleContains(title));
	}

	/**
	 * 
	 * @param title
	 * @param timeOut
	 */
	public void titleIs(String title, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.titleIs(title));
	}

	/**
	 * 
	 * @param url
	 * @param timeOut
	 */
	public void urlContains(String url, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.urlContains(url));
	}

	/**
	 * 
	 * @param url
	 * @param timeOut
	 */
	public void urlToBe(String url, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.urlToBe(url));
	}

	/**
	 * 
	 * @param timeOut
	 */
	public void alertIsPresent(int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.alertIsPresent());
	}

	/**
	 * 
	 * @param locator
	 * @param attribute
	 * @param value
	 * @param timeOut
	 */
	public void attributeToBe(By locator, String attribute, String value, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.attributeToBe(locator, attribute, value));
	}

	/**
	 * 
	 * @param locator
	 * @param attribute
	 * @param value
	 * @param timeOut
	 */
	public void attributeContains(By locator, String attribute, String value, int timeOut) {
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.attributeContains(locator, attribute, value));
	}
}