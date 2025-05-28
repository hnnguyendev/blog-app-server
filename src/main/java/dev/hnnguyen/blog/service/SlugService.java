package dev.hnnguyen.blog.service;

import dev.hnnguyen.blog.domain.Category;
import dev.hnnguyen.blog.domain.Post;
import dev.hnnguyen.blog.domain.Tag;
import dev.hnnguyen.blog.repository.CategoryRepository;
import dev.hnnguyen.blog.repository.PostRepository;
import dev.hnnguyen.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlugService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public String generateUniqueSlug(String baseSlug, Class<?> entityClass) {
        String slug = baseSlug;
        int counter = 1;

        while (slugExists(slug, entityClass)) {
            slug = baseSlug + "-" + counter++;
        }

        return slug;
    }

    public boolean slugExists(String slug, Class<?> entityClass) {
        if (entityClass.equals(Post.class)) {
            return postRepository.existsBySlug(slug);
        } else if (entityClass.equals(Category.class)) {
            return categoryRepository.existsBySlug(slug);
        } else if (entityClass.equals(Tag.class)) {
            return tagRepository.existsBySlug(slug);
        }
        throw new IllegalArgumentException("Slug check not supported for: " + entityClass);
    }
}
