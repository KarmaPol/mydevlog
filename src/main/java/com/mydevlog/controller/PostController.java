package com.mydevlog.controller;

import com.mydevlog.domain.Post;
import com.mydevlog.request.PostCreate;
import com.mydevlog.request.PostEdit;
import com.mydevlog.request.PostSearch;
import com.mydevlog.response.PostResponse;
import com.mydevlog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public Post post(@RequestBody @Valid PostCreate request) {
        request.validate();
        return postService.write(request);
    }

    @GetMapping("/posts/{id}")
    public PostResponse get(@PathVariable Long id){
        return postService.get(id);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{id}")
    public PostResponse edit(@PathVariable Long id, @RequestBody @Valid PostEdit request){
        return postService.edit(id, request);
    }

    @DeleteMapping("/posts/{id}")
    public void delete(@PathVariable Long id){
        postService.delete(id);
    }
}