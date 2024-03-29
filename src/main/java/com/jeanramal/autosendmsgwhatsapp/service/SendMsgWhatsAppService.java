package com.jeanramal.autosendmsgwhatsapp.service;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeanramal.autosendmsgwhatsapp.bean.Parameters;
import com.jeanramal.autosendmsgwhatsapp.util.Util;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SendMsgWhatsAppService {
	
	@Autowired
	private Parameters parameters;
	
	private WebDriver driver;
	
	public void sendMsg() {
		
		long timeStart = System.currentTimeMillis();
		String transaccion = Util.getTransaccion();
		String trazabilidad = "[" + transaccion + "] ";
		
		int numberNumbers = 0;
		int numberProcessedNumbers = 0;
		int numberUnprocessedNumbers = 0;
		String invalidNumbers = "";
		
		log.info(trazabilidad + "-------------- START_PROCESS -------------------------------------");
		log.info(trazabilidad + "Sending Message WhatsApp");
		
		try {
			
			log.info(trazabilidad + "parameters: " + parameters.toString());
		
			prepareDriver(trazabilidad, parameters.getPathDriver());
			
			driver.manage().window().maximize();
			
			log.info(trazabilidad + "Loading page...");
			driver.get("https://web.whatsapp.com/");
			log.info(trazabilidad + "Page loaded.");
			
			
			log.info(trazabilidad + "Waiting scanning...");
			while(existsElement(By.className("_11ozL"))) {
				Thread.sleep(1000);
			}
			log.info(trazabilidad + "Scanned.");
			
			
			log.info(trazabilidad + "Waiting connection...");
			while(!existsElement(By.className("_64p9P"))) {//_64p9P//_1135V//_3SOOk//_3PwsU
				Thread.sleep(1000);
			}
			log.info(trazabilidad + "Connected.");
				
			createTab(trazabilidad);
		    
		    String[] numbers = parameters.getNumbers().split(",");
		    int current = 1;
		    
		    numberNumbers = numbers.length;
		    
		    String message = parameters.getMessage();

		    
		    for (String number : numbers) {
		    	
		    	log.info(trazabilidad + "");
				
				long loadTimeStart = System.currentTimeMillis();
				String loadTransaccion = Util.getTransaccion();
				String loadTrazabilidad = trazabilidad + "[" + loadTransaccion + "] ";
		    	
		    	String feedback = current + "/" + numberNumbers;
		    	
		    	log.info(loadTrazabilidad + "-------------- START_SENDING ----------------------");
		    	
		    	try {
		    		
			    	number = number.trim();
			    	
			    	log.info(loadTrazabilidad + feedback + " - " + number + ": Sending message...");
			    	
			    	driver.get("https://web.whatsapp.com/send?phone=" + number + "&text=" + message);
			    	
			    	//Thread.sleep(1000);
			    	
				    while(!existsElement(By.className("epia9gcq"))) {//_2xy_p _3XKXx//tvf2evcx oq44ahr5 lb5m6g5c svlsagor p2rjqpw5 epia9gcq
						Thread.sleep(1000);
						if(existsElement(By.cssSelector("[role='dialog']"))) {
							var text = driver.findElement(By.cssSelector("[role='dialog']"))
							.findElement(By.tagName("div"))
							.findElements(By.tagName("div"))
							.get(0).getText();
							if(!text.contains("chat") && !text.contains("Cancel") && !text.isEmpty()) {
								throw new Exception(text, new Throwable("InvalidNumberException"));
							}
						}
						/*
						if(existsElement(By.className("_1CnF3")) 
						   || existsElement(By.className("_3lLzD"))
						   || existsElement(By.className("P8cO8"))
						   ){
							throw new Exception("Invalid number", new Throwable("InvalidNumberException"));
						}
						*/
					}
					
					driver.findElement(By.className("epia9gcq")).click();
					
					current++;
					numberProcessedNumbers++;
					
					Thread.sleep(1500);
					
					log.info(loadTrazabilidad + feedback + " - " + number + ": Message sended.");
					
		    	} catch (Exception e) {
		    		current++;
		    		invalidNumbers += number + ",";
		    		String cause = e == null?"null": (e.getCause()==null?"null": e.getCause().getMessage());
		    		String mesanje = e == null?"null": (e.getMessage()==null?"null": e.getMessage());
		    		log.error(loadTrazabilidad + feedback + " - " + number + ": [" + cause + "] - " + mesanje);
		    		numberUnprocessedNumbers++;
				} finally {
					log.info(loadTrazabilidad + "Elapsed sending time: " + (System.currentTimeMillis() - loadTimeStart) + " ms.");
					log.info(loadTrazabilidad + "-------------- FINISH_SENDING ----------------------");
				}
			}
		
		} catch (InterruptedException e) {
			log.error(trazabilidad + "InterruptedException", e);
		} catch (TimeoutException e) {
			log.error(trazabilidad + "TimeoutException", e);
		} catch (WebDriverException e) {
			log.error(trazabilidad + "WebDriverException", e);
		} catch (Exception e) {
			log.error(trazabilidad + "Exception", e);
		} finally {
			
			log.info(trazabilidad + "");
			closeAllWindows(trazabilidad);
			
			log.info(trazabilidad + "");
			log.info(trazabilidad + "Statistics:");
			log.info(trazabilidad + "Number of numbers: " + numberNumbers);
			log.info(trazabilidad + "Number of processed numbers: " + numberProcessedNumbers);
			log.info(trazabilidad + "Number of unprocessed numbers: " + numberUnprocessedNumbers);
			log.info(trazabilidad + "Invalid numbers: " + invalidNumbers);
			
			log.info(trazabilidad + "");
			log.info(trazabilidad + "Elapsed total time: " + (System.currentTimeMillis() - timeStart) + " ms.");
			log.info(trazabilidad + "-------------- FINISH_PROCESS -------------------------------------");
		}
	}

	private void closeAllWindows(String trazabilidad) {
		log.info(trazabilidad + "closeAllWindows");
		try {
			ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
			for (String tab : tabs) {
				driver.switchTo().window(tab).close();
			}
		} catch (Exception e) {
			
		}
	}

	private void createTab(String trazabilidad) {
		log.info(trazabilidad + "createTab");
		((JavascriptExecutor)driver).executeScript("window.open()");
		
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(tabs.size()-1));
	}
	
	private void prepareDriver(String trazabilidad, String pathDriver) {
		log.info(trazabilidad + "prepareDriver");
		System.setProperty("webdriver.chrome.driver", pathDriver);
		WebDriverManager.chromedriver().setup();
		this.driver = new ChromeDriver();
	}
	
	private boolean existsElement(By by) {
		try {
			driver.findElement(by);
		}catch (org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
		return true;
	}

}
