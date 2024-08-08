package com.fiiiiive.zippop.member.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Builder
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    @Getter
    private final Long idx;
    private final String email;
    private final String name;
    private final String password;
    private final String role;
    private final Boolean enabled;

    public CustomUserDetails(Long idx, String email, String role) {
        this.idx = idx;
        this.email = email;
        this.role = role;
        this.name = null;
        this.password = null;
        this.enabled = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority(){
            @Override
            public String getAuthority() {
                return role;
            }
        });
        return collection;
    }
    public String getName(){ return name; }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return enabled;
    }
}
