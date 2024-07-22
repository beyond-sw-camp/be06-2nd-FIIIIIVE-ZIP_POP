package com.fiiiiive.zippop.config.filter;

import com.fiiiiive.zippop.member.model.CustomOauth2UserDetails;
import com.fiiiiive.zippop.utils.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOauth2UserDetails oAuth2Member = (CustomOauth2UserDetails) authentication.getPrincipal();
        Long idx = oAuth2Member.getIdx();
        String username = oAuth2Member.getUsername();
        String role = oAuth2Member.getCustomer().getRole();
        String token = jwtUtil.createToken(idx, username, role);
        log.info(idx + " " + role + " " + username);
        response.addHeader("Authorization", "Bearer " + token);
        PrintWriter out = response.getWriter();
        out.println("{\"username\":\"" + username + "\", \"isSuccess\": true, \"accessToken\": \"" + token + "\"}");
//        String token = jwtUtil.createToken(10L, email, "ROLE_CUSTOMER");
//        log.info(10L + " " + role + " " + email);
//        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
//        Cookie aToken = new Cookie("OAuthToken", token);
//        aToken.setHttpOnly(true);
//        aToken.setSecure(true);
//        aToken.setPath("/");
//        aToken.setMaxAge(60 * 60 * 1);
//        response.addCookie(aToken);
//        getRedirectStrategy().sendRedirect(request, response, "http://localhost/test.html");
    }
}
