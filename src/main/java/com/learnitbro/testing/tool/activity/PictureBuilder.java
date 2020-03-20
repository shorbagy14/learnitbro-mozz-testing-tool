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

import com.github.kilianB.hashAlgorithms.RotAverageHash;
import com.github.kilianB.matcher.exotic.SingleImageMatcher;
import com.learnitbro.testing.tool.exceptions.ReadFileException;
import com.learnitbro.testing.tool.file.DirectoryHandler;
import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.os.OS;
import com.learnitbro.testing.tool.reporting.Report;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class PictureBuilder {

	private WebDriver driver;
	private Report report;
	private JavascriptExecutor js;
	private String location;

	@SuppressWarnings("unused")
	private PictureBuilder() {
		// Leave Empty -- Always keep this constructor private
		throw new NullPointerException("You forgot to pass the driver to this class: " + this.getClass().getName());
	}

	public PictureBuilder(WebDriver driver, Report report) {
		this.driver = driver;
		this.report = report;
		if (js == null)
			js = ((JavascriptExecutor) this.driver);

		location = DirectoryHandler.create(String.format("%s/screenshots/screenshots-%s-%s", FileHandler.getUserDir(),
				report.getDetails(), report.getTime())).getPath();
	}

	/**
	 * 
	 */
	public Screenshot fullpageScreenshot() {
		report.info("Taking a full page screenshot");
		
		// Hides the scroll bar when taking the screenshot
		js.executeScript("$('html').css('overflow', 'hidden')");
		// Freezes the header when taking the screenshot
		js.executeScript("$('header').css('position', 'absolute')");
		
		OS.OSType ostype = OS.getOperatingSystemType();
		Screenshot sc = null;
		String png = location + File.separator + getTime() + ".png";
		switch (ostype) {
		case Windows:
			sc = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1500)).takeScreenshot(driver);
			break;
		case MacOS:
			sc = new AShot().shootingStrategy(ShootingStrategies.viewportRetina(1500, 0, 0, getPixelRatio()))
					.takeScreenshot(driver);
			break;
		case Linux:
			sc = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1500)).takeScreenshot(driver);
			break;
		default:
			throw new RuntimeException("Opearting system is not detected");
		}
		
		driver.navigate().refresh();

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
	public boolean isFullpageScreenshotWithFileMatch(String expected) {
		Screenshot sc = fullpageScreenshot();
		String actual = location + File.separator + getTime() + "-actual.png";
		try {
			ImageIO.write(sc.getImage(), "PNG", new File(actual));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Screenshot exp = null;
		try {
			exp = new Screenshot(ImageIO.read(new File(expected)));
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new ReadFileException("Image is not found", e1);
		}

		ImageDiff diff = new ImageDiffer().makeDiff(exp, sc);
		String png = location + File.separator + getTime() + "-diff.png";
		BufferedImage diffImage = diff.getMarkedImage();

		try {
			ImageIO.write(diffImage, "PNG", new File(png));
		} catch (Exception e2) {
			e2.printStackTrace();
		}

//		return diff.getDiffSize() < 20000;
		return match(new File(expected), new File(actual));
	}

	/**
	 * 
	 * @param file
	 * @param locator
	 */
	public boolean isElementScreenshotWithFileMatch(String expected, By locator) {
		Screenshot sc = elementScreenshot(locator);
		String actual = location + File.separator + getTime() + "-actual.png";
		try {
			ImageIO.write(sc.getImage(), "PNG", new File(actual));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Screenshot exp = null;
		try {
			exp = new Screenshot(ImageIO.read(new File(expected)));
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new ReadFileException("Image is not found", e1);
		}

		ImageDiff diff = new ImageDiffer().makeDiff(exp, sc);
		String png = location + File.separator + getTime() + "-diff.png";
		BufferedImage diffImage = diff.getMarkedImage();
		try {
			ImageIO.write(diffImage, "PNG", new File(png));
		} catch (Exception e2) {
			e2.printStackTrace();
		}

//		return diff.getDiffSize() < 20000;
		return match(new File(expected), new File(actual));
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

	/**
	 * 
	 * @param img0
	 * @param img1
	 * @return
	 */
	private boolean match(File img0, File img1) {
		try {
			SingleImageMatcher matcher = new SingleImageMatcher();
			matcher.addHashingAlgorithm(new RotAverageHash(32), .2);

			if (matcher.checkSimilarity(img0, img1)) {
				report.info("Images are similar using a threshold of 20%");
				return true;
			} else {
				report.info("Images are different using a threshold of 20%");
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
