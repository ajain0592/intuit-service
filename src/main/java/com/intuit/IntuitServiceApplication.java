package com.intuit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IntuitServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntuitServiceApplication.class, args);
	}

}
