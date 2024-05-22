package io.github.fherbreteau.functional.infra;

import io.github.fherbreteau.functional.driven.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UserRepositoryTest {

    private UserRepository repository;

    @BeforeEach
    public void setup() {
        repository = new UserRepositoryImpl();
    }

    @Test
    void fakeCoverage() {
        assertThatThrownBy(() -> repository.exists(""))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.findByName(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.exists(UUID.randomUUID()))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.findById(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.save(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.delete(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.updatePassword(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.checkPassword(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.hasUserWithGroup(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.removeGroupFromUser(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
