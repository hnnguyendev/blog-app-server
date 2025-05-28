package dev.hnnguyen.blog.repository;

import dev.hnnguyen.blog.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link Tag} entity.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {

    boolean existsBySlug(String slug);

    boolean existsByNameIgnoreCase(String name);
}
