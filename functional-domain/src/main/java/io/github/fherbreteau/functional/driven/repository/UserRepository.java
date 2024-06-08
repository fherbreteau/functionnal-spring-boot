package io.github.fherbreteau.functional.driven.repository;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;

import java.util.UUID;

public interface UserRepository {

    boolean exists(String name);

    User findByName(String name);

    boolean exists(UUID userId);

    User findById(UUID userId);

    User create(User user);

    User update(User user);

    void delete(User user);

    User updatePassword(User user, String password);

    boolean checkPassword(User user, String password);

    boolean hasUserWithGroup(String name);

    void removeGroupFromUser(Group group);
}
