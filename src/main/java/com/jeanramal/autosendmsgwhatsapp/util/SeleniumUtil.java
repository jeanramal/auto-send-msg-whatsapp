package com.jeanramal.autosendmsgwhatsapp.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SeleniumUtil {

	private static WebDriver driver;

	public static void clickById(String id) throws InterruptedException {
		click(By.id(id));
	}

	public static void clickByCssSelector(String cssSelector) throws InterruptedException {
		click(By.cssSelector(cssSelector));
	}

	public static void sendById(String id, String... keysToSend) throws InterruptedException {
		sendKeys(By.id(id), keysToSend);
	}

	public static void sendByCssSelector(String cssSelector, String... keysToSend) throws InterruptedException {
		sendKeys(By.cssSelector(cssSelector), keysToSend);
	}

	public static void sendByName(String name, String... keysToSend) throws InterruptedException {
		sendKeys(By.name(name), keysToSend);
	}

	public static void click(By by) throws InterruptedException {
		boolean result = false;
		do {
			result = clickWrapper(by);
		} while (!result);
	}

	public WebElement findElement(By by) throws InterruptedException {
		boolean result = false;
		do {
			result = findElementWrapper(by);
		} while (!result);
		return driver.findElement(by);
	}

	public static boolean clickWrapper(By by) throws InterruptedException {
		while (!existsElement(by)) {
			Thread.sleep(1000);
		}

		try {
			driver.findElement(by).click();
			return true;
		} catch (org.openqa.selenium.ElementNotInteractableException e) {
			Thread.sleep(1000);
			return false;
		}
	}

	public boolean findElementWrapper(By by) throws InterruptedException {
		while (!existsElement(by)) {
			Thread.sleep(1000);
		}

		try {
			driver.findElement(by);
			return true;
		} catch (org.openqa.selenium.ElementNotInteractableException e) {
			Thread.sleep(1000);
			return false;
		}
	}

	public static void sendKeys(By by, String... keysToSend) throws InterruptedException {
		boolean result = false;
		do {
			result = sendKeysWrapper(by, keysToSend);
		} while (!result);
	}

	public static boolean sendKeysWrapper(By by, String... keysToSend) throws InterruptedException {
		while (!existsElement(by)) {
			Thread.sleep(1000);
		}

		Thread.sleep(1000);

		try {
			driver.findElement(by).sendKeys(keysToSend);
			return true;
		} catch (org.openqa.selenium.ElementNotInteractableException e) {
			Thread.sleep(1000);
			return false;
		}
	}

	public String getTextById(String id) throws InterruptedException {
		return getText(By.id(id));
	}

	public String getText(By by) throws InterruptedException {
		String result = null;
		do {
			result = getTextWrapper(by);
		} while (result == null);
		return result;
	}

	public String getTextWrapper(By by) throws InterruptedException {
		while (!existsElement(by)) {
			Thread.sleep(1000);
		}
		try {
			String result = driver.findElement(by).getText();
			return result == null ? "" : result;
		} catch (org.openqa.selenium.ElementNotInteractableException e) {
			Thread.sleep(1000);
			return null;
		}
	}

	public static boolean existsElement(By by) {
		try {
			driver.findElement(by);
		} catch (org.openqa.selenium.NoSuchElementException e) {
			// log.error("existsElement [NoSuchElementException] " + e.getMessage());
			return false;
		}

		return true;
	}

	public WebElement findElementByCssSelector(String cssSelector) throws InterruptedException {
		return findElement(By.cssSelector(cssSelector));
	}

	public WebElement findElementByCssSelector(WebElement webElement, String cssSelector) {
		return webElement.findElement(By.cssSelector(cssSelector));
	}

}
