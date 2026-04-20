package com.example.portfolio.controller;

import com.example.portfolio.domain.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    // 좋아요 토글
    @PostMapping("/{postId}")
    public String like(@PathVariable Long postId,
                       Authentication authentication) {

        String username = authentication.getName();

        return likeService.like(postId, username);
    }

    // 좋아요 개수 조회
    @GetMapping("/{postId}")
    public Long count(@PathVariable Long postId) {
        return likeService.count(postId);
    }
}