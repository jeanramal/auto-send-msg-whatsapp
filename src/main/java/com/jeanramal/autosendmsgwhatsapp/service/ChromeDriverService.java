package com.jeanramal.autosendmsgwhatsapp.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeanramal.autosendmsgwhatsapp.payload.ChromeDriverClient;
import com.jeanramal.autosendmsgwhatsapp.util.ZipUtil;

import feign.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChromeDriverService {
	
	@Autowired
	private ChromeDriverClient chromeDriverClient;
	
	public void getLatestReleaseTest() throws IOException {
		//Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
		killProcess("chromedriver.exe");
		// String latestRelease = "90.0.4430.24";
		String latestRelease = chromeDriverClient.getLatestRelease();
		log.info("latestRelease: " + latestRelease);
		// Response body =
		// chromeDriverClient.getLatestReleaseFile(latestRelease).getBody();
		Response response = chromeDriverClient.getLatestReleaseFile(latestRelease);
		Response.Body body = response.body();
		log.info("version descargada");
		ZipUtil.upZip(body.asInputStream(), "target/chromedriver/latest/chromedriver_win32");
		log.info("file: " + body.length());
		log.info("version Instalada");
	}
	
	private void killProcess(String process) throws IOException {
		Runtime rt = Runtime.getRuntime();
		  if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) 
		     rt.exec("taskkill /F /IM " + process);
		   else
		     rt.exec("kill -9 " + process);
	}

}
