package com.example.portfolio.domain.post;

import com.example.portfolio.domain.comment.CommentRepository;
import com.example.portfolio.domain.like.LikeRepository;
import com.example.portfolio.domain.post.dto.PostCreateRequest;
import com.example.portfolio.domain.post.dto.PostResponse;
import com.example.portfolio.domain.user.User;
import com.example.portfolio.domain.user.UserRepository;
import com.example.portfolio.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    // 게시글 생성
    @Transactional
    public PostResponse create(PostCreateRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("사용자 없음", HttpStatus.NOT_FOUND));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .viewCount(0L)
                .build();

        Post saved = postRepository.save(post);
        return PostResponse.from(saved, 0);
    }

    // 전체 조회 (페이징 + DTO + 좋아요 + 조회수 포함)
    @Transactional(readOnly = true)
    public Page<PostResponse> findAll(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(post -> {
                    long likeCount = likeRepository.countByPostId(post.getId());
                    return PostResponse.from(post, likeCount);
                });
    }

    // 단건 조회 (조회수 증가)
    @Transactional
    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException("게시글 없음", HttpStatus.NOT_FOUND));

        post.increaseViewCount();

        long likeCount = likeRepository.countByPostId(post.getId());
        return PostResponse.from(post, likeCount);
    }

    // 수정
    @Transactional
    public PostResponse update(Long id, PostCreateRequest request, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException("게시글 없음", HttpStatus.NOT_FOUND));

        if (!post.getUser().getUsername().equals(username)) {
            throw new CustomException("수정 권한 없음", HttpStatus.FORBIDDEN);
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        long likeCount = likeRepository.countByPostId(post.getId());
        return PostResponse.from(post, likeCount);
    }

    // 삭제
    @Transactional
    public void delete(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException("게시글 없음", HttpStatus.NOT_FOUND));

        if (!post.getUser().getUsername().equals(username)) {
            throw new CustomException("삭제 권한 없음", HttpStatus.FORBIDDEN);
        }

        // 자식 데이터 먼저 삭제
        likeRepository.deleteByPostId(id);
        commentRepository.deleteByPostId(id);

        // 마지막에 부모 데이터 삭제
        postRepository.delete(post);
    }
}