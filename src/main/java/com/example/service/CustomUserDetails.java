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
    private  String username;
    private  String password;
    private  Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String username,String password,Collection<? extends GrantedAuthority> authorities){
        this.username = username;
        this.password = password;
        this.authorities = authorities;
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
        return username;
    }

    @Override
    public String getUsername() {
        return password;
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
