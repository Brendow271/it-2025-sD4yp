package com.harmony.controller;

import com.harmony.dto.AuthResponse;
import com.harmony.dto.LoginRequest;
import com.harmony.dto.RegisterRequest;
import com.harmony.entity.UserAuth;
import com.harmony.service.ServiceUserAuth;
import com.harmony.utils.JwtUtils;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private ServiceUserAuth authService;

    @Autowired
    private JwtUtils jwtUtils;

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
