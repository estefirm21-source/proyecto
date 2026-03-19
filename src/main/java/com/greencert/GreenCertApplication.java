package com.greencert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GreenCertApplication {
    public static void main(String[] args) {
        SpringApplication.run(GreenCertApplication.class, args);
        System.out.println("=== GreenCert Web Application Started ===");
        System.out.println("Abra su navegador en: http://localhost:8080");
    }
}
