package com.example.portfolio.domain.comment;

import com.example.portfolio.domain.post.Post;
import com.example.portfolio.domain.post.PostRepository;
import com.example.portfolio.domain.user.User;
import com.example.portfolio.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.portfolio.global.error.exception.CustomException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Comment create(Long postId, String content, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("사용자 없음", HttpStatus.NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시글 없음", HttpStatus.NOT_FOUND));

        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }

    public List<Comment> findByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}