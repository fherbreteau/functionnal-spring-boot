package io.github.fherbreteau.functional.infra;

import io.github.fherbreteau.functional.driven.ContentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.io.InputStream.nullInputStream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ContentRepositoryTest {

    private ContentRepository repository;

    @BeforeEach
    public void setup() {
        repository = new FileRepositoryImpl();
    }

    @Test
    void fakeCoverage() {
        assertThatThrownBy(() -> repository.readContent(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.writeContent(null, nullInputStream()))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
