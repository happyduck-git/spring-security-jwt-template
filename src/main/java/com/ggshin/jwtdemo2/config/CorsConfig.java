package com.ggshin.jwtdemo2.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/*JWT 사용을 위한 CorsConfig 추가*/

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true); //서버가 응답할 때 json을 자바스크립트에서 처리할 수 있게 설정
        corsConfiguration.addAllowedOrigin("*"); //모든 ip에 응답 허용
        corsConfiguration.addAllowedHeader("*"); //모든 header에 응답 허용
        corsConfiguration.addAllowedMethod("*"); //모든 post, get, patch, delete 요청 허용
        source.registerCorsConfiguration("/api/**", corsConfiguration);

        return new CorsFilter(source);
    }

}
