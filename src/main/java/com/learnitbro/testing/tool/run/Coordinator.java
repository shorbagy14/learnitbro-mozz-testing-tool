package com.learnitbro.testing.tool.run;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.file.JSONHandler;
import com.learnitbro.testing.tool.reporting.Email;
import com.learnitbro.testing.tool.reporting.Report;
import com.learnitbro.testing.tool.App;
import com.learnitbro.testing.tool.activity.Action;

public class Coordinator {
	
	public static String emailList = "shorbagy14@gmail.com";

	private WebDriver driver;
//	private SoftAssert softAssert;
	private Report report;
	private Email email;

	@SuppressWarnings("unused")
	private Coordinator() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public Coordinator(WebDriver driver, Report report) {
		this.driver = driver;
		this.report = report;
//		softAssert = new SoftAssert();
		email = new Email();
	}

	public void runTests() {
		try {
			build();
		} catch (Exception | AssertionError e) {
			throw new RuntimeException("Test has failed. Refer to the report");
		} finally {
			driver.quit();
			report.flush();
			String info = " - " + Control.browser.toUpperCase();
			email.sendAttachmentInEmail("Test Report - " + report.getTime() + info,
					"This is an automated email. Here is the report for the test. Mohamed Elshorbagy", emailList,
					report.getAllReports());
			// softAssert.assertAll();
		}
	}

	/**
	 * building the steps from JSON to selenium
	 */
	public void build() {
		String content = null;
		String DESCRIPTION = null;
		try {
			content = JSONHandler.read(new File(FileHandler.getUserDir() + "/tree.json"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONObject obj = new JSONObject(content);
		JSONArray category = (JSONArray) obj.get("children");
		for (int x = 0; x < category.length(); x++) {
			JSONObject cat = (JSONObject) category.get(x);
			DESCRIPTION = (String) cat.get("userObject");
			report.createTest(DESCRIPTION);
			JSONArray testCase = (JSONArray) cat.get("children");
			for (int y = 0; y < testCase.length(); y++) {
				JSONObject test = (JSONObject) testCase.get(y);
				report.info((String) test.get("userObject"));
				JSONArray input = (JSONArray) test.get("children");
				try {
					for (int i = 0; i < input.length(); i++) {
						JSONObject run = (JSONObject) input.get(i);
						steps(run);
					}
					report.pass(DESCRIPTION + " - PASS");
				} catch (Exception e) {
					e.printStackTrace();
					report.fail(DESCRIPTION + " - FAIL", e);
				}
			}
		}
	}

	/**
	 * Create steps here
	 * @param run (JSONObject)
	 */
	public void steps(JSONObject run) {
		Action a = new Action(driver, report);
		String userObject = run.getString("userObject");
		
		String text = null;
		String url = null;
		By locator = null;
		
		if(run.has("url"))
			url = run.getString("url");
		
		if(run.has("text"))
			text = run.getString("text");
		
		if(run.has("locator")) {
			String locatorType = run.getString("locatorType");
			String locatorValue = run.getString("locator");
			locator = getLocator(locatorType, locatorValue);
		}

		switch (userObject.toLowerCase()) {
		case "link":
			a.link(url);
			break;
		case "click":
			a.click(locator);
			break;
		case "send keys":
			a.inputText(locator, text);
			break;
		case "clear":
			a.clear(locator);
			break;
		case "upload":
			a.upload(locator, text);
			break;
		}
	}
	
	/**
	 * Returns the locator based on type in JSON
	 * 
	 * @param type - locator type
	 * @param text - locator value
	 * @return locator
	 */
	private By getLocator(String type, String text) {
		if(type.equals("xpath"))
			return By.xpath(text);
		else if(type.equals("class"))
			return By.className(text);
		else if(type.equals("css"))
			return By.cssSelector(text);
		else if(type.equals("name"))
			return By.name(text);
		else if(type.equals("id"))
			return By.id(text);
		return null;
	}
}