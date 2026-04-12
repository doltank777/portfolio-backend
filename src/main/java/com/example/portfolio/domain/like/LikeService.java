package com.example.portfolio.domain.like;

import com.example.portfolio.domain.post.PostRepository;
import com.example.portfolio.domain.user.UserRepository;
import com.example.portfolio.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final StringRedisTemplate redisTemplate;

    // 🔥 properties에서 가져오기
    @Value("${redis.like.count.prefix}")
    private String LIKE_COUNT_KEY;

    @Value("${redis.like.user.prefix}")
    private String LIKE_USER_KEY;

    // 🔥 TTL 설정 (10분)
    private static final long TTL = 10;

    public String like(Long postId, String username) {

        userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("사용자 없음", HttpStatus.NOT_FOUND));

        postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시글 없음", HttpStatus.NOT_FOUND));

        String userKey = LIKE_USER_KEY + postId;
        String countKey = LIKE_COUNT_KEY + postId;

        // 🔥 이미 좋아요 했는지 체크
        Boolean isMember = redisTemplate.opsForSet().isMember(userKey, username);

        if (Boolean.TRUE.equals(isMember)) {
            // 👍 좋아요 취소
            redisTemplate.opsForSet().remove(userKey, username);
            redisTemplate.opsForValue().decrement(countKey);

        } else {
            // 👍 좋아요 추가
            redisTemplate.opsForSet().add(userKey, username);
            redisTemplate.opsForValue().increment(countKey);
        }

        // 🔥 TTL 적용 (매번 갱신)
        redisTemplate.expire(userKey, TTL, TimeUnit.MINUTES);
        redisTemplate.expire(countKey, TTL, TimeUnit.MINUTES);

        return Boolean.TRUE.equals(isMember) ? "좋아요 취소" : "좋아요 추가";
    }

    public long count(Long postId) {

        String countKey = LIKE_COUNT_KEY + postId;

        String value = redisTemplate.opsForValue().get(countKey);

        // 🔥 캐시 hit
        if (value != null) {
            return Long.parseLong(value);
        }

        // 🔥 [중요] DB fallback (현재는 0, 나중에 DB 연동 가능)
        long count = 0L;

        // 🔥 TTL 적용해서 저장
        redisTemplate.opsForValue().set(countKey, String.valueOf(count), TTL, TimeUnit.MINUTES);

        return count;
    }
}