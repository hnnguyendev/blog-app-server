package dev.hnnguyen.blog.repository;

import dev.hnnguyen.blog.domain.dto.post.RequestSearchPost;
import dev.hnnguyen.blog.domain.dto.post.ResponsePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<ResponsePost> getAllPostByFilterCondition(RequestSearchPost request, Pageable pageable);
}
