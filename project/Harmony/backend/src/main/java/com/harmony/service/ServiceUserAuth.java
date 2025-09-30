package com.harmony.service;

import com.harmony.entity.UserAuth;
import com.harmony.repository.UserAuthRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ServiceUserAuth {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    

    private void validateRegistrationData(String name, String email, String password){

        if (name == null || name.trim().isEmpty()){
            throw new RuntimeException("Имя не может быть пустым");
        }

        if (name.length() < 2 || name.length() > 35){
            throw new RuntimeException("Имя должно содержать от 2 до 35 символов");
        }

        validateEmail(email);
        validatePassword(password);
    }

    private void validateEmail(String email){

        if (email == null || email.trim().isEmpty()){
            throw new RuntimeException("Email не может быть пустым");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            throw new RuntimeException("Некорректный формат email");
        }

        if (email.length() > 255) {
            throw new RuntimeException("Email слишком длинный");
        }
    }

    private void validatePassword(String password){
        if (password == null || password.trim().isEmpty()){
            throw new RuntimeException("Email не может быть пустым");
        }

        if (password.length() < 8 || password.length() > 35){
            throw new RuntimeException("Пароль должен содержать от 8 до 35 символов");
        }
    }
}
