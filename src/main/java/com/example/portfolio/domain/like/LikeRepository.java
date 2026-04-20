package com.example.portfolio.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    long countByPostId(Long postId);

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    @Modifying
    @Transactional
    @Query("""
        delete from Like l
        where l.user.id = :userId
          and l.post.id = :postId
    """)
    void deleteLike(
            @Param("userId") Long userId,
            @Param("postId") Long postId
    );
}