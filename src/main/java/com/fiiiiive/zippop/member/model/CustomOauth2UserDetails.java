//package com.fiiiiive.zippop.member.model;
//
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Map;
//
//@Getter
//public class CustomOauth2UserDetails implements UserDetails, OAuth2User {
//
//    private final Customer customer;
//    private Map<String, Object> attributes;
//
//    public CustomOauth2UserDetails(Customer customer, Map<String, Object> attributes) {
//        this.customer = customer;
//        this.attributes = attributes;
//    }
//
//    public Long getIdx(){
//        return customer.getCustomerIdx();
//    }
//
//    @Override
//    public Map<String, Object> getAttributes() {
//        return attributes;
//    }
//
//    @Override
//    public String getName() {
//        return null;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> collection = new ArrayList<>();
//        collection.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return customer.getRole();
//            }
//        });
//        return collection;
//    }
//
//    @Override
//    public String getPassword() {
//        return customer.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return customer.getEmail();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return customer.getEnabled();
//    }
//
//}
package com.fiiiiive.zippop.member.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOauth2UserDetails implements UserDetails, OAuth2User {

    private final Customer customer;
    private Map<String, Object> attributes;

    public CustomOauth2UserDetails(Customer customer, Map<String, Object> attributes) {
        this.customer = customer;
        this.attributes = attributes;
    }

    public Long getIdx(){
        return customer.getCustomerIdx();
    }

    public String getRole() {
        return customer.getRole();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return customer.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return customer.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return customer.getEnabled();
    }
}
