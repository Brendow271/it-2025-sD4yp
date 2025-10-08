package com.harmony.config;

import com.harmony.entity.UserAuth;
import com.harmony.service.ServiceUserAuth;
import com.harmony.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ServiceUserAuth userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{
        try{
            String token = jwtUtils.extractTokenFromHeader(request.getHeader("Authorization"));

            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null){

                if (jwtUtils.validateToken(token)){
                    String email = jwtUtils.getEmailFromToken(token);
                    UserAuth user = userService.getUserByEmail(email);

                    if (user.isActive()){
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        user,
                                        null,
                                        new ArrayList<>()
                                );

                         authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (Exception e){
            System.err.println("Ошибка при установке аутентификации: " + e.getMessage());
        }
        filterChain.doFilter(request, response);

    }

}