package com.fiiiiive.zippop.config.filter;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws  ServletException, IOException, InternalAuthenticationServiceException {
        try{
            String authorization = request.getHeader("Authorization");
            if(authorization == null || !authorization.startsWith("Bearer")){
                log.info("Bearer 토큰 없음");
                try {
                    filterChain.doFilter(request, response);
                    return;
                } catch (NullPointerException e){
                    throw new NullPointerException("ddd");
                }
            }
            String token = authorization.split(" ")[1];
            if(jwtUtil.isExpired(token)){
                filterChain.doFilter(request, response);
                return;
            }
            Long idx = jwtUtil.getIdx(token);
            String email = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);
            log.info(idx + " " + email + " " + role);
            CustomUserDetails customUserDetails = new CustomUserDetails(idx, email, role);
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        }catch (ServletException e){
            throw new ServletException(e);
        }
    }
}
