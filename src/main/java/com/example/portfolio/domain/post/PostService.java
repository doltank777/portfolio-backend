package com.example.portfolio.domain.post;

import com.example.portfolio.domain.like.LikeRepository;
import com.example.portfolio.domain.post.dto.PostCreateRequest;
import com.example.portfolio.domain.post.dto.PostResponse;
import com.example.portfolio.domain.user.User;
import com.example.portfolio.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.portfolio.global.error.exception.CustomException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    // 게시글 생성
    public PostResponse create(PostCreateRequest request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("사용자 없음", HttpStatus.NOT_FOUND));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        Post saved = postRepository.save(post);

        return PostResponse.from(saved, 0);
    }

    // 전체 조회 (페이징 + DTO + 좋아요)
    public Page<PostResponse> findAll(Pageable pageable) {

        return postRepository.findAll(pageable)
                .map(post -> {
                    long likeCount = likeRepository.countByPostId(post.getId());
                    return PostResponse.from(post, likeCount);
                });
    }

    // 단건 조회
    public PostResponse findById(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException("게시글 없음", HttpStatus.NOT_FOUND));

        long likeCount = likeRepository.countByPostId(post.getId());

        return PostResponse.from(post, likeCount);
    }

    // 수정
    public PostResponse update(Long id, PostCreateRequest request, String username) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException("게시글 없음", HttpStatus.NOT_FOUND));

        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("수정 권한 없음");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        Post updated = postRepository.save(post);

        long likeCount = likeRepository.countByPostId(updated.getId());

        return PostResponse.from(updated, likeCount);
    }

    // 삭제
    public void delete(Long id, String username) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException("게시글 없음", HttpStatus.NOT_FOUND));

        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("삭제 권한 없음");
        }

        postRepository.delete(post);
    }
}