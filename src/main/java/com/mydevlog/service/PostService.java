package com.mydevlog.service;

import com.mydevlog.domain.Post;
import com.mydevlog.repository.PostRepository;
import com.mydevlog.request.PostCreate;
import com.mydevlog.request.PostSearch;
import com.mydevlog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    public Post write(PostCreate postCreate){
        Post post = new Post(postCreate.getTitle(), postCreate.getContent());
        return postRepository.save(post);
    }

    public PostResponse get(Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하는 않는 글입니다."));

        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .title(post.getTitle()).build();
    }

    public List<PostResponse> getList(PostSearch postSearch){
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
}
