package com.mydevlog.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostSearch {

    private static final int MAX_SIZE = 2000;

    private int page;
    private int size;

    public long getOffset() {
        return (long) (Math.max(1,page) - 1) * Math.min(size, MAX_SIZE);
    }
}
