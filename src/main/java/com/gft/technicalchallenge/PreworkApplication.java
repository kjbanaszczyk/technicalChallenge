package com.gft.technicalchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ScopedProxyMode;

import java.io.IOException;

@SpringBootApplication
public class PreworkApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(PreworkApplication.class, args);
	}

}