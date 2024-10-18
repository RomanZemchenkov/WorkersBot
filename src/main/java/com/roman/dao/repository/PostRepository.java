package com.roman.dao.repository;

import com.roman.dao.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<Post> findPostByTitle(String title);
}
