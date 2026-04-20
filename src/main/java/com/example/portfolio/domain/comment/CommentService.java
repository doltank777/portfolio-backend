package com.example.portfolio.domain.comment;

import com.example.portfolio.domain.post.Post;
import com.example.portfolio.domain.post.PostRepository;
import com.example.portfolio.domain.user.User;
import com.example.portfolio.domain.user.UserRepository;
import com.example.portfolio.global.error.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 댓글 작성
    public Comment create(Long postId, String content, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new CustomException("사용자 없음", HttpStatus.NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new CustomException("게시글 없음", HttpStatus.NOT_FOUND));

        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }

    // 댓글 목록 조회
    public List<Comment> findByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long commentId, String username) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new CustomException("댓글 없음", HttpStatus.NOT_FOUND));

        // 본인 댓글만 삭제 가능
        if (!comment.getUser().getUsername().equals(username)) {
            throw new CustomException("삭제 권한 없음", HttpStatus.FORBIDDEN);
        }

        commentRepository.delete(comment);
    }
}