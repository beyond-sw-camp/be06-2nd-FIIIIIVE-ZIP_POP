package com.fiiiiive.zippop.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiiiiive.zippop.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.request.PostLoginReq;


import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        PostLoginReq dto;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            dto = objectMapper.readValue(messageBody, PostLoginReq.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword(), null);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 그림에서 10번
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails member = (CustomUserDetails)authResult.getPrincipal();
        Long idx = member.getIdx();
        String email = member.getEmail();
        String name = member.getName();
        String role = member.getRole();
        log.info(idx + " " + role + " " + email);
        String aTokenString = jwtUtil.createToken(idx, email, role);
        Cookie aToken = new Cookie("ATOKEN", aTokenString);
        aToken.setHttpOnly(true);
        aToken.setSecure(true);
        aToken.setPath("/");
        aToken.setMaxAge(60 * 60 * 1);
        response.addCookie(aToken);
        String combinedValue = name + "|" + role;
        log.info(combinedValue);
        Cookie uToken = new Cookie("UTOKEN", combinedValue);
        uToken.setHttpOnly(false);
        uToken.setSecure(false);
        uToken.setPath("/");
        uToken.setMaxAge(60 * 60 * 1); // 여기도 1시간으로 설정
        response.addCookie(uToken);
    }
}
