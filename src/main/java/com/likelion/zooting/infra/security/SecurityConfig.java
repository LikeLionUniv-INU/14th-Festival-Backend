package com.likelion.zooting.infra.security;

import com.likelion.zooting.global.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 기능
                .formLogin(AbstractHttpConfigurer::disable) // 로그인 ui
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/actuator/health",
                                "/actuator/prometheus",
                                "/api/onboarding/instagram",
                                "/api/internal/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        /*
         * 현재는 프론트 연동 테스트 단계이므로 모든 출처 허용
         *
         * - 쿠키/세션/인증 정보를 포함한 요청을 허용하려면 "*" 대신 정확한 프론트 주소를 적어야 함
         */
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://14th-festival-frontend.vercel.app",
                "https://zooting.site",
                "https://www.zooting.site"
        ));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));

        /*
         * 현재는 쿠키/세션 인증을 사용하지 않으므로 false
         *
         * 나중에 쿠키 기반 인증 또는 credentials 포함 요청을 사용할 경우:
         * - setAllowedOrigins(List.of("http://localhost:3000"))처럼 명확한 Origin 지정
         * - setAllowCredentials(true)로 변경
         */
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 모든 경로에 위 CORS 정책 적용
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}