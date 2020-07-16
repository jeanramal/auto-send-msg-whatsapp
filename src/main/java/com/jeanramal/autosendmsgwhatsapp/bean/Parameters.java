package com.jeanramal.autosendmsgwhatsapp.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("parameters")
public class Parameters {
	
	private String pathDriver;
	
	private String numbers;
	
	private String message;

}
