package com.example.portfolio.domain.like;

import com.example.portfolio.domain.post.PostRepository;
import com.example.portfolio.domain.user.UserRepository;
import com.example.portfolio.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String LIKE_COUNT_KEY = "post:like:count:";
    private static final String LIKE_USER_KEY = "post:like:user:";

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

            return "좋아요 취소";
        } else {
            // 👍 좋아요 추가
            redisTemplate.opsForSet().add(userKey, username);
            redisTemplate.opsForValue().increment(countKey);

            return "좋아요 추가";
        }
    }

    public long count(Long postId) {

        String countKey = LIKE_COUNT_KEY + postId;

        String value = redisTemplate.opsForValue().get(countKey);

        if (value != null) {
            return Long.parseLong(value);
        }

        // 🔥 Redis 없으면 DB 조회 후 세팅
        long count = 0L; // 초기값 (DB 연동 시 변경 가능)

        redisTemplate.opsForValue().set(countKey, String.valueOf(count));

        return count;
    }
}