package com.anderson.pmanager.infra.security;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    @Autowired
    SecurityFilter securityFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/user/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/user/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/projects").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/projects").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/projects/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/projects/{id}").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return  authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
