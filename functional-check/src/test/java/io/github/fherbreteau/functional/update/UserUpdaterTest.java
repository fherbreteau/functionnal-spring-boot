package io.github.fherbreteau.functional.update;

import io.github.fherbreteau.functional.driven.rules.UserUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UserUpdaterTest {

    private UserUpdater userUpdater;

    @BeforeEach
    public void setup() {
        userUpdater = new UserUpdaterImpl();
    }

    @Test
    void fakeCoverage() {
        assertThatThrownBy(() -> userUpdater.createUser(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> userUpdater.updateUser(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> userUpdater.deleteUser(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> userUpdater.createGroup(null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> userUpdater.updateGroup(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> userUpdater.deleteGroup(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
