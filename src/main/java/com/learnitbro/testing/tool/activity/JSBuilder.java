package com.learnitbro.testing.tool.activity;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.learnitbro.testing.tool.reporting.Report;

public class JSBuilder {
	
	private WebDriver driver;
	private Report report;
	private JavascriptExecutor js;

	@SuppressWarnings("unused")
	private JSBuilder() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public JSBuilder(WebDriver driver, Report report) {
		this.driver = driver;
		this.report = report;
		if (js == null)
			js = ((JavascriptExecutor) driver);

	}

}
