package com.jeanramal.autosendmsgwhatsapp.service;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeanramal.autosendmsgwhatsapp.bean.Parameters;
import com.jeanramal.autosendmsgwhatsapp.util.InvalidNumber;
import com.jeanramal.autosendmsgwhatsapp.util.Util;

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
		String numbersUnProcessed = "";
		
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
			while(existsElement(By.cssSelector("canvas[aria-label='Scan me!']"))) {
				Thread.sleep(1000);
			}
			log.info(trazabilidad + "Scanned.");
			
			
			log.info(trazabilidad + "Waiting connection...");
			while(!existsElement(By.cssSelector("div[data-asset-intro-image-light='true']"))) {
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
			    	
			    	if(number.equals("")) {
			    		throw new InvalidNumber("Invalid number. Empty", new Throwable("InvalidNumber"));
			    	}
			    	
			    	if(number.length() < 7 || number.length() > 13) {
			    		throw new InvalidNumber("Invalid number. length " + number.length(), new Throwable("InvalidNumber"));
			    	}
			    	
			    	log.info(loadTrazabilidad + feedback + " - " + number + ": Sending message...");
			    	
			    	log.info(loadTrazabilidad + feedback + " - " + number + ": Loading Page...");
			    	driver.get("https://web.whatsapp.com/send?phone=" + number + "&text=" + message);
			    	log.info(loadTrazabilidad + feedback + " - " + number + ": Page loaded.");
			    	
			    	try {
			    		
			    		Thread.sleep(2000);
			    		
				    	while(!existsElement(By.cssSelector("span[data-icon='send']"))) {
							//Thread.sleep(1000);
							
							if(existsElement(By.cssSelector("div[data-animate-modal-popup='true']"))){
								
								String text = driver.findElement(By.cssSelector("div[data-animate-modal-body='true'] > div:first-child")).getText();
								
								throw new InvalidNumber(text, new Throwable("InvalidNumber"));
							}
							
						}
			    	}catch (UnhandledAlertException e) {
			    		log.error("[UnhandledAlertException]");
			    		
			    		Thread.sleep(2000);
			    		
//			    		Alert alert = this.driver.switchTo().alert();
//			            if (alert != null){
//			              alert.accept();
//			            }
			            while(!existsElement(By.cssSelector("span[data-icon='send']"))) {
							//Thread.sleep(1000);
							
							if(existsElement(By.cssSelector("div[data-animate-modal-popup='true']"))){
								
								String text = driver.findElement(By.cssSelector("div[data-animate-modal-body='true'] > div:first-child")).getText();
								
								throw new InvalidNumber(text, new Throwable("InvalidNumber"));
							}
							
						}
					}
					
				    //Thread.sleep(1000);
				    
				    log.info(loadTrazabilidad + feedback + " - " + number + ": Sending message...");
				    driver.findElement(By.cssSelector("span[data-icon='send']")).click();
					log.info(loadTrazabilidad + feedback + " - " + number + ": Message sended.");
					
					//Thread.sleep(1000);
					
					current++;
					numberProcessedNumbers++;
					
		    	} catch (InvalidNumber e) {
		    		current++;
		    		invalidNumbers += number + ",";
		    		String cause = e == null?"null": (e.getCause()==null?"null": e.getCause().getMessage());
		    		String mesanje = e == null?"null": (e.getMessage()==null?"null": e.getMessage());
		    		log.error(loadTrazabilidad + feedback + " - " + number + ": [" + cause + "] - " + mesanje);
		    		numberUnprocessedNumbers++;
				} catch (Exception e) {
		    		current++;
		    		numbersUnProcessed += number + ",";
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
			log.info(trazabilidad + "Numbers unprocessed: " + numbersUnProcessed);
			
			log.info(trazabilidad + "");
			log.info(trazabilidad + "Elapsed total time: " + (System.currentTimeMillis() - timeStart) + " ms.");
			log.info(trazabilidad + "-------------- FINISH_PROCESS -------------------------------------");
			log.info("");
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
		driver = new ChromeDriver();
		System.out.println("timeouts: " + driver.manage().timeouts().toString());
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
