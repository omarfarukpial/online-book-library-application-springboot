package com.pial.onlinebooklibraryapplication.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pial.onlinebooklibraryapplication.SpringApplicationContext;
import com.pial.onlinebooklibraryapplication.constants.AppConstants;
import com.pial.onlinebooklibraryapplication.dto.UserDto;
import com.pial.onlinebooklibraryapplication.dto.UserLoginRequestModel;
import com.pial.onlinebooklibraryapplication.service.UserService;
import com.pial.onlinebooklibraryapplication.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            UserLoginRequestModel credential = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestModel.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credential.getEmail(),credential.getPassword())
            );
        } catch (IOException e) {
            log.info("Exception occurred at attemptAuthentication method: {}",e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String user = ((User)authResult.getPrincipal()).getUsername();
        String accessToken = JWTUtils.generateToken(user);
        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImplementation");
        UserDto userDto = null;
        try {
            userDto = userService.getUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("userId", userDto.getUserId());
        responseBody.put("email", userDto.getEmail());
        responseBody.put(AppConstants.HEADER_STRING, AppConstants.TOKEN_PREFIX + accessToken);
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyJson = objectMapper.writeValueAsString(responseBody);
        response.setContentType("application/json");
        response.getWriter().write(responseBodyJson);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Authentication failed");
        errorResponse.put("message", "Invalid email or password");
        ObjectMapper objectMapper = new ObjectMapper();
        String errorResponseJson = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(errorResponseJson);
    }

}
