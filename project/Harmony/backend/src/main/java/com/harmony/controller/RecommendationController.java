package com.harmony.controller;

import com.harmony.service.RecommendationService;
import com.harmony.service.UserInfoService;
import com.harmony.dto.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/recommendations")
@CrossOrigin(origins = "*")
@Tag(name = "Recommendations", description = "API для получения рекомендаций пользователей")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "Получить следующую рекомендацию", description = "Возвращает следующую карточку пользователя из очереди рекомендаций")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Рекомендация успешно получена",
                    content = @Content(schema = @Schema(implementation = UserInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка при получении рекомендации")
    })
    @GetMapping("/next/{userId}")
    public ResponseEntity<?> getNextRecommendation(@PathVariable Long userId) {
        try {
            Long recommendedUserId = recommendationService.getNextRecommendation(userId);

            if (recommendedUserId == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Нет доступных рекомендаций");
                return ResponseEntity.ok(response);
            }

            var userInfo = userInfoService.getUserInfo(recommendedUserId);
            return ResponseEntity.ok(new UserInfoResponse(userInfo));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}