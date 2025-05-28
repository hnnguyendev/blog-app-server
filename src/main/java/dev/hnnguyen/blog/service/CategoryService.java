package dev.hnnguyen.blog.service;

import dev.hnnguyen.blog.domain.Category;
import dev.hnnguyen.blog.domain.dto.category.RequestCategory;
import dev.hnnguyen.blog.domain.dto.category.ResponseCategory;
import dev.hnnguyen.blog.repository.CategoryRepository;
import dev.hnnguyen.blog.web.rest.errors.BadRequestAlertException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private static final String ENTITY_NAME = "Category";

    public Category createCategory(RequestCategory requestCategory) {
        if (categoryRepository.existsByNameIgnoreCase(requestCategory.getName())) {
            throw new BadRequestAlertException("category already exists", ENTITY_NAME, "nameexists");
        }

        Category category = Category.builder()
            .name(requestCategory.getName())
            .parentId(requestCategory.getParentId())
            .build();
        categoryRepository.save(category);

        return category;
    }

    public List<ResponseCategory> getCategoryOptions() {
        return categoryRepository.findAll()
            .stream()
            .sorted(Comparator.comparing(Category::getName, String.CASE_INSENSITIVE_ORDER))
            .map(ResponseCategory::new)
            .toList();
    }
}
