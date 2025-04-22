package com.matzzangteam.matzzang.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzzangteam.matzzang.dto.exception.ClientErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        ClientErrorResponse errorResponse = new ClientErrorResponse(
                HttpStatus.FORBIDDEN,
                "No Authority"
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
