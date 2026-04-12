package com.example.portfolio.domain.like;

import com.example.portfolio.domain.post.Post;
import com.example.portfolio.domain.post.PostRepository;
import com.example.portfolio.domain.user.User;
import com.example.portfolio.domain.user.UserRepository;
import com.example.portfolio.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final StringRedisTemplate redisTemplate;

    @Value("${redis.like.count.prefix}")
    private String LIKE_COUNT_KEY;

    private static final long TTL = 10;

    // 👍 좋아요 토글
    public String like(Long postId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("사용자 없음", HttpStatus.NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시글 없음", HttpStatus.NOT_FOUND));

        boolean exists = likeRepository
                .findByUserIdAndPostId(user.getId(), postId)
                .isPresent();

        if (exists) {
            // 👍 좋아요 취소 (DB)
            likeRepository.deleteByUserIdAndPostId(user.getId(), postId);
        } else {
            // 👍 좋아요 추가 (DB)
            Like like = Like.builder()
                    .user(user)
                    .post(post)
                    .build();

            likeRepository.save(like);
        }

        // 🔥 캐시 무효화
        redisTemplate.delete(LIKE_COUNT_KEY + postId);

        return exists ? "좋아요 취소" : "좋아요 추가";
    }

    // 👍 좋아요 수 조회
    public long count(Long postId) {

        String key = LIKE_COUNT_KEY + postId;

        String value = redisTemplate.opsForValue().get(key);

        // 🔥 캐시 HIT
        if (value != null) {
            return Long.parseLong(value);
        }

        // 🔥 캐시 MISS → DB 조회
        long count = likeRepository.countByPostId(postId);

        // 🔥 Redis 저장 (TTL)
        redisTemplate.opsForValue()
                .set(key, String.valueOf(count), TTL, TimeUnit.MINUTES);

        return count;
    }
}