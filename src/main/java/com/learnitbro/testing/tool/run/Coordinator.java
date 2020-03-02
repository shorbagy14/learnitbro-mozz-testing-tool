package com.learnitbro.testing.tool.run;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.file.URLHandler;
import com.learnitbro.testing.tool.reporting.Email;
import com.learnitbro.testing.tool.reporting.Report;
import com.learnitbro.testing.tool.web.ElementHandler;
import com.learnitbro.testing.tool.activity.ActionBuilder;
import com.learnitbro.testing.tool.activity.AssertBuilder;
import com.learnitbro.testing.tool.activity.WaitBuilder;

public class Coordinator {

	private String text = null;
	private String url = null;
	private String file = null;
	private By locator = null;
	private int time = 0;

	private String emailList = "shorbagy14@gmail.com";

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

	public void runTests(JSONObject obj) {
		try {
			build(obj);
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
	public void build(JSONObject obj) {
		String DESCRIPTION = null;
		JSONArray category = obj.getJSONArray("children");
		for (int x = 0; x < category.length(); x++) {
			JSONObject cat = category.getJSONObject(x);
			DESCRIPTION = cat.getString("userObject");
			report.createTest(DESCRIPTION);
			JSONArray testCase = cat.getJSONArray("children");
			for (int y = 0; y < testCase.length(); y++) {
				JSONObject test = testCase.getJSONObject(y);
				report.info((String) test.get("userObject"));
				JSONArray input = test.getJSONArray("children");
				try {
					for (int i = 0; i < input.length(); i++) {
						JSONObject run = input.getJSONObject(i);
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
	 * 
	 * @param run (JSONObject)
	 */
	public void steps(JSONObject run) {
		checkActions(run);
		checkAsserts(run);
	}

	private void checkActions(JSONObject run) {
		ActionBuilder a = new ActionBuilder(driver, report);
		String userObject = run.getString("userObject");
		setValues(run);

		switch (userObject.toLowerCase()) {
		case "link":
			a.link(url);
			break;
		case "click":
			a.click(locator);
			break;
		case "input text":
			a.inputText(locator, text);
			break;
		case "submit":
			a.submit(locator);
			break;
		case "clear":
			a.clear(locator);
			break;
		case "sleep":
			a.sleep(time);
			break;
		case "upload":
			a.upload(locator, file);
			break;
		case "back":
			a.back();
			break;
		case "forward":
			a.forward();
			break;
		case "refresh":
			a.refresh();
			break;
		case "close":
			a.close();
			break;
		case "click and hold":
			a.close();
			break;
		case "release":
			a.close();
			break;
		case "drag and drop":
			// FIX THIS
			a.dragAndDrop(locator, locator);
			// a.dragAndDrop(locator[0], locator[1]);
			break;
		case "hover":
			a.hover(locator);
			break;
		case "context click":
			a.contextClick(locator);
			break;
		case "double click":
			a.doubleClick(locator);
			break;
		}
	}
	
	private void checkAsserts(JSONObject run) {
		AssertBuilder a = new AssertBuilder(driver, report);
		String userObject = run.getString("userObject");
		setValues(run);

		switch (userObject.toLowerCase()) {
		case "Displayed":
			Assert.assertTrue(a.isDisplayed(locator));
			break;
		case "Enabled":
			Assert.assertTrue(a.isEnabled(locator));
			break;
		case "Selected":
			Assert.assertTrue(a.isSelected(locator));
			break;
		}
	}
	
	private void checkWaits(JSONObject run) {
		WaitBuilder a = new WaitBuilder(driver, report);
		String userObject = run.getString("userObject");
		setValues(run);

		switch (userObject.toLowerCase()) {
		case "link":
			a.pageToLoad(time);
			break;
		}
	}

	private void setValues(JSONObject run) {
		text = null;
		url = null;
		file = null;
		locator = null;
		time = 0;

		if (run.has("text"))
			text = run.getString("text");
		else if (run.has("text")) {
			file = run.getString("file");
			FileHandler.isValidFile(new File(file));
		} else if (run.has("url")) {
			url = run.getString("url");
			URLHandler.isURLValid(url);
		} else if (run.has("locator")) {
			String locatorType = run.getString("locatorType");
			String locatorValue = run.getString("locator");
			locator = ElementHandler.getLocator(locatorType, locatorValue);
		} else if (run.has("time")) {
			time = Integer.valueOf(run.getString("time"));
		}
	}
}