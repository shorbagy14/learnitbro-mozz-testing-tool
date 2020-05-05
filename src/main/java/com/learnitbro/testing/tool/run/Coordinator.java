package com.learnitbro.testing.tool.run;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.file.URLHandler;
import com.learnitbro.testing.tool.mail.Email;
import com.learnitbro.testing.tool.reporting.Report;
import com.learnitbro.testing.tool.web.ElementHandler;
import com.learnitbro.testing.tool.activity.ActionBuilder;
import com.learnitbro.testing.tool.activity.AssertBuilder;
import com.learnitbro.testing.tool.activity.PictureBuilder;
import com.learnitbro.testing.tool.activity.ScriptBuilder;
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
	private JSONArray number = null;
	private JSONArray numberValue = null;
	
	private boolean isFail = false;

	private WebDriver driver;
	private SoftAssert softAssert;
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
		softAssert = new SoftAssert();
		email = new Email();
	}

	public void runTests(JSONObject obj) {
		try {
			build(obj);
		} catch (AssertionError | Exception e) {
			throw new RuntimeException("Test has failed. Refer to the report");
		} finally {
			driver.quit();
			report.flush();
			softAssert.assertAll();

//			String info = " - " + Control.browser.toUpperCase();
//			email.sendAttachmentInEmail("Test Report - " + report.getTime() + info,
//					"This is an automated email. Here is the report for the test. Mohamed Elshorbagy", emailList,
//					report.getAllReports());
		}
	}

	/**
	 * building the steps from JSON to selenium
	 */
	public void build(JSONObject obj) {
		String DESCRIPTION = null;
		String TEST_DESCRIPTION = null;
		JSONArray category = obj.getJSONArray("children");
		for (int x = 0; x < category.length(); x++) {
			JSONObject cat = category.getJSONObject(x);
			DESCRIPTION = cat.getString("userObject");
			report.createTest(DESCRIPTION);
			JSONArray testCase = cat.getJSONArray("children");
			for (int y = 0; y < testCase.length(); y++) {
				JSONObject test = testCase.getJSONObject(y);
				TEST_DESCRIPTION = test.getString("userObject");
				report.info(TEST_DESCRIPTION);
				JSONArray input = test.getJSONArray("children");
				try {
					
					isFail = false;
					for (int i = 0; i < input.length(); i++) {
						JSONObject run = input.getJSONObject(i);
						steps(run);
					}
					
					if(isFail)
						throw new Exception("Test : " + TEST_DESCRIPTION + " is marked as 'FAIL' due to the errors above");
					else
						report.pass(TEST_DESCRIPTION + " - PASS");
				} catch (Exception e) {
					e.printStackTrace();
					report.fail(TEST_DESCRIPTION + " - FAIL", e);
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
		String category = run.getString("category");
		switch (category.toLowerCase()) {
		case "action":
			checkActions(run);
			break;
		case "assert":
			checkAsserts(run);
			break;
		case "wait":
			checkWaits(run);
			break;
		case "video":
			checkVideos(run);
			break;
		case "script":
			checkScripts(run);
			break;
		case "picture":
			checkPictures(run);
			break;
		}
	}

	private void checkActions(JSONObject run) {
		ActionBuilder a = new ActionBuilder(driver, report);
		ScriptBuilder j = new ScriptBuilder(driver, report);
		String userObject = run.getString("userObject");
		setValues(run);

		try {
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
				a.clickAndHold((By) locator.get(0));
				break;
			case "release":
				a.release();
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
			case "maximize":
				a.maximize();
				break;
			case "minimize":
				a.minimize();
				break;
			case "fullscreen":
				a.fullscreen();
				break;
			case "new tab":
				j.openNewTab(url.getString(0));
				break;
			case "scroll into view":
				j.scrollIntoView((By) locator.get(0));
				break;
			case "scroll by":
				j.scrollBy(number.getInt(0), number.getInt(1));
				break;
			case "switch to window":
				a.switchToWindow(number.getInt(0));
				break;
			case "switch to frame by index":
				a.switchToFrame(number.getInt(0));
				break;
			case "switch to frame by locator":
				a.switchToFrame((By) locator.get(0));
				break;
			case "switch to default frame":
				a.switchToDefaultFrame();
				break;
			case "select dropdown by text":
				a.selectDropdownByText((By) locator.get(0), text.getString(0));
				break;
			case "select dropdown by value":
				a.selectDropdownByValue((By) locator.get(0), text.getString(0));
				break;
			case "select dropdown by index":
				a.selectDropdownByIndex((By) locator.get(0), number.getInt(0));
				break;
			}
		} catch (AssertionError | Exception e) {
			report.fail("Assert check: FAIL", e);
			isFail = true;
			softAssert.assertTrue(false);
		}
	}

	private void checkAsserts(JSONObject run) {
		AssertBuilder a = new AssertBuilder(driver, report);
		String userObject = run.getString("userObject");
		setValues(run);

		try {
			switch (userObject.toLowerCase()) {
			case "displayed":
				Assert.assertTrue(a.isDisplayed((By) locator.get(0)), userObject);
				break;
			case "enabled":
				Assert.assertTrue(a.isEnabled((By) locator.get(0)), userObject);
				break;
			case "selected":
				Assert.assertTrue(a.isSelected((By) locator.get(0)), userObject);
				break;
			case "text contains":
				Assert.assertTrue(a.textContains((By) locator.get(0), text.getString(0)), userObject);
				break;
			case "text equals":
				Assert.assertTrue(a.textEquals((By) locator.get(0), text.getString(0)), userObject);
				break;
			case "text starts with":
				Assert.assertTrue(a.textStartsWith((By) locator.get(0), text.getString(0)), userObject);
				break;
			case "text ends with":
				Assert.assertTrue(a.textEndsWith((By) locator.get(0), text.getString(0)), userObject);
				break;
			case "title contains":
				Assert.assertTrue(a.titleContains(text.getString(0)), userObject);
				break;
			case "title equals":
				Assert.assertTrue(a.titleEquals(text.getString(0)), userObject);
				break;
			case "title starts with":
				Assert.assertTrue(a.titleStartsWith(text.getString(0)), userObject);
				break;
			case "title ends with":
				Assert.assertTrue(a.titleEndsWith(text.getString(0)), userObject);
				break;
			case "url contains":
				Assert.assertTrue(a.urlContains(text.getString(0)), userObject);
				break;
			case "url equals":
				Assert.assertTrue(a.urlEquals(url.getString(0)), userObject);
				break;
			case "url starts with":
				Assert.assertTrue(a.urlStartsWith(text.getString(0)), userObject);
				break;
			case "url ends with":
				Assert.assertTrue(a.urlEndsWith(text.getString(0)), userObject);
				break;
			case "page source contains":
				Assert.assertTrue(a.pageSourceContains(text.getString(0)), userObject);
				break;
			case "attribute contains":
				Assert.assertTrue(a.attributeContains((By) locator.get(0), text.getString(0), text.getString(1)),
						userObject);
				break;
			case "attribute equals":
				Assert.assertTrue(a.attributeEquals((By) locator.get(0), text.getString(0), text.getString(1)),
						userObject);
				break;
			case "attribute starts with":
				Assert.assertTrue(a.attributeStartsWith((By) locator.get(0), text.getString(0), text.getString(1)),
						userObject);
				break;
			case "attribute ends with":
				Assert.assertTrue(a.attributeEndsWith((By) locator.get(0), text.getString(0), text.getString(1)),
						userObject);
				break;
			}
		} catch (AssertionError | Exception e) {
			report.fail("Assert check: FAIL", e);
			isFail = true;
			softAssert.assertTrue(false);
		}
	}

	private void checkWaits(JSONObject run) {
		WaitBuilder w = new WaitBuilder(driver, report);
		String userObject = run.getString("userObject");
		setValues(run);

		try {
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
			case "visible":
				w.visibility((By) locator.get(0), time.getInt(0));
				break;
			case "clickable":
				w.clickable((By) locator.get(0), time.getInt(0));
				break;
			case "invisible":
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
				w.urlContains(text.getString(0), time.getInt(0));
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
		} catch (AssertionError | Exception e) {
			report.fail("Assert check: FAIL", e);
			isFail = true;
			softAssert.assertTrue(false);
		}
	}

	private void checkVideos(JSONObject run) {
		VideoBuilder v = new VideoBuilder(driver, report);
		String userObject = run.getString("userObject");
		setValues(run);

		try {
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
				Assert.assertFalse(v.isPaused((By) locator.get(0)), userObject);
				break;
			case "is paused":
				Assert.assertTrue(v.isPaused((By) locator.get(0)), userObject);
				break;
			case "is unmuted":
				Assert.assertFalse(v.isMuted((By) locator.get(0)), userObject);
				break;
			case "is muted":
				Assert.assertTrue(v.isMuted((By) locator.get(0)), userObject);
				break;
			case "is loaded":
				Assert.assertTrue(v.isLoaded((By) locator.get(0)), userObject);
				break;
			case "time equals":
				Assert.assertTrue(v.timeEquals((By) locator.get(0), time.getDouble(0)), userObject);
				break;
			case "volume equals":
				Assert.assertFalse(v.volumeEquals((By) locator.get(0), time.getDouble(0)), userObject);
				break;
			}
		} catch (AssertionError | Exception e) {
			report.fail("Assert check: FAIL", e);
			isFail = true;
			softAssert.assertTrue(false);
		}
	}

	private void checkScripts(JSONObject run) {
		ScriptBuilder j = new ScriptBuilder(driver, report);
		String userObject = run.getString("userObject");
		setValues(run);

		try {
			switch (userObject.toLowerCase()) {
			case "execute js command":
				j.executeJS(text.getString(0));
				break;
			case "execute js command by locator":
				j.executeJS(text.getString(0), (By) locator.get(0));
				break;
			case "executed js result equals":
				j.executedJSResultEquals(text.getString(0), text.getString(1));
				break;
			case "executed js result contains":
				j.executedJSResultContains(text.getString(0), text.getString(1));
				break;
			case "executed java file":
				j.executeJavaFile(file.getString(0));
				break;
			}
		} catch (AssertionError | Exception e) {
			report.fail("Assert check: FAIL", e);
			isFail = true;
			softAssert.assertTrue(false);
		}
	}

	private void checkPictures(JSONObject run) {
		PictureBuilder p = new PictureBuilder(driver, report);
		String userObject = run.getString("userObject");
		setValues(run);

		try {
			switch (userObject.toLowerCase()) {
			case "full page screenshot":
				p.fullpageScreenshot();
				break;
			case "element screenshot":
				p.elementScreenshot((By) locator.get(0));
				break;
			case "compare full page screenshot with":
				Assert.assertTrue(p.isFullpageScreenshotWithFileMatch(file.getString(0)), userObject);
				break;
			case "compare element screenshot with":
				Assert.assertTrue(p.isElementScreenshotWithFileMatch(file.getString(0), (By) locator.get(0)),
						userObject);
				break;
			}
		} catch (AssertionError | Exception e) {
			report.fail("Assert check: FAIL", e);
			isFail = true;
			softAssert.assertTrue(false);
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

		if (run.has("number")) {
			numberValue = run.getJSONArray("number");
			for (int x = 0; x < numberValue.length(); x++)
				number.put(Double.valueOf(numberValue.getString(x)));
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
		number = new JSONArray();
		numberValue = new JSONArray();
	}
}