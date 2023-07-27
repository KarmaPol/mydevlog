package com.mydevlog.controller;

import com.mydevlog.domain.Post;
import com.mydevlog.request.PostCreate;
import com.mydevlog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public Post post(@RequestBody @Valid PostCreate request) {
       return postService.write(request);
    }

    @GetMapping("/posts/{id}")
    public Post post(@PathVariable Long id){
        Post post = postService.get(id);
        return post;
    }
}