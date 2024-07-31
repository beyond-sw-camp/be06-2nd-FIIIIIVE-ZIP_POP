package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.member.model.CustomOauth2UserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.member.model.KakaoUserDetails;
import com.fiiiiive.zippop.member.model.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2Service extends DefaultOAuth2UserService {
    private final CustomerRepository customerRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}",oAuth2User.getAttributes());
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = null;
        if(provider.equals("kakao")){
            log.info("카카오 로그인");
            oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
        }
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();
        String role = "ROLE_CUSTOMER";
        log.info(email + " " + name + " " + role);
        Optional<Customer> result = customerRepository.findByCustomerEmail(email);
        Customer customer = null;
        if(result.isEmpty()){
            customer = Customer.builder()
                    .role(role)
                    .name(name)
                    .email(email)
                    .point(3000)
                    .enabled(true)
                    .build();
            customerRepository.save(customer);
        } else {
            customer = result.get();
        }
        return new CustomOauth2UserDetails(customer, oAuth2User.getAttributes());
    }
}