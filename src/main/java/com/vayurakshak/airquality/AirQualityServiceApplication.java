package com.vayurakshak.airquality;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AirQualityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirQualityServiceApplication.class, args);
	}

}
