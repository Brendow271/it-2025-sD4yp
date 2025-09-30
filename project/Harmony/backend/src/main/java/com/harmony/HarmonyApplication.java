package com.harmony;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Главный класс Spring Boot приложения Harmony
 * 
 * @author Harmony Team
 * @version 1.0
 */
@SpringBootApplication
@EnableCaching
public class HarmonyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HarmonyApplication.class, args);
    }
}
