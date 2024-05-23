package io.github.fherbreteau.functional.infra;

import io.github.fherbreteau.functional.driven.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class GroupRepositoryTest {

    private GroupRepository repository;

    @BeforeEach
    public void setup() {
        repository = new GroupRepositoryImpl();
    }

    @Test
    void fakeCoverage() {
        assertThatThrownBy(() -> repository.exists((String) null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.findByName(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.exists((UUID) null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.findById(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.save(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.delete(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
