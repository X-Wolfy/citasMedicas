package com.luis.pacientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.luis.pacientes", "com.luis.commons"})
public class PacientesMsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(PacientesMsvApplication.class, args);
	}

}
