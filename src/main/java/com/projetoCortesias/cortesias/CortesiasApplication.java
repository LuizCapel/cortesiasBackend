package com.projetoCortesias.cortesias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CortesiasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CortesiasApplication.class, args);
	}

}
