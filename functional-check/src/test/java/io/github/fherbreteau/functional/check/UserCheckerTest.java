package io.github.fherbreteau.functional.check;

import io.github.fherbreteau.functional.driven.rules.UserChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UserCheckerTest {

    private UserChecker userChecker;

    @BeforeEach
    public void setup() {
        userChecker = new UserCheckerImpl();
    }

    @Test
    void fakeCoverage() {
        assertThatThrownBy(() -> userChecker.canCreateUser(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> userChecker.canUpdateUser(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> userChecker.canDeleteUser(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> userChecker.canCreateGroup(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> userChecker.canUpdateGroup(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> userChecker.canDeleteGroup(null, null))
                .isInstanceOf(UnsupportedOperationException.class);
    }

}
