package dev.hnnguyen.blog.repository;

import dev.hnnguyen.blog.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link Section} entity.
 */
@Repository
public interface SectionRepository extends JpaRepository<Section, UUID> {

}
