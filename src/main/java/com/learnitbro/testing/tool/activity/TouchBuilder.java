package com.learnitbro.testing.tool.activity;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;

import com.learnitbro.testing.tool.reporting.Report;

public class TouchBuilder {

	private WebDriver driver;
	private Report report;
	private TouchActions touch;

	@SuppressWarnings("unused")
	private TouchBuilder() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public TouchBuilder(WebDriver driver, Report report) {
		this.driver = driver;
		this.report = report;
		if (touch == null)
			touch = new TouchActions(driver);
	}

//	/**
//	 * 
//	 * @param locator
//	 */
//	public void doubleTap(By locator) {
//		report.info("Double tap");
//		WebElement onElement = driver.findElement(locator);
//		touch.doubleTap(onElement);
//	}
//
//	/**
//	 * 
//	 * @param locator
//	 */
//	public void singleTap(By locator) {
//		report.info("Single tap");
//		WebElement onElement = driver.findElement(locator);
//		touch.singleTap(onElement);
//	}
//
//	/**
//	 * 
//	 * @param locator
//	 */
//	public void longPress(By locator) {
//		report.info("Long press");
//		WebElement onElement = driver.findElement(locator);
//		touch.longPress(onElement);
//	}
//
//	/**
//	 * 
//	 * @param x
//	 * @param y
//	 */
//	public void upGesture(int x, int y) {
//		report.info("Up gesture");
//		touch.up(x, y);
//	}
//
//	/**
//	 * 
//	 * @param x
//	 * @param y
//	 */
//	public void downGesture(int x, int y) {
//		report.info("Down gesture");
//		touch.down(x, y);
//	}
//
//	/**
//	 * 
//	 * @param xOffset
//	 * @param yOffset
//	 */
//	public void scroll(int xOffset, int yOffset) {
//		report.info("Scroll");
//		touch.scroll(xOffset, yOffset);
//	}
//
//	/**
//	 * 
//	 * @param xSpeed
//	 * @param ySpeed
//	 */
//	public void flick(int xSpeed, int ySpeed) {
//		report.info("Flick");
//		touch.flick(xSpeed, ySpeed);
//	}
}
