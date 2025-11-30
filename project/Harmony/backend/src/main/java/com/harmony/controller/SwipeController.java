package com.harmony.controller;

import com.harmony.dto.SwipeRequest;
import com.harmony.dto.SwipeResponse;
import com.harmony.service.SwipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/swipes")
@CrossOrigin(origins = "*")
@Tag(name = "Swipes", description = "API для управления свайпами")
public class SwipeController {

    @Autowired
    private SwipeService swipeService;

    @Operation(summary = "Создать свайп", description = "Создает новый свайп между двумя пользователями")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Свайп успешно создан",
                    content = @Content(schema = @Schema(implementation = SwipeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка при создании свайпа")
    })
    @PostMapping("/create")
    public ResponseEntity<?> createSwipe(@RequestBody SwipeRequest request){
        try {
            SwipeResponse response = swipeService.createSwipe(
                    request.getUserId1(),
                    request.getUserId2(),
                    request.getDecision()
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @Operation(summary = "Получить мэтчи пользователя", description = "Возвращает список всех мэтчей для указанного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список мэтчей успешно получен"),
            @ApiResponse(responseCode = "400", description = "Ошибка при получении мэтчей")
    })
    @GetMapping("/matches/{userId}")
    public ResponseEntity<?> getMatches(@PathVariable Long userId){
        try {
            List<SwipeResponse> matches = swipeService.getMatches(userId);
            return ResponseEntity.ok(matches);
        } catch (Exception e){
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка при получении мэтчей: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Получить историю свайпов", description = "Возращает всю историю свайпов для указанного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "История свайпов успешно получена"),
            @ApiResponse(responseCode = "400", description = "Ошибка при получении истории")
    })
    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getSwipeHistrory(@PathVariable Long userId){
        try {
            List<SwipeResponse> history = swipeService.getSwipeHistory(userId);
            return ResponseEntity.ok(history);
        } catch (Exception e){
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка при получении истории: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Проверить, был ли свайп", description = "Проверяет, свайпнул ли пользователь другого пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Проверка выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка при проверке")
    })
    @GetMapping("/check/{userId1}/{userId2}")
    public ResponseEntity<?> hasSwiped(@PathVariable Long userId1, @PathVariable Long userId2) {
        try {
            boolean hasSwiped = swipeService.hasSwiped(userId1, userId2);
            Map<String, Object> response = new HashMap<>();
            response.put("hasSwiped", hasSwiped);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка при проверке: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}













