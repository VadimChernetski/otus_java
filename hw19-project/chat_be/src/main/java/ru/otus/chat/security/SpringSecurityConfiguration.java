package ru.otus.chat.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;


@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfiguration {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
      .cors().configurationSource(request -> {
        var config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setMaxAge(3600L);
        return config;
      }).and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
      .and()
      .csrf().disable()
      .httpBasic().disable()
      .authorizeHttpRequests(requests -> requests
        .requestMatchers("/register").anonymous()
        .anyRequest().authenticated())
      .formLogin(login -> login
        .loginProcessingUrl("/login")
        .successHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK)))
      .logout(logout -> logout
        .logoutUrl("/logout")
        .deleteCookies("JSESSIONID")
        .logoutSuccessHandler(((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK))))
      .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
