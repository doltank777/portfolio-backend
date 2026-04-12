package com.example.portfolio.domain.like;

import com.example.portfolio.domain.post.Post;
import com.example.portfolio.domain.post.PostRepository;
import com.example.portfolio.domain.user.User;
import com.example.portfolio.domain.user.UserRepository;
import com.example.portfolio.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final StringRedisTemplate redisTemplate; // 🔥 추가

    private static final String LIKE_KEY = "post:like:";

    public String like(Long postId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("사용자 없음", HttpStatus.NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시글 없음", HttpStatus.NOT_FOUND));

        boolean exists = likeRepository
                .findByUserIdAndPostId(user.getId(), postId)
                .isPresent();

        if (exists) {
            // 👍 좋아요 취소
            likeRepository.deleteByUserIdAndPostId(user.getId(), postId);

            // 🔥 Redis 감소
            redisTemplate.opsForValue().decrement(LIKE_KEY + postId);

            return "좋아요 취소";
        } else {
            // 👍 좋아요 추가
            Like like = Like.builder()
                    .user(user)
                    .post(post)
                    .build();

            likeRepository.save(like);

            // 🔥 Redis 증가
            redisTemplate.opsForValue().increment(LIKE_KEY + postId);

            return "좋아요 추가";
        }
    }

    public long count(Long postId) {

        String value = redisTemplate.opsForValue().get(LIKE_KEY + postId);

        // 🔥 Redis 값 있으면 사용
        if (value != null) {
            return Long.parseLong(value);
        }

        // 🔥 없으면 DB 조회 후 Redis 저장
        long count = likeRepository.countByPostId(postId);
        redisTemplate.opsForValue().set(LIKE_KEY + postId, String.valueOf(count));

        return count;
    }
}