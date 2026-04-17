package com.example.portfolio.config;

import com.example.portfolio.domain.user.UserRepository;
import com.example.portfolio.jwt.JwtAuthenticationFilter;
import com.example.portfolio.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 🔥 CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 🔥 기본 로그인 폼 비활성화
                .formLogin(AbstractHttpConfigurer::disable)

                // 🔥 Basic Auth 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                // 🔥 JWT 방식이므로 세션 사용 안 함
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 🔥 권한 설정
                .authorizeHttpRequests(auth -> auth

                        // 인증 없이 허용
                        .requestMatchers("/api/auth/**").permitAll()

                        // 프론트 정적 파일 허용 (React/Vue 빌드 시 대비)
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/static/**",
                                "/assets/**",
                                "/favicon.ico"
                        ).permitAll()

                        // H2 콘솔 허용
                        .requestMatchers("/h2-console/**").permitAll()

                        // 게시글 조회 공개
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()

                        // 댓글 조회 공개
                        .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()

                        // 좋아요 개수 조회 공개
                        .requestMatchers(HttpMethod.GET, "/api/likes/**").permitAll()

                        // OPTIONS 요청 허용 (CORS preflight)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 나머지는 인증 필요
                        .anyRequest().authenticated()
                )

                // 🔥 H2 콘솔 iframe 허용
                .headers(headers ->
                        headers.frameOptions(frame -> frame.disable())
                )

                // 🔥 JWT 필터 등록
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, userRepository),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}