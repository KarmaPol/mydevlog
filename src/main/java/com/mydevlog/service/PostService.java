package com.mydevlog.service;

import com.mydevlog.domain.Post;
import com.mydevlog.repository.PostRepository;
import com.mydevlog.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    public Post write(PostCreate postCreate){
        Post post = new Post(postCreate.getTitle(), postCreate.getContent());
        return postRepository.save(post);
    }

    public Post get(Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하는 않는 글 ID입니다."));

        return post;
    }
}
