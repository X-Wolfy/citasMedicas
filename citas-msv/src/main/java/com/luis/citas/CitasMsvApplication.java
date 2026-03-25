package com.luis.citas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.luis.citas", "com.luis.commons"})
public class CitasMsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(CitasMsvApplication.class, args);
	}

}
