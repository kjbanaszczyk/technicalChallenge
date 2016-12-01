package com.gft.technicalchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class PreworkApplication {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext ctx = SpringApplication.run(PreworkApplication.class, args);
    }
}