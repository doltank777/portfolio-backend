package com.example.portfolio.domain.like;

import com.example.portfolio.domain.post.Post;
import com.example.portfolio.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 유저
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 🔥 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}