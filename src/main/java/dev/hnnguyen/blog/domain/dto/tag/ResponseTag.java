package dev.hnnguyen.blog.domain.dto.tag;

import dev.hnnguyen.blog.domain.Tag;
import dev.hnnguyen.blog.domain.dto.category.ResponseCategory;

public class ResponseTag extends ResponseCategory {

    public ResponseTag(Tag tag) {
        super(tag.getId(), tag.getName(), tag.getSlug());
    }
}
