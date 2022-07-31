package com.ggshin.jwtdemo2.config;

import com.ggshin.jwtdemo2.filter.JwtAuthenticationFilter;
import com.ggshin.jwtdemo2.filter.JwtAuthorizationFilter;
import com.ggshin.jwtdemo2.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter; //(4)CorsFilter 추가
    private final MemberRepository memberRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.addFilterBefore(new FirstFilter(), SecurityContextHolderFilter.class); //BasicAuthenticationFilter 전에 FirstFilter가 동작하도록 필터 추가!


        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();

        /*JWT 사용을 위한 기본 설정 추가*/
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //(1) session / cookie를 만들지 않고 STATELESS하게 진행하겠다는 뜻
                .and()
                .addFilter(corsFilter) //(5)
                .formLogin().disable() //(2) form login을 사용하지 않는다는 뜻
                .httpBasic().disable() //(3) http 방식을 사용하지 않겠다는 뜻
                .apply(new CustomDsl())//JWT 로그인을 위해 추가된 부분
                .and()
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

    /* JWT login을 위해 추가된 부분: 이런식으로 inner class를 만들어서 처리해주어야 함 */
    public class CustomDsl extends AbstractHttpConfigurer<CustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder
                    .addFilter(corsFilter)
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, memberRepository)); //권한 확인 필요한 요청시 동작될 필터 추가
        }
    }
}
