package dev.hnnguyen.blog.domain.listener;

import dev.hnnguyen.blog.domain.User;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.Objects;

public class UserEntityListener {

    @PrePersist
    @PreUpdate
    public void setFullName(User user) {
        if (Objects.nonNull(user.getFirstName()) && Objects.nonNull(user.getLastName())) {
            String fullName = (user.getFirstName().trim() + " " + user.getLastName().trim());
            user.setFullName(fullName);
        }
    }
}
