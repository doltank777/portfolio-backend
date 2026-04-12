package com.example.portfolio.controller;

import com.example.portfolio.domain.post.PostService;
import com.example.portfolio.domain.post.dto.PostCreateRequest;
import com.example.portfolio.domain.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping
    public PostResponse create(@Valid @RequestBody PostCreateRequest request,
                               Authentication authentication) {

        String username = authentication.getName();

        return postService.create(request, username);
    }

    // 전체 조회 (페이징 + 정렬)
    @GetMapping
    public Page<PostResponse> list(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return postService.findAll(pageable);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public PostResponse get(@PathVariable Long id) {
        return postService.findById(id);
    }

    // 수정
    @PutMapping("/{id}")
    public PostResponse update(@PathVariable Long id,
                               @Valid @RequestBody PostCreateRequest request,
                               Authentication authentication) {

        String username = authentication.getName();

        return postService.update(id, request, username);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
                       Authentication authentication) {

        String username = authentication.getName();

        postService.delete(id, username);
    }
}