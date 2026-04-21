package com.example.portfolio.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);

    @Modifying
    @Transactional
    @Query("""
        delete from Comment c
        where c.post.id = :postId
    """)
    void deleteByPostId(@Param("postId") Long postId);

}