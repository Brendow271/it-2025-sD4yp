package com.harmony.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Простой инициализатор для загрузки переменных из .env файла
 * 
 * Этот класс автоматически загружает переменные из .env файла
 * в системные переменные окружения при запуске Spring Boot приложения.
 */
public class DotenvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            String projectRoot = System.getProperty("user.dir");
            String envPath = projectRoot + "/../.env"; 
            
            Dotenv dotenv = Dotenv.configure()
                    .directory("../")
                    .filename(".env")
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();
            
            System.out.println("Ищем .env файл по пути: " + envPath);
            
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                
                if (System.getenv(key) == null) {
                    System.setProperty(key, value);
                    System.out.println("Загружена переменная: " + key + " = " + 
                        (key.toLowerCase().contains("password") || key.toLowerCase().contains("secret") ? "***" : value));
                } else {
                    System.out.println("Переменная " + key + " уже установлена в системе, пропускаем");
                }
            });
            
            System.out.println("Успешно загружено " + dotenv.entries().size() + " переменных из .env файла");
            
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке .env файла: " + e.getMessage());
            System.err.println("Продолжаем работу с системными переменными окружения");
        }
    }
}
