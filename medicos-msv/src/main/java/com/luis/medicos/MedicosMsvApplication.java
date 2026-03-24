package com.luis.medicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = {"com.luis.medicos", "com.luis.commons"})
public class MedicosMsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicosMsvApplication.class, args);
	}

}
