package com.matzzangteam.matzzang.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzzangteam.matzzang.dto.exception.ClientErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        ClientErrorResponse errorResponse = new ClientErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Need Authentication"
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
