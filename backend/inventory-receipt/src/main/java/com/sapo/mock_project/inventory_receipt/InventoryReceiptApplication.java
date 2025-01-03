package com.sapo.mock_project.inventory_receipt;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class InventoryReceiptApplication {
	@Value("${time.zone}")
	private String timeZone;

	public static void main(String[] args) {
		SpringApplication.run(InventoryReceiptApplication.class, args);
	}

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
	}
}
