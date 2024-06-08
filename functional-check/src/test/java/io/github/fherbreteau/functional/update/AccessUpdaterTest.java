package io.github.fherbreteau.functional.update;

import io.github.fherbreteau.functional.driven.rules.AccessUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AccessUpdaterTest {

    private AccessUpdater accessUpdater;

    @BeforeEach
    public void setup() {
        accessUpdater = new AccessUpdaterImpl();
    }

    @Test
    void fakeCoverage() {
        assertThatThrownBy(() -> accessUpdater.createItem(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> accessUpdater.updateOwner(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> accessUpdater.updateGroup(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> accessUpdater.updateOwnerAccess(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> accessUpdater.updateGroupAccess(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> accessUpdater.updateOtherAccess(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> accessUpdater.deleteItem(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
