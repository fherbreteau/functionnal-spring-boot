package io.github.fherbreteau.functional.check;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccessCheckerTest {

    private AccessChecker accessChecker;

    @BeforeEach
    public void setup() {
        accessChecker = new AccessCheckerImpl();
    }

    @Test
    void fakeCoverage() {
        assertThatThrownBy(() -> accessChecker.canRead(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> accessChecker.canWrite(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> accessChecker.canExecute(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> accessChecker.canChangeMode(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> accessChecker.canChangeOwner(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> accessChecker.canChangeGroup(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
    }

}
