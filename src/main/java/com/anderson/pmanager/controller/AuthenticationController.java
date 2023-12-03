package com.anderson.pmanager.controller;

import com.anderson.pmanager.dtos.AuthenticationDto;
import com.anderson.pmanager.dtos.LoginResponseDTO;
import com.anderson.pmanager.dtos.RegisterDto;
import com.anderson.pmanager.infra.security.TokenService;
import com.anderson.pmanager.model.UserModel;
import com.anderson.pmanager.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDto data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token =tokenService.generateToken((UserModel) auth.getPrincipal());

        System.out.println(auth);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }



}
