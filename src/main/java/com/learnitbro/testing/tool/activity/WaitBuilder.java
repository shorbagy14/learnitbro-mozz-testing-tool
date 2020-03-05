package com.learnitbro.testing.tool.activity;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.learnitbro.testing.tool.reporting.Report;

import org.openqa.selenium.JavascriptExecutor;

public class WaitBuilder {

	private WebDriver driver;
	private Report report;

	@SuppressWarnings("unused")
	private WaitBuilder() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public WaitBuilder(WebDriver driver, Report report) {
		this.driver = driver;
		this.report = report;

	}
	
	/**
	 * 
	 * @param time
	 */
	public void sleep(long time) {
		try {
			Thread.sleep(time*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param timeOut
	 */
	public void pageToLoad(int timeOut) {
		report.info("Waiting for page to load for a maxium of " + timeOut + " seconds");
		new WebDriverWait(driver, timeOut)
				.until(m -> ((JavascriptExecutor) m).executeScript("return document.readyState").equals("complete"));
	}

	/**
	 * 
	 * @param locator
	 * @param timeOut
	 */
	public void presence(By locator, int timeOut) {
		report.info("Waiting for element " + locator + " to be presence for a maximum of " + timeOut + " seconds");
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.presenceOfElementLocated((locator)));
	}

	/**
	 * 
	 * @param locator
	 * @param timeOut
	 */
	public void visibility(By locator, int timeOut) {
		report.info("Waiting for element " + locator + " to be visible for a maximum of " + timeOut + " seconds");
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.visibilityOfElementLocated((locator)));
	}

	/**
	 * 
	 * @param locator
	 * @param timeOut
	 */
	public void clickable(By locator, int timeOut) {
		report.info("Waiting for element " + locator + " to be clickable for a maximum of " + timeOut + " seconds");
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.elementToBeClickable(locator));
	}

	/**
	 * 
	 * @param locator
	 * @param timeOut
	 */
	public void invisibility(By locator, int timeOut) {
		report.info("Waiting for element " + locator + " to be invisible for a maximum of " + timeOut + " seconds");
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.invisibilityOfElementLocated((locator)));
	}

	/**
	 * 
	 * @param locator
	 * @param timeOut
	 */
	public void selected(By locator, int timeOut) {
		report.info("Waiting for element " + locator + " to be selected for a maximum of " + timeOut + " seconds");
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.elementToBeSelected((locator)));
	}

	/**
	 * 
	 * @param title
	 * @param timeOut
	 */
	public void titleContains(String title, int timeOut) {
		report.info("Waiting for title to contain " + title + " for a maximum of " + timeOut + " seconds");
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.titleContains(title));
	}

	/**
	 * 
	 * @param title
	 * @param timeOut
	 */
	public void titleToBe(String title, int timeOut) {
		report.info("Waiting for title to be " + title + " for a maximum of " + timeOut + " seconds");
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.titleIs(title));
	}

	/**
	 * 
	 * @param url
	 * @param timeOut
	 */
	public void urlContains(String url, int timeOut) {
		report.info("Waiting for url to contain " + url + " for a maximum of " + timeOut + " seconds");
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.urlContains(url));
	}

	/**
	 * 
	 * @param url
	 * @param timeOut
	 */
	public void urlToBe(String url, int timeOut) {
		report.info("Waiting for url to be " + url + " for a maximum of " + timeOut + " seconds");
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.urlToBe(url));
	}

	/**
	 * 
	 * @param timeOut
	 */
	public void alertIsPresent(int timeOut) {
		report.info("Waiting for alert to present for a maximum of " + timeOut + " seconds");
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
		report.info("Waiting for element " + locator + " to have an attribute " + attribute + " to be " + value
				+ " for a maximum of " + timeOut + " seconds");
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
		report.info("Waiting for element " + locator + " to have an attribute " + attribute + " to contain " + value
				+ " for a maximum of " + timeOut + " seconds");
		WebDriverWait waiting = new WebDriverWait(driver, timeOut);
		waiting.until(ExpectedConditions.attributeContains(locator, attribute, value));
	}
}