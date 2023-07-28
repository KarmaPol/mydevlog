package com.mydevlog.repository;

import com.mydevlog.domain.Post;
import com.mydevlog.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mydevlog.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset((long) (postSearch.getOffset()) * postSearch.getSize())
                .orderBy(post.id.desc())
                .fetch();
    }
}
