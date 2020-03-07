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
import com.learnitbro.testing.tool.activity.JSBuilder;
import com.learnitbro.testing.tool.activity.VideoBuilder;
import com.learnitbro.testing.tool.activity.WaitBuilder;

public class Coordinator {

	private JSONArray text = null;
	private JSONArray url = null;
	private JSONArray file = null;
	private JSONArray locator = null;
	private JSONArray locatorValue = null;
	private JSONArray locatorType = null;
	private JSONArray time = null;
	private JSONArray timeValue = null;

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
		checkWaits(run);
		checkVideos(run);
	}

	private void checkActions(JSONObject run) {
		ActionBuilder a = new ActionBuilder(driver, report);
		JSBuilder j = new JSBuilder(driver, report);
		String userObject = run.getString("userObject");
		setValues(run);

		switch (userObject.toLowerCase()) {
		case "link":
			a.link(url.getString(0));
			break;
		case "click":
			a.click((By) locator.get(0));
			break;
		case "input text":
			a.inputText((By) locator.get(0), text.getString(0));
			break;
		case "submit":
			a.submit((By) locator.get(0));
			break;
		case "clear":
			a.clear((By) locator.get(0));
			break;
		case "upload":
			a.upload((By) locator.get(0), file.getString(0));
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
			a.dragAndDrop((By) locator.get(0), (By) locator.get(1));
			break;
		case "hover":
			a.hover((By) locator.get(0));
			break;
		case "context click":
			a.contextClick((By) locator.get(0));
			break;
		case "double click":
			a.doubleClick((By) locator.get(0));
			break;
		}
	}

	private void checkAsserts(JSONObject run) {
		AssertBuilder a = new AssertBuilder(driver, report);
		String userObject = run.getString("userObject");
		setValues(run);

		switch (userObject.toLowerCase()) {
		case "displayed":
			Assert.assertTrue(a.isDisplayed((By) locator.get(0)));
			break;
		case "enabled":
			Assert.assertTrue(a.isEnabled((By) locator.get(0)));
			break;
		case "selected":
			Assert.assertTrue(a.isSelected((By) locator.get(0)));
			break;
		case "text contains":
			Assert.assertTrue(a.textContains((By) locator.get(0), text.getString(0)));
			break;
		case "text equals":
			Assert.assertTrue(a.textEquals((By) locator.get(0), text.getString(0)));
			break;
		}
	}

	private void checkWaits(JSONObject run) {
		WaitBuilder w = new WaitBuilder(driver, report);
		String userObject = run.getString("userObject");
		setValues(run);

		switch (userObject.toLowerCase()) {
		case "sleep":
			w.sleep(time.getInt(0));
			break;
		case "page to load":
			w.pageToLoad(time.getInt(0));
			break;
		case "presence":
			w.presence((By) locator.get(0), time.getInt(0));
			break;
		case "visble":
			w.visibility((By) locator.get(0), time.getInt(0));
			break;
		case "clickable":
			w.clickable((By) locator.get(0), time.getInt(0));
			break;
		case "invisble":
			w.invisibility((By) locator.get(0), time.getInt(0));
			break;
		case "selected":
			w.selected((By) locator.get(0), time.getInt(0));
			break;
		case "title contains":
			w.titleContains(text.getString(0), time.getInt(0));
			break;
		case "title to be":
			w.titleToBe(text.getString(0), time.getInt(0));
			break;
		case "url contains":
			w.urlContains(url.getString(0), time.getInt(0));
			break;
		case "url to be":
			w.urlToBe(url.getString(0), time.getInt(0));
			break;
		case "attribute contains":
			w.attributeContains((By) locator.get(0), text.getString(0), text.getString(1), time.getInt(0));
			break;
		case "attribute to be":
			w.attributeToBe((By) locator.get(0), text.getString(0), text.getString(1), time.getInt(0));
			break;
		}
	}

	private void checkVideos(JSONObject run) {
		VideoBuilder v = new VideoBuilder(driver, report);
		String userObject = run.getString("userObject");
		setValues(run);

		switch (userObject.toLowerCase()) {
		case "pause":
			v.pause((By) locator.get(0));
			break;
		case "play":
			v.play((By) locator.get(0));
			break;
		case "mute":
			v.mute((By) locator.get(0));
			break;
		case "unmute":
			v.unmute((By) locator.get(0));
			break;
		case "replay":
			v.replay((By) locator.get(0));
			break;
		case "set volume":
			v.setVolume((By) locator.get(0), time.getDouble(0));
			break;
		case "set time":
			v.setVolume((By) locator.get(0), time.getDouble(0));
			break;
		case "is playing":
			Assert.assertFalse(v.isPaused((By) locator.get(0)));
			break;
		case "is paused":
			Assert.assertTrue(v.isPaused((By) locator.get(0)));
			break;
		case "is unmuted":
			Assert.assertFalse(v.isMuted((By) locator.get(0)));
			break;
		case "is muted":
			Assert.assertTrue(v.isMuted((By) locator.get(0)));
			break;
		}
	}

	private void setValues(JSONObject run) {
		restValues();

		if (run.has("text"))
			text = run.getJSONArray("text");

		if (run.has("file")) {
			file = run.getJSONArray("file");
			for (int x = 0; x < file.length(); x++)
				FileHandler.isValidFile(new File(file.getString(x)));
		}

		if (run.has("url")) {
			url = run.getJSONArray("url");
			for (int x = 0; x < url.length(); x++)
				URLHandler.isURLValid(url.getString(x));
		}

		if (run.has("locator")) {
			locatorType = run.getJSONArray("locatorType");
			locatorValue = run.getJSONArray("locator");
			for (int x = 0; x < locatorValue.length(); x++)
				locator.put(ElementHandler.getLocator(locatorType.getString(x), locatorValue.getString(x)));
		}

		if (run.has("time")) {
			timeValue = run.getJSONArray("time");
			for (int x = 0; x < timeValue.length(); x++)
				time.put(Integer.valueOf(timeValue.getString(x)));
		}
	}

	private void restValues() {
		text = new JSONArray();
		url = new JSONArray();
		file = new JSONArray();
		locator = new JSONArray();
		locatorValue = new JSONArray();
		locatorType = new JSONArray();
		time = new JSONArray();
		timeValue = new JSONArray();
	}
}