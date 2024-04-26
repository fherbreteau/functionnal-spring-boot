package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestCommandTest {

    @Test
    void shouldThrowAnExceptionWhenCheckFailed() {
        // GIVEN
        AbstractCheckCommand<Command<Output>> command = new AbstractCheckCommand<>(null, null) {
            @Override
            protected boolean checkAccess(User actor) {
                return false;
            }

            @Override
            protected Command<Output> createSuccess() {
                throw new UnsupportedOperationException("Should never be called");
            }
        };
        // WHEN
        assertThatThrownBy(() -> command.execute(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Unsupported Command always succeed");
    }
}
