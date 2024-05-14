package io.github.fherbreteau.functional.infra;

import io.github.fherbreteau.functional.driven.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class FileRepositoryTest {

    private FileRepository repository;

    @BeforeEach
    public void setup() {
        repository = new FileRepositoryImpl();
    }

    @Test
    void fakeCoverage() {
        assertThatThrownBy(() -> repository.exists(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.save(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.findByParentAndUser(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.findByNameAndParentAndUser(null, null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.getItem(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
