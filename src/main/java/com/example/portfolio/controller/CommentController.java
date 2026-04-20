package com.example.portfolio.controller;

import com.example.portfolio.domain.comment.Comment;
import com.example.portfolio.domain.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/{postId}")
    public Comment create(@PathVariable Long postId,
                          @RequestBody Comment request,
                          Authentication authentication) {

        String username = authentication.getName();

        return commentService.create(postId, request.getContent(), username);
    }

    // 특정 게시글 댓글 조회
    @GetMapping("/{postId}")
    public List<Comment> list(@PathVariable Long postId) {
        return commentService.findByPost(postId);
    }

    // 특정 게시글 댓글 삭제
    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long commentId,
                       Authentication authentication) {

        String username = authentication.getName();

        commentService.delete(commentId, username);
    }
}