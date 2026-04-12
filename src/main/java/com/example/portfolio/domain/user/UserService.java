package com.example.portfolio.domain.user;

import com.example.portfolio.domain.user.dto.SignupRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest request) {

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getUsername(),
                encodedPassword,
                "ROLE_USER"
        );

        userRepository.save(user);
    }

}