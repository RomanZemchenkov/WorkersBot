package com.roman.service;

import com.roman.dao.entity.Post;
import com.roman.dao.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post findPostOrCreate(String title){
        return postRepository.findPostByTitle(title)
                .orElseGet(() -> postRepository.save(new Post(title)));
    }

    @Transactional
    public Optional<Post> findWorkerPost(Long workerId){
        return postRepository.findPostByPersonalInfoId(workerId);
    }



}
