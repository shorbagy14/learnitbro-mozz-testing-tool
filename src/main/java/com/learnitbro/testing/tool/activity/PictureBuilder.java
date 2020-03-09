package com.learnitbro.testing.tool.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.learnitbro.testing.tool.file.DirectoryHandler;
import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.reporting.Report;
import com.learnitbro.testing.tool.run.OS;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class PictureBuilder {

	private WebDriver driver;
	private Report report;
	private String location;

	@SuppressWarnings("unused")
	private PictureBuilder() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public PictureBuilder(WebDriver driver, Report report) {
		this.driver = driver;
		this.report = report;

		location = DirectoryHandler.create(String.format("%s/screenshots/screenshots-%s-%s", FileHandler.getUserDir(),
				report.getDetails(), report.getTime())).getPath();
	}

	public void fullpageScreenshot() {
		report.info("Taking a full page screenshot");
		OS.OSType ostype = OS.getOperatingSystemType();
		Screenshot sc = null;
		String png = location + File.separator + getTime() + ".png";
		switch (ostype) {
		case Windows:
			sc = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
			break;
		case MacOS:
			sc = new AShot().shootingStrategy(ShootingStrategies.viewportRetina(25, 0, 0, getPixelRatio()))
					.takeScreenshot(driver);
			break;
		case Linux:
			sc = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
			break;
		default:
			throw new RuntimeException("Opearting system is not detected");
		}
		
		try {
			ImageIO.write(sc.getImage(), "PNG", new File(png));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void elementScreenshot(By locator) {
		report.info("Taking an element screenshot of : " + locator);
		OS.OSType ostype = OS.getOperatingSystemType();
		Screenshot sc = null;
		String png = location + File.separator + getTime() + ".png";
		switch (ostype) {
		case Windows:
			sc = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver,
					driver.findElement(locator));
			break;
		case MacOS:
			sc = new AShot().shootingStrategy(ShootingStrategies.viewportRetina(25, 0, 0, getPixelRatio()))
					.takeScreenshot(driver, driver.findElement(locator));
			break;
		case Linux:
			sc = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver,
					driver.findElement(locator));
			break;
		default:
			throw new RuntimeException("Opearting system is not detected");
		}

		try {
			ImageIO.write(sc.getImage(), "PNG", new File(png));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private float getPixelRatio() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return Float.parseFloat(js.executeScript("return window.devicePixelRatio;").toString());
	}

	private String getTime() {
		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime());
	}

}
