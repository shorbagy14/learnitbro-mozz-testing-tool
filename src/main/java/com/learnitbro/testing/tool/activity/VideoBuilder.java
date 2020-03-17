package com.learnitbro.testing.tool.activity;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.learnitbro.testing.tool.reporting.Report;

public class VideoBuilder {

	private WebDriver driver;
	private Report report;
	private JavascriptExecutor js;

	@SuppressWarnings("unused")
	private VideoBuilder() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public VideoBuilder(WebDriver driver, Report report) {
		this.driver = driver;
		this.report = report;
		if (js == null)
			js = ((JavascriptExecutor) this.driver);
	}

	/**
	 * 
	 * @param locator
	 */
	public void pause(By locator) {
		report.info("Pause video of " + locator);
		js.executeScript("arguments[0].pause()", driver.findElement(locator));
	}

	/**
	 * 
	 * @param locator
	 */
	public void play(By locator) {
		report.info("Play video of " + locator);
		js.executeScript("arguments[0].play()", driver.findElement(locator));
	}

	/**
	 * 
	 * @param locator
	 */
	public void mute(By locator) {
		report.info("Mute video of " + locator);
		js.executeScript("arguments[0].muted=true", driver.findElement(locator));
	}

	/**
	 * 
	 * @param locator
	 */
	public void unmute(By locator) {
		report.info("Unmute video of " + locator);
		js.executeScript("arguments[0].muted=false", driver.findElement(locator));
	}

	/**
	 * 
	 * @param locator
	 */
	public void replay(By locator) {
		report.info("Replay video of " + locator);
		js.executeScript("arguments[0].currentTime=0", driver.findElement(locator));
	}

	/**
	 * 
	 * @param locator
	 * @param number
	 */
	public void setVolume(By locator, double number) {
		report.info("Set video volume of " + locator + " to " + number);
		js.executeScript(String.format("arguments[0].volume=%s", number), driver.findElement(locator));
	}

	/**
	 * 
	 * @param locator
	 * @param number
	 */
	public void setTime(By locator, double number) {
		report.info("Set video time of " + locator + " to " + number);
		js.executeScript(String.format("arguments[0].currentTime=%s", number), driver.findElement(locator));
	}

	/**
	 * 
	 * @param locator
	 * @return
	 */
	public boolean isMuted(By locator) {
		report.info("Checking video is muted");
		boolean isTrue = (boolean) js.executeScript("return arguments[0].muted", driver.findElement(locator));
		report.info("Video of " + locator + " is muted : " + isTrue);
		return isTrue;
	}

	/**
	 * 
	 * @param locator
	 * @return
	 */
	public boolean isPaused(By locator) {
		report.info("Checking video is paused");
		boolean isTrue = (boolean) js.executeScript("return arguments[0].paused", driver.findElement(locator));
		report.info("Video of " + locator + " is paused : " + isTrue);
		return isTrue;
	}

	/**
	 * 
	 * @param locator
	 * @return
	 */
	public boolean isLoaded(By locator) {
		report.info("Checking video is loaded");
		boolean isTrue = (long) js.executeScript("return arguments[0].readyState", driver.findElement(locator)) == 4;
		report.info("Video of " + locator + " is loaded : " + isTrue);
		return isTrue;
	}

	/**
	 * 
	 * @param locator
	 * @param time
	 * @return
	 */
	public boolean volumeEquals(By locator, double time) {
		report.info("Checking volume level");
		boolean isTrue = getVolume(locator) == time;
		report.info("Video of " + locator + " volume is equal to " + time + " : " + isTrue);
		return isTrue;
	}

	/**
	 * 
	 * @param locator
	 * @param time
	 * @return
	 */
	public boolean timeEquals(By locator, double time) {
		report.info("Checking video current time");
		boolean isTrue = getTime(locator) == time;
		report.info("Video of " + locator + " time is equal to " + time + " : " + isTrue);
		return isTrue;
	}

	/**
	 * 
	 * @param locator
	 * @return
	 */
	private double getVolume(By locator) {
		report.info("Getting video volume level");
		return (double) js.executeScript("return arguments[0].volume", driver.findElement(locator));
	}

	/**
	 * 
	 * @param locator
	 * @return
	 */
	private double getTime(By locator) {
		report.info("Getting video current time");
		return (double) js.executeScript("return arguments[0].currentTime", driver.findElement(locator));
	}
}
