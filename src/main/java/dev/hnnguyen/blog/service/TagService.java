package dev.hnnguyen.blog.service;

import dev.hnnguyen.blog.domain.Tag;
import dev.hnnguyen.blog.domain.dto.tag.RequestTag;
import dev.hnnguyen.blog.domain.dto.tag.ResponseTag;
import dev.hnnguyen.blog.repository.TagRepository;
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
public class TagService {

    private final TagRepository tagRepository;

    private static final String ENTITY_NAME = "Tag";

    public Tag createTag(RequestTag requestTag) {
        if (tagRepository.existsByNameIgnoreCase(requestTag.getName())) {
            throw new BadRequestAlertException("tag already exists", ENTITY_NAME, "nameexists");
        }

        Tag tag = Tag.builder()
            .name(requestTag.getName())
            .build();
        tagRepository.save(tag);

        return tag;
    }

    public List<ResponseTag> getTagOptions() {
        return tagRepository.findAll()
            .stream()
            .sorted(Comparator.comparing(Tag::getName, String.CASE_INSENSITIVE_ORDER))
            .map(ResponseTag::new)
            .toList();
    }
}
