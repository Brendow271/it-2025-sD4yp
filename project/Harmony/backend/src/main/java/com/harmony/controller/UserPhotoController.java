package com.harmony.controller;

import com.harmony.dto.UserPhotoResponse;
import com.harmony.entity.UserAuth;
import com.harmony.entity.UserPhoto;
import com.harmony.service.UserPhotoService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("photo")
@CrossOrigin(origins = "*")
@Tag(name = "User Photos", description = "API для работы с фотографиями пользователей")
public class UserPhotoController {

    @Autowired
    private UserPhotoService userPhotoService;
    @Operation(summary = "Загрузка фотографии", description = "Загружает фотографию для текущего авторизованного пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Фотография успешно загружена",
                content = @Content(schema = @Schema(implementation = UserPhotoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации файла"),
        @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера при сохранении файла")
    })
    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            
            UserPhoto photo = userPhotoService.uploadPhoto(userId, file);
            UserPhotoResponse response = new UserPhotoResponse(photo);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка при сохранении файла: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Получение фотографий пользователя", description = "Возвращает список всех фотографий указанного пользователя")
     
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список фотографий успешно получен"),
        @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserPhotos(@PathVariable Long userId) {
        try {
            List<UserPhoto> photos = userPhotoService.getUserPhotos(userId);
            List<UserPhotoResponse> responses = photos.stream()
                    .map(UserPhotoResponse::new)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка при получении фотографий: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Удаление фотографии", description = "Удаляет фотографию по её ID. Доступно только владельцу фотографии")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Фотография успешно удалена"),
        @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        @ApiResponse(responseCode = "403", description = "Нет доступа к удалению этой фотографии"),
        @ApiResponse(responseCode = "404", description = "Фотография не найдена")
    })
    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deletePhoto(
            @PathVariable Long imageId,
            Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            
            userPhotoService.deletePhoto(imageId, userId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Фотография успешно удалена");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (SecurityException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка при удалении файла: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new SecurityException("Пользователь не авторизован");
        }
        
        UserAuth user = (UserAuth) authentication.getPrincipal();
        return user.getUserId();
    }
}

