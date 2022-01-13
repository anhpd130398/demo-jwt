package com.example.controller;

import com.example.jwt.JwtUtils;
import com.example.model.entities.Roles;
import com.example.model.entities.Users;
import com.example.model.response.AuthRequest;
import com.example.model.response.AuthResponse;
import com.example.model.response.BaseResponse;
import com.example.repository.RoleRepo;
import com.example.repository.UserRepo;
import com.example.service.CustomUserDetails;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class Controller {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register/save-user")
    public BaseResponse saveUser(@RequestBody AuthRequest authRequest) {
        List<Users> usersList = userRepo.findByUserName(authRequest.getUsername());
        if (usersList.size() > 0) {
            return new BaseResponse("Invalid User Name");
        } else {
            Users users = new Users();
            users.setUsername(authRequest.getUsername());
            users.setPassword(passwordEncoder.encode(authRequest.getPassword()));
            Set<Roles> rolesSet = new HashSet<>();
            rolesSet.add(roleRepo.getByName("ROLE_USER"));
            users.setRoles(rolesSet);
            users = userRepo.save(users);
            return new BaseResponse(users);
        }
    }

    @PostMapping("/user/login")
    public AuthResponse loginUser(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken((CustomUserDetails) authentication.getPrincipal());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        return authResponse;
    }


    @GetMapping("/user")
    public List<Users> getList() {
        return userRepo.findAll();
    }

}
