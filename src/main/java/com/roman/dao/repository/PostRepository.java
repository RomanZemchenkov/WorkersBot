package com.roman.dao.repository;

import com.roman.dao.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<Post> findPostByTitle(String title);

    @Query(value = "SELECT p.* FROM post AS p JOIN personal_info pi ON p.id = pi.post_id WHERE pi.worker_id = ?1", nativeQuery = true)
    Optional<Post> findPostByPersonalInfoId(Long id);

}
