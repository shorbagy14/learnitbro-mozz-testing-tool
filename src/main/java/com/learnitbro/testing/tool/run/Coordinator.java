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

public class Coordinator {

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
					"This is an automated email. Here is the report for the test. Mohamed Elshorbagy", App.emailList,
					report.getAllReports());
			// softAssert.assertAll();
		}
	}

	public void build() {
		String content = null;
		String DESCRIPTION = null;
		try {
			content = JSONHandler.read(new File(FileHandler.getUserDir() + "/tree.json"));
		} catch (IOException e) {
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
						if (((String) run.get("userObject")).equalsIgnoreCase("link")) {
							driver.get(((String) run.get("value")));
							report.info("Going to " + ((String) run.get("value")));
						} else if (((String) run.get("userObject")).equalsIgnoreCase("click")) {
							driver.findElement(By.xpath(((String) run.get("value")))).click();
							report.info("Clicking on " + ((String) run.get("value")));
						} else if (((String) run.get("userObject")).equalsIgnoreCase("send keys")) {
							driver.findElement(By.xpath(((String) run.get("value")))).sendKeys((String) run.get("object"));
							report.info("Send Keys of " +  ((String) run.get("object")) + " on " + ((String) run.get("value")));
						} else if (((String) run.get("userObject")).equalsIgnoreCase("clear")) {
							driver.findElement(By.xpath(((String) run.get("value")))).clear();
							report.info("Clearing field on " + ((String) run.get("value")));
						} else if (((String) run.get("userObject")).equalsIgnoreCase("send keys")) {
							driver.findElement(By.xpath(((String) run.get("value")))).sendKeys((String) run.get("object"));
							report.info("Uploading " +  ((String) run.get("object")) + " on " + ((String) run.get("value")));
						}
					}
					report.pass(DESCRIPTION + " - PASS");
				} catch (Exception e) {
					e.printStackTrace();
					report.fail(DESCRIPTION + " - FAIL", e);
				}
			}
		}
	}
}