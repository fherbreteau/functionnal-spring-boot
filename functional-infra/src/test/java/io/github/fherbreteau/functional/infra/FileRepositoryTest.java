package io.github.fherbreteau.functional.infra;

import io.github.fherbreteau.functional.driven.FileRepository;
import io.github.fherbreteau.functional.infra.impl.FileRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class FileRepositoryTest {

    private FileRepository repository;

    @BeforeEach
    public void setup() {
        repository = new FileRepositoryImpl(null);
    }

    @Test
    void fakeCoverage() {
        assertThatThrownBy(() -> repository.save(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.findByParentAndUser(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.findByNameAndParentAndUser(null, null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.delete(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
