package com.example.service;

import com.example.model.entities.Roles;
import com.example.model.entities.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
public class CustomUserDetails implements UserDetails {

    Users users;
    public CustomUserDetails(Users users) {
        this.users = users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Roles> roles = users.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Roles rolesList : roles) {
            authorities.add(new SimpleGrantedAuthority(rolesList.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return users.getUsername();
    }

    @Override
    public String getUsername() {
        return users.getPassword();
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
}
