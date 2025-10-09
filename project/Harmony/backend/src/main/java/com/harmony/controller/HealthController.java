package com.harmony.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для проверки состояния приложения
 * 
 * @author Harmony Team
 * @version 1.0
 */
@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "API для проверки состояния приложения")
public class HealthController {

    @Operation(summary = "Проверка состояния", description = "Возвращает статус работы приложения")
    @ApiResponse(responseCode = "200", description = "Приложение работает нормально")
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "Harmony Backend");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }
}
