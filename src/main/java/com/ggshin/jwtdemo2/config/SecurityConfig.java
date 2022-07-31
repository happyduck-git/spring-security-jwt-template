package com.ggshin.jwtdemo2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter; //(4)CorsFilter 추가

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();

        /*JWT 사용을 위한 기본 설정 추가*/
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //(1) session / cookie를 만들지 않고 STATELESS하게 진행하겠다는 뜻
                .and()
                .addFilter(corsFilter) //(5)
                .formLogin().disable() //(2) form login을 사용하지 않는다는 뜻
                .httpBasic().disable() //(3) http 방식을 사용하지 않겠다는 뜻
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();

        return httpSecurity.build();
    }
}
