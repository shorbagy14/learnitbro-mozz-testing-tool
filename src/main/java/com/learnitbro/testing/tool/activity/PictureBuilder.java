package com.learnitbro.testing.tool.activity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.learnitbro.testing.tool.exceptions.ReadFileException;
import com.learnitbro.testing.tool.file.DirectoryHandler;
import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.reporting.Report;
import com.learnitbro.testing.tool.run.OS;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
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

	/**
	 * 
	 */
	public Screenshot fullpageScreenshot() {
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
		return sc;
	}

	/**
	 * 
	 * @param locator
	 */
	public Screenshot elementScreenshot(By locator) {
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
		return sc;
	}

	/**
	 * 
	 * @param file
	 */
	public void comapreFullpageScreenshotWith(String file) {
		Screenshot sc = fullpageScreenshot();
		Screenshot expected = null;
		try {
			expected = new Screenshot(ImageIO.read(new File(file)));
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new ReadFileException("Image is not found", e1);
		}

		ImageDiff diff = new ImageDiffer().makeDiff(expected, sc);
		String png = location + File.separator + getTime() + ".png";
		BufferedImage diffImage = diff.getMarkedImage();

		try {
			ImageIO.write(diffImage, "PNG", new File(png));
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * 
	 * @param file
	 * @param locator
	 */
	public void comapreElementScreenshotWith(String file, By locator) {
		Screenshot sc = elementScreenshot(locator);
		Screenshot expected = null;
		try {
			expected = new Screenshot(ImageIO.read(new File(file)));
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new ReadFileException("Image is not found", e1);
		}

		ImageDiff diff = new ImageDiffer().makeDiff(expected, sc);
		String png = location + File.separator + getTime() + ".png";
		BufferedImage diffImage = diff.getMarkedImage();

		try {
			ImageIO.write(diffImage, "PNG", new File(png));
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	private float getPixelRatio() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return Float.parseFloat(js.executeScript("return window.devicePixelRatio;").toString());
	}

	/**
	 * 
	 * @return
	 */
	private String getTime() {
		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime());
	}
}
