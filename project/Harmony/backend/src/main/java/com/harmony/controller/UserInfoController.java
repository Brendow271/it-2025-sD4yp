package com.harmony.controller;

import com.harmony.dto.UserInfoRequest;
import com.harmony.dto.UserInfoResponse;
import com.harmony.entity.UserInfo;
import com.harmony.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("info")
@CrossOrigin(origins = "*")
@Tag(name = "User Information", description = "Изменение информации о пользователе")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "Обновление информации о пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные обновлены успешно",
                    content = @Content(schema = @Schema(implementation = UserInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка обновления данных")
    })
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserInfoRequest request){
        try {
            UserInfo user = userInfoService.updateInfo(
                    request.getUserId(),
                    request.getAge(),
                    request.getGenres(),
                    request.getInstruments(),
                    request.getLocation(),
                    request.getAbout()
            );
            UserInfoResponse response = new UserInfoResponse(user);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обновлении информации: " + e.getMessage());
        }
    }

    @Operation(summary = "Получение информации о пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена успешно",
                    content = @Content(schema = @Schema(implementation = UserInfoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Профиль не найден")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> getInfo(@PathVariable Long userId) {
        try {
            UserInfo userInfo = userInfoService.getUserInfo(userId);
            UserInfoResponse response = new UserInfoResponse(userInfo);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Профиль не найден: " + e.getMessage());
        }
    }

    @Operation(summary = "Проверка полноты профиля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Проверка выполнена"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден")
    })
    @GetMapping("/complete/{userId}")
    public ResponseEntity<?> checkProfileComplete(@PathVariable Long userId) {
        try {
            boolean isComplete = userInfoService.isProfileComplete(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("isComplete", isComplete);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Профиль не найден: " + e.getMessage());
        }
    }
}
