package com.example.portfolio.config;

import com.example.portfolio.domain.user.UserRepository;
import com.example.portfolio.jwt.JwtAuthenticationFilter;
import com.example.portfolio.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod; // 🔥 추가
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // 🔥 추가
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // 🔥 추가

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 🔥 최신 방식 disable
                .csrf(AbstractHttpConfigurer::disable)

                // 🔥 기본 로그인 폼 비활성화
                .formLogin(AbstractHttpConfigurer::disable)

                // 🔥 Basic Auth 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                // 🔥 세션 사용 안 함 (JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 🔥 권한 설정
                .authorizeHttpRequests(auth -> auth

                        // 인증 없이 허용
                        .requestMatchers("/api/auth/**").permitAll()

                        // H2 콘솔 사용할 경우 허용 (현재 MySQL이면 유지해도 무방)
                        .requestMatchers("/h2-console/**").permitAll() // 🔥 추가

                        // 게시글 조회는 공개
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll() // 🔥 추가

                        // 댓글 조회 공개
                        .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll() // 🔥 추가

                        // 좋아요 개수 조회 공개
                        .requestMatchers(HttpMethod.GET, "/api/likes/**").permitAll() // 🔥 추가

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