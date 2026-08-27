package com.example.InkHub_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.InkHub_backend.mapper")
public class InkHubBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(InkHubBackendApplication.class, args);
	}

}
