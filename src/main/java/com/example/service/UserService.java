package com.example.service;

import com.example.model.entities.Users;
import com.example.repository.UserRepo;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


@Component
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepo.getByUserName(username);
        if (users == null) {
            throw new UsernameNotFoundException("null");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        return new CustomUserDetails(users.getUsername(), users.getPassword(), authorities);
    }

}
