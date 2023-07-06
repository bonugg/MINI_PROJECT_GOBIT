package com.gobit.minipj_gobit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();

        auths.add(
                new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return user.getUSERPOSITION();
                    }
                }
        );

        return auths;
    }

    @Override
    public String getPassword() {
        return this.user.getUSER_PWD();
    }

    @Override
    public String getUsername() {
        String usereno = Long.toString(this.user.getUSERENO());
        return usereno;
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
        return true;
    }

    public User getUser() {
        return user;
    }
}
