package com.example.portfolio.controller;

import com.example.portfolio.domain.user.User;
import com.example.portfolio.domain.user.UserRepository;
import com.example.portfolio.domain.user.UserService;
import com.example.portfolio.domain.user.dto.LoginRequest;
import com.example.portfolio.domain.user.dto.SignupRequest;
import com.example.portfolio.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {

        userService.signup(request);

        return "회원가입 완료";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호 틀림");
        }

        return jwtTokenProvider.createToken(user.getUsername());
    }
}