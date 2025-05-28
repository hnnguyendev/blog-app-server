package dev.hnnguyen.blog.domain.listener;

import dev.hnnguyen.blog.service.SlugService;
import dev.hnnguyen.blog.util.BeanUtil;
import dev.hnnguyen.blog.util.SlugUtils;
import jakarta.persistence.PrePersist;

import java.lang.reflect.Field;

public class SlugListenerEntity {

    @PrePersist
    public void generateSlug(Object entity) {
        try {
            String sourceValue = null;
            String slugValue = null;

            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(SlugSource.class)) {
                    field.setAccessible(true);
                    sourceValue = (String) field.get(entity);
                }

                if (field.getName().equalsIgnoreCase("slug")) {
                    field.setAccessible(true);
                    slugValue = (String) field.get(entity);
                }
            }

            if (sourceValue != null && (slugValue == null || slugValue.isBlank())) {
                SlugService slugService = BeanUtil.getBean(SlugService.class);
                String baseSlug = SlugUtils.toSlug(sourceValue);
                String uniqueSlug = slugService.generateUniqueSlug(baseSlug, entity.getClass());

                // Set slug back
                for (Field field : entity.getClass().getDeclaredFields()) {
                    if (field.getName().equalsIgnoreCase("slug")) {
                        field.setAccessible(true);
                        field.set(entity, uniqueSlug);
                        break;
                    }
                }
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not generate slug", e);
        }
    }
}
