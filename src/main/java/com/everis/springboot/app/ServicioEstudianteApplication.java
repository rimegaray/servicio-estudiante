package com.everis.springboot.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ServicioEstudianteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioEstudianteApplication.class, args);
	}

}
