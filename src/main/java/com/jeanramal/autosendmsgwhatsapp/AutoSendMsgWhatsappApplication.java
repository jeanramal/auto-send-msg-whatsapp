package com.jeanramal.autosendmsgwhatsapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.jeanramal.autosendmsgwhatsapp.service.SendMsgWhatsAppService;

@EnableFeignClients("com.jeanramal.autosendmsgwhatsapp.payload")
@SpringBootApplication
public class AutoSendMsgWhatsappApplication implements CommandLineRunner {
	
	@Autowired
	private SendMsgWhatsAppService service;

	public static void main(String[] args) {
		SpringApplication.run(AutoSendMsgWhatsappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		service.sendMsg();
	}

}
