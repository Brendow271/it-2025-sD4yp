package com.harmony.controller;

import com.harmony.dto.AuthResponse;
import com.harmony.dto.LoginRequest;
import com.harmony.dto.RegisterRequest;
import com.harmony.entity.UserAuth;
import com.harmony.service.UserAuthService;
import com.harmony.utils.JwtUtils;
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
@RequestMapping("auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "API для аутентификации пользователей")
public class AuthController {

    @Autowired
    private UserAuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "Регистрация нового пользователя", description = "Создает нового пользователя в системе")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
                content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации или пользователь уже существует")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        try {
            UserAuth user = authService.registerUser(
                request.getName(),
                request.getEmail(),
                request.getPassword()
            );

            AuthResponse response = new AuthResponse(user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e){

            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @Operation(summary = "Вход в систему", description = "Аутентификация пользователя по email и паролю")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешная аутентификация",
                content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try {
            AuthResponse response = authService.loginUser(
                request.getEmail(),
                request.getPassword()
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e){
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @Operation(summary = "Валидация токена", description = "Проверяет валидность JWT токена")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Токен валиден"),
        @ApiResponse(responseCode = "401", description = "Токен невалиден или отсутствует")
    })
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader){
        try {
            String token = jwtUtils.extractTokenFromHeader(authHeader);

            if (token == null){
                Map<String,String> error = new HashMap<>();
                error.put("error", "Токен не найден");
                return ResponseEntity.badRequest().body(error);
            }

            UserAuth user = authService.validateTokenAndGetUser(token);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e){
            Map<String,String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @Operation(summary = "Обновление токена", description = "Обновляет JWT токен пользователя")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Токен успешно обновлен"),
        @ApiResponse(responseCode = "401", description = "Токен невалиден или отсутствует")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader){
        try {
            String token = jwtUtils.extractTokenFromHeader(authHeader);

            if (token == null){
                Map<String,String> error = new HashMap<>();
                error.put("error", "Токен не найден");
                return ResponseEntity.badRequest().body(error);
            }

            String newToken = authService.refreshToken(token);

            Map<String,String> response = new HashMap<>();
            response.put("token", newToken);
            response.put("type", "Bearer");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e){
            Map<String,String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email){
        try{
            boolean exists = authService.isEmailExists(email);

            Map<String,Object> response = new HashMap<>();
            response.put("email", email);
            response.put("exists", exists);

            return ResponseEntity.ok(response);
        } catch (Exception e){
            Map<String,String> error = new HashMap<>();
            error.put("error", "Ошибка при проверке email");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
