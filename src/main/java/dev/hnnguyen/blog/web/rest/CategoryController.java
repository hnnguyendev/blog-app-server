package dev.hnnguyen.blog.web.rest;

import dev.hnnguyen.blog.domain.Category;
import dev.hnnguyen.blog.domain.dto.category.RequestCategory;
import dev.hnnguyen.blog.domain.dto.category.ResponseCategory;
import dev.hnnguyen.blog.security.AuthoritiesConstants;
import dev.hnnguyen.blog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping()
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody RequestCategory requestCategory) throws URISyntaxException {
        log.debug("REST request to save Category : {}", requestCategory);
        Category category = categoryService.createCategory(requestCategory);
        return ResponseEntity.created(new URI("/api/categories/" + requestCategory.getId()))
            .body(category);
    }

    @GetMapping("/options")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<ResponseCategory> getCategoryOptions() {
        log.debug("REST request to get all Category Options");
        return categoryService.getCategoryOptions();
    }
}
