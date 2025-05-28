package dev.hnnguyen.blog.repository;

import dev.hnnguyen.blog.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link Category} entity.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsBySlug(String slug);

    boolean existsByNameIgnoreCase(String name);
}
