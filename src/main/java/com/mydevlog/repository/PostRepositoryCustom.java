package com.mydevlog.repository;

import com.mydevlog.domain.Post;
import com.mydevlog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
