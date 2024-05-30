package io.github.fherbreteau.functional.infra;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String NOT_IMPLEMENTED = "Not Implemented Yet";

    @Override
    public boolean exists(String name) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public User findByName(String name) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean exists(UUID userId) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public User findById(UUID userId) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public User save(User user) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void delete(User user) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public User updatePassword(User user, String password) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean checkPassword(User user, String password) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean hasUserWithGroup(String name) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void removeGroupFromUser(Group group) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }
}
