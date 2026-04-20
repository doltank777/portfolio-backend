package com.example.portfolio.domain.post.dto;

import com.example.portfolio.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String username;
    private long likeCount;
    private long viewCount;
    private LocalDateTime createdAt;

    public static PostResponse from(Post post, long likeCount) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .username(post.getUser().getUsername())
                .likeCount(likeCount)
                .viewCount(post.getViewCount() == null ? 0L : post.getViewCount())
                .createdAt(post.getCreatedAt())
                .build();
    }
}