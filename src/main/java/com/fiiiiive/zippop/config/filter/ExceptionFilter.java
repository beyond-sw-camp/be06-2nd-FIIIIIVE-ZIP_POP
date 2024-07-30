package com.fiiiiive.zippop.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ExceptionFilter extends OncePerRequestFilter {
    private void setErrorResponse(HttpServletResponse response, BaseResponseMessage baseResponseMessage, String errorMessage){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(response.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        try{
            BaseResponse baseResponse = new BaseResponse(baseResponseMessage, errorMessage);
            response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e){
            log.error("Bearer 토큰이 만료되었습니다.");
            setErrorResponse(response, BaseResponseMessage.MEMBER_ACCESS_TOKEN_EXPIRED, e.getMessage());
        } catch (JwtException | IllegalArgumentException e){
            log.error("Bearer 토큰이 유효하지 않습니다.");
            setErrorResponse(response, BaseResponseMessage.MEMBER_ACCESS_TOKEN_INVALID, e.getMessage());
        }
    }
}
